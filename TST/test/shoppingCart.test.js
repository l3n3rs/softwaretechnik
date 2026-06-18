const { ShoppingCart } = require("../src/shoppingCart.js");

test("Warenkorb hat Wert von 0", () => {
    const cart = new ShoppingCart();
    expect(cart.getTotal()).toBe(0);
});

test("Item zum Warenkorb hinzufügen", () => {
    const cart = new ShoppingCart();
    cart.addItem(5);
    expect(cart.getTotal()).toBe(5);
});