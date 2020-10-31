package com.kirekov.spec_builder.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Getter
@Entity
@Table
public class Employee {
    @Id
    @GeneratedValue
    private final Long id;
    private final String name;

    public static String NAME = "name";
}