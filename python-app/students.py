"""Gestion des étudiants dans PostgreSQL (version Python)."""
import psycopg2


def connect():
    """Ouvre une connexion à la base tp sur localhost."""
    return psycopg2.connect(
        host="localhost",
        port=5432,
        dbname="tp",
        user="postgres",
        password="postgres",
    )


def add_student(conn, name, grade):
    """Ajoute un étudiant et retourne son id."""
    with conn.cursor() as cur:
        cur.execute(
            "INSERT INTO students (name, grade) VALUES (%s, %s) RETURNING id",
            (name, grade),
        )
        new_id = cur.fetchone()[0]
    conn.commit()
    return new_id


def get_student(conn, student_id):
    """Retourne {'name': ..., 'grade': ...} ou None si l'étudiant n'existe pas."""
    with conn.cursor() as cur:
        cur.execute("SELECT name, grade FROM students WHERE id = %s", (student_id,))
        row = cur.fetchone()
    if row is None:
        return None
    return {"name": row[0], "grade": float(row[1])}


def average_grade(conn):
    """Retourne la moyenne des notes.

    TODO (exercice 2) : écrire une requête SQL avec AVG(grade),
    puis retourner le résultat sous forme de float.
    """
    raise NotImplementedError("average_grade n'est pas encore écrite")
