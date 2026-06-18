class ShoppingCart {
    constructor() {
        this.items = [];
    }

    addItem(price) {
        this.items.push(price);
    }

    getTotal() {
        return this.items.reduce((sum, price) => sum + price, 0);
    }
}

module.exports = { ShoppingCart };