package com.example.demo.types.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.types.model.TypeEntity;

public interface TypeRepository extends CrudRepository<TypeEntity, Long>, PagingAndSortingRepository<TypeEntity, Long> {
    Optional<TypeEntity> findByNameIgnoreCase(String name);
}
