import request from "supertest";
import bcrypt from "bcryptjs";
import { MoreThan } from "typeorm";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";
import { User } from "../../entities/User";
import { Category } from "../../entities/Category";
import { Product } from "../../entities/Product";

describe("System Test - System.test.ts", () => {

  let token: string;
  let categoryId: number;
  let productId: number;

  beforeAll(async () => {
    await AppDataSource.initialize();

    const userRepo = AppDataSource.getRepository(User);
    const categoryRepo = AppDataSource.getRepository(Category);
    const productRepo = AppDataSource.getRepository(Product);

    // ordem: produto (FK) antes de categoria
    await productRepo.delete({ id: MoreThan(0) });
    await categoryRepo.delete({ id: MoreThan(0) });
    await userRepo.delete({ email: "system@test.com" });

    const hash = await bcrypt.hash("123456", 10);

    await userRepo.save({
      email: "system@test.com",
      password_hash: hash
    });

    const login = await request(app)
      .post("/api/v1/login")
      .send({
        email: "system@test.com",
        password: "123456"
      });

    token = login.body.token;
  });

  afterAll(async () => {
    await AppDataSource.getRepository(Product).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(Category).delete({ id: MoreThan(0) });
    await AppDataSource.getRepository(User).delete({ email: "system@test.com" });

    await AppDataSource.destroy();
  });

  test("fluxo completo do sistema", async () => {

    // 1. cria categoria
    const catRes = await request(app)
      .post("/api/v1/categories")
      .set("Authorization", `Bearer ${token}`)
      .send({ name: "Sistema Cat" });

    expect(catRes.status).toBe(201);
    categoryId = catRes.body.id;

    // 2. lista categorias
    const listCat = await request(app)
      .get("/api/v1/categories");

    expect(listCat.status).toBe(200);

    // 3. cria produto
    const prodRes = await request(app)
      .post("/api/v1/products")
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Produto Sistema",
        price: 99.9,
        categoryId
      });

    expect(prodRes.status).toBe(201);
    productId = prodRes.body.id;

    // 4. busca produtos por categoria
    const byCat = await request(app)
      .get(`/api/v1/categories/${categoryId}/products`);

    expect(byCat.status).toBe(200);
  });

});