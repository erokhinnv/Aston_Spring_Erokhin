package universities.controllers;

import com.google.gson.Gson;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import universities.dto.DepartmentCreationDto;
import universities.dto.DepartmentDto;
import universities.dto.DepartmentFullDto;
import universities.dto.DepartmentUpdateDto;
import universities.entities.Department;
import universities.entities.DepartmentFull;
import universities.entities.University;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import universities.services.DepartmentService;
import universities.utils.Mapper;
import universities.utils.ParseUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

class DepartmentControllerTest {
    @Test
    void testGetAll() {
        DepartmentService service;
        DepartmentController controller;
        ArrayList<Department> departments;
        Department department;
        University university, secondUniversity;
        Collection<DepartmentDto> response;
        String responseJson;

        departments = new ArrayList<>(2);
        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new Department();
        department.setId(1);
        department.setName("MEHMAT");
        department.setUniversity(university);
        departments.add(department);

        secondUniversity = new University();
        secondUniversity.setId(5);
        secondUniversity.setName("ITMO");
        secondUniversity.setCity("Saint-Petersburg");
        department = new Department();
        department.setId(2);
        department.setName("ITAP");
        department.setUniversity(secondUniversity);
        departments.add(department);

        service = Mockito.mock(DepartmentService.class);
        Mockito.doReturn(departments).when(service).get();

        controller = new DepartmentController(service, mapper);
        response = controller.getAll();
        responseJson = parser.toJson(response);

        Assertions.assertEquals("[{\"id\":1,\"university\":{\"id\":1,\"name\":\"PSTU\",\"city\":\"Perm\"},\"name\":\"MEHMAT\"},{\"id\":2,\"university\":{\"id\":5,\"name\":\"ITMO\",\"city\":\"Saint-Petersburg\"},\"name\":\"ITAP\"}]", responseJson);
    }

    @Test
    void testGetOne() {
        DepartmentService service;
        DepartmentController controller;
        DepartmentFull department;
        University university;
        ResponseEntity<DepartmentFullDto> response;
        String responseJson;

        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("Perm");
        department = new DepartmentFull();
        department.setId(1);
        department.setName("MEHMAT");
        department.setUniversity(university);

        service = Mockito.mock(DepartmentService.class);
        Mockito.doReturn(department).when(service).getById(department.getId());

        controller = new DepartmentController(service, mapper);
        response = controller.getOne(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.hasBody());
        responseJson = parser.toJson(response.getBody());
        Assertions.assertEquals("{\"id\":1,\"university\":{\"id\":1,\"name\":\"PSTU\",\"city\":\"Perm\"},\"name\":\"MEHMAT\"}", responseJson);
    }

    @Test
    void testGetNotFound() {
        DepartmentService service;
        DepartmentController controller;
        ResponseEntity<DepartmentFullDto> response;

        service = Mockito.mock(DepartmentService.class);
        Mockito.doReturn(null).when(service).getById(Mockito.anyInt());

        controller = new DepartmentController(service, mapper);
        response = controller.getOne(1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreate() {
        DepartmentService service;
        DepartmentController controller;
        DepartmentCreationDto request;
        DepartmentFullDto response;
        String responseJson;

        service = Mockito.mock(DepartmentService.class);
        Mockito.doAnswer(invocation -> {
            Department arg;
            DepartmentFull departmentFull;

            departmentFull = new DepartmentFull();
            arg = invocation.getArgument(0);
            arg.setId(24);
            departmentFull.setId(arg.getId());
            departmentFull.setName(arg.getName());
            departmentFull.setUniversity(arg.getUniversity());
            return departmentFull;
        }).when(service).add(Mockito.any(Department.class));

        request = new DepartmentCreationDto();
        request.name = "MEHMAT";
        request.universityId = 1;

        controller = new DepartmentController(service, mapper);
        response = controller.post(request);
        responseJson = parser.toJson(response);

        Assertions.assertEquals("{\"id\":24,\"university\":{\"id\":1},\"name\":\"MEHMAT\"}", responseJson);
    }

    @Test
    void testUpdate() {
        DepartmentService service;
        DepartmentController controller;
        DepartmentFull department;
        DepartmentUpdateDto request;
        ResponseEntity<DepartmentFullDto> response;
        String responseJson;

        department = new DepartmentFull();
        department.setId(24);

        service = Mockito.mock(DepartmentService.class);
        Mockito.doReturn(department).when(service).getById(department.getId());
        Mockito.doReturn(true).when(service).update(Mockito.any(Department.class));

        request = new DepartmentUpdateDto();
        request.name = Optional.of("MEHMAT");
        request.universityId = OptionalInt.of(1);

        controller = new DepartmentController(service, mapper);
        response = controller.patch(24, request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.hasBody());
        responseJson = parser.toJson(response.getBody());
        Assertions.assertEquals("{\"id\":24,\"university\":{\"id\":1},\"name\":\"MEHMAT\"}", responseJson);
    }

    @Test
    void testUpdateNotFound() {
        DepartmentService service;
        DepartmentController controller;
        DepartmentUpdateDto request;
        ResponseEntity<DepartmentFullDto> response;

        service = Mockito.mock(DepartmentService.class);
        Mockito.doReturn(null).when(service).getById(24);

        request = new DepartmentUpdateDto();

        controller = new DepartmentController(service, mapper);
        response = controller.patch(24, request);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDelete() {
        DepartmentService service;
        DepartmentController controller;
        ResponseEntity<Void> response;

        service = Mockito.mock(DepartmentService.class);
        Mockito.doReturn(true).when(service).delete(24);

        controller = new DepartmentController(service, mapper);
        response = controller.delete(24);

        Assertions.assertFalse(response.getStatusCode().isError());
    }

    @Test
    void testDeleteNotFound() {
        DepartmentService service;
        DepartmentController controller;
        ResponseEntity<Void> response;

        service = Mockito.mock(DepartmentService.class);
        Mockito.doReturn(null).when(service).getById(24);

        controller = new DepartmentController(service, mapper);
        response = controller.delete(24);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private final Mapper mapper = Mappers.getMapper(Mapper.class);
    private final Gson parser = ParseUtils.createParser();
}
