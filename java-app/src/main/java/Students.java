import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Gestion des étudiants dans PostgreSQL (version Java). */
public class Students {

    /** Un étudiant : son nom et sa note. */
    public record Student(String name, double grade) {}

    /** Ouvre une connexion à la base tp sur localhost. */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/tp", "postgres", "postgres");
    }

    /** Ajoute un étudiant et retourne son id. */
    public static int addStudent(Connection conn, String name, double grade) throws SQLException {
        String sql = "INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setBigDecimal(2, java.math.BigDecimal.valueOf(grade));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    /** Retourne l'étudiant, ou Optional.empty() s'il n'existe pas. */
    public static Optional<Student> getStudent(Connection conn, int id) throws SQLException {
        String sql = "SELECT name, grade FROM students WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Student(rs.getString("name"), rs.getDouble("grade")));
            }
        }
    }

    /** Retourne la moyenne des notes. */
    public static double averageGrade(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT AVG(grade) FROM students");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getDouble(1);
        }
    }
}
