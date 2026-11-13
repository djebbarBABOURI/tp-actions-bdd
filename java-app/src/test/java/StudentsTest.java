import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentsTest {

    private Connection conn;

    // La table est vidée avant chaque test.
    @BeforeEach
    void setUp() throws SQLException {
        conn = Students.connect();
        try (Statement st = conn.createStatement()) {
            st.execute("TRUNCATE students RESTART IDENTITY");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.close();
    }

    @Test
    void ajoutEtLecture() throws SQLException {
        int id = Students.addStudent(conn, "Sara", 17.5);
        Optional<Students.Student> student = Students.getStudent(conn, id);
        assertTrue(student.isPresent());
        assertEquals("Sara", student.get().name());
        assertEquals(17.5, student.get().grade());
    }

    @Test
    void etudiantInexistant() throws SQLException {
        assertEquals(Optional.empty(), Students.getStudent(conn, 9999));
    }

    @Test
    void moyenne() throws SQLException {
        Students.addStudent(conn, "Amel", 10);
        Students.addStudent(conn, "Karim", 14);
        assertEquals(12.0, Students.averageGrade(conn));
    }
}
