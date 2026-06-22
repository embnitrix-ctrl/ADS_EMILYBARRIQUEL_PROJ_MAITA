import request from "supertest";
import app from "../../app";
import { AppDataSource } from "../../config/datasource";

beforeAll(async () => {
  await AppDataSource.initialize();
});

afterAll(async () => {
  await AppDataSource.destroy();
});

describe("Category Integration", () => {

  test("deve listar categorias", async () => {
    const response = await request(app)
      .get("/api/v1/categories");

    expect(response.status).toBe(200);
  });

});