package com.kirekov.spec_builder.builder;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

class NullSpecification<Entity> implements Specification<Entity> {
    private static final NullSpecification<?> INSTANCE = new NullSpecification<>();

    @SuppressWarnings("unchecked")
    public static <Entity> NullSpecification<Entity> getInstance() {
        return (NullSpecification<Entity>) INSTANCE;
    }

    @Override
    public Predicate toPredicate(Root<Entity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
