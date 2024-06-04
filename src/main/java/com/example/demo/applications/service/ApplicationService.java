package com.example.demo.applications.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.applications.model.ApplicationEntity;
import com.example.demo.applications.model.ApplicationGrouped;
import com.example.demo.applications.repository.ApplicationRepository;
import com.example.demo.core.error.NotFoundException;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;

@Service
public class ApplicationService {
    private final ApplicationRepository repository;
    private final UserService userService;

    public ApplicationService(ApplicationRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ApplicationEntity> getAll(long userId, long itemId) {
        userService.get(userId);
        if (itemId <= 0L) {
            return repository.findByUserId(userId);
        } else {
            return repository.findByUserIdAndItemId(userId, itemId);
        }
    }

    @Transactional(readOnly = true)
    public Page<ApplicationEntity> getAll(long userId, long itemId, int page, int size) {
        final Pageable pageRequest = PageRequest.of(page, size);
        userService.get(userId);
        if (itemId <= 0L) {
            return repository.findByUserId(userId, pageRequest);
        }
        return repository.findByUserIdAndItemId(userId, itemId, pageRequest);
    }

    @Transactional(readOnly = true)
    public ApplicationEntity get(long userId, long id) {
        userService.get(userId);
        return repository.findOneByUserIdAndId(userId, id);
                //.orElseThrow(() -> new NotFoundException(ApplicationEntity.class, id));
    }

    @Transactional
    public ApplicationEntity create(long userId, ApplicationEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        final UserEntity existsUser = userService.get(userId);
        entity.setUser(existsUser);
        return repository.save(entity);
    }

    @Transactional
    public ApplicationEntity update(long userId, long id, ApplicationEntity entity) {
        userService.get(userId);
        final ApplicationEntity existsEntity = get(userId, id);
        existsEntity.setItem(entity.getItem());
        existsEntity.setPhone(entity.getPhone());
        existsEntity.setDate(entity.getDate());
        return repository.save(existsEntity);
    }

    @Transactional
    public ApplicationEntity delete(long userId, long id) {
        userService.get(userId);
        final ApplicationEntity existsEntity = get(userId, id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    /*@Transactional(readOnly = true)
    public List<ApplicationGrouped> getTotal(long userId) {
        userService.get(userId);
        return repository.getApplicationsTotalByItem(userId);
    }*/
}
