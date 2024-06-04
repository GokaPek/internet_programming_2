package com.example.demo.items.model;

import java.util.Objects;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.types.model.TypeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
public class ItemEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "typeId", nullable = false)
    private TypeEntity type;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String name;

    public ItemEntity() {
    }

    public ItemEntity(TypeEntity type, String name, Double price) {
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, price );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ItemEntity other = (ItemEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getType(), type)            
                && Objects.equals(other.getName(), name)
                && Objects.equals(other.getPrice(), price);
    }
}
