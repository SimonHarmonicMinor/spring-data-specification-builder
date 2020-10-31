package com.kirekov.spec_builder.builder;

import com.kirekov.spec_builder.provider.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.Attribute;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FluentSpecificationBuilder<Entity> implements NotSpecificationProvider<Entity, FluentSpecificationBuilder<Entity>>, SpecificationBuilder<Entity> {
    private final boolean denied;
    private final boolean and;
    private final Specification<Entity> result;

    public static <Entity> FluentSpecificationBuilder<Entity> combinedWithAnd() {
        return combined(true);
    }

    public static <Entity> FluentSpecificationBuilder<Entity> combinedWithOr() {
        return combined(false);
    }

    private static <Entity> FluentSpecificationBuilder<Entity> combined(boolean and) {
        return new FluentSpecificationBuilder<>(false, and, NullSpecification.getInstance());
    }

    private static <Entity> FluentSpecificationBuilder<Entity> from(Specification<Entity> result, boolean and) {
        return from(result, false, and);
    }

    private static <Entity> FluentSpecificationBuilder<Entity> deniedFrom(Specification<Entity> result, boolean and) {
        return from(result, true, and);
    }

    private static <Entity> FluentSpecificationBuilder<Entity> from(Specification<Entity> result, boolean denied, boolean and) {
        return new FluentSpecificationBuilder<>(denied, and, result);
    }

    @Override
    public CombinedSpecificationProvider<Entity, FluentSpecificationBuilder<Entity>> not() {
        return deniedFrom(result, and);
    }

    @Override
    public FluentSpecificationBuilder<Entity> eq(Attribute<Entity, ?> field, Object value) {
        return eq(field.getName(), value);
    }

    @Override
    public FluentSpecificationBuilder<Entity> eq(String field, Object value) {
        return applySpecification((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value));
    }

    @Override
    public FluentSpecificationBuilder<Entity> like(Attribute<Entity, ?> field, String pattern) {
        return like(field.getName(), pattern);
    }

    @Override
    public FluentSpecificationBuilder<Entity> like(String field, String pattern) {
        return applySpecification((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(field), pattern));
    }

    @Override
    public FluentSpecificationBuilder<Entity> likeIgnoreCase(Attribute<Entity, ?> field, String pattern) {
        return likeIgnoreCase(field.getName(), pattern);
    }

    @Override
    public FluentSpecificationBuilder<Entity> likeIgnoreCase(String field, String pattern) {
        return applySpecification((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(field)), pattern.toLowerCase()));
    }

    @Override
    public FluentSpecificationBuilder<Entity> specification(Specification<Entity> specification) {
        return applySpecification(specification);
    }

    @Override
    public Specification<Entity> build() {
        return result;
    }

    private FluentSpecificationBuilder<Entity> applySpecification(Specification<Entity> specification) {
        final var spec = getCombinedSpecification(
                (root, query, criteriaBuilder) -> wrapWithNotIfNeeded(specification.toPredicate(root, query, criteriaBuilder))
        );
        return from(spec, and);
    }

    private Specification<Entity> getCombinedSpecification(Specification<Entity> specification) {
        if (and) {
            return result.and(specification);
        } else {
            return result.or(specification);
        }
    }

    private Predicate wrapWithNotIfNeeded(Predicate predicate) {
        if (denied) {
            return predicate.not();
        }
        return predicate;
    }
}
