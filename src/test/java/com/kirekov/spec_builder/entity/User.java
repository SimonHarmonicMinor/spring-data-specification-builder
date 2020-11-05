package com.kirekov.spec_builder.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@Table
public class User {
    @Id
    @GeneratedValue
    private final Long id;
    private final String login;
    @ManyToMany
    private final List<Role> roles;

    public static final String ID = "id";
    public static final String LOGIN = "login";
    public static final String ROLES = "roles";
}
