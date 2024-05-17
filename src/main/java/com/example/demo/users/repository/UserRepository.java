package com.example.demo.users.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.users.model.UserEntity;

public interface UserRepository
        extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginIgnoreCase(String login);
}
