package com.example.demo.reviews.model;

import java.time.LocalDate;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.users.model.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "reviews")
public class ReviewEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;
    @Column(nullable = false, length = 300)
    private String text;
    @Column(nullable = false)
    private LocalDate date;

    public ReviewEntity() {
    }

    public ReviewEntity(String text, LocalDate date) {
        this.text = text;
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        if (!user.getReviews().contains(this))
            user.addReview(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getId(), text, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ReviewEntity other = (ReviewEntity) obj;
        return Objects.equals(other.getId(), id) 
            && Objects.equals(other.getUser().getId(), user.getId())
                && Objects.equals(other.getText(), text)
                && Objects.equals(other.getDate(), date);
    }
}
