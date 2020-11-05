package com.kirekov.spec_builder.from;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.function.Function;

public interface PathFunction<Entity> extends Function<Root<Entity>, Expression<String>> {
    @Override
    Expression<String> apply(Root<Entity> root);
}
