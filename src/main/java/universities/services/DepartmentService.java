package universities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import universities.entities.Department;
import universities.entities.DepartmentFull;
import universities.entities.University;
import universities.exceptions.ValidationException;
import universities.repositories.DepartmentRepository;
import universities.repositories.UniversityRepository;

import java.sql.SQLException;
import java.util.Collection;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DepartmentService {

    public DepartmentService(@Autowired DepartmentRepository repository, @Autowired UniversityRepository universityRepository) {
        this.repository = repository;
        this.universityRepository = universityRepository;
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public DepartmentFull add(Department department) {
        try {
            validate(department);
            repository.add(department);
            return repository.getById(department.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public boolean delete(int id) {
        try {
            return repository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public boolean update(Department department) {
        try {
            validate(department);
            return repository.update(department);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public DepartmentFull getById(int id) {
        try {
            return repository.getById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public Collection<Department> get() {
        try {
            return repository.get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(Department department) throws SQLException {
        University university;

        if (department.getName() == null) {
            throw new ValidationException("Name of department cannot be null");
        }

        university = department.getUniversity();
        if (university == null || universityRepository.getById(university.getId()) == null) {
            throw new ValidationException("University of department does not exist");
        }
    }

    private final DepartmentRepository repository;
    private final UniversityRepository universityRepository;
}
