package com.example.demo.applications.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.applications.model.*;
import com.example.demo.reviews.model.ReviewEntity;;

public interface ApplicationRepository extends CrudRepository<ApplicationEntity, Long> {
    //Optional<ApplicationEntity> findByNameIgnoreCase(String name);

    List<ApplicationEntity> findByUserId(long userId);
    Page<ApplicationEntity> findByUserId(long userId, Pageable pageable);
    List<ApplicationEntity> findByUserIdAndItemId(long userId, long itemId);
    Page<ApplicationEntity> findByUserIdAndItemId(long userId, long itemId, Pageable pageable);
    ApplicationEntity findOneByUserIdAndId(long userId, long id);
}