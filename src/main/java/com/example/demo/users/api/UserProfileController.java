package com.example.demo.users.api;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.core.security.UserPrincipal;
import com.example.demo.items.api.ItemDto;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.service.ItemService;
import com.example.demo.lines.api.LineDto;
import com.example.demo.lines.api.LineGroupedDto;
import com.example.demo.lines.model.LineEntity;
import com.example.demo.lines.model.LineGrouped;
import com.example.demo.lines.service.LineService;
//import com.example.demo.orders.api.OrderDto;
// import com.example.demo.orders.api.OrderGroupedDto;
// import com.example.demo.orders.model.OrderEntity;
// import com.example.demo.orders.model.OrderGrouped;
// import com.example.demo.orders.service.OrderService;
// import com.example.demo.items.api.itemDto;
// import com.example.demo.items.model.itemEntity;
// import com.example.demo.items.service.itemService;
// import com.example.demo.users.model.UserSubscriptionWithStatus;
import com.example.demo.users.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserProfileController {
    private static final String PROFILE_VIEW = "profile";

    private static final String PAGE_ATTRIBUTE = "page";
    private static final String itemID_ATTRIBUTE = "itemId";
    private static final String PROFILE_ATTRIBUTE = "profile";

    private final LineService lineService;
    private final ItemService itemService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserProfileController(
            LineService lineService,
            ItemService itemService,
            UserService userService,
            ModelMapper modelMapper) {
        this.lineService = lineService;
        this.itemService = itemService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private LineDto toDto(LineEntity entity) {
        return modelMapper.map(entity, LineDto.class);
    }

    private LineGroupedDto toGroupedDto(LineGrouped entity) {
        return modelMapper.map(entity, LineGroupedDto.class);
    }

    private ItemDto toItemDto(ItemEntity entity) {
        return modelMapper.map(entity, ItemDto.class);
    }

    @GetMapping
    public String getProfile(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            @RequestParam(name = itemID_ATTRIBUTE, defaultValue = "0") int itemId,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {
        final long userId = principal.getId();
        model.addAttribute(PAGE_ATTRIBUTE, page);
        model.addAttribute(itemID_ATTRIBUTE, itemId);
        model.addAllAttributes(PageAttributesMapper.toAttributes(
                userService.getLines(userId, page, Constants.DEFUALT_PAGE_SIZE),
                this::toDto));
        model.addAttribute("stats",
                lineService.getTop(0, 5).stream()
                        .map(this::toGroupedDto)
                        .toList());
        model.addAttribute("items",
                itemService.getAll().stream()
                        .map(this::toItemDto)
                        .toList());
        List<LineGroupedDto> linesGr = lineService.getTop(0, 5).stream().map(this::toGroupedDto).toList();
        model.addAttribute("linesGr", linesGr);
        return PROFILE_VIEW;
    }


    @GetMapping("/user/{userId}/lines")
    public String getUserLines(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        if (userId <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute("lines",
                userService.getLines(userId, page, Constants.DEFUALT_PAGE_SIZE).stream().map(this::toLineDto).toList());
        model.addAttribute(PAGE_ATTRIBUTE, page);

        List<LineGroupedDto> linesGr = lineService.getTop(0, 5).stream().map(this::toGroupedDto).toList();
        model.addAttribute("linesGr", linesGr);


        return PROFILE_VIEW;
    }

    @PostMapping("/user/{id}/lines/{lineId}")
    public String addLine(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "lineId") Long lineId,
            RedirectAttributes redirectAttributes) {
        userService.addLine(id, lineId);
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE);
        return Constants.REDIRECT_VIEW + "/line";
    }

    @PostMapping("/user/{id}/lines/remove/{lineId}")
    public String removeUserLine(
            @PathVariable(name = "id") Long id,
            @PathVariable(name = "lineId") Long lineId,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        userService.removeLine(id, lineId);
        model.addAttribute("lines",
                userService.getLines(id, page, Constants.DEFUALT_PAGE_SIZE).stream().map(this::toLineDto).toList());
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return PROFILE_VIEW;
    }

    private LineDto toLineDto(LineEntity entity) {
        return modelMapper.map(entity, LineDto.class);
    }

    // @PostMapping
    // public String saveProfile(
    // @ModelAttribute(name = PROFILE_ATTRIBUTE) @Valid UserProfileDto profile,
    // BindingResult bindResult,
    // Model model,
    // @AuthenticationPrincipal UserPrincipal principal) {
    // if (bindResult.hasErrors()) {
    // return PROFILE_VIEW;
    // }
    // userService.saveUserSubscriptions(principal.getId(),
    // profile.getSubscriptions().stream()
    // .map(this::toSubscriptionWithStatus)
    // .collect(Collectors.toSet()));
    // return Constants.REDIRECT_VIEW + "/";
    // }

    @PostMapping("/delete/{id}")
    public String deleteOrder(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            @RequestParam(name = itemID_ATTRIBUTE, defaultValue = "0") int itemId,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserPrincipal principal) {
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE, page);
        redirectAttributes.addAttribute(itemID_ATTRIBUTE, itemId);
        userService.removeLine(principal.getId(), id);
        return Constants.REDIRECT_VIEW + "/";
    }
}
