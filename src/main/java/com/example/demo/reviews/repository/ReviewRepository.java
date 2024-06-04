package com.example.demo.reviews.repository;

import com.example.demo.reviews.model.ReviewEntity;
//import com.example.demo.reviews.model.ReviewGrouped;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository
        extends CrudRepository<ReviewEntity, Long>, PagingAndSortingRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findOneByUserIdAndId(long userId, long id);

    List<ReviewEntity> findByUserId(long userId);

    Page<ReviewEntity> findByUserId(long userId, Pageable pageable);

}
