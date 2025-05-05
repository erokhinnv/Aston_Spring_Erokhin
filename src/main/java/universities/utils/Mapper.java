package universities.utils;

import org.mapstruct.*;
import universities.dto.*;
import universities.entities.*;

import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

@org.mapstruct.Mapper
public interface Mapper {
    UniversityDto toDto(University university);
    UniversityFullDto toFullDto(University university);
    @AfterMapping
    default void afterToFullDto(@MappingTarget UniversityFullDto dto) {
        if (dto == null || dto.departments == null) return;

        for (DepartmentDto department : dto.departments) {
            department.university = null;
        }
    }
    University toUniversity(UniversityCreationDto creationDto);
    @Mapping(source = "name", target = "name", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "city", target = "city", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUniversity(UniversityUpdateDto updateDto, @MappingTarget University university);
    Collection<UniversityDto> toUniversityDtos(Collection<University> universities);

    DepartmentDto toDto(Department department);
    DepartmentFullDto toFullDto(Department department);
    @AfterMapping
    default void afterToFullDto(@MappingTarget DepartmentFullDto dto) {
        if (dto == null || dto.professors == null) return;

        for (ProfessorDto professor : dto.professors) {
            professor.department = null;
        }
    }
    @Mapping(source = "universityId", target = "university.id")
    Department toDepartment(DepartmentCreationDto creationDto);
    @Mapping(source = "name", target = "name", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "universityId", target = "university.id", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toDepartment(DepartmentUpdateDto updateDto, @MappingTarget Department department);
    Collection<DepartmentDto> toDepartmentDtos(Collection<Department> departments);

    ProfessorDto toDto(Professor professor);
    @Mapping(source = "departmentId", target = "department.id")
    Professor toProfessor(ProfessorCreationDto creationDto);
    @Mapping(source = "name", target = "name", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "degree", target = "degree", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "birthday", target = "birthday", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "departmentId", target = "department.id", qualifiedByName = "unwrap", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toProfessor(ProfessorUpdateDto updateDto, @MappingTarget Professor professor);
    Collection<ProfessorDto> toProfessorDtos(Collection<Professor> professors);

    @Named("unwrap")
    default <T> T unwrap(Optional<T> optional) {
        return optional.orElse(null);
    }

    @Named("unwrap")
    default int unwrap(OptionalInt optional) {
        return optional.orElse(0);
    }
}
