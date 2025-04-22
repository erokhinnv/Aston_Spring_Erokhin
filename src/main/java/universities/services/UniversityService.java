package universities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import universities.entities.University;
import universities.entities.UniversityFull;
import universities.exceptions.ValidationException;
import universities.repositories.UniversityRepository;

import java.sql.SQLException;
import java.util.Collection;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class UniversityService {

    public UniversityService(@Autowired UniversityRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public UniversityFull add(University university) {
        validate(university);
        try {
            repository.add(university);
            return repository.getById(university.getId());
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
    public boolean update(University university) {
        validate(university);
        try {
            return repository.update(university);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public UniversityFull getById(int id) {
        try {
            return repository.getById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("java:S112") // Все необрабатываемые исключения считаем Internal Server Error (500)
    public Collection<University> get() {
        try {
            return repository.get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void validate(University university) {
        if (university.getName() == null) {
            throw new ValidationException("Name of university cannot be null");
        }
        if (university.getCity() == null) {
            throw new ValidationException("City of university cannot be null");
        }
    }

    private final UniversityRepository repository;
}
