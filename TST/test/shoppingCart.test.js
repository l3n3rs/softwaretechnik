const { ShoppingCart } = require("../src/shoppingCart.js");

test("Warenkorb hat Wert von 0", () => {
    const cart = new ShoppingCart();
    expect(cart.getTotal()).toBe(0);
});

test("Ein Item zum Warenkorb hinzufügen, Warenkorbwert erhöht sich", () => {
    const cart = new ShoppingCart();
    cart.addItem(5);
    expect(cart.getTotal()).toBe(5);
});

test("Mehrere Items wurden zum Warenkorb hinzugefügt", () => {
    const cart = new ShoppingCart();
    cart.addItem(5);
    cart.addItem(5);
    cart.addItem(5);
    expect(cart.getTotal()).toBe(15);
});

test("Anzahl an Items", () => {
    const cart = new ShoppingCart();
    cart.addItem(5);
    cart.addItem(5);
    cart.addItem(5);
    expect(cart.getItemCount()).toBe(3);
});