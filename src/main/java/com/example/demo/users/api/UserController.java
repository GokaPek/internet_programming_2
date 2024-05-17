package com.example.demo.users.api;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.core.api.PageAttributesMapper;

import com.example.demo.core.configuration.Constants;
import com.example.demo.lines.api.LineDto;
import com.example.demo.lines.model.LineEntity;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(UserController.URL)
public class UserController {
    public static final String URL = Constants.ADMIN_PREFIX + "/user";
    private static final String USER_VIEW = "user";
    private static final String USER_EDIT_VIEW = "user-edit";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String USER_ATTRIBUTE = "user";

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private UserDto toDto(UserEntity entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    private UserEntity toEntity(UserDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    @GetMapping
    public String getAll(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
                //map
        final Map<String, Object> attributes = PageAttributesMapper.toAttributes(
                userService.getAll(page, Constants.DEFUALT_PAGE_SIZE), this::toDto);
        model.addAllAttributes(attributes);
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return USER_VIEW;
    }

     @GetMapping("/edit/")
    public String create(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        model.addAttribute(USER_ATTRIBUTE, new UserDto());
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return USER_EDIT_VIEW;
    }

    @PostMapping("/edit/")
    public String create(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            @ModelAttribute(name = USER_ATTRIBUTE) @Valid UserDto user,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(PAGE_ATTRIBUTE, page);
            return USER_EDIT_VIEW;
        }
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE, page);
        userService.create(toEntity(user));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(USER_ATTRIBUTE, toDto(userService.get(id)));
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return USER_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            @ModelAttribute(name = USER_ATTRIBUTE) @Valid UserDto user,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute(PAGE_ATTRIBUTE, page);
            return USER_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE, page);
        userService.update(id, toEntity(user));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute(PAGE_ATTRIBUTE, page);
        userService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }

    // логика для книг
    private LineDto toLineDto(LineEntity entity) {
        return modelMapper.map(entity, LineDto.class);
    }

    @PostMapping("/edit/{id}/lines/{lineId}")
    public UserDto addLine(@PathVariable(name = "id") Long id, @PathVariable(name = "lineId") Long lineId) {
        return toDto(userService.addLine(id, lineId));
    }

    @DeleteMapping("/edit/{id}/lines/{lineId}")
    public UserDto removeLine(@PathVariable(name = "id") Long userId,
            @PathVariable(name = "lineId") Long lineId) {
        return toDto(userService.removeLine(userId, lineId));
    }

    @DeleteMapping("/edit/{id}/lines/")
    public UserDto removeLines(@PathVariable(name = "id") Long userId) {
        return toDto(userService.removeAllLines(userId));
    }

    @GetMapping("/edit/{id}/lines")
    public List<LineDto> getLines(@PathVariable(name = "id") Long id) {
        return userService.getLines(id, 0, 5).stream().map(this::toLineDto).toList();
    }
}
