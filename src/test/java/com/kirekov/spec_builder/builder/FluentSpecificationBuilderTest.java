package com.kirekov.spec_builder.builder;

import com.kirekov.spec_builder.entity.Employee;
import com.kirekov.spec_builder.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import java.lang.reflect.Member;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FluentSpecificationBuilderTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void eqReturnsEqualsFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .eq(createAttribute(Employee.NAME), employeeName)
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(employeeName, employee.get().getName());
    }

    @Test
    void eqDoesNotReturnNotEqualFields() {
        final var employeeName = "WrongEmployeeName";
        saveEmployee(employeeName + "rrrr");
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .eq(createAttribute(Employee.NAME), employeeName)
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertFalse(employee.isPresent());
    }

    @Test
    void notEqFindsNotEqualFields() {
        final var employeeName = "rwedfsf";
        final var wrongEmployeeName = employeeName + "1123";
        saveEmployee(wrongEmployeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .not().eq(createAttribute(Employee.NAME), employeeName)
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(wrongEmployeeName, employee.get().getName());
    }

    @Test
    void notEqDoesNotFindEqualFields() {
        final var employeeName = "rqwasd";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .not().eq(createAttribute(Employee.NAME), employeeName)
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertFalse(employee.isPresent());
    }

    private void saveEmployee(String name) {
        employeeRepository.saveAndFlush(
                Employee.builder()
                        .name(name)
                        .build()
        );
    }

    private static <Entity> Attribute<Entity, ?> createAttribute(String field) {
        return new Attribute<>() {
            @Override
            public String getName() {
                return field;
            }

            @Override
            public PersistentAttributeType getPersistentAttributeType() {
                return null;
            }

            @Override
            public ManagedType<Entity> getDeclaringType() {
                return null;
            }

            @Override
            public Class<Object> getJavaType() {
                return null;
            }

            @Override
            public Member getJavaMember() {
                return null;
            }

            @Override
            public boolean isAssociation() {
                return false;
            }

            @Override
            public boolean isCollection() {
                return false;
            }
        };
    }
}