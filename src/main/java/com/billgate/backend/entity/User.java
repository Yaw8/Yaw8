package com.billgate.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    // Primary key for each user.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User's display name.
    private String name;

    // User's email address.
    // Later this should be unique.
    private String email;

    // User password.
    // Later we will hash this instead of storing plain text.
    private String password;
}