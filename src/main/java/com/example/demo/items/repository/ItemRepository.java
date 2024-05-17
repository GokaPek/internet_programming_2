package com.example.demo.items.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.items.model.ItemEntity;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    Optional<ItemEntity> findByNameIgnoreCase(String name);
}
