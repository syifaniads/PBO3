public class CartItem {
    Menu menuItem;
    int qty;
    String startDate;

    CartItem(Menu menuItem, int qty, String startDate) {
        this.menuItem = menuItem;
        this.qty = qty;
        this.startDate = startDate;
    }
}
