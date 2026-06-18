const { ShoppingCart } = require("../src/shoppingCart.js");

test("Warenkorb hat Wert von 0", () => {
    const cart = new ShoppingCart();
    expect(cart.getTotal()).toBe(0);
});

