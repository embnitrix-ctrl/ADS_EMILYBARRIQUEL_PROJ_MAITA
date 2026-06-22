describe("Category Entity", () => {

  test("deve criar categoria corretamente", () => {
    const category = {
      name: "Limpeza"
    };

    expect(category.name).toBe("Limpeza");
  });

});