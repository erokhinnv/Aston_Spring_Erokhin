package universities.services;

import universities.entities.Department;
import universities.entities.University;
import universities.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import universities.repositories.DepartmentRepository;
import universities.repositories.UniversityRepository;


import java.util.ArrayList;
import java.util.Optional;

class DepartmentServiceTest {
    @Test
    void testGet() {
        ArrayList<Department> departments;
        UniversityRepository universityRepository;
        DepartmentRepository repository;
        DepartmentService service;

        departments = new ArrayList<>();
        departments.add(new Department());
        departments.add(new Department());
        universityRepository = Mockito.mock(UniversityRepository.class);
        repository = Mockito.mock(DepartmentRepository.class);
        Mockito.when(repository.findAll()).thenReturn(departments);
        service = new DepartmentService(repository, universityRepository);
        Assertions.assertIterableEquals(departments, service.get());
    }

    @Test
    void testGetById() {
        Department department;
        University university;
        UniversityRepository universityRepository;
        DepartmentRepository repository;
        DepartmentService service;

        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setName("PSTU");
        department.setUniversity(university);
        department.setId(1);
        universityRepository = Mockito.mock(UniversityRepository.class);
        repository = Mockito.mock(DepartmentRepository.class);
        Mockito.doReturn(Optional.of(department)).when(repository).findById(department.getId());
        service = new DepartmentService(repository, universityRepository);
        Assertions.assertNull(service.getById(department.getId() + 1));
        Assertions.assertEquals(department, service.getById(department.getId()));
    }

    @Test
    void testValidAdd() {
        UniversityRepository universityRepository;
        DepartmentRepository repository;
        DepartmentService service;
        Department department;
        University university;

        university = new University();
        university.setId(5);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setName("ITAS");
        department.setUniversity(university);
        universityRepository = Mockito.mock(UniversityRepository.class);
        Mockito.doReturn(true).when(universityRepository).existsById(university.getId());
        repository = Mockito.mock(DepartmentRepository.class);
        Mockito.doAnswer(invocation -> {
            Department departmentArg;

            departmentArg = invocation.getArgument(0);
            departmentArg.setId(1);
            return departmentArg;
        }).when(repository).save(department);
        service = new DepartmentService(repository, universityRepository);
        service.add(department);
        Assertions.assertEquals(1, department.getId());
    }

    @Test
    void testInvalidAdd() {
        UniversityRepository universityRepository;
        DepartmentRepository repository;
        DepartmentService service;
        Department department;
        University university;

        university = new University();
        university.setId(2);
        university.setName("PSU");
        university.setCity("Perm");
        department = new Department();
        department.setName("ITAS");
        universityRepository = Mockito.mock(UniversityRepository.class);
        repository = Mockito.mock(DepartmentRepository.class);
        service = new DepartmentService(repository, universityRepository);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.add(department);
        });
        department.setName(null);
        department.setUniversity(university);
        Mockito.doReturn(true).when(universityRepository).existsById(university.getId());
        Assertions.assertThrows(ValidationException.class, () -> {
            service.add(department);
        });
    }

    @Test
    void testValidUpdate() {
        UniversityRepository universityRepository;
        DepartmentRepository repository;
        DepartmentService service;
        Department department;
        University university;

        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setName("ITAS");
        department.setUniversity(university);
        universityRepository = Mockito.mock(UniversityRepository.class);
        repository = Mockito.mock(DepartmentRepository.class);
        Mockito.doReturn(true).when(universityRepository).existsById(university.getId());
        Mockito.doAnswer(invocation -> {
            Department departmentArg;

            departmentArg = invocation.getArgument(0);
            return departmentArg;
        }).when(repository).save(department);
        Mockito.doAnswer(invocation -> {
            Integer idArg;

            idArg = invocation.getArgument(0);
            return idArg == 10;
        }).when(repository).existsById(Mockito.anyInt());
        service = new DepartmentService(repository, universityRepository);
        department.setId(10);
        Assertions.assertNotNull(service.update(department));
        department.setId(11);
        Assertions.assertNull(service.update(department));
    }

    @Test
    void testInvalidUpdate() {
        UniversityRepository universityRepository;
        DepartmentRepository repository;
        DepartmentService service;
        Department department;
        University university;

        university = new University();
        university.setId(2);
        university.setName("PSU");
        university.setCity("Perm");
        department = new Department();
        department.setName("IT");
        universityRepository = Mockito.mock(UniversityRepository.class);
        repository = Mockito.mock(DepartmentRepository.class);
        service = new DepartmentService(repository, universityRepository);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.update(department);
        });
        department.setName(null);
        department.setUniversity(university);
        Mockito.doReturn(true).when(universityRepository).existsById(university.getId());
        Assertions.assertThrows(ValidationException.class, () -> {
            service.update(department);
        });
    }

    @Test
    void testDelete() {
        UniversityRepository universityRepository;
        DepartmentRepository repository;
        DepartmentService service;

        universityRepository = Mockito.mock(UniversityRepository.class);
        repository = Mockito.mock(DepartmentRepository.class);
        Mockito.doAnswer(invocation -> {
            Integer idArg;

            idArg = invocation.getArgument(0);
            return idArg == 10;
        }).when(repository).existsById(Mockito.anyInt());
        service = new DepartmentService(repository, universityRepository);
        Assertions.assertTrue(service.delete(10));
        Assertions.assertFalse(service.delete(11));
    }
}
