package com.example.demo.items.api;

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
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.service.ItemService;
import com.example.demo.types.api.TypeDto;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(ItemController.URL)
public class ItemController {
    public static final String URL = Constants.ADMIN_PREFIX + "/item";
    private static final String ITEM_VIEW = "item";
    private static final String ITEM_EDIT_VIEW = "item-edit";
    private static final String PAGE_ATTRIBUTE = "page";
    private static final String ITEM_ATTRIBUTE = "item";

    private final ItemService itemService;
    private final TypeService typeService;
    private final ModelMapper modelMapper;

    public ItemController(ItemService itemService, TypeService typeService, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.typeService = typeService;
        this.modelMapper = modelMapper;
    }

    private ItemDto toDto(ItemEntity entity) {
        return modelMapper.map(entity, ItemDto.class);
    }

    private ItemEntity toEntity(ItemDto dto) {
        final ItemEntity entity = modelMapper.map(dto, ItemEntity.class);
        entity.setType(typeService.get(dto.getTypeId()));
        return entity;
    }


    @GetMapping
    public String getAll(@RequestParam(name = "typeId", defaultValue = "0") Long typeId, @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page, Model model)
        {
            final Map<String, Object> attributes = PageAttributesMapper.toAttributes(itemService.getAll(typeId, page, Constants.DEFUALT_PAGE_SIZE), this::toDto);
            model.addAllAttributes(attributes);
            model.addAttribute(PAGE_ATTRIBUTE, page);

            model.addAttribute("types",
                typeService.getAll().stream()
                        .map(this::toTypeDto)
                        .toList());

            return ITEM_VIEW;
        }


        private TypeDto toTypeDto(TypeEntity entity) {
            return modelMapper.map(entity, TypeDto.class);
        }


    @GetMapping("/{id}")
    public ItemDto get(@PathVariable(name = "id") Long id) {
        return toDto(itemService.get(id));
    }

    @GetMapping("/edit/")
    public String create(Model model) {
        {
            model.addAttribute(ITEM_ATTRIBUTE, new ItemDto());

            model.addAttribute("types",
                typeService.getAll().stream()
                        .map(this::toTypeDto)
                        .toList());

            return ITEM_EDIT_VIEW;
        }
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = ITEM_ATTRIBUTE) @Valid ItemDto item,
            BindingResult bindingResult,
            Model model) {
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

        // Получение списка всех type и сохранение его в модель
        List<TypeDto> types = typeService.getAll().stream()
                .map(this::toTypeDto)
                .toList();
        model.addAttribute("types", types);

        return ITEM_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = ITEM_ATTRIBUTE) @Valid ItemDto line,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, возвращаем форму с сообщениями об ошибках
            // И передаем id книги для редактирования
            redirectAttributes.addFlashAttribute(ITEM_ATTRIBUTE, line);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + ITEM_ATTRIBUTE,
                    bindingResult);
                    return ITEM_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        itemService.update(id, toEntity(line));

        // Получение списка всех type и сохранение его в сессии
        List<TypeDto> types = typeService.getAll().stream()
                .map(this::toTypeDto)
                .toList();
        redirectAttributes.addFlashAttribute("types", types);

        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        itemService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
