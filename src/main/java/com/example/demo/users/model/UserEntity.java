package com.example.demo.users.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.example.demo.applications.model.ApplicationEntity;
import com.example.demo.core.model.BaseEntity;
import com.example.demo.reviews.model.ReviewEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true, length = 20)
    private String login;

    private String password;
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private final Set<ApplicationEntity> applications = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private final Set<ReviewEntity> reviews = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = UserRole.USER;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void UserRole(UserRole role) {
        this.role = role;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ReviewEntity> getReviews() {
        return reviews;
    }

    public Set<ApplicationEntity> getApplications() {
        return applications;
    }

    public void addReview(ReviewEntity review) {
        if (review.getUser() != this) {
            review.setUser(this);
        }
        reviews.add(review);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserEntity other = (UserEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getLogin(), login);
    }
}
