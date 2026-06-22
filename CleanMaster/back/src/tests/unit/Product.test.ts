describe("Product Entity", () => {

  test("deve criar produto corretamente", () => {
    const product = { 
        name: "Detergente",
        price: 10.5
    };

    expect(product.name).toBe("Detergente");
    expect(product.price).toBe(10.5);
  });

});