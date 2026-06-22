import request from "supertest";
import bcrypt from "bcryptjs";
import { MoreThan } from "typeorm";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";
import { User } from "../../entities/User";
import { Category } from "../../entities/Category";
import { Product } from "../../entities/Product";

describe("System Test - Update Flow", () => {

  let token: string;
  let categoryId: number;
  let productId: number;

  beforeAll(async () => {
    await AppDataSource.initialize();

    const userRepo = AppDataSource.getRepository(User);
    const categoryRepo = AppDataSource.getRepository(Category);
    const productRepo = AppDataSource.getRepository(Product);

    await productRepo.delete({ id: MoreThan(0) });
    await categoryRepo.delete({ id: MoreThan(0) });
    await userRepo.delete({ email: "system.update@test.com" });

    const hash = await bcrypt.hash("123456", 10);

    await userRepo.save({
      email: "system.update@test.com",
      password_hash: hash
    });

    const login = await request(app)
      .post("/api/v1/login")
      .send({
        email: "system.update@test.com",
        password: "123456"
      });

    token = login.body.token;

    // cria categoria e produto base pra atualizar depois
    const catRes = await request(app)
      .post("/api/v1/categories")
      .set("Authorization", `Bearer ${token}`)
      .send({ name: "Categoria Original" });

    categoryId = catRes.body.id;

    const prodRes = await request(app)
      .post("/api/v1/products")
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Produto Original",
        price: 50,
        categoryId
      });

    productId = prodRes.body.id;
  });

  afterAll(async () => {
    await AppDataSource.getRepository(Product).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(Category).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(User).delete({ email: "system.update@test.com" });

    await AppDataSource.destroy();
  });

  test("deve atualizar uma categoria existente", async () => {
    const response = await request(app)
      .put(`/api/v1/categories/${categoryId}`)
      .set("Authorization", `Bearer ${token}`)
      .send({ name: "Categoria Atualizada" });

    expect(response.status).toBe(200);

    // confirma que a mudança persistiu
    const check = await request(app).get(`/api/v1/categories/${categoryId}`);
    expect(check.status).toBe(200);
    expect(check.body.name).toBe("Categoria Atualizada");
  });

  test("deve atualizar um produto existente", async () => {
    const response = await request(app)
      .put(`/api/v1/products/${productId}`)
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Produto Atualizado",
        price: 75.5,
        categoryId
      });

    expect(response.status).toBe(200);

    // confirma que a mudança persistiu
    const check = await request(app).get(`/api/v1/products/${productId}`);
    expect(check.status).toBe(200);
    expect(check.body.name).toBe("Produto Atualizado");
    expect(Number(check.body.price)).toBe(75.5);
  });

  test("NÃO deve atualizar categoria sem token", async () => {
    const response = await request(app)
      .put(`/api/v1/categories/${categoryId}`)
      .send({ name: "Tentativa Sem Token" });

    expect(response.status).toBe(401);
  });

});