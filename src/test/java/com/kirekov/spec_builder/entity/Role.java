package com.kirekov.spec_builder.entity;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Entity
@Table
public class Role {
    @Id
    @GeneratedValue
    private final Long id;
    private final String name;
    @ManyToMany
    private final List<User> users;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String USERS = "users";
}
