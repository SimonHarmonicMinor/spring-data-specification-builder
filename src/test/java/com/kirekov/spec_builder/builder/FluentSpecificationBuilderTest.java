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

    @Test
    void likeFindsMatchedFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .like(createAttribute(Employee.NAME), "%plo%")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(employeeName, employee.get().getName());
    }

    @Test
    void likeDoesNotFindUnmatchedFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .like(createAttribute(Employee.NAME), "%een%")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertFalse(employee.isPresent());
    }

    @Test
    void notLikeFindsUnmatchedFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .not().like(createAttribute(Employee.NAME), "%yeena%")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(employeeName, employee.get().getName());
    }

    @Test
    void notLikeDoesNotFindMatchedFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .not().like(createAttribute(Employee.NAME), "%" + employeeName + "%")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertFalse(employee.isPresent());
    }

    @Test
    void likeIgnoreCaseFindsMatchedFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .likeIgnoreCase(Employee.NAME, "%" + employeeName.toLowerCase() + "%")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(employeeName, employee.get().getName());
    }

    @Test
    void likeIgnoreCaseDoesNotFindUnmatchedFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .likeIgnoreCase(Employee.NAME, "asdasd")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertFalse(employee.isPresent());
    }

    @Test
    void notLikeIgnoreCaseFindsUnmatchedFields() {
        final var employeeName = "EmployeeName";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .not().likeIgnoreCase(createAttribute(Employee.NAME), "asdasd")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(employeeName, employee.get().getName());
    }

    @Test
    void notLikeDoesNotFindUnmatchedFields() {
        final var employeeName = "asdasdgsdg";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .not().likeIgnoreCase(createAttribute(Employee.NAME), "%" + employeeName + "%")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertFalse(employee.isPresent());
    }

    @Test
    void specificationFindsMatchedFields() {
        final var employeeName = "t23gdfhadfhDF";
        saveEmployee(employeeName);
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .specification((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Employee.NAME), employeeName))
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(employeeName, employee.get().getName());
    }

    @Test
    void combinedWithAndRequiresAllMatched() {
        final var employeeName = "EmployeeName";
        final var age = 22;
        employeeRepository.saveAndFlush(
                Employee.builder()
                        .age(age)
                        .name(employeeName)
                        .build()
        );
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .eq(createAttribute(Employee.AGE), age)
                        .like(createAttribute(Employee.NAME), "%" + employeeName + "%")
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(employeeName, employee.get().getName());
        assertEquals(age, employee.get().getAge());
    }

    @Test
    void combinedWithAndFailsIfAnySpecIsFalse() {
        final var name = "asdad";
        final var age = 55;
        employeeRepository.saveAndFlush(
                Employee.builder()
                        .age(age)
                        .name(name)
                        .build()
        );
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithAnd()
                        .eq(createAttribute(Employee.NAME), name)
                        .eq(createAttribute(Employee.AGE), age + 1)
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertFalse(employee.isPresent());
    }

    @Test
    void combinedWithAndRequiresAnyMatch() {
        final var name = "dasdasga";
        final var age = 18;
        employeeRepository.saveAndFlush(
                Employee.builder()
                        .name(name)
                        .age(age)
                        .build()
        );
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithOr()
                        .eq(Employee.NAME, name + "11")
                        .eq(Employee.AGE, age)
                        .build();
        final var employee = employeeRepository.findOne(spec);
        assertTrue(employee.isPresent());
        assertEquals(name, employee.get().getName());
        assertEquals(age, employee.get().getAge());
    }

    @Test
    void combinedWithOrFailsIfAllSpecAreFalse() {
        final var name = "sagasg";
        final var age = 51;
        employeeRepository.saveAndFlush(
                Employee.builder()
                        .age(age)
                        .name(name)
                        .build()
        );
        final var spec =
                FluentSpecificationBuilder.<Employee>combinedWithOr()
                        .eq(Employee.NAME, name + "123")
                        .eq(Employee.AGE, age + 12)
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