package universities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import universities.dto.*;
import universities.entities.University;
import universities.entities.UniversityFull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import universities.services.UniversityService;
import universities.utils.Mapper;

import java.util.Collection;

@RestController
@RequestMapping("/universities")
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class UniversityController {

    public UniversityController(@Autowired UniversityService service, @Autowired Mapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(path = {"", "/"})
    public Collection<UniversityDto> getAll() {
        return getUniversities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityFullDto> getOne(@PathVariable int id) {
        return ResponseEntity.ofNullable(getUniversity(id));
    }

    @PostMapping(path = {"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public UniversityFullDto post(@RequestBody UniversityCreationDto creationDto) {
        return createUniversity(creationDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UniversityFullDto> patch(@PathVariable int id, @RequestBody UniversityUpdateDto updateDto) {
        return ResponseEntity.ofNullable(updateUniversity(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return (deleteUniversity(id) ? ResponseEntity.ok() : ResponseEntity.notFound()).build();
    }

    private Collection<UniversityDto> getUniversities() {
        Collection<University> universities;
        Collection<UniversityDto> result;

        universities = service.get();
        result = mapper.toUniversityDtos(universities);
        return result;
    }

    private UniversityFullDto getUniversity(int id) {
        UniversityFull university;
        UniversityFullDto result;

        university = service.getById(id);
        result = mapper.toDto(university);
        return result;
    }

    private UniversityFullDto createUniversity(UniversityCreationDto creationDto) {
        University university;
        UniversityFull universityFull;
        UniversityFullDto result;

        university = mapper.toUniversity(creationDto);
        universityFull = service.add(university);
        result = mapper.toDto(universityFull);
        return result;
    }

    private UniversityFullDto updateUniversity(int id, UniversityUpdateDto updateDto) {
        UniversityFull university;
        UniversityFullDto result;

        result = null;
        university = service.getById(id);
        if (university != null) {
            mapper.toUniversity(updateDto, university);
            if (service.update(university)) {
                result = mapper.toDto(university);
            }
        }

        return result;
    }

    private boolean deleteUniversity(int id) {
        return service.delete(id);
    }

    private final UniversityService service;
    private final Mapper mapper;
}