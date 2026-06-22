describe("User Entity", () => {

  test("deve criar usuário corretamente", () => {
    const user = {
      email: "teste@email.com",
      password_hash: "123456"
    };

    expect(user.email).toBe("teste@email.com");
    expect(user.password_hash).toBe("123456");
  });

});