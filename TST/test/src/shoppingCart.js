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
        let count = 0;

        for (const item of this.items) {
            if (item.name === itemName) {
                count++;
            }
        }

        console.log(`${itemName}: ${count}`);

        return count;
    }
}
module.exports = {
    ShoppingCart
};