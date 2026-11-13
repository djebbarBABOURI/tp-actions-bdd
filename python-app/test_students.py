import psycopg2
import pytest

from students import add_student, average_grade, connect, get_student


@pytest.fixture
def conn():
    """Connexion à la base ; la table est vidée avant chaque test."""
    connection = connect()
    with connection.cursor() as cur:
        cur.execute("TRUNCATE students RESTART IDENTITY")
    connection.commit()
    yield connection
    connection.close()


def test_ajout_et_lecture(conn):
    new_id = add_student(conn, "Sara", 17.5)
    student = get_student(conn, new_id)
    assert student["name"] == "Sara"
    assert student["grade"] == 17.5


def test_etudiant_inexistant(conn):
    assert get_student(conn, 9999) is None


def test_moyenne(conn):
    add_student(conn, "Amel", 10)  # 1. Préparer : deux étudiants
    add_student(conn, "Karim", 14)
    assert average_grade(conn) == 13.0  # 2. Agir et 3. Vérifier : (10 + 14) / 2 = 12
