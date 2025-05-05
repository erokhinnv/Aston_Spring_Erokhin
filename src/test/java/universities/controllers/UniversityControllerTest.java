package universities.controllers;

import com.google.gson.Gson;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import universities.dto.UniversityCreationDto;
import universities.dto.UniversityDto;
import universities.dto.UniversityFullDto;
import universities.dto.UniversityUpdateDto;
import universities.entities.Department;
import universities.entities.University;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import universities.services.UniversityService;
import universities.utils.Mapper;
import universities.utils.ParseUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

class UniversityControllerTest {
    @Test
    void testGetAll() {
        UniversityService service;
        UniversityController controller;
        ArrayList<University> universities;
        University university;
        Collection<UniversityDto> response;
        String responseJson;

        universities = new ArrayList<>(2);
        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("PERM");
        universities.add(university);

        university = new University();
        university.setId(2);
        university.setName("SPBSU");
        university.setCity("SPB");
        universities.add(university);

        service = Mockito.mock(UniversityService.class);
        Mockito.doReturn(universities).when(service).get();

        controller = new UniversityController(service, mapper);
        response = controller.getAll();
        responseJson = parser.toJson(response);

        Assertions.assertEquals("[{\"id\":1,\"name\":\"PSTU\",\"city\":\"PERM\"},{\"id\":2,\"name\":\"SPBSU\",\"city\":\"SPB\"}]", responseJson);
    }

    @Test
    void testGetOne() {
        UniversityService service;
        UniversityController controller;
        University university;
        Department first, second;
        ArrayList<Department> departments;
        ResponseEntity<UniversityFullDto> response;
        String responseJson;

        departments = new ArrayList<>();
        university = new University();
        university.setId(1);
        university.setName("PSTU");
        university.setCity("PERM");
        first = new Department();
        first.setId(1);
        first.setName("ITAS");
        first.setUniversity(university);
        departments.add(first);
        second = new Department();
        second.setId(2);
        second.setName("AT");
        second.setUniversity(university);
        departments.add(second);
        university.setDepartments(departments);

        service = Mockito.mock(UniversityService.class);
        Mockito.doReturn(university).when(service).getById(university.getId());

        controller = new UniversityController(service, mapper);
        response = controller.getOne(1);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.hasBody());
        responseJson = parser.toJson(response.getBody());
        Assertions.assertEquals("{\"departments\":[{\"id\":1,\"name\":\"ITAS\"},{\"id\":2,\"name\":\"AT\"}],\"id\":1,\"name\":\"PSTU\",\"city\":\"PERM\"}", responseJson);
    }

    @Test
    void testGetNotFound() {
        UniversityService service;
        UniversityController controller;
        ResponseEntity<UniversityFullDto> response;

        service = Mockito.mock(UniversityService.class);
        Mockito.doReturn(null).when(service).getById(Mockito.anyInt());

        controller = new UniversityController(service, mapper);
        response = controller.getOne(1);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreate() {
        UniversityService service;
        UniversityController controller;
        UniversityCreationDto request;
        UniversityFullDto response;
        String responseJson;

        service = Mockito.mock(UniversityService.class);
        Mockito.doAnswer(invocation -> {
            University arg;
            University universityFull;

            universityFull = new University();
            arg = invocation.getArgument(0);
            arg.setId(10);
            universityFull.setId(arg.getId());
            universityFull.setName(arg.getName());
            universityFull.setCity(arg.getCity());
            return universityFull;
        }).when(service).add(Mockito.any(University.class));

        request = new UniversityCreationDto();
        request.name = "PSTU";
        request.city = "PERM";

        controller = new UniversityController(service, mapper);
        response = controller.post(request);
        responseJson = parser.toJson(response);

        Assertions.assertEquals("{\"id\":10,\"name\":\"PSTU\",\"city\":\"PERM\"}", responseJson);
    }

    @Test
    void testUpdate() {
        UniversityService service;
        UniversityController controller;
        UniversityUpdateDto request;
        University university;
        ResponseEntity<UniversityFullDto> response;
        String responseJson;

        university = new University();
        university.setId(12);

        service = Mockito.mock(UniversityService.class);
        Mockito.doReturn(university).when(service).getById(university.getId());
        Mockito.doReturn(university).when(service).update(Mockito.any(University.class));

        request = new UniversityUpdateDto();
        request.name = Optional.of("SPBSU");
        request.city = Optional.of("SPB");

        controller = new UniversityController(service, mapper);
        response = controller.patch(university.getId(), request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.hasBody());
        responseJson = parser.toJson(response.getBody());
        Assertions.assertEquals("{\"id\":12,\"name\":\"SPBSU\",\"city\":\"SPB\"}", responseJson);
    }

    @Test
    void testUpdateNotFound() {
        UniversityService service;
        UniversityController controller;
        UniversityUpdateDto request;
        ResponseEntity<UniversityFullDto> response;

        service = Mockito.mock(UniversityService.class);
        Mockito.doReturn(null).when(service).getById(12);

        request = new UniversityUpdateDto();

        controller = new UniversityController(service, mapper);
        response = controller.patch(12, request);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDelete() {
        UniversityService service;
        UniversityController controller;
        ResponseEntity<Void> response;

        service = Mockito.mock(UniversityService.class);
        Mockito.doReturn(true).when(service).delete(12);

        controller = new UniversityController(service, mapper);
        response = controller.delete(12);

        Assertions.assertFalse(response.getStatusCode().isError());
    }

    @Test
    void testDeleteNotFound() {
        UniversityService service;
        UniversityController controller;
        ResponseEntity<Void> response;

        service = Mockito.mock(UniversityService.class);
        Mockito.doReturn(false).when(service).delete(12);

        controller = new UniversityController(service, mapper);
        response = controller.delete(12);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private final Mapper mapper = Mappers.getMapper(Mapper.class);
    private final Gson parser = ParseUtils.createParser();
}
