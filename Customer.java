import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Customer {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected int balance;
    protected Map<String, CartItem> cart;
    protected List<Order> orderHistory;

    public Customer(String id, String firstName, String lastName, int initialBalance) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = initialBalance;
        this.cart = new HashMap<>();
        this.orderHistory = new ArrayList<>();
    }

    public abstract String getFullName();
    public abstract int getBalance();
    public abstract void topUp(int amount);
    public abstract boolean addToCart(Menu menuItem, int qty, String startDate);
    abstract Order makeOrder(LocalDate orderDate, LocalDate endDate, double subTotal, double shippingFee, double discount, double total);
    public abstract Map<Menu, CartItem> getCart();
    abstract void confirmPay(int orderNumber);

    public boolean removeFromCart(Menu menuItem, int qty) {
        // Find the cart item corresponding to the provided menu
        for (Map.Entry<String, CartItem> entry : cart.entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem.menuItem.IDMenu.equals(menuItem.IDMenu)) {
                // Decrement the quantity by the provided amount
                cartItem.qty -= qty;
                if (cartItem.qty <= 0) {
                    // Remove the item from the cart if quantity becomes zero or negative
                    cart.remove(entry.getKey());
                    System.out.println("REMOVE_FROM_CART SUCCESS: " + cartItem.menuItem.NamaMenu + " IS REMOVED");
                } else {
                    System.out.println("REMOVE_FROM_CART SUCCESS: " + cartItem.menuItem.NamaMenu + " QUANTITY IS DECREMENTED");
                }
                return true;
            }
        }
        // If the menu item is not found in the cart
        System.out.println("REMOVE_FROM_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
        return false;
    }


    public void printOrder() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat currencyFormatter = new DecimalFormat("###,###.##", symbols);

        System.out.println("Kode Pemesan: " + id);
        System.out.println("Nama: " + getFullName());

        List<CartItem> menusToPrint;
        if (!cart.isEmpty()) {
            menusToPrint = new ArrayList<>(cart.values());
        } else if (!orderHistory.isEmpty()) {
            menusToPrint = orderHistory.get(orderHistory.size() - 1).getMenus();
        } else {
            System.out.println("No orders to display.");
            return;
        }

        menusToPrint.sort(Comparator.comparing((CartItem c) -> LocalDate.parse(c.startDate, dateFormatter)).thenComparingDouble(c -> c.menuItem.Harga));

        System.out.println("No | Menu                                 |  Dur. | Subtotal");
        System.out.println("============================================");
        int no = 1;
        double subtotal = 0;
        for (CartItem cartItem : menusToPrint) {
            double itemSubtotal = cartItem.qty * cartItem.menuItem.Harga;
            System.out.printf("%2d | %-30s | %4d | %s\n", no++, cartItem.menuItem.NamaMenu + " " + cartItem.menuItem.PlatNomor, cartItem.qty, currencyFormatter.format(itemSubtotal));
            System.out.printf("     %s - %s\n", cartItem.startDate, LocalDate.parse(cartItem.startDate, dateFormatter).plusDays(cartItem.qty).format(dateFormatter));
            subtotal += itemSubtotal;
        }
        System.out.println("============================================");
        System.out.printf("SubTotal                               : %s\n", currencyFormatter.format(subtotal));
        System.out.println("============================================");
        System.out.printf("Total                                  : %s\n", currencyFormatter.format(subtotal));
        System.out.printf("Saldo                                  : %s\n", currencyFormatter.format(balance));
    }

    public void printOrderHistory() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);

        System.out.println("Kode Pemesan: " + id);
        System.out.println("Nama: " + getFullName());
        System.out.println("Saldo: " + formatter.format(balance));
        System.out.println("No |  Nomor Pesanan  |  Subtotal  |  PROMO");
        System.out.println("==========================================");

        int no = 1;
        for (Order order : orderHistory) {
            String promo = order.promotion == null ? "" : order.promotion.getPromoCode();
            System.out.printf("%2d | %14d | %9s | %-8s\n", no++, order.orderNumber, formatter.format(order.subTotal), promo);
        }
        System.out.println("==========================================");
    }
}
