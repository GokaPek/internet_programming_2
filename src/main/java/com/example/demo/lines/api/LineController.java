package com.example.demo.lines.api;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.service.ItemService;
import com.example.demo.lines.model.LineEntity;
import com.example.demo.lines.model.LineGrouped;
import com.example.demo.lines.service.LineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(LineController.URL)
public class LineController {
    public static final String URL = Constants.ADMIN_PREFIX + "/lines";
    private static final String LINE_VIEW = "lines";
    private static final String LINE_EDIT_VIEW = "line-edit";
    private static final String LINE_ATTRIBUTE = "lines";
    private static final String PAGE_ATTRIBUTE = "lines";

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
    // public PageDto<LineDto> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
    //         @RequestParam(name = "size", Constants.DEFUALT_PAGE_SIZE) int size) {
    //     return PageDtoMapper.toDto(lineService.getAll(page, size), this::toDto);
    // }
    @GetMapping
    public String getAll(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        final Map<String, Object> attributes = PageAttributesMapper.toAttributes(
                lineService.getAll(page, Constants.DEFUALT_PAGE_SIZE), this::toDto);
        model.addAllAttributes(attributes);
        model.addAttribute(PAGE_ATTRIBUTE, page);
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

    @PostMapping
    public LineDto create(@RequestBody @Valid LineDto dto) {
        return toDto(lineService.create(toEntity(dto)));
    }

    @PutMapping("/{id}")
    public LineDto update(@PathVariable(name = "id") Long id, @RequestBody LineDto dto) {
        return toDto(lineService.update(id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public LineDto delete(@PathVariable(name = "id") Long id) {
        return toDto(lineService.delete(id));
    }
}
