package com.example.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.items.service.ItemService;

@SpringBootTest
class ItemServiceTests {
    @Autowired
    private ItemService typeService;

    private ItemEntity type;

    @BeforeEach
    void createData() {
        removeData();

        type = typeService.create(new ItemEntity("Книга"));
        typeService.create(new ItemEntity("Комикс"));
        typeService.create(new ItemEntity("Аудио"));
    }

    @AfterEach
    void removeData() {
        typeService.getAll().forEach(item -> typeService.delete(item.getId()));
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> typeService.get(0L));
    }

    @Test
    void createTest() {
        Assertions.assertEquals(3, typeService.getAll().size());
        Assertions.assertEquals(type, typeService.get(type.getId()));
    }

    @Test
    void createNotUniqueTest() {
        final ItemEntity nonUniqueType = new ItemEntity("Ноутбук");
        Assertions.assertThrows(IllegalArgumentException.class, () -> typeService.create(nonUniqueType));
    }

    @Test
    void createNullableTest() {
        final ItemEntity nullableType = new ItemEntity(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> typeService.create(nullableType));
    }

    @Test
    void updateTest() {
        final String test = "TEST";
        final String oldName = type.getName();
        final ItemEntity newEntity = typeService.update(type.getId(), new ItemEntity(test));
        Assertions.assertEquals(3, typeService.getAll().size());
        Assertions.assertEquals(newEntity, typeService.get(type.getId()));
        Assertions.assertEquals(test, newEntity.getName());
        Assertions.assertNotEquals(oldName, newEntity.getName());
    }

    @Test
    void deleteTest() {
        typeService.delete(type.getId());
        Assertions.assertEquals(2, typeService.getAll().size());

        final ItemEntity newEntity = typeService.create(new ItemEntity(type.getName()));
        Assertions.assertEquals(3, typeService.getAll().size());
        Assertions.assertNotEquals(type.getId(), newEntity.getId());
    }
}
