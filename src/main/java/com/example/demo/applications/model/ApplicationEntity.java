package com.example.demo.applications.model;

import java.time.LocalDate;
import java.util.Objects;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.items.model.ItemEntity;
import com.example.demo.users.model.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
 //убрать почту
@Entity
@Table(name = "applications")
public class ApplicationEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "itemId", nullable = false)
    private ItemEntity item;
    @Column(nullable = false, length = 50)
    private String phone;
    @Column(nullable = false)
    private LocalDate date;

    public ApplicationEntity() {
        
    }

    public ApplicationEntity(ItemEntity item, String phone, LocalDate date) {
        this.item = item;
        this.phone = phone;
        this.date = date;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        if (!user.getApplications().contains(this)) {
            user.getApplications().add(this);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, item.getId(), user.getId(), phone, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ApplicationEntity other = (ApplicationEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getItem(), item)
                && Objects.equals(other.getUser().getId(), user.getId())
                && Objects.equals(other.getPhone(), phone)
                && Objects.equals(other.getDate(), date);
    }
}
