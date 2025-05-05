package universities.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import universities.dto.*;
import universities.entities.Department;
import jakarta.servlet.http.HttpServlet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import universities.services.DepartmentService;
import universities.utils.Mapper;

import java.util.Collection;

@RestController
@RequestMapping("/departments")
public class DepartmentController extends HttpServlet {

    DepartmentController(@Autowired DepartmentService service, @Autowired Mapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(path = {"", "/"})
    public Collection<DepartmentDto> getAll() {
        return getDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentFullDto> getOne(@PathVariable int id) {
        return ResponseEntity.ofNullable(getDepartment(id));
    }

    @PostMapping(path = {"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentFullDto post(@RequestBody DepartmentCreationDto creationDto) {
        return createDepartment(creationDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentFullDto> patch(@PathVariable int id, @RequestBody DepartmentUpdateDto updateDto)  {
        return ResponseEntity.ofNullable(updateDepartment(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return (deleteDepartment(id) ? ResponseEntity.ok() : ResponseEntity.notFound()).build();
    }

    private Collection<DepartmentDto> getDepartments() {
        Collection<Department> departments;
        Collection<DepartmentDto> result;

        departments = service.get();
        result = mapper.toDepartmentDtos(departments);
        return result;
    }

    private DepartmentFullDto getDepartment(int id) {
        DepartmentFullDto result;
        Department department;

        department = service.getById(id);
        result = mapper.toFullDto(department);
        return result;
    }

    private DepartmentFullDto createDepartment(DepartmentCreationDto creationDto) {
        DepartmentFullDto result;
        Department department;

        department = mapper.toDepartment(creationDto);
        department = service.add(department);
        result = mapper.toFullDto(department);
        return result;
    }

    private DepartmentFullDto updateDepartment(int id, DepartmentUpdateDto updateDto) {
        Department department;
        DepartmentFullDto result;

        result = null;
        department = service.getById(id);
        if (department != null) {
            mapper.toDepartment(updateDto, department);
            department = service.update(department);
            if (department != null) {
                result = mapper.toFullDto(department);
            }
        }
        return result;
    }

    private boolean deleteDepartment(int id) {
        return service.delete(id);
    }

    private final DepartmentService service;
    private final Mapper mapper;

}