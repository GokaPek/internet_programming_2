package com.example.demo.users.api;

import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.core.configuration.Constants;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(UserSignupController.URL)
public class UserSignupController {
    public static final String URL = "/signup";

    private static final String SIGNUP_VIEW = "signup";
    private static final String USER_ATTRIBUTE = "user";

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserSignupController(
            UserService userService,
            ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private UserEntity toEntity(UserSignupDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    @GetMapping
    public String getSignup(Model model) {
        model.addAttribute(USER_ATTRIBUTE, new UserSignupDto());
        return SIGNUP_VIEW;
    }

    @PostMapping
    public String signup(
            @ModelAttribute(name = USER_ATTRIBUTE) @Valid UserSignupDto user,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return SIGNUP_VIEW;
        }
        if (!Objects.equals(user.getPassword(), user.getPasswordConfirm())) {
            bindingResult.rejectValue("password", "signup:passwords", "Пароли не совпадают.");
            model.addAttribute(USER_ATTRIBUTE, user);
            return SIGNUP_VIEW;
        }
        userService.create(toEntity(user));
        return Constants.REDIRECT_VIEW + Constants.LOGIN_URL + "?signup";
    }

}
