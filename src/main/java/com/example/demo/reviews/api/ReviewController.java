package com.example.demo.reviews.api;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.api.PageDto;
import com.example.demo.core.api.PageDtoMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.api.ItemDto;
import com.example.demo.reviews.model.ReviewEntity;
import com.example.demo.reviews.service.ReviewService;
import com.example.demo.types.api.TypeDto;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.users.api.UserDto;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(ReviewController.URL)
public class ReviewController {
    public static final String URL = Constants.ADMIN_PREFIX + "/review";
    private static final String REVIEW_VIEW = "review";
    private static final String REVIEW_EDIT_VIEW = "review-edit";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String REVIEW_ATTRIBUTE = "item";

    private final ReviewService reviewService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ReviewController(ReviewService reviewService, UserService userService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    private ReviewDto toDto(ReviewEntity entity) {
        return modelMapper.map(entity, ReviewDto.class);
    }

    private ReviewEntity toEntity(ReviewDto dto) {
        final ReviewEntity entity = modelMapper.map(dto, ReviewEntity.class);
        return entity;
    }


    @GetMapping
    public String getAll(
        @RequestParam(name = "userId", defaultValue = "0") Long userId, @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size, Model model) {
            final Map<String, Object> attributes = PageAttributesMapper.toAttributes(reviewService.getAll(userId, page, Constants.DEFUALT_PAGE_SIZE), this::toDto);
            model.addAllAttributes(attributes);
            model.addAttribute(PAGE_ATTRIBUTE, page);

            model.addAttribute("users",
                userService.getAll().stream()
                        .map(this::toUserDto)
                        .toList());

            return REVIEW_VIEW;
        }

        private UserDto toUserDto(UserEntity entity) {
            return modelMapper.map(entity, UserDto.class);
        }


    @GetMapping("/{id}")
    public ReviewDto get(
            @PathVariable(name = "user") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(reviewService.get(userId, id));
    }

    @PostMapping
    public ReviewDto create(
            @PathVariable(name = "user") Long userId,
            @RequestBody @Valid ReviewDto dto) {
        return toDto(reviewService.create(userId, toEntity(dto)));
    }


    @GetMapping("/edit/")
    public String create(Model model) {
        {
            model.addAttribute(REVIEW_ATTRIBUTE, new ReviewDto());

            // model.addAttribute("types",
            //     typeService.getAll().stream()
            //             .map(this::toTypeDto)
            //             .toList());

            return REVIEW_EDIT_VIEW;
        }
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = REVIEW_ATTRIBUTE) @Valid ReviewDto item,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return REVIEW_EDIT_VIEW;
        }
        reviewService.create(item.getUserId(), toEntity(item));
        return Constants.REDIRECT_VIEW + URL;
    }

    // @PutMapping("/{id}")
    // public ReviewDto update(
    //         @PathVariable(name = "user") Long userId,
    //         @PathVariable(name = "id") Long id,
    //         @RequestBody @Valid ReviewDto dto) {
    //     return toDto(reviewService.update(userId, id, toEntity(dto)));
    // }

    // @DeleteMapping("/{id}")
    // public ReviewDto delete(
    //         @PathVariable(name = "user") Long userId,
    //         @PathVariable(name = "id") Long id) {
    //     return toDto(reviewService.delete(userId, id));
    // }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(REVIEW_ATTRIBUTE, toDto(reviewService.get(id)));

        

        return REVIEW_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = REVIEW_ATTRIBUTE) @Valid ReviewDto line,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, возвращаем форму с сообщениями об ошибках
            // И передаем id книги для редактирования
            redirectAttributes.addFlashAttribute(REVIEW_ATTRIBUTE, line);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + REVIEW_ATTRIBUTE,
                    bindingResult);
                    return REVIEW_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        reviewService.update(id, toEntity(line));

        // List<TypeDto> types = typeService.getAll().stream()
        //         .map(this::toTypeDto)
        //         .toList();
        // redirectAttributes.addFlashAttribute("types", types);

        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        reviewService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
