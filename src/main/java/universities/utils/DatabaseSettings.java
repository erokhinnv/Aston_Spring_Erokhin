package universities.utils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S3008"}) // Поля намеренно делаем публичными
public class DatabaseSettings {
    public static String URL = "jdbc:postgresql://localhost:5432/mydb2";
    public static String USERNAME = "postgres";
    public static String PASSWORD = "q";

    private DatabaseSettings() {
    }
}
