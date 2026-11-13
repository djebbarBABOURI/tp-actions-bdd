// Gestion des étudiants dans PostgreSQL (version JavaScript).
const { Pool } = require("pg");

function createPool() {
  return new Pool({
    host: "localhost",
    port: 5432,
    database: "tp",
    user: "postgres",
    password: "postgres",
  });
}

// Ajoute un étudiant et retourne son id.
async function addStudent(pool, name, grade) {
  const res = await pool.query(
    "INSERT INTO students (name, grade) VALUES ($1, $2) RETURNING id",
    [name, grade]
  );
  return res.rows[0].id;
}

// Retourne { name, grade } ou null si l'étudiant n'existe pas.
async function getStudent(pool, id) {
  const res = await pool.query(
    "SELECT name, grade FROM students WHERE id = $1",
    [id]
  );
  if (res.rows.length === 0) return null;
  return { name: res.rows[0].name, grade: parseFloat(res.rows[0].grade) };
}

// Retourne la moyenne des notes.
async function averageGrade(pool) {
  const res = await pool.query("SELECT AVG(grade) AS avg FROM students");
  return parseFloat(res.rows[0].avg);
}

module.exports = { createPool, addStudent, getStudent, averageGrade };
