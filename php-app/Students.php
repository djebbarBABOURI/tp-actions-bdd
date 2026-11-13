<?php
// Gestion des étudiants dans PostgreSQL (version PHP).

class Students
{
    /** Ouvre une connexion PDO à la base tp sur localhost. */
    public static function connect(): PDO
    {
        $pdo = new PDO(
            'pgsql:host=localhost;port=5432;dbname=tp',
            'postgres',
            'postgres'
        );
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        return $pdo;
    }

    /** Ajoute un étudiant et retourne son id. */
    public static function addStudent(PDO $pdo, string $name, float $grade): int
    {
        $stmt = $pdo->prepare(
            'INSERT INTO students (name, grade) VALUES (:name, :grade) RETURNING id'
        );
        $stmt->execute(['name' => $name, 'grade' => $grade]);
        return (int) $stmt->fetchColumn();
    }

    /** Retourne ['name' => ..., 'grade' => ...] ou null si l'étudiant n'existe pas. */
    public static function getStudent(PDO $pdo, int $id): ?array
    {
        $stmt = $pdo->prepare('SELECT name, grade FROM students WHERE id = :id');
        $stmt->execute(['id' => $id]);
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        if ($row === false) {
            return null;
        }
        return ['name' => $row['name'], 'grade' => (float) $row['grade']];
    }

    /** Retourne la moyenne des notes. */
    public static function averageGrade(PDO $pdo): float
    {
        $avg = $pdo->query('SELECT AVG(grade) FROM students')->fetchColumn();
        return (float) $avg;
    }
}
