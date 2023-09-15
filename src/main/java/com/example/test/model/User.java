package com.example.test.model;
import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phone"),
        @UniqueConstraint(columnNames = "email")
})
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = true, name = "name")
    private String username;

    @Column(length = 255, nullable = true, name = "email")
    private String email;

    @Column(length = 40, nullable = true, name = "phone")
    private String phone;

    @Column(length = 255, nullable = true, name = "password")

    private String password;

    
    public User() {
    }

    public User(String username, String email, String password, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Long id, String username, String email, String password, String phone) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }


    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
