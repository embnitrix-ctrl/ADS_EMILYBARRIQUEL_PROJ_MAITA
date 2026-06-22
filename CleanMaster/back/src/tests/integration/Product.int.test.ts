import request from "supertest";
import bcrypt from "bcryptjs";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";
import { User } from "../../entities/User";
import { Product } from "../../entities/Product";
import { Category } from "../../entities/Category";

describe("Product Integration", () => {

  let token: string;
  let categoryId: number;

  beforeAll(async () => {
    await AppDataSource.initialize();

    const userRepo = AppDataSource.getRepository(User);
    const categoryRepo = AppDataSource.getRepository(Category);
    const productRepo = AppDataSource.getRepository(Product);

    // ordem: produto (depende) antes de categoria (referenciada)
    await productRepo.delete({ name: "Produto Teste" });
    await categoryRepo.delete({ name: "Categoria Teste" });
    await userRepo.delete({ email: "prod@test.com" });

    const hash = await bcrypt.hash("123456", 10);

    await userRepo.save({
      email: "prod@test.com",
      password_hash: hash
    });

    const login = await request(app)
      .post("/api/v1/login")
      .send({
        email: "prod@test.com",
        password: "123456"
      });

    token = login.body.token;

    const category = await categoryRepo.save({
      name: "Categoria Teste"
    });

    categoryId = category.id;
  });

  afterAll(async () => {
    await AppDataSource.getRepository(Product).delete({ name: "Produto Teste" });
    await AppDataSource.getRepository(Category).delete({ name: "Categoria Teste" });
    await AppDataSource.getRepository(User).delete({ email: "prod@test.com" });
    await AppDataSource.destroy();
  });

  test("deve criar produto com categoria e token", async () => {
    const response = await request(app)
      .post("/api/v1/products")
      .set("Authorization", `Bearer ${token}`)
      .send({
        name: "Produto Teste",
        price: 10.5,
        categoryId: categoryId
      });

    expect(response.status).toBe(201);
  });

});