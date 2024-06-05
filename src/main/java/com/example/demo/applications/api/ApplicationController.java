package com.example.demo.applications.api;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.applications.model.ApplicationEntity;
import com.example.demo.applications.model.ApplicationGrouped;
import com.example.demo.applications.service.ApplicationService;
import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.api.PageDto;
import com.example.demo.core.api.PageDtoMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.api.ItemDto;
import com.example.demo.items.service.ItemService;
import com.example.demo.types.api.TypeDto;

import jakarta.validation.Valid;

@Controller
@RequestMapping(ApplicationController.URL)
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    public static final String URL = Constants.ADMIN_PREFIX + "/application";
    private static final String APP_VIEW = "application";
    private static final String APP_EDIT_VIEW = "application-edit";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String APP_ATTRIBUTE = "item";


    public ApplicationController(ApplicationService applicationService, ItemService itemService, ModelMapper modelMapper) {
        this.applicationService = applicationService;
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    private ApplicationDto toDto(ApplicationEntity entity) {
        return modelMapper.map(entity, ApplicationDto.class);
    }

    private ApplicationEntity toEntity(ApplicationDto dto) {
        final ApplicationEntity entity = modelMapper.map(dto, ApplicationEntity.class);
        entity.setItem(itemService.get(dto.getItemId()));
        return entity;
    }

    private ApplicationGroupedDto toGroupedDto(ApplicationGrouped entity) {
        return modelMapper.map(entity, ApplicationGroupedDto.class);
    }

    // @GetMapping
    // public PageDto<ApplicationDto> getAll(
    //         @PathVariable(name = "user") Long userId,
    //         @RequestParam(name = "itemId", defaultValue = "0") Long itemId,
    //         @RequestParam(name = "page", defaultValue = "0") int page,
    //         @RequestParam(name = "size", defaultValue = "5") int size) {
    //     return PageDtoMapper.toDto(applicationService.getAll(userId, itemId, page, size), this::toDto);
    // }


    @GetMapping
    public String getAll(@RequestParam(name = "user", defaultValue = "0") Long userId,
    @RequestParam(name = "itemId", defaultValue = "0") Long itemId,
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size, 
    Model model)
        {
            final Map<String, Object> attributes = PageAttributesMapper.toAttributes(applicationService.getAll(userId, itemId, page, size), this::toDto);
            model.addAllAttributes(attributes);
            model.addAttribute(PAGE_ATTRIBUTE, page);

            // model.addAttribute("items",
            //     itemService.getAll().stream()
            //             .map(this::toTypeDto)
            //             .toList());

            return APP_VIEW;
        }




    @GetMapping("/edit/")
    public String create(Model model) {
        {
            model.addAttribute(APP_ATTRIBUTE, new ApplicationDto());

            return APP_EDIT_VIEW;
        }
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = APP_ATTRIBUTE) @Valid ApplicationDto item,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return APP_EDIT_VIEW;
        }
        applicationService.create(item.getUserId(), toEntity(item));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(APP_ATTRIBUTE, toDto(applicationService.get(id)));

        return APP_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = APP_ATTRIBUTE) @Valid ApplicationDto line,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, возвращаем форму с сообщениями об ошибках
            // И передаем id книги для редактирования
            redirectAttributes.addFlashAttribute(APP_ATTRIBUTE, line);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + APP_ATTRIBUTE,
                    bindingResult);
                    return APP_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        applicationService.update(line.getUserId(), line.getId(), toEntity(line));

        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        applicationService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
