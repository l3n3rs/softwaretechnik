class ShoppingCart {
    constructor() {
        this.items = [];
    }

    addItem(name, price) {
        this.items.push({ name, price });
    }

    getTotal() {
        return this.items.reduce((sum, item) => sum + item.price, 0);
    }

    getTotalItemCount() {
        return this.items.length;
    }

    getItems() {
        return this.items;
    }

    getSpecificItemCount(itemName) {
        return this.items.filter((item) => item.name === itemName).length;
    }
}
module.exports = {
    ShoppingCart
};