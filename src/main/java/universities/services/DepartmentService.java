package universities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import universities.entities.Department;
import universities.entities.University;
import universities.exceptions.ValidationException;
import universities.repositories.DepartmentRepository;
import universities.repositories.UniversityRepository;

import java.util.Collection;

@Service
public class DepartmentService {

    public DepartmentService(@Autowired DepartmentRepository repository, @Autowired UniversityRepository universityRepository) {
        this.repository = repository;
        this.universityRepository = universityRepository;
    }

    public Department add(Department department) {
        validate(department);
        return repository.save(department);
    }

    public boolean delete(int id) {
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
        }
        return exists;
    }

    public Department update(Department department) {
        validate(department);
        return repository.existsById(department.getId()) ? repository.save(department) : null;
    }

    public Department getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Collection<Department> get() {
        return repository.findAll();
    }

    private void validate(Department department) {
        University university;

        if (department.getName() == null) {
            throw new ValidationException("Name of department cannot be null");
        }

        university = department.getUniversity();
        if (university == null || !universityRepository.existsById(university.getId())) {
            throw new ValidationException("University of department does not exist");
        }
    }

    private final DepartmentRepository repository;
    private final UniversityRepository universityRepository;
}
