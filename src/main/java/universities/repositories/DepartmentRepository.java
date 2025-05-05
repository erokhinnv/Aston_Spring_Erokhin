package universities.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import universities.entities.Department;

@Repository
public interface DepartmentRepository extends ListCrudRepository<Department, Integer> {
}
