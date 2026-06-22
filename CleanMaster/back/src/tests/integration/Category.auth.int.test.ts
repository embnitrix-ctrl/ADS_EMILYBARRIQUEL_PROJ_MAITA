import request from "supertest";
import bcrypt from "bcryptjs";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";
import { User } from "../../entities/User";
import { Category } from "../../entities/Category";

describe("Category Auth Integration", () => {
  let token: string;

  beforeAll(async () => {
    await AppDataSource.initialize();

    const userRepo = AppDataSource.getRepository(User);
    const categoryRepo = AppDataSource.getRepository(Category);

    // remove resíduos de execuções anteriores
    await userRepo.delete({ email: "teste@email.com" });
    await categoryRepo.delete({ name: "Limpeza" });

    const hashedPassword = await bcrypt.hash("123456", 10);
    await userRepo.save({
      email: "teste@email.com",
      password_hash: hashedPassword
    });

    const login = await request(app)
      .post("/api/v1/login")
      .send({
        email: "teste@email.com",
        password: "123456"
      });

    token = login.body.token;
  });

  afterAll(async () => {
    await AppDataSource.getRepository(Category).delete({ name: "Limpeza" });
    await AppDataSource.getRepository(User).delete({ email: "teste@email.com" });
    await AppDataSource.destroy();
  });

  test("deve listar categorias (rota pública)", async () => {
    const response = await request(app)
      .get("/api/v1/categories");
    expect(response.status).toBe(200);
  });

  test("NÃO deve criar categoria sem token", async () => {
    const response = await request(app)
      .post("/api/v1/categories")
      .send({
        name: "Limpeza"
      });
    expect(response.status).toBe(401);
  });

  test("deve criar categoria com token", async () => {
    const response = await request(app)
      .post("/api/v1/categories")
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Limpeza"
      });
    expect(response.status).toBe(201);
  });
});