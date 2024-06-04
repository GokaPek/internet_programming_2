package com.example.demo.items.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.repository.ItemRepository;
import com.example.demo.types.model.TypeEntity;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
     public Page<ItemEntity> getAll(Long typeId, int page, int size) {
        if (Objects.equals(typeId, 0L)) {
            return repository.findAll(PageRequest.of(page, size, Sort.by("id")));
        }

        return repository.findByTypeId(typeId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public ItemEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
    }

    @Transactional
    public ItemEntity create(ItemEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    @Transactional
    public ItemEntity update(Long id, ItemEntity entity) {
        final ItemEntity existsEntity = get(id);
        existsEntity.setType(entity.getType());
        existsEntity.setPrice(entity.getPrice());
        existsEntity.setName(entity.getName());
        return repository.save(existsEntity);
    }

    @Transactional
    public ItemEntity delete(Long id) {
        final ItemEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    @Transactional(readOnly = true)
    public List<ItemEntity> getProductWherePriceMoreThen(int value) {
        return repository.findWherePriceMoreThen(value);
    }
}
