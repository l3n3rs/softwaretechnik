class ShoppingCart {
    constructor() {
        this.items = [];
    }

    addItem(itemPrice) {
        this.items.push(itemPrice);
    }

    getTotal() {
        return this.items.reduce((sum, itemPrice) => sum + itemPrice, 0);
    }
}

module.exports = {
    ShoppingCart
};