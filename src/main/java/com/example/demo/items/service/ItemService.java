package com.example.demo.items.service;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.repository.ItemRepository;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    private void checkName(String name) {
        if (repository.findByNameIgnoreCase(name).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Item with name %s is already exists", name));
        }
    }

    @Transactional(readOnly = true)
    public List<ItemEntity> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    @Transactional(readOnly = true)
    public ItemEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ItemEntity.class, id));
    }

    @Transactional
    public ItemEntity create(ItemEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        checkName(entity.getName());
        return repository.save(entity);
    }

    @Transactional
    public ItemEntity update(Long id, ItemEntity entity) {
        final ItemEntity existsEntity = get(id);
        checkName(entity.getName());
        existsEntity.setName(entity.getName());
        return repository.save(existsEntity);
    }

    @Transactional
    public ItemEntity delete(Long id) {
        final ItemEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}
