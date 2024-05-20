package com.example.demo.lines.api;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.api.ItemDto;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.service.ItemService;
import com.example.demo.lines.model.LineEntity;
import com.example.demo.lines.model.LineGrouped;
import com.example.demo.lines.service.LineService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(LineController.URL)
@SessionAttributes("items")
public class LineController {
    public static final String URL = "/line";
    private static final String LINE_VIEW = "line";
    private static final String LINE_EDIT_VIEW = "line-edit";
    private static final String LINE_ATTRIBUTE = "line";
    private static final String PAGE_ATTRIBUTE = "line";

    private final LineService lineService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    public LineController(LineService lineService, ItemService itemService, ModelMapper modelMapper) {
        this.lineService = lineService;
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    private LineDto toDto(LineEntity entity) {
        return modelMapper.map(entity, LineDto.class);
    }

    private LineGroupedDto toGroupedDto(LineGrouped entity) {
        return modelMapper.map(entity, LineGroupedDto.class);
    }

    private LineEntity toEntity(LineDto dto) {
        final LineEntity entity = modelMapper.map(dto, LineEntity.class);
        entity.setItem(itemService.get(dto.getItemId()));
        return entity;
    }

    /*
     * @GetMapping
     * public List<LineDto> getAll(@RequestParam(name = "itemId", defaultValue =
     * "0") Long itemId) {
     * return lineService.getAll(itemId).stream().map(this::toDto).toList();
     * }
     */

    // @GetMapping
    // public PageDto<LineDto> getAll(@RequestParam(name = "page", defaultValue =
    // "0") int page,
    // @RequestParam(name = "size", Constants.DEFUALT_PAGE_SIZE) int size) {
    // return PageDtoMapper.toDto(lineService.getAll(page, size), this::toDto);
    // }

    @GetMapping
    public String getAll(
            @RequestParam(name = "itemId", defaultValue = "0") Long itemId,
            Model model) {
        List<LineDto> lines;
        if (itemId == 0) {
            // If no itemId is provided, get all lines
            lines = lineService.getAll().stream()
                    .map(this::toDto)
                    .toList();
        } else {
            // If an itemId is provided, filter lines by item
            lines = lineService.getAll(itemId).stream()
                    .map(this::toDto)
                    .toList();
        }
        model.addAttribute("lines", lines);
        model.addAttribute("items",
                itemService.getAll().stream()
                        .map(this::toItemDto)
                        .toList());
        return LINE_VIEW;
    }

    @GetMapping("/{id}")
    public LineDto get(@PathVariable(name = "id") Long id) {
        return toDto(lineService.get(id));
    }

    @GetMapping("/top")
    public List<LineGroupedDto> getTop() {
        return lineService.getTop(0, 5).stream().map(this::toGroupedDto).toList();
    }

    @GetMapping("/edit/")
    public String create(Model model) {
        {
            model.addAttribute(LINE_ATTRIBUTE, new LineDto());
            return LINE_EDIT_VIEW;
        }
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = LINE_ATTRIBUTE) @Valid LineDto line,
            BindingResult bindingResult,
            Model model, /* ! */ SessionStatus sessionStatus) {
        if (bindingResult.hasErrors()) {
            return LINE_EDIT_VIEW;
        }
        lineService.create(toEntity(line));
        //
        sessionStatus.setComplete();
        //
        return Constants.REDIRECT_VIEW + URL;
    }


    @GetMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(LINE_ATTRIBUTE, toDto(lineService.get(id)));

        // Получение списка всех item и сохранение его в модель
        List<ItemDto> items = itemService.getAll().stream()
                .map(this::toItemDto)
                .toList();
        model.addAttribute("items", items);

        return LINE_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = LINE_ATTRIBUTE) @Valid LineDto line,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Если есть ошибки валидации, возвращаем форму с сообщениями об ошибках
            // И передаем id книги для редактирования
            redirectAttributes.addFlashAttribute(LINE_ATTRIBUTE, line);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + LINE_ATTRIBUTE,
                    bindingResult);
            return Constants.REDIRECT_VIEW + "/line/edit/{id}";
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        lineService.update(id, toEntity(line));

        // Получение списка всех item и сохранение его в сессии
        List<ItemDto> items = itemService.getAll().stream()
                .map(this::toItemDto)
                .toList();
        redirectAttributes.addFlashAttribute("items", items);

        return Constants.REDIRECT_VIEW + URL;
    }

    private ItemDto toItemDto(ItemEntity entity) {
        return modelMapper.map(entity, ItemDto.class);
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        lineService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}
