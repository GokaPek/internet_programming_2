package com.example.demo.reviews.api;

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

import com.example.demo.core.api.PageDto;
import com.example.demo.core.api.PageDtoMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.reviews.model.ReviewEntity;
//import com.example.demo.reviews.model.ReviewGrouped;
import com.example.demo.reviews.service.ReviewService;
import com.example.demo.users.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user/{user}/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    public ReviewController(ReviewService reviewService, ModelMapper modelMapper) {
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }

    private ReviewDto toDto(ReviewEntity entity) {
        return modelMapper.map(entity, ReviewDto.class);
    }

    private ReviewEntity toEntity(ReviewDto dto) {
        final ReviewEntity entity = modelMapper.map(dto, ReviewEntity.class);
        return entity;
    }

    // private ReviewGroupedDto toGroupedDto(ReviewGrouped entity) {
    //     return modelMapper.map(entity, ReviewGroupedDto.class);
    // }

    @GetMapping
    public PageDto<ReviewDto> getAll(
            @PathVariable(name = "user") Long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return PageDtoMapper.toDto(reviewService.getAll(userId, page, size), this::toDto);
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

    @PutMapping("/{id}")
    public ReviewDto update(
            @PathVariable(name = "user") Long userId,
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid ReviewDto dto) {
        return toDto(reviewService.update(userId, id, toEntity(dto)));
    }

    @DeleteMapping("/{id}")
    public ReviewDto delete(
            @PathVariable(name = "user") Long userId,
            @PathVariable(name = "id") Long id) {
        return toDto(reviewService.delete(userId, id));
    }
}
