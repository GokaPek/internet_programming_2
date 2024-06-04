package com.example.demo.reviews.service;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.reviews.model.ReviewEntity;
// import com.example.demo.reviews.model.ReviewGrouped;
import com.example.demo.reviews.repository.ReviewRepository;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ReviewService {
    private final ReviewRepository repository;
    private final UserService userService;

    public ReviewService(ReviewRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ReviewEntity> getAll(long userId) {
        userService.get(userId);
        return repository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<ReviewEntity> getAll(long userId, int page, int size) {
        final Pageable pageRequest = PageRequest.of(page, size);
        userService.get(userId);
        return repository.findByUserId(userId, pageRequest);
    }

    @Transactional(readOnly = true)
    public ReviewEntity get(long userId, long id) {
        userService.get(userId);
        return repository.findOneByUserIdAndId(userId, id)
                .orElseThrow(() -> new NotFoundException(ReviewEntity.class, id));
    }

    @Transactional
    public ReviewEntity create(long userId, ReviewEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        final UserEntity existsUser = userService.get(userId);
        entity.setUser(existsUser);
        return repository.save(entity);
    }

    @Transactional
    public ReviewEntity update(long userId, long id, ReviewEntity entity) {
        userService.get(userId);
        final ReviewEntity existsEntity = get(userId, id);
        existsEntity.setText(entity.getText());
        return repository.save(existsEntity);
    }

    @Transactional
    public ReviewEntity delete(long userId, long id) {
        userService.get(userId);
        final ReviewEntity existsEntity = get(userId, id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
