package com.example.demo.types.api;

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
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.api.ItemDto;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

import aj.org.objectweb.asm.Type;
import jakarta.validation.Valid;

@Controller
@RequestMapping(TypeController.URL)
public class TypeController {
    public static final String URL = Constants.ADMIN_PREFIX + "/type";
    private static final String TYPE_VIEW = "type";
    private static final String TYPE_EDIT_VIEW = "type-edit";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String TYPE_ATTRIBUTE = "item";

    private final TypeService typeService;
    private final ModelMapper modelMapper;

    public TypeController(TypeService typeService, ModelMapper modelMapper) {
        this.typeService = typeService;
        this.modelMapper = modelMapper;
    }

    private TypeDto toDto(TypeEntity entity) {
        return modelMapper.map(entity, TypeDto.class);
    }

    private TypeEntity toEntity(TypeDto dto) {
        return modelMapper.map(dto, TypeEntity.class);
    }

    @GetMapping
    public String getAll(@RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page, Model model)
        {
            final Map<String, Object> attributes = PageAttributesMapper.toAttributes(typeService.getAll(page, Constants.DEFUALT_PAGE_SIZE), this::toDto);
            model.addAllAttributes(attributes);
            model.addAttribute(PAGE_ATTRIBUTE, page);
            return TYPE_VIEW;
        }

    
    @GetMapping("/{id}")
    public TypeDto get(@PathVariable(name = "id") Long id) {
        return toDto(typeService.get(id));
    }

    // @PostMapping
    // public TypeDto create(@RequestBody @Valid TypeDto dto) {
    //     return toDto(typeService.create(toEntity(dto)));
    // }

    @GetMapping("/edit/")
    public String create(Model model) {
        {
            model.addAttribute(TYPE_ATTRIBUTE, new TypeDto());
            return TYPE_EDIT_VIEW;
        }
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = TYPE_ATTRIBUTE) @Valid TypeDto item,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return TYPE_EDIT_VIEW;
        }
        typeService.create(toEntity(item));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(TYPE_ATTRIBUTE, toDto(typeService.get(id)));
        return TYPE_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = TYPE_ATTRIBUTE) @Valid TypeDto item,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return TYPE_EDIT_VIEW;
        }
        typeService.update(id, toEntity(item));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        typeService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }

    // @PutMapping("/{id}")
    // public TypeDto update(
    //         @PathVariable(name = "id") Long id,
    //         @RequestBody @Valid TypeDto dto) {
    //     return toDto(typeService.update(id, toEntity(dto)));
    // }

    // @DeleteMapping("/{id}")
    // public TypeDto delete(@PathVariable(name = "id") Long id) {
    //     return toDto(typeService.delete(id));
    // }
}
