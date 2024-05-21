package com.example.demo.lines.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.lines.model.LineEntity;
import com.example.demo.lines.model.LineGrouped;
import com.example.demo.lines.repository.LineRepository;

@Service
public class LineService {
    private final LineRepository repository;

    public LineService(LineRepository repository) {
        this.repository = repository;
    }

    // возможно стоит сделать как в примере TypeService
    @Transactional(readOnly = true)
    public List<LineEntity> getAll(long itemId) {
        if (itemId <= 0L) {
            return repository.findAll();
        }
        return repository.findByItemId(itemId);
    }

    @Transactional(readOnly = true)
    public List<LineEntity> getAll(long itemId, int page, int size) {
        if (itemId <= 0L) {
            return repository.findAll();
        }
        return repository.findByItemId(itemId, PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public Page<LineEntity> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

    @Transactional(readOnly = true)
    public List<LineEntity> getAll() {
        return repository.findAll();
    }

    @Transactional
    public LineEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(LineEntity.class, id));
    }

    @Transactional
    public LineEntity create(LineEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    @Transactional
    public LineEntity update(Long id, LineEntity entity) {
        final LineEntity existsEntity = get(id);
        existsEntity.setItem(entity.getItem());
        existsEntity.setPrice(entity.getPrice());
        existsEntity.setName(entity.getName());
        existsEntity.setAuthor(entity.getAuthor());
        existsEntity.setIsbn(entity.getIsbn());
        return repository.save(existsEntity);
    }

    @Transactional
    public LineEntity delete(Long id) {
        final LineEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<LineGrouped> getTop(int page, int size) {
        return repository.findTopLinesWithUserCount(PageRequest.of(page, size));
    }
}
