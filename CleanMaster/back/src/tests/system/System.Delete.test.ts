import request from "supertest";
import bcrypt from "bcryptjs";
import { MoreThan } from "typeorm";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";
import { User } from "../../entities/User";
import { Category } from "../../entities/Category";
import { Product } from "../../entities/Product";

describe("System Test - Delete Flow", () => {

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
    await userRepo.delete({ email: "system.delete@test.com" });

    const hash = await bcrypt.hash("123456", 10);

    await userRepo.save({
      email: "system.delete@test.com",
      password_hash: hash
    });

    const login = await request(app)
      .post("/api/v1/login")
      .send({
        email: "system.delete@test.com",
        password: "123456"
      });

    token = login.body.token;

    const catRes = await request(app)
      .post("/api/v1/categories")
      .set("Authorization", `Bearer ${token}`)
      .send({ name: "Categoria Para Deletar" });

    categoryId = catRes.body.id;

    const prodRes = await request(app)
      .post("/api/v1/products")
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Produto Para Deletar",
        price: 30,
        categoryId
      });

    productId = prodRes.body.id;
  });

  afterAll(async () => {
    await AppDataSource.getRepository(Product).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(Category).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(User).delete({ email: "system.delete@test.com" });

    await AppDataSource.destroy();
  });

  test("NÃO deve deletar produto sem token", async () => {
    const response = await request(app)
      .delete(`/api/v1/products/${productId}`);

    expect(response.status).toBe(401);
  });

  test("deve deletar o produto (precisa ser antes da categoria por causa da FK)", async () => {
    const response = await request(app)
      .delete(`/api/v1/products/${productId}`)
      .set("Authorization", `Bearer ${token}`);

    expect(response.status).toBe(204);

    // confirma que não existe mais
    const check = await request(app).get(`/api/v1/products/${productId}`);
    expect(check.status).toBe(404);
  });

  test("deve deletar a categoria depois do produto", async () => {
    const response = await request(app)
      .delete(`/api/v1/categories/${categoryId}`)
      .set("Authorization", `Bearer ${token}`);

    expect(response.status).toBe(204);

    // confirma que não existe mais
    const check = await request(app).get(`/api/v1/categories/${categoryId}`);
    expect(check.status).toBe(404);
  });

});