package com.example.demo.items.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.model.ItemGrouped;

public interface ItemRepository
        extends CrudRepository<ItemEntity, Long>, PagingAndSortingRepository<ItemEntity, Long> {
    List<ItemEntity> findByTypeId(long typeId);

    Page<ItemEntity> findByTypeId(long typeId, Pageable pageable);
    Page<ItemEntity> findAll(Pageable pageable);

    @Query("select p from ItemEntity p where p.price > ?1")
    List<ItemEntity> findWherePriceMoreThen(int value);

    @Query("SELECT a.item, COUNT(a) as applicationCount, a.item.id as id, a.item.name as name " +
            "FROM ApplicationEntity a " +
            "JOIN ItemEntity it on a.item.id = it.id " +
            "GROUP BY a.item.id " +
            "ORDER BY applicationCount DESC")
    List<ItemGrouped> findTop5PopularServices(Pageable pageable);
}