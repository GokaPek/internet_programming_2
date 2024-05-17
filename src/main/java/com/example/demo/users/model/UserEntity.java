package com.example.demo.users.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.lines.model.LineEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true, length = 20)
    private String login;

    @ManyToMany()
    @OrderBy("id ASC")
    private Set<LineEntity> lines = new HashSet<>();

    private String password;
    private UserRole role;

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

    public Set<LineEntity> getLines() {
        return lines;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addLine(LineEntity line) {
        /*
         * if (order.getUser() != this) {
         * order.setUser(this);
         * }
         */
        lines.add(line);
    }

    public void removeLine(LineEntity lineEntity) {
        this.lines.remove(lineEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, role, lines);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserEntity other = (UserEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getLogin(), login)
                && Objects.equals(other.getLines(), lines);
    }
}
