import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.sound.midi.Soundbank;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

public abstract class Customer {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected int balance;
    protected Map<String, CartItem> cart = new HashMap<>();
    protected static List<Order> orderHistory = new ArrayList<>() ;
    protected boolean isCheckedOut = false;  // Flag untuk mengecek apakah checkout sudah dilakukan

    public abstract Promotion getPromo();
    public abstract void setPromo(Promotion promo);
    public Customer(String id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public abstract String getFullName();
    public abstract int getBalance();

    public abstract void topUp(int amount);
    abstract Order makeOrder(LocalDate orderDate, LocalDate endDate, double subTotal, double total, Customer customer, Map<String, CartItem> map);
    public abstract Map<String, CartItem> getCart();
    public boolean confirmPay(Order order) {
        // System.out.println(this.balance);
        // Order order = null;
        for (Order o : orderHistory) {
            if (order == order) {
                order = o;
                break;
            }
        }
        if (order != null) {
            if (this instanceof Member) {
                Member member = (Member) this;
                if (member.getPromo() instanceof CashbackPromo) {
                    try {
                        this.balance += (member.getPromo().calculateTotalCashback(order) - order.subTotal);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else if (member.getPromo() instanceof PercentOffPromo) {
                    try {
                        System.out.println(balance);
                        balance -= (order.subTotal - member.getPromo().calculateTotalDiscount(order));
                        System.out.println(order.subTotal);
                        System.out.println(balance);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            this.isCheckedOut = true;
            order.setStatus(Status.SUCCESSFUL);
            return true;
        }
        return false;
    }
    // INI KODE LAMA (GABISA PRINT CHECKOUT KEDUA/ NULL)
    // public boolean confirmPay(Order order){ 
    //     if (order != null) {
    //         if (this instanceof Member) {
    //             Member member = (Member) this;
    //             if (member.getPromo() instanceof CashbackPromo) {
    //                 try {
    //                     this.balance -= order.getSubTotal();
    //                     this.balance += member.getPromo().calculateTotalCashback(order);
    //                 } catch (Exception e) {
    //                     throw new RuntimeException(e);
    //                 }
    //             } else if (member.getPromo() instanceof PercentOffPromo) {
    //                 try {
    //                     balance -= (order.getSubTotal() - member.getPromo().calculateTotalDiscount(order));
    //                 } catch (Exception e) {
    //                     throw new RuntimeException(e);
    //                 }
    //             }else{
    //                 balance -= order.getSubTotal();
    //             }
    //         }
    //         this.isCheckedOut = true;
    //         order.setStatus(Status.SUCCESSFUL);
    //         return true;
    //     }
    //     return false;
    // } KODE LAMA

    public boolean addToCart(Menu menuItem, int qty, String startDate) {
        if (cart.containsKey(menuItem.IDMenu)) {
            CartItem cartItem = cart.get(menuItem.IDMenu);
            cartItem.qty += qty;
            return false; // Updated
        } else {
            cart.put(menuItem.IDMenu, new CartItem(menuItem, qty, startDate));
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

    public int getLastIndex(Customer customer){
        for (int i = 0; orderHistory.size()-1 >= 0; i--) {
            if(orderHistory.get(i).customer.equals(customer)){
                return i+1;
            }
        }
        return -1;
    }

    public Order getLast(Customer customer){
        for (int i = orderHistory.size() - 1; i >= 0; i--) {
            if(orderHistory.get(i).customer.equals(customer)){
                //System.out.println(orderHistory.get(i).customer.id);
                return orderHistory.get(i);
            }
        }
        return null;
    }

    public void printOrder() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat currencyFormatter = new DecimalFormat("###,###.##", symbols);
        Order tmp = null;
        //System.out.println(cart);
        Map<String, CartItem> menusToPrint;
        if (!cart.isEmpty()) {
            menusToPrint = cart;
        } else if (!orderHistory.isEmpty() && cart.isEmpty()) {
            tmp = getLast(this);
            menusToPrint = tmp.getMenus();
        } else {
            System.out.println("PRINT FAILED: NON EXISTENT ORDER");
            return;
        }
        List<CartItem> sortedItems = new ArrayList<>(menusToPrint.values());
    sortedItems.sort(Comparator.comparing((CartItem c) -> LocalDate.parse(c.startDate, dateFormatter))
                                .thenComparingDouble(c -> c.menuItem.Harga));
        System.out.println("Kode Pemesan: " + id);
        System.out.println("Nama: " + getFullName());
        if(tmp != null ){
            if(tmp.orderNumb != 0 && tmp.orderDate != null){
                System.out.println("Nomor Pesanan: " + tmp.orderNumb);
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));
                System.out.println("Tanggal Pesanan: " + currentDate.format(formatter));
            }
            
        }
        boolean cek = false;
        System.out.printf("%3s | %-25s | %3s | %8s \n", "No", "Menu", "Dur.", "Subtotal");
        System.out.println("=".repeat(55));
        int no = 1;
        double subtotal = 0;
        for (CartItem cartItem : sortedItems) {
            double itemSubtotal = cartItem.qty * cartItem.menuItem.Harga;
            System.out.printf("%3d | %-25s | %3d  |%8s\n", no++, cartItem.menuItem.NamaMenu + " " + cartItem.menuItem.PlatNomor, cartItem.qty, currencyFormatter.format(itemSubtotal));
            System.out.printf("      %s - %s\n", cartItem.startDate, LocalDate.parse(cartItem.startDate, dateFormatter).plusDays(cartItem.qty).format(dateFormatter));
            subtotal += itemSubtotal;
        }
        double promoNya = 0;
        System.out.println("=".repeat(55));
        System.out.printf("%-32s: %14s\n", "SubTotal", currencyFormatter.format(subtotal));
        if (this instanceof Guest) {
            this.balance -= subtotal;
        }
        System.out.println("=".repeat(55));
        System.out.printf("%-32s: %14s\n", "Total", currencyFormatter.format((cek) ? subtotal - promoNya:subtotal));
        if (this instanceof Member) {
            Member member = (Member) this;
            if(getPromo() != null){
                try {
                    if (member.getPromo() instanceof PercentOffPromo) {
                        cek = true;
                        promoNya = member.getPromo().calculateTotalDiscount(member.getOrder());
                    } else {
                        promoNya = member.getPromo().calculateTotalCashback(member.getOrder());
                    }
                } catch (Exception e) {
                    System.out.println();
                }
//            this.balance -= subtotal;
                // System.out.printf("%-7s%-25s:%15s\n", "PROMO:", member.getPromo().getPromoCode(), (member.getPromo() instanceof CashbackPromo) ? currencyFormatter.format(promoNya) : "-" + currencyFormatter.format(promoNya)); KODE LAMA
                System.out.printf("%-7s%-25s:%15s\n", "PROMO:", member.getPromo().getPromoCode(), (member.getPromo() instanceof CashbackPromo) ? currencyFormatter.format(promoNya) : "-" + currencyFormatter.format(promoNya));
            }else if(tmp != null && tmp.getPromotion() != null){
                try {
                    if (tmp.getPromotion() instanceof PercentOffPromo) {
                        cek = true;
                        promoNya = tmp.getPromotion().calculateTotalDiscount(member.getOrder());
                    } else {
                        promoNya = tmp.getPromotion().calculateTotalCashback(member.getOrder());
                    }
                } catch (Exception e) {
                    System.out.println();
                }
                System.out.printf("%-7s%-25s:%15s\n", "PROMO:", tmp.getPromotion().getPromoCode(), (tmp.getPromotion() instanceof CashbackPromo) ? currencyFormatter.format(promoNya) : "-" + currencyFormatter.format(promoNya));
            }
            
        }
        System.out.printf("%-32s: %14s\n", "Saldo", currencyFormatter.format(balance));
        if (isCheckedOut) cart.clear(); // klo belum di checkout, empty cart
        //getCart().clear();
    }

    public void printOrderHistory() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);

        System.out.println("Kode Pemesan: " + id);
        System.out.println("Nama: " + getFullName());
        System.out.println("Saldo: " + formatter.format(balance));
        System.out.printf("%4s | %10s | %5s | %5s | %8s | %-8s\n", "No", "No. Pesanan", "Motor", "Mobil", " Subtotal", "PROMO");
        System.out.println("===========================================================");

        int no = 1;
        for (Order order : orderHistory) {
            int moto = 0;
            int car = 0;
            for (Map.Entry<String, CartItem> map : order.menus.entrySet()) {
                CartItem tmp = map.getValue();
                // System.out.println(tmp.menuItem.CustomType);
                if(tmp.menuItem.CustomType == "L"){
                    car++;
                    // System.out.println(tmp.menuItem.CustomType);
                }
                moto++;
            }
            if(this instanceof Guest) {
                System.out.printf("%4d | %11d | %5d | %5d |  %8s |  %-8s\n", no++, order.orderNumb, moto, car, formatter.format(order.subTotal), "");
            }
            else System.out.printf("%4d | %11d | %5s | %5s |  %8s |  %-8s\n", no++, order.orderNumb, moto, car, formatter.format(order.subTotal), (getPromo() == null) ? "-" : getPromo());
        }
        System.out.println("===========================================================");
    }
}

