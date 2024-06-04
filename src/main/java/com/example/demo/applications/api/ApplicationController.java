package com.example.demo.applications.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.applications.model.ApplicationEntity;
import com.example.demo.applications.model.ApplicationGrouped;
import com.example.demo.applications.service.ApplicationService;
import com.example.demo.core.api.PageDto;
import com.example.demo.core.api.PageDtoMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.items.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/{user}/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

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

    @GetMapping
    public PageDto<ApplicationDto> getAll(
            @PathVariable(name = "user") Long userId,
            @RequestParam(name = "itemId", defaultValue = "0") Long itemId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return PageDtoMapper.toDto(applicationService.getAll(userId, itemId, page, size), this::toDto);
    }

    @GetMapping("/{id}")
    public ApplicationDto get(
            @PathVariable(name = "user") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(applicationService.get(userId, id));
    }

    @PostMapping
    public ApplicationDto create(
            @PathVariable(name = "user") Long userId,
            @RequestBody @Valid ApplicationDto dto) {
        return toDto(applicationService.create(userId, toEntity(dto)));
    }

    @PutMapping("/{id}")
    public ApplicationDto update(
            @PathVariable(name = "user") Long userId,
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid ApplicationDto dto) {
        return toDto(applicationService.update(userId, id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ApplicationDto delete(
            @PathVariable(name = "user") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(applicationService.delete(userId, id));
    }

    /*@GetMapping("/total")
    public List<ApplicationGroupedDto> getMethodName(@PathVariable(name = "user") Long userId) {
        return applicationService.getTotal(userId).stream()
                .map(this::toGroupedDto)
                .toList();
    }*/

}
