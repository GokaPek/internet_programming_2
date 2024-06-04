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
    private static final String PROFILE_ATTRIBUTE = "profile";

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserProfileController(
            UserService userService,
            ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String getProfile(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {
        final long userId = principal.getId();
        model.addAttribute(PAGE_ATTRIBUTE, page);
        // model.addAllAttributes(PageAttributesMapper.toAttributes(
        //         userService.getLines(userId, page, Constants.DEFUALT_PAGE_SIZE),
        //         this::toDto));
        // model.addAttribute("stats",
        //         lineService.getTop(0, 5).stream()
        //                 .map(this::toGroupedDto)
        //                 .toList());
        // model.addAttribute("items",
        //         itemService.getAll().stream()
        //                 .map(this::toItemDto)
        //                 .toList());
        // List<LineGroupedDto> linesGr = lineService.getTop(0, 5).stream().map(this::toGroupedDto).toList();
        return PROFILE_VIEW;
    }

    @GetMapping("/profile")
    public String profile(Model model){
        // List<LineGroupedDto> linesGr = lineService.getTop(0, 5).stream().map(this::toGroupedDto).toList();
        // model.addAttribute("linesGr", linesGr);
        return PROFILE_VIEW;
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserPrincipal principal) {
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE, page);
        return Constants.REDIRECT_VIEW + "/";
    }
}
