package com.example.demo.users.api;

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

import com.example.demo.applications.api.ApplicationDto;
import com.example.demo.applications.model.ApplicationEntity;
import com.example.demo.applications.service.ApplicationService;
import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.api.ItemDto;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.service.ItemService;
import com.example.demo.types.api.TypeDto;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(UserSignsController.URL)
public class UserSignsController {
    public static final String URL = "/sign";
    private static final String SIGN_VIEW = "sign";
    private static final String SIGN_EDIT_VIEW = "sign-edit";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String ITEM_ATTRIBUTE = "item";

    private final ItemService itemService;
    private final TypeService typeService;
    private final ApplicationService applicationService;
    private final ModelMapper modelMapper;

    public UserSignsController(ItemService itemService, TypeService typeService, ApplicationService applicationService,
            ModelMapper modelMapper) {
        this.itemService = itemService;
        this.typeService = typeService;
        this.modelMapper = modelMapper;
        this.applicationService = applicationService;
    }

    private ItemDto toItemDto(ItemEntity entity) {
        return modelMapper.map(entity, ItemDto.class);
    }

    private ApplicationDto toAppDto(ApplicationEntity entity) {
        return modelMapper.map(entity, ApplicationDto.class);
    }

    private ApplicationEntity toAppEntity(ApplicationDto dto) {
        final ApplicationEntity entity = modelMapper.map(dto, ApplicationEntity.class);
        entity.setItem(itemService.get(dto.getItemId()));
        return entity;
    }

    @GetMapping
    public String getAll(@RequestParam(name = "typeId", defaultValue = "0") Long typeId,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page, Model model) {
        final Map<String, Object> attributes = PageAttributesMapper
                .toAttributes(itemService.getAll(typeId, page, Constants.DEFUALT_PAGE_SIZE), this::toItemDto);
        model.addAllAttributes(attributes);
        model.addAttribute(PAGE_ATTRIBUTE, page);

        model.addAttribute("types",
                typeService.getAll().stream()
                        .map(this::toTypeDto)
                        .toList());

        return SIGN_VIEW;
    }

    private TypeDto toTypeDto(TypeEntity entity) {
        return modelMapper.map(entity, TypeDto.class);
    }

    @GetMapping("/edit")
    public String create(
            @RequestParam("itemId") Long itemId,
            @RequestParam("userId") Long userId,
            Model model) {
        {
            ItemDto itemDto = toItemDto(itemService.get(itemId));

            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setItemId(itemId);
            applicationDto.setUserId(userId);

            // Добавляем объект в модель для использования на странице
            model.addAttribute("application", applicationDto);
            model.addAttribute("sign", itemDto);
            model.addAttribute(ITEM_ATTRIBUTE, new ApplicationDto());

            return SIGN_EDIT_VIEW;
        }
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = ITEM_ATTRIBUTE) @Valid ApplicationDto item,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return SIGN_EDIT_VIEW;
        }
        applicationService.create(item.getUserId(), toAppEntity(item));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(ITEM_ATTRIBUTE, toAppDto(applicationService.get(id)));

        return SIGN_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = ITEM_ATTRIBUTE) @Valid ApplicationDto line,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, возвращаем форму с сообщениями об ошибках
            // И передаем id книги для редактирования
            redirectAttributes.addFlashAttribute(ITEM_ATTRIBUTE, line);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + ITEM_ATTRIBUTE,
                    bindingResult);
            return SIGN_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        applicationService.update(line.getUserId(), line.getId(), toAppEntity(line));

        return Constants.REDIRECT_VIEW + URL;
    }

    

}
