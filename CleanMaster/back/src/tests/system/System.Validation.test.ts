import request from "supertest";
import bcrypt from "bcryptjs";
import { MoreThan } from "typeorm";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";
import { User } from "../../entities/User";
import { Category } from "../../entities/Category";
import { Product } from "../../entities/Product";

describe("System Test - Validation Flow", () => {

  let token: string;
  let categoryId: number;

  beforeAll(async () => {
    await AppDataSource.initialize();

    const userRepo = AppDataSource.getRepository(User);
    const categoryRepo = AppDataSource.getRepository(Category);
    const productRepo = AppDataSource.getRepository(Product);

    await productRepo.delete({ id: MoreThan(0) });
    await categoryRepo.delete({ id: MoreThan(0) });
    await userRepo.delete({ email: "system.validation@test.com" });

    const hash = await bcrypt.hash("123456", 10);

    await userRepo.save({
      email: "system.validation@test.com",
      password_hash: hash
    });

    const login = await request(app)
      .post("/api/v1/login")
      .send({
        email: "system.validation@test.com",
        password: "123456"
      });

    token = login.body.token;

    const catRes = await request(app)
      .post("/api/v1/categories")
      .set("Authorization", `Bearer ${token}`)
      .send({ name: "Categoria Validação" });

    categoryId = catRes.body.id;
  });

  afterAll(async () => {
    await AppDataSource.getRepository(Product).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(Category).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(User).delete({ email: "system.validation@test.com" });

    await AppDataSource.destroy();
  });

  test("NÃO deve criar categoria sem nome", async () => {
    const response = await request(app)
      .post("/api/v1/categories")
      .set("Authorization", `Bearer ${token}`)
      .send({});

    expect(response.status).toBe(400);
  });

  test("NÃO deve criar produto sem nome", async () => {
    const response = await request(app)
      .post("/api/v1/products")
      .set("Authorization", `Bearer ${token}`)
      .send({
        price: 10,
        categoryId
      });

    expect(response.status).toBe(400);
  });

  test("NÃO deve criar produto sem preço", async () => {
    const response = await request(app)
      .post("/api/v1/products")
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Produto Sem Preço",
        categoryId
      });

    expect(response.status).toBe(400);
  });

  test("NÃO deve criar produto com categoryId inexistente", async () => {
    const response = await request(app)
      .post("/api/v1/products")
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Produto Categoria Fantasma",
        price: 10,
        categoryId: 999999
      });

    expect(response.status).toBe(404);
  });

  test("NÃO deve buscar produto com ID inexistente", async () => {
    const response = await request(app)
      .get("/api/v1/products/999999");

    expect(response.status).toBe(404);
  });

  test("NÃO deve buscar categoria com ID inexistente", async () => {
    const response = await request(app)
      .get("/api/v1/categories/999999");

    expect(response.status).toBe(404);
  });

});