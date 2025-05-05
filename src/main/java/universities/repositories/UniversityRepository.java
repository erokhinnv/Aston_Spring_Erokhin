package universities.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import universities.entities.University;

@Repository
public interface UniversityRepository extends ListCrudRepository<University, Integer> {

}
