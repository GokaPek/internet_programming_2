package com.example.demo.items.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.configuration.Constants;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ItemController.URL + "/item")
public class ItemController {
    public static final String URL = Constants.ADMIN_PREFIX + "/item";
    private static final String ITEM_VIEW = "item";
    private static final String ITEM_EDIT_VIEW = "item-edit";
    private static final String ITEM_ATTRIBUTE = "item";

    private final ItemService itemService;
    private final ModelMapper modelMapper;

    public ItemController(ItemService itemService, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    private ItemDto toDto(ItemEntity entity) {
        return modelMapper.map(entity, ItemDto.class);
    }

    private ItemEntity toEntity(ItemDto dto) {
        return modelMapper.map(dto, ItemEntity.class);
    }

    @GetMapping
    public List<ItemDto> getAll() {
        return itemService.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable(name = "id") Long id) {
        return toDto(itemService.get(id));
    }

    @GetMapping("/edit/")
    public String create(Model model) { {
        model.addAttribute(ITEM_ATTRIBUTE, new ItemDto());
        return ITEM_EDIT_VIEW;
        }
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = ITEM_ATTRIBUTE) @Valid ItemDto item,
            BindingResult bindingResult,
            ModelMap model) {
        if (bindingResult.hasErrors()) {
            return ITEM_EDIT_VIEW;
        }
        itemService.create(toEntity(item));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(ITEM_ATTRIBUTE, toDto(itemService.get(id)));
        return ITEM_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = ITEM_ATTRIBUTE) @Valid ItemDto item,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return ITEM_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        itemService.update(id, toEntity(item));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        itemService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
