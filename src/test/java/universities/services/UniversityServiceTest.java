package universities.services;

import universities.entities.University;
import universities.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import universities.repositories.UniversityRepository;

import java.util.ArrayList;
import java.util.Optional;

class UniversityServiceTest {
    @Test
    void testGet() {
        ArrayList<University> universities;
        UniversityRepository repository;
        UniversityService service;

        universities = new ArrayList<>();
        universities.add(new University());
        universities.add(new University());
        repository = Mockito.mock(UniversityRepository.class);
        Mockito.when(repository.findAll()).thenReturn(universities);
        service = new UniversityService(repository);
        Assertions.assertIterableEquals(universities, service.get());
    }

    @Test
    void testGetById() {
        University university;
        UniversityRepository repository;
        UniversityService service;

        university = new University();
        university.setName("PSTU");
        university.setCity("Perm");
        university.setId(1);
        repository = Mockito.mock(UniversityRepository.class);
        Mockito.doReturn(Optional.of(university)).when(repository).findById(university.getId());
        service = new UniversityService(repository);
        Assertions.assertNull(service.getById(university.getId() + 1));
        Assertions.assertEquals(university, service.getById(university.getId()));
    }

    @Test
    void testValidAdd() {
        UniversityRepository repository;
        UniversityService service;
        University university;

        university = new University();
        university.setName("PSTU");
        university.setCity("Perm");
        repository = Mockito.mock(UniversityRepository.class);
        Mockito.doAnswer(invocation -> {
            University universityArg;

            universityArg = invocation.getArgument(0);
            universityArg.setId(1);
            return universityArg;
        }).when(repository).save(university);
        service = new UniversityService(repository);
        university = service.add(university);
        Assertions.assertEquals(1, university.getId());
    }

    @Test
    void testInvalidAdd() {
        UniversityRepository repository;
        UniversityService service;
        University university;

        university = new University();
        university.setCity("Perm");
        repository = Mockito.mock(UniversityRepository.class);
        service = new UniversityService(repository);
        Assertions.assertThrows(ValidationException.class, () -> {
           service.add(university);
        });
        university.setName("PSTU");
        university.setCity(null);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.add(university);
        });
    }

    @Test
    void testValidUpdate() {
        UniversityRepository repository;
        UniversityService service;
        University university;

        university = new University();
        university.setName("PSTU");
        university.setCity("Perm");
        repository = Mockito.mock(UniversityRepository.class);
        Mockito.doAnswer(invocation -> {
            University universityArg;

            universityArg = invocation.getArgument(0);
            return universityArg;
        }).when(repository).save(university);
        Mockito.doAnswer(invocation -> {
            Integer idArg;

            idArg = invocation.getArgument(0);
            return idArg == 10;
        }).when(repository).existsById(Mockito.anyInt());
        service = new UniversityService(repository);
        university.setId(10);
        Assertions.assertNotNull(service.update(university));
        university.setId(11);
        Assertions.assertNull(service.update(university));
    }

    @Test
    void testInvalidUpdate() {
        UniversityRepository repository;
        UniversityService service;
        University university;

        university = new University();
        university.setCity("Perm");
        repository = Mockito.mock(UniversityRepository.class);
        service = new UniversityService(repository);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.update(university);
        });
        university.setName("PSTU");
        university.setCity(null);
        Assertions.assertThrows(ValidationException.class, () -> {
            service.update(university);
        });
    }

    @Test
    void testDelete() {
        UniversityRepository repository;
        UniversityService service;

        repository = Mockito.mock(UniversityRepository.class);
        Mockito.doAnswer(invocation -> {
            Integer idArg;

            idArg = invocation.getArgument(0);
            return idArg == 10;
        }).when(repository).existsById(Mockito.anyInt());
        service = new UniversityService(repository);
        Assertions.assertTrue(service.delete(10));
        Assertions.assertFalse(service.delete(11));
    }

}
