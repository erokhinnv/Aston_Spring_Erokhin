package universities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import universities.entities.Department;
import universities.entities.Professor;
import universities.exceptions.ValidationException;
import universities.repositories.DepartmentRepository;
import universities.repositories.ProfessorRepository;

import java.util.Collection;

@Service
public class ProfessorService {

    public ProfessorService(@Autowired ProfessorRepository repository, @Autowired DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    public Professor add(Professor professor) {
        validate(professor);
        return repository.save(professor);
    }

    public boolean delete(int id) {
        boolean exists;

        exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
        }
        return exists;
    }

    public Professor update(Professor professor) {
        validate(professor);
        return repository.existsById(professor.getId()) ? repository.save(professor) : null;
    }

    public Professor getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Collection<Professor> get() {
        return repository.findAll();
    }

    private void validate(Professor professor) {
        Department department;

        if (professor.getName() == null) {
            throw new ValidationException("Professor's name cannot be null");
        }
        if (professor.getPhoneNumber() == null) {
            throw new ValidationException("Professor's phone number cannot be null");
        }
        if (professor.getDegree() == null) {
            throw new ValidationException("Professor's degree cannot be null");
        }
        if (professor.getBirthday() == null) {
            throw new ValidationException("Professor's birthday cannot be null");
        }

        department = professor.getDepartment();
        if (department == null || !departmentRepository.existsById(department.getId())) {
            throw new ValidationException("Department does not exist");
        }
    }

    private final ProfessorRepository repository;
    private final DepartmentRepository departmentRepository;
}
