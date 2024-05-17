package com.example.demo.lines.model;

import java.util.Objects;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.items.model.ItemEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lines")
public class LineEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "itemId", nullable = false)
    private ItemEntity item;
    private Double price;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String isbn;

    public LineEntity() {
    }

    public LineEntity(ItemEntity item, Double price, String name, String author, String isbn) {
        this.item = item;
        this.price = price;
        this.name = name;
        this.author = author;
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ItemEntity getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item, price, name, author, isbn);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final LineEntity other = (LineEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getItem(), item)
                && Objects.equals(other.getPrice(), price)
                && Objects.equals(other.getName(), name)
                && Objects.equals(other.getAuthor(), author)
                && Objects.equals(other.getIsbn(), isbn);
    }
}
