package universities.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import universities.entities.Department;
import universities.entities.DepartmentFull;
import universities.entities.Professor;
import universities.entities.University;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DepartmentRepository extends Repository {
    public DepartmentRepository(@Autowired Connection connection) throws SQLException {
        super(connection);
    }

    public void add(Department department) throws SQLException {
        Connection connection;
        ResultSet resultSet;

        connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO departments (university_id, name)" +
                "VALUES (?, ?) returning id")) {
            preparedStatement.setInt(1, department.getUniversity().getId());
            preparedStatement.setString(2, department.getName());

            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            department.setId(resultSet.getInt(1));
        }

    }

    public boolean delete(int id) throws SQLException {
        Connection connection;
        ResultSet resultSet;
        boolean deleted;

        connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM departments WHERE id = ? returning id")) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            deleted = resultSet.next();
        }
        return deleted;
    }

    public boolean update(Department department) throws SQLException {
        Connection connection;
        ResultSet resultSet;
        boolean updated;

        connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE departments SET university_id = ?, name = ? " +
                "WHERE id = ? returning id")) {
            preparedStatement.setInt(1, department.getUniversity().getId());
            preparedStatement.setString(2, department.getName());
            preparedStatement.setInt(3, department.getId());
            resultSet = preparedStatement.executeQuery();
            updated = resultSet.next();
        }
        return updated;
    }

    public DepartmentFull getById(int id) throws SQLException {
        Connection connection;
        DepartmentFull department;

        connection = getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT d.name, u.id, u.name, u.city " +
                "FROM departments d INNER JOIN universities u ON d.university_id = u.id WHERE d.id = ?")) {
            ResultSet resultSet;

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                University university;

                department = new DepartmentFull();
                department.setId(id);
                department.setName(resultSet.getString(1));
                university = new University();
                university.setId(resultSet.getInt(2));
                university.setName(resultSet.getString(3));
                university.setCity(resultSet.getString(4));
                department.setUniversity(university);
            } else {
                department = null;
            }
        }

        if (department != null) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT name, phone_number, degree, birthday, id FROM professors WHERE department_id = ?")) {
                ResultSet resultSet;
                ArrayList<Professor> professors;

                statement.setInt(1, id);
                resultSet = statement.executeQuery();
                professors = new ArrayList<>();
                while (resultSet.next()) {
                    Professor professor;

                    professor = new Professor();
                    professor.setId(resultSet.getInt(5));
                    professor.setDepartment(department);
                    professor.setName(resultSet.getString(1));
                    professor.setPhoneNumber(resultSet.getString(2));
                    professor.setDegree(resultSet.getString(3));
                    professor.setBirthday(resultSet.getDate(4));
                    professors.add(professor);
                }
                department.setProfessors(professors);
            }
        }
        return department;
    }

    public Collection<Department> get() throws SQLException {
        Connection connection;
        ArrayList<Department> departments;
        HashMap<Integer, University> universities;
        ResultSet resultSet;

        connection = getConnection();
        departments = new ArrayList<>();
        universities = new HashMap<>();
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery("SELECT d.id, d.name, u.id, u.name, u.city " +
                    "FROM departments d INNER JOIN universities u ON d.university_id = u.id");
            while (resultSet.next()) {
                Department department;
                University university;
                int universityId;

                department = new Department();
                department.setId(resultSet.getInt(1));
                department.setName(resultSet.getString(2));
                universityId = resultSet.getInt(3);
                university = universities.get(universityId);
                if (university == null) {
                    university = new University();
                    university.setId(universityId);
                    university.setName(resultSet.getString(4));
                    university.setCity(resultSet.getString(5));
                    universities.put(universityId, university);
                }
                department.setUniversity(university);
                departments.add(department);
            }
        }
        return departments;
    }

}
