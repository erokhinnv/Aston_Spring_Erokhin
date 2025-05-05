package universities.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import universities.dto.ProfessorCreationDto;
import universities.dto.ProfessorDto;
import universities.dto.ProfessorUpdateDto;
import universities.entities.Professor;
import jakarta.servlet.http.HttpServlet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import universities.services.ProfessorService;
import universities.utils.Mapper;

import java.util.Collection;

@RestController
@RequestMapping("/professors")
public class ProfessorController extends HttpServlet {
    public ProfessorController(@Autowired ProfessorService service, @Autowired Mapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping(path = {"", "/"})
    public Collection<ProfessorDto> getAll() {
        return getProfessors();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDto> getOne(@PathVariable int id) {
        return ResponseEntity.ofNullable(getProfessor(id));
    }

    @PostMapping(path = {"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessorDto post(@RequestBody ProfessorCreationDto creationDto) {
        return createProfessor(creationDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfessorDto> patch(@PathVariable int id, @RequestBody ProfessorUpdateDto updateDto) {
        return ResponseEntity.ofNullable(updateProfessor(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return (deleteProfessor(id) ? ResponseEntity.ok() : ResponseEntity.notFound()).build();
    }

    private Collection<ProfessorDto> getProfessors() {
        Collection<Professor> professors;
        Collection<ProfessorDto> result;

        professors = service.get();
        result = mapper.toProfessorDtos(professors);
        return result;
    }

    private ProfessorDto getProfessor(int id) {
        ProfessorDto result;
        Professor professor;

        professor = service.getById(id);
        result = mapper.toDto(professor);
        return result;
    }

    private ProfessorDto createProfessor(ProfessorCreationDto creationDto) {
        ProfessorDto result;
        Professor professor;

        professor = mapper.toProfessor(creationDto);
        professor = service.add(professor);
        result = mapper.toDto(professor);
        return result;
    }

    private ProfessorDto updateProfessor(int id, ProfessorUpdateDto updateDto) {
        Professor professor;
        ProfessorDto result;

        result = null;
        professor = service.getById(id);
        if (professor != null) {
            mapper.toProfessor(updateDto, professor);
            professor = service.update(professor);
            if (professor != null) {
                result = mapper.toDto(professor);
            }
        }

        return result;
    }

    private boolean deleteProfessor(int id) {
        return service.delete(id);
    }

    private final ProfessorService service;
    private final Mapper mapper;
}
