package universities.services;

import universities.entities.Department;
import universities.entities.Professor;
import universities.entities.University;
import universities.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import universities.repositories.DepartmentRepository;
import universities.repositories.ProfessorRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

class ProfessorServiceTest {
    @Test
    void testGet() {
        ArrayList<Professor> professors;
        DepartmentRepository departmentRepository;
        ProfessorRepository repository;
        ProfessorService service;

        professors = new ArrayList<>();
        professors.add(new Professor());
        professors.add(new Professor());
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        repository = Mockito.mock(ProfessorRepository.class);
        Mockito.when(repository.findAll()).thenReturn(professors);
        service = new ProfessorService(repository, departmentRepository);
        Assertions.assertIterableEquals(professors, service.get());
    }

    @Test
    void testGetById() {
        Professor professor;
        Department department;
        University university;
        DepartmentRepository departmentRepository;
        ProfessorRepository repository;
        ProfessorService service;
        Date birthdate;

        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("ITAS");
        department.setUniversity(university);
        professor = new Professor();
        professor.setName("Ivan");
        professor.setPhoneNumber("+79998884334");
        professor.setDegree("PhD in Computer Science");
        birthdate = new Date();
        birthdate.setTime(0);
        professor.setId(20);
        professor.setBirthday(birthdate);
        professor.setDepartment(department);
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        repository = Mockito.mock(ProfessorRepository.class);
        Mockito.doReturn(Optional.of(professor)).when(repository).findById(professor.getId());
        service = new ProfessorService(repository, departmentRepository);
        Assertions.assertNull(service.getById(professor.getId() + 1));
        Assertions.assertEquals(professor, service.getById(professor.getId()));
    }

    @Test
    void testValidAdd() {
        DepartmentRepository departmentRepository;
        ProfessorRepository repository;
        ProfessorService service;
        Professor professor;
        Department department;
        University university;
        Date birthdate;

        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("ITAS");
        department.setUniversity(university);
        professor = new Professor();
        professor.setName("Ivan");
        professor.setPhoneNumber("+79998884334");
        professor.setDegree("PhD in Computer Science");
        birthdate = new Date();
        birthdate.setTime(0);
        professor.setBirthday(birthdate);
        professor.setDepartment(department);
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        Mockito.doReturn(true).when(departmentRepository).existsById(department.getId());
        repository = Mockito.mock(ProfessorRepository.class);
        Mockito.doAnswer(invocation -> {
            Professor professorArg;

            professorArg = invocation.getArgument(0);
            professorArg.setId(1);
            return professorArg;
        }).when(repository).save(professor);
        service = new ProfessorService(repository, departmentRepository);
        service.add(professor);
        Assertions.assertEquals(1, professor.getId());
    }

    @Test
    void testInvalidAdd() {
        DepartmentRepository departmentRepository;
        ProfessorRepository repository;
        ProfessorService service;
        Professor professor;
        Department department;
        University university;
        Date birthdate;

        professor = new Professor();
        professor.setName("Ivan");
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        repository = Mockito.mock(ProfessorRepository.class);
        service = new ProfessorService(repository, departmentRepository);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.add(professor);
        });
        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("ITAS");
        department.setUniversity(university);
        professor.setName(null);
        professor.setPhoneNumber("+79998884334");
        professor.setDegree("PhD in Computer Science");
        birthdate = new Date();
        birthdate.setTime(0);
        professor.setBirthday(birthdate);
        professor.setDepartment(department);
        Mockito.doReturn(true).when(departmentRepository).existsById(department.getId());
        Assertions.assertThrows(ValidationException.class, () -> {
            service.add(professor);
        });
    }

    @Test
    void testValidUpdate() {
        DepartmentRepository departmentRepository;
        ProfessorRepository repository;
        ProfessorService service;
        Professor professor;
        Department department;
        University university;
        Date birthdate;

        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("ITAS");
        department.setUniversity(university);
        professor = new Professor();
        professor.setName("Ivan");
        professor.setPhoneNumber("+79998884334");
        professor.setDegree("PhD in Computer Science");
        birthdate = new Date();
        birthdate.setTime(0);
        professor.setBirthday(birthdate);
        professor.setDepartment(department);
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        Mockito.doReturn(true).when(departmentRepository).existsById(department.getId());
        repository = Mockito.mock(ProfessorRepository.class);
        Mockito.doAnswer(invocation -> {
            Professor professorArg;

            professorArg = invocation.getArgument(0);
            return professorArg;
        }).when(repository).save(professor);
        Mockito.doAnswer(invocation -> {
            Integer idArg;

            idArg = invocation.getArgument(0);
            return idArg == 100;
        }).when(repository).existsById(Mockito.anyInt());
        service = new ProfessorService(repository, departmentRepository);
        professor.setId(100);
        Assertions.assertNotNull(service.update(professor));
        professor.setId(101);
        Assertions.assertNull(service.update(professor));
    }

    @Test
    void testInvalidUpdate() {
        DepartmentRepository departmentRepository;
        ProfessorRepository repository;
        ProfessorService service;
        Professor professor;
        Department department;
        University university;
        Date birthdate;

        professor = new Professor();
        professor.setName("Ivan");
        departmentRepository = Mockito.mock(DepartmentRepository.class);
        repository = Mockito.mock(ProfessorRepository.class);
        service = new ProfessorService(repository, departmentRepository);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.update(professor);
        });
        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("ITAS");
        department.setUniversity(university);
        professor.setName(null);
        professor.setPhoneNumber("+79998884334");
        professor.setDegree("PhD in Computer Science");
        birthdate = new Date();
        birthdate.setTime(0);
        professor.setBirthday(birthdate);
        professor.setDepartment(department);
        Mockito.doReturn(true).when(departmentRepository).existsById(department.getId());
        Assertions.assertThrows(ValidationException.class, () -> {
            service.update(professor);
        });
    }

    @Test
    void testDelete() {
        DepartmentRepository departmentRepository;
        ProfessorRepository repository;
        ProfessorService service;

        departmentRepository = Mockito.mock(DepartmentRepository.class);
        repository = Mockito.mock(ProfessorRepository.class);
        Mockito.doAnswer(invocation -> {
            Integer idArg;

            idArg = invocation.getArgument(0);
            return idArg == 10;
        }).when(repository).existsById(Mockito.anyInt());
        service = new ProfessorService(repository, departmentRepository);
        Assertions.assertTrue(service.delete(10));
        Assertions.assertFalse(service.delete(11));
    }
}
