package universities.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import universities.entities.Professor;

@Repository
public interface ProfessorRepository extends ListCrudRepository<Professor, Integer> {
}
