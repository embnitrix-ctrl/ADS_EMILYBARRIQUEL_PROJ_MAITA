import request from "supertest";
import bcrypt from "bcryptjs";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";
import { User } from "../../entities/User";

describe("User Integration", () => {

  beforeAll(async () => {
    await AppDataSource.initialize();

    const repo = AppDataSource.getRepository(User);

    await repo.delete({ email: "user@test.com" });

    const hash = await bcrypt.hash("123456", 10);

    await repo.save({
      email: "user@test.com",
      password_hash: hash
    });
  });

  afterAll(async () => {
    await AppDataSource.getRepository(User).delete({ email: "user@test.com" });
    await AppDataSource.destroy();
  });

  test("deve fazer login e retornar token", async () => {
    const response = await request(app)
      .post("/api/v1/login")
      .send({
        email: "user@test.com",
        password: "123456"
      });

    expect(response.status).toBe(200);
    expect(response.body).toHaveProperty("token");
  });

});