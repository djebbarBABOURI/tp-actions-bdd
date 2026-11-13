const { createPool, addStudent, getStudent, averageGrade } = require("./students");

let pool;

beforeAll(() => {
  pool = createPool();
});

// La table est vidée avant chaque test.
beforeEach(async () => {
  await pool.query("TRUNCATE students RESTART IDENTITY");
});

afterAll(async () => {
  await pool.end();
});

test("ajout et lecture", async () => {
  const id = await addStudent(pool, "Sara", 17.5);
  const student = await getStudent(pool, id);
  expect(student.name).toBe("Sara");
  expect(student.grade).toBe(17.5);
});

test("étudiant inexistant", async () => {
  expect(await getStudent(pool, 9999)).toBeNull();
});

test("moyenne", async () => {
  await addStudent(pool, "Amel", 10);
  await addStudent(pool, "Karim", 14);
  expect(await averageGrade(pool)).toBe(12);
});
