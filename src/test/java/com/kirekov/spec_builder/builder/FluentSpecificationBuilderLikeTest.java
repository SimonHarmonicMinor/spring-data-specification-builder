package com.kirekov.spec_builder.builder;

import com.kirekov.spec_builder.entity.Role;
import com.kirekov.spec_builder.entity.User;
import com.kirekov.spec_builder.repository.RoleRepository;
import com.kirekov.spec_builder.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class FluentSpecificationBuilderLikeTest {
    private static final String ANALYTIC_ROLE_NAME = "analytic";
    private static final String MANAGER_ROLE_NAME = "manager";
    private static final String ANALYTIC_USER_LOGIN = "analyticLogin";
    private static final String MANAGER_USER_LOGIN = "managerLogin";
    private static final String ADMIN_USER_LOGIN = "adminLogin";

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        final var analyticRole = roleRepository.saveAndFlush(Role.builder().name(ANALYTIC_ROLE_NAME).build());
        final var managerRole = roleRepository.saveAndFlush(Role.builder().name(MANAGER_ROLE_NAME).build());
        userRepository.saveAndFlush(
                User.builder()
                        .login(ANALYTIC_USER_LOGIN)
                        .roles(List.of(analyticRole))
                        .build()
        );
        userRepository.saveAndFlush(
                User.builder()
                        .login(MANAGER_USER_LOGIN)
                        .roles(List.of(managerRole))
                        .build()
        );
        userRepository.saveAndFlush(
                User.builder()
                        .login(ADMIN_USER_LOGIN)
                        .roles(List.of(analyticRole, managerRole))
                        .build()
        );
    }

    @Test
    void likeReturnsMatchedFields() {
        final var spec =
                FluentSpecificationBuilder.<User>combinedWithOr()
                        .like(this::getRolesPath, asLikeString(ANALYTIC_ROLE_NAME))
                        .like(this::getRolesPath, asLikeString(ADMIN_USER_LOGIN))
                        .buildDistinct();
        final var users = userRepository.findAll(spec);
        assertEquals(2, users.size());

        final var analytic = findUserByLogin(ANALYTIC_USER_LOGIN, users);
        final var admin = findUserByLogin(ADMIN_USER_LOGIN, users);
        assertTrue(analytic.isPresent());
        assertTrue(admin.isPresent());
    }

    @Test
    void likeIgnoreCaseMatchedFields() {
        final var spec =
                FluentSpecificationBuilder.<User>combinedWithOr()
                        .likeIgnoreCase(this::getRolesPath, asLikeString(ANALYTIC_ROLE_NAME.toUpperCase()))
                        .likeIgnoreCase(this::getRolesPath, asLikeString(ADMIN_USER_LOGIN.toUpperCase()))
                        .buildDistinct();
        final var users = userRepository.findAll(spec);
        assertEquals(2, users.size());

        final var analytic = findUserByLogin(ANALYTIC_USER_LOGIN, users);
        final var admin = findUserByLogin(ADMIN_USER_LOGIN, users);
        assertTrue(analytic.isPresent());
        assertTrue(admin.isPresent());
    }

    private String asLikeString(String str) {
        return "%" + str + "%";
    }

    private Optional<User> findUserByLogin(String login, Collection<User> users) {
        return users.stream().filter(user -> Objects.equals(login, user.getLogin())).findAny();
    }

    private Expression<String> getRolesPath(Root<User> root) {
        return root.join(User.ROLES).get(Role.NAME);
    }
}