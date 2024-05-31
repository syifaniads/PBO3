import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public abstract class Customer {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected int balance;
    protected Map<String, CartItem> cart;
    protected List<Order> orderHistory;
    protected boolean isCheckedOut = false;  // Flag untuk mengecek apakah checkout sudah dilakukan

    public abstract Promotion getPromo();
    public abstract void setPromo(Promotion promo);
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
    abstract Order makeOrder(LocalDate orderDate, LocalDate endDate, double subTotal, double total);
    public abstract Map<Menu, CartItem> getCart();

    public boolean confirmPay(int orderNumber) {
        Order order = null;
        for (Order o : orderHistory) {
            if (o.orderNumber == orderNumber) {
                order = o;
                break;
            }
        }
        if (order != null) {
            if(this instanceof Member) {
                Member member = (Member) this;
                if(member.getPromo() instanceof CashbackPromo) {
                    try {
                        this.balance += member.getPromo().calculateTotalCashback(order) - order.subTotal;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else if(member.getPromo() instanceof PercentOffPromo) {
                    try {
                        this.balance -= order.total - member.getPromo().calculateTotalDiscount(order);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
//            } else {
//                this.balance -= (int) order.total; // Deduct balance by total order amount
            }
             // Set flag that checkout is done
            order.setStatus(Status.SUCCESSFUL);  // Update order status to SUCCESSFUL
            this.isCheckedOut = true;
            return true;
        }
        return false;
    }


    public boolean addToCart(Menu menuItem, int qty, String startDate) {
        String key = menuItem.IDMenu + startDate;
        if (cart.containsKey(key)) {
            CartItem cartItem = cart.get(key);
            cartItem.qty += qty;
            return false; // Updated
        } else {
            cart.put(key, new CartItem(menuItem, qty, startDate));
            return true; // New
        }
    }

    public boolean removeFromCart(Menu menuItem, int qty) {
        for (Map.Entry<String, CartItem> entry : cart.entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem.menuItem.IDMenu.equals(menuItem.IDMenu)) {
                cartItem.qty -= qty;
                if (cartItem.qty <= 0) {
                    cart.remove(entry.getKey());
                    System.out.println("REMOVE_FROM_CART SUCCESS: " + cartItem.menuItem.NamaMenu + " IS REMOVED");
                } else {
                    System.out.println("REMOVE_FROM_CART SUCCESS: " + cartItem.menuItem.NamaMenu + " QUANTITY IS DECREMENTED");
                }
                return true;
            }
        }
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
            System.out.println("PRINT FAILED: NON EXISTENT ORDER");
            return;
        }

        menusToPrint.sort(Comparator.comparing((CartItem c) -> LocalDate.parse(c.startDate, dateFormatter)).thenComparingDouble(c -> c.menuItem.Harga));

        System.out.println("No | Menu                           | Dur. | Subtotal");
        System.out.println("=".repeat(55));
        int no = 1;
        double subtotal = 0;
        for (CartItem cartItem : menusToPrint) {
            double itemSubtotal = cartItem.qty * cartItem.menuItem.Harga;
            System.out.printf("%2d | %-30s | %4d | %s\n", no++, cartItem.menuItem.NamaMenu + " " + cartItem.menuItem.PlatNomor, cartItem.qty, currencyFormatter.format(itemSubtotal));
            System.out.printf("     %s - %s\n", cartItem.startDate, LocalDate.parse(cartItem.startDate, dateFormatter).plusDays(cartItem.qty).format(dateFormatter));
            subtotal += itemSubtotal;
        }
        System.out.println("=".repeat(55));
        System.out.printf("SubTotal                            :        %s\n", currencyFormatter.format(subtotal));
        if(this instanceof Member) {
            Member member = (Member) this;
            double promoNya;
            try {
                if(member.getPromo() instanceof PercentOffPromo) {
                    promoNya = member.getPromo().calculateTotalDiscount(member.getOrder());
                }
                else promoNya = member.getPromo().calculateTotalCashback(member.getOrder());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.printf("PROMO: %s                       :        %s\n", member.getPromo().getPromoCode(), (member.getPromo() instanceof CashbackPromo) ? currencyFormatter.format(promoNya) : "-"+currencyFormatter.format(promoNya));
        }
        System.out.println("=".repeat(55));
        System.out.printf("Total                               :        %s\n", currencyFormatter.format(subtotal));
        if(this instanceof Guest) balance -= subtotal;
        System.out.printf("Saldo                               :        %s\n", currencyFormatter.format(balance));
        if(isCheckedOut) cart.clear();      //klo belum di checkout, empty cart
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
        System.out.println("===========================================");

        int no = 1;
        for (Order order : orderHistory) {
            if(order.promotion == null) {
                System.out.printf("%2d | %14d  |  %9s |  %-8s\n", no++, order.orderNumber, formatter.format(order.subTotal), "");
            }
            else System.out.printf("%2d | %14d  |  %9s |  %-8s\n", no++, order.orderNumber, formatter.format(order.subTotal), getPromo());
        }
        System.out.println("===========================================");
    }
}
