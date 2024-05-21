// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.example.demo.lines.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.lines.model.LineEntity;
import com.example.demo.lines.model.LineGrouped;

import java.util.List;

@Repository
public interface LineRepository extends CrudRepository<LineEntity, Long>, PagingAndSortingRepository<LineEntity, Long> {
    Optional<LineEntity> findByNameIgnoreCase(String name);

    List<LineEntity> findAll();

    List<LineEntity> findByItemId(long itemId, Pageable page);

    List<LineEntity> findByItemId(long itemId);

    // получить топ 5 самых залайканных книг

    // @Query(value = "SELECT l.*, l.item_id as itemId, COUNT(ul.user_ENTITY_ID) as
    // count FROM Lines l " +
    // "JOIN Users_Lines ul ON l.id = ul.lines_id " +
    // "GROUP BY l.id " +
    // "ORDER BY count DESC " +
    // "LIMIT 5 ", nativeQuery = true)

    @Query("SELECT l, COUNT(u.id) as count, l.id as id, l.price as price, l.author as author, l.name as name, l.isbn as isbn, i.name as itemName FROM UserEntity u JOIN u.lines l JOIN l.item i GROUP BY l ORDER BY count DESC")

    List<LineGrouped> findTopLinesWithUserCount(Pageable pageable);
}
