package universities.controllers;

import com.google.gson.Gson;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import universities.dto.ProfessorCreationDto;
import universities.dto.ProfessorDto;
import universities.dto.ProfessorUpdateDto;
import universities.entities.Department;
import universities.entities.Professor;
import universities.entities.University;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import universities.services.ProfessorService;
import universities.utils.Mapper;
import universities.utils.ParseUtils;

import java.util.*;

class ProfessorControllerTest {
    @Test
    void testGetAll() {
        ProfessorService service;
        ProfessorController controller;
        HttpServletRequest request;
        ArrayList<Professor> professors;
        Professor professor;
        Department department, secondDepartment;
        University university;
        Collection<ProfessorDto> response;
        String responseJson;
        Date birthdate;

        professors = new ArrayList<>(2);
        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("MEHMAT");
        department.setUniversity(university);
        professor = new Professor();
        professor.setId(100);
        professor.setName("Ivan");
        professor.setPhoneNumber("+79998884334");
        professor.setDegree("PhD in Computer Science");
        birthdate = new Date();
        birthdate.setTime(0);
        professor.setBirthday(birthdate);
        professor.setDepartment(department);
        professors.add(professor);

        professor = new Professor();
        professor.setId(200);
        professor.setName("Petr");
        professor.setPhoneNumber("+79824863265");
        professor.setDegree("PhD in Technical Science");
        birthdate = new Date();
        birthdate.setTime(169344000);
        professor.setBirthday(birthdate);
        secondDepartment = new Department();
        secondDepartment.setId(2);
        secondDepartment.setName("ITAS");
        secondDepartment.setUniversity(university);
        professor.setDepartment(secondDepartment);
        professors.add(professor);

        service = Mockito.mock(ProfessorService.class);
        Mockito.doReturn(professors).when(service).get();

        request = Mockito.mock(HttpServletRequest.class);
        Mockito.doReturn(null).when(request).getPathInfo();

        controller = new ProfessorController(service, mapper);
        response = controller.getAll();
        responseJson = parser.toJson(response);

        Assertions.assertEquals("[{\"id\":100,\"department\":{\"id\":1,\"university\":{\"id\":1,\"name\":\"PSTU\",\"city\":\"Perm\"},\"name\":\"MEHMAT\"},\"name\":\"Ivan\",\"phone_number\":\"+79998884334\",\"degree\":\"PhD in Computer Science\",\"birthday\":\"1970-01-01\"},{\"id\":200,\"department\":{\"id\":2,\"university\":{\"id\":1,\"name\":\"PSTU\",\"city\":\"Perm\"},\"name\":\"ITAS\"},\"name\":\"Petr\",\"phone_number\":\"+79824863265\",\"degree\":\"PhD in Technical Science\",\"birthday\":\"1970-01-03\"}]", responseJson);
    }

    @Test
    void testGetOne() {
        ProfessorService service;
        ProfessorController controller;
        Professor professor;
        University university;
        Department department;
        ResponseEntity<ProfessorDto> response;
        String responseJson;
        Date birthdate;

        professor = new Professor();
        professor.setId(200);
        professor.setName("Petr");
        professor.setPhoneNumber("+79824863265");
        professor.setDegree("PhD in Technical Science");
        birthdate = new Date();
        birthdate.setTime(169344000);
        professor.setBirthday(birthdate);
        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("MEHMAT");
        department.setUniversity(university);
        professor.setDepartment(department);

        service = Mockito.mock(ProfessorService.class);
        Mockito.doReturn(professor).when(service).getById(professor.getId());

        controller = new ProfessorController(service, mapper);
        response = controller.getOne(200);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.hasBody());
        responseJson = parser.toJson(response.getBody());
        Assertions.assertEquals("{\"id\":200,\"department\":{\"id\":1,\"university\":{\"id\":1,\"name\":\"PSTU\",\"city\":\"Perm\"},\"name\":\"MEHMAT\"},\"name\":\"Petr\",\"phone_number\":\"+79824863265\",\"degree\":\"PhD in Technical Science\",\"birthday\":\"1970-01-03\"}", responseJson);
    }

    @Test
    void testGetNotFound() {
        ProfessorService service;
        ProfessorController controller;
        ResponseEntity<ProfessorDto> response;

        service = Mockito.mock(ProfessorService.class);
        Mockito.doReturn(null).when(service).getById(Mockito.anyInt());

        controller = new ProfessorController(service, mapper);
        response = controller.getOne(1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreate() {
        ProfessorService service;
        ProfessorController controller;
        ProfessorCreationDto request;
        ProfessorDto response;
        String responseJson;

        service = Mockito.mock(ProfessorService.class);
        Mockito.doAnswer(invocation -> {
            Professor arg;

            arg = invocation.getArgument(0);
            arg.setId(200);
            return arg;
        }).when(service).add(Mockito.any(Professor.class));

        request = new ProfessorCreationDto();
        request.name = "Petr";
        request.departmentId = 24;
        request.phoneNumber = "+79824863265";
        request.degree = "PhD in Technical Science";
        request.birthday = new Date(2*86400000);

        controller = new ProfessorController(service, mapper);
        response = controller.post(request);
        responseJson = parser.toJson(response);

        Assertions.assertEquals("{\"id\":200,\"department\":{\"id\":24},\"name\":\"Petr\",\"phone_number\":\"+79824863265\",\"degree\":\"PhD in Technical Science\",\"birthday\":\"1970-01-03\"}", responseJson);
    }

    @Test
    void testUpdate() {
        ProfessorService service;
        ProfessorController controller;
        Professor professor;
        ProfessorUpdateDto request;
        ResponseEntity<ProfessorDto> response;
        String responseJson;

        professor = new Professor();
        professor.setId(200);

        service = Mockito.mock(ProfessorService.class);
        Mockito.doReturn(professor).when(service).getById(professor.getId());
        Mockito.doReturn(professor).when(service).update(Mockito.any(Professor.class));

        request = new ProfessorUpdateDto();
        request.name = Optional.of("Petr");
        request.departmentId = OptionalInt.of(30);
        request.phoneNumber = Optional.of("+79824863265");
        request.degree = Optional.of("PhD in Technical Science");
        request.birthday = Optional.of(new Date(2*86400000));

        controller = new ProfessorController(service, mapper);
        response = controller.patch(200, request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.hasBody());
        responseJson = parser.toJson(response.getBody());
        Assertions.assertEquals("{\"id\":200,\"department\":{\"id\":30},\"name\":\"Petr\",\"phone_number\":\"+79824863265\",\"degree\":\"PhD in Technical Science\",\"birthday\":\"1970-01-03\"}", responseJson);
    }

    @Test
    void testUpdateNotFound() {
        ProfessorService service;
        ProfessorController controller;
        ProfessorUpdateDto request;
        ResponseEntity<ProfessorDto> response;

        service = Mockito.mock(ProfessorService.class);
        Mockito.doReturn(null).when(service).getById(200);

        request = new ProfessorUpdateDto();

        controller = new ProfessorController(service, mapper);
        response = controller.patch(200, request);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDelete() {
        ProfessorService service;
        ProfessorController controller;
        ResponseEntity<Void> response;

        service = Mockito.mock(ProfessorService.class);
        Mockito.doReturn(true).when(service).delete(200);

        controller = new ProfessorController(service, mapper);
        response = controller.delete(200);

        Assertions.assertFalse(response.getStatusCode().isError());
    }

    @Test
    void testDeleteNotFound() {
        ProfessorService service;
        ProfessorController controller;
        ResponseEntity<Void> response;

        service = Mockito.mock(ProfessorService.class);
        Mockito.doReturn(null).when(service).getById(200);

        controller = new ProfessorController(service, mapper);
        response = controller.delete(200);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private final Mapper mapper = Mappers.getMapper(Mapper.class);
    private final Gson parser = ParseUtils.createParser();
}
