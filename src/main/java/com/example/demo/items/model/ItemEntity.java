package com.example.demo.items.model;

import java.util.Objects;

import com.example.demo.core.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Items")
public class ItemEntity extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    public ItemEntity() {
    }

    public ItemEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ItemEntity other = (ItemEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getName(), name);
    }

}
