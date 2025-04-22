package universities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import universities.entities.Department;
import universities.entities.Professor;
import universities.exceptions.ValidationException;
import universities.repositories.DepartmentRepository;
import universities.repositories.ProfessorRepository;

import java.sql.SQLException;
import java.util.Collection;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ProfessorService {

    public ProfessorService(@Autowired ProfessorRepository repository, @Autowired DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public Professor add(Professor professor) {
        try {
            validate(professor);
            repository.add(professor);
            return repository.getById(professor.getId());
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
    public boolean update(Professor professor) {
        try {
            validate(professor);
            return repository.update(professor);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public Professor getById(int id) {
        try {
            return repository.getById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public Collection<Professor> get() {
        try {
            return repository.get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validate(Professor professor) throws SQLException {
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
        if (department == null || departmentRepository.getById(department.getId()) == null) {
            throw new ValidationException("Department does not exist");
        }
    }

    private final ProfessorRepository repository;
    private final DepartmentRepository departmentRepository;
}
