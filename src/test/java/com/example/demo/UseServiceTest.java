package com.example.demo;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.service.ItemService;
import com.example.demo.lines.model.LineEntity;
import com.example.demo.lines.service.LineService;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;

import jakarta.persistence.EntityManager;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class UseServiceTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ItemService itemService;
    @Autowired
    private LineService lineService;
    @Autowired
    private UserService userService;

    private ItemEntity type1;
    private ItemEntity type2;
    private ItemEntity type3;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void createData() {
        removeData();

        user1 = userService.create(new UserEntity("null12", "null"));
        user2 = userService.create(new UserEntity("null13", "null"));

        type1 = itemService.create(new ItemEntity("Ноутбук"));
        type2 = itemService.create(new ItemEntity("Телефон"));
        type3 = itemService.create(new ItemEntity("Игровая приставка"));

        final var lines = List.of(
                new LineEntity(type1, 100.0, "qw1", "2dd",
                        "3"),
                new LineEntity(type1, 100.0, "1e", "2da",
                        "3"),
                new LineEntity(type2, 100.0, "1sa", "2dd",
                        "3"),
                new LineEntity(type2, 100.0, "1sad", "2axc",
                        "3"),
                new LineEntity(type2, 100.0, "1ww", "asd2",
                        "3"),
                new LineEntity(type3, 100.0, "1s", "2fff",
                        "3"),
                new LineEntity(type3, 100.0, "1a", "2s", "cc3"));
        lines.forEach(line -> {
            LineEntity createdLine = lineService.create(line);
            userService.addLine(user1.getId(), createdLine.getId());
        });
    }

    @AfterEach
    void removeData() {
        userService.getAll().forEach(item -> userService.delete(item.getId()));

        lineService.getAll(0).forEach(item -> lineService.delete(item.getId()));
        itemService.getAll().forEach(item -> itemService.delete(item.getId()));
    }

//     @Test

//     @Order(1)
//     void createTest() {
//         Assertions.assertEquals(7, userService.getLines(user1.getId()).size());
//         Assertions.assertEquals(0, userService.getLines(user2.getId()).size());
//     }

//     @Test

//     @Order(2)
//     void subscriptionsDeleteTest() {
//         userService.removeAllLines(user1.getId());
//         Assertions.assertTrue(userService.getLines(user1.getId()).isEmpty());
//     }

    // @Test
    // @Order(3)
    // void userCascadeDeleteTest() {
    // userService.delete(user1.getId());
    // final var lines = entityManager.createQuery(
    // "select count(u) from UserEntity u where u.line.id = :lineId");
    // Assertions.assertEquals(0, lines.getSingleResult());
    // }
}
