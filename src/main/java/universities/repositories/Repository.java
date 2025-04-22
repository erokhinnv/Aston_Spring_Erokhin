package universities.repositories;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Repository {

    protected Repository(Connection connection) throws SQLException {
        this.connection = connection;
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS public.universities" +
                    "(" +
                    "    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 )," +
                    "    name character varying COLLATE pg_catalog.\"default\" NOT NULL," +
                    "    city character varying COLLATE pg_catalog.\"default\" NOT NULL," +
                    "    CONSTRAINT universities_pkey PRIMARY KEY (id)" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS public.departments" +
                    "(" +
                    "    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 )," +
                    "    university_id integer NOT NULL," +
                    "    name character varying COLLATE pg_catalog.\"default\" NOT NULL," +
                    "    CONSTRAINT departments_pkey PRIMARY KEY (id)," +
                    "    CONSTRAINT university_department_fkey FOREIGN KEY (university_id)" +
                    "        REFERENCES public.universities (id) MATCH SIMPLE" +
                    "        ON UPDATE NO ACTION" +
                    "        ON DELETE CASCADE" +
                    "        NOT VALID" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS public.professors" +
                    "(" +
                    "    department_id integer NOT NULL," +
                    "    name character varying COLLATE pg_catalog.\"default\" NOT NULL," +
                    "    phone_number character varying COLLATE pg_catalog.\"default\" NOT NULL," +
                    "    degree character varying COLLATE pg_catalog.\"default\" NOT NULL," +
                    "    birthday date NOT NULL," +
                    "    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),\n" +
                    "    CONSTRAINT professors_pkey PRIMARY KEY (id)," +
                    "    CONSTRAINT department_professor_fkey FOREIGN KEY (department_id)" +
                    "        REFERENCES public.departments (id) MATCH SIMPLE" +
                    "        ON UPDATE NO ACTION" +
                    "        ON DELETE CASCADE" +
                    "        NOT VALID" +
                    ")");
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    private final Connection connection;

}
