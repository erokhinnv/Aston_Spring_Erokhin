package universities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import universities.entities.University;
import universities.exceptions.ValidationException;
import universities.repositories.UniversityRepository;

import java.util.Collection;

@Service
public class UniversityService {

    public UniversityService(@Autowired UniversityRepository repository) {
        this.repository = repository;
    }

    public University add(University university) {
        validate(university);
        return repository.save(university);
    }

    public boolean delete(int id) {
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
        }
        return exists;
    }

    public University update(University university) {
        validate(university);
        return repository.existsById(university.getId()) ? repository.save(university) : null;
    }

    public University getById(int id) {
        return repository.findById(id).orElse(null);

    }

    public Collection<University> get() {
        return repository.findAll();
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
