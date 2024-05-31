/*
 * 235150201111062 LATIFA ANGGIA FITRIANA
 * 235150201111060 MAULIA DWI ANTHESA SUGEHA
 * 235150207111052 SYIFANI ADILLAH SALSABILA
 * 215150207111031 KUSUMA ANISA ANGGRANI
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.*;

public class MainTravel {
    private static List<Member> members = new ArrayList<>();
    private static List<Guest> guests = new ArrayList<>();
    private static List<Menu> menu = new ArrayList<>();
    private static List<Promotion> promoList = new ArrayList<>();
    private final static Map<String, Customer> customers = new HashMap<>();
    public static int totalCar = 0;
    public static int totalMotorcycles = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
//        List<String> inputs = new ArrayList<>();

        // Process inputs
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            processInput(input, scanner);
        }
        scanner.close();
    }


    private static void processInput(String input, Scanner scanner) {
        if (input.startsWith("CREATE MEMBER ")) {
            loginMember(input);
        }
        else if (input.startsWith("CREATE GUEST")) {
            loginGuest(input);
        }
        else if (input.startsWith("CREATE MENU")) {
            processCreateMenu(input);
        }
        else if (input.startsWith("CREATE PROMO ")) {
            processCreatePromo(input, scanner);
        }
        else if (input.startsWith("ADD_TO_CART ")) {
            processAddToCart(input);
        }
        else if (input.startsWith("REMOVE_FROM_CART ")) {
            processRemoveFromCart(input);
        }
        else if (input.startsWith("APPLY_PROMO ")) {
            processApplyPromo(input);
        }
        else if (input.startsWith("TOPUP ")) {
            processTopUp(input);
        }
        else if (input.startsWith("CHECK_OUT ")) {
            processCheckout(input);
        }
        else if(input.startsWith("PRINT_HISTORY")) {
            String[] parts = input.split(" ");
            String customerId = parts[1];
            if (!customers.containsKey(customerId)) {
                System.out.println("PRINT FAILED: NON EXISTENT CUSTOMER");
            } else {
                Customer customer = customers.get(customerId);
                customer.printOrderHistory();
            }
        }
        else {
            String[] parts = input.split(" ");
            String customerId = parts[1];
            if (!customers.containsKey(customerId)) {
                System.out.println("PRINT FAILED: NON EXISTENT CUSTOMER");
            } else {
                Customer customer = customers.get(customerId);
                customer.printOrder();
            }
        }
    }

    private static void loginMember(String input) {
        String[] parts = input.split("\\|");
        if (parts.length == 4 && input.startsWith("CREATE MEMBER ")) {
            String id = parts[0].substring(14);
            String name = parts[1];
            LocalDate membershipDate = LocalDate.parse(parts[2], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            int startBalance = Integer.parseInt(parts[3]);

            if (!isMemberExist(id)) {
                String[] fullName = name.split(" ");
                String firstName = fullName[0];
                String lastName = fullName.length > 1 ? fullName[1] : "";
                Member newMember = new Member(id, firstName, lastName, membershipDate, startBalance);
                members.add(newMember);
                members = mergeSortMembers(members);
                customers.put(id, newMember);
                System.out.println("\nCREATE MEMBER SUCCESS: " + id + " " + name);
            } else {
                System.out.println("CREATE MEMBER FAILED: " + id + " IS EXISTS");
            }
        }
    }

    private static void loginGuest(String input) {
        String[] parts = input.split("\\|");
        if (parts.length == 2 && input.startsWith("CREATE GUEST ")) {
            String id = parts[0].substring(13); // Mengambil ID dari bagian pertama setelah substring(13)
            int startBalance = Integer.parseInt(parts[1]);

            if (!isGuestExist(id)) {
                Guest guest = new Guest(id, startBalance);
                guests.add(guest);
                guests = mergeSortGuests(guests);
                customers.put(id, guest);
                System.out.println("CREATE GUEST SUCCESS: " + id);
            }
            else {
                System.out.println("CREATE GUEST FAILED: " + id + " IS EXISTS");
            }
        }
    }


    private static boolean isMemberExist(String id) {
        return binarySearchMember(id) != null;
    }

    public static boolean isGuestExist(String id) {
        return binarySearchGuest(id) != null;
    }

    private static Member binarySearchMember(String id) {
        int left = 0, right = members.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = members.get(mid).getId().compareTo(id);
            if (cmp == 0) {
                return members.get(mid);
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }

    private static Guest binarySearchGuest(String id) {
        int left = 0, right = guests.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = guests.get(mid).getId().compareTo(id);
            if (cmp == 0) {
                return guests.get(mid);
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }
    private static List<Member> mergeSortMembers(List<Member> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<Member> left = new ArrayList<>(list.subList(0, mid));
        List<Member> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSortMembers(left);
        right = mergeSortMembers(right);

        return mergeMembers(left, right);
    }

    private static List<Member> mergeMembers(List<Member> left, List<Member> right) {
        List<Member> merged = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getId().compareTo(right.get(rightIndex).getId()) <= 0) {
                merged.add(left.get(leftIndex++));
            } else {
                merged.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex++));
        }

        return merged;
    }

    private static List<Guest> mergeSortGuests(List<Guest> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<Guest> left = new ArrayList<>(list.subList(0, mid));
        List<Guest> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSortGuests(left);
        right = mergeSortGuests(right);

        return mergeGuests(left, right);
    }

    private static List<Guest> mergeGuests(List<Guest> left, List<Guest> right) {
        List<Guest> merged = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).getId().compareTo(right.get(rightIndex).getId()) <= 0) {
                merged.add(left.get(leftIndex++));
            }
            else {
                merged.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex++));
        }

        return merged;
    }


    private static void processCreateMenu(String input) {
        String[] parts = input.split(" ", 3);
        String type = parts[1]; // This is the type of the vehicle (e.g., MOTOR, MOBIL)
        String[] details = parts[2].split("\\|");

        String IDMenu = details[0].trim().substring(6);
        String NamaMenu = details[1].trim();
        String PlatNomor = details[2].trim();
        int Harga = Integer.parseInt(details[3].trim());
        String CustomType = type.equalsIgnoreCase("MOBIL") ? details[4].trim() : null;

        for (Menu item : menu) {
            if (item.IDMenu.equals(IDMenu)) {
                System.out.println("CREATE MENU FAILED: " + IDMenu + " IS EXISTS");
                return;
            }
            if (item.PlatNomor.equals(PlatNomor)) {
                System.out.println("CREATE MENU FAILED: " + PlatNomor + " IS EXISTS");
                return;
            }
        }

        Menu newItem;
        if (type.equalsIgnoreCase("MOBIL")) {
            newItem = new Menu(IDMenu, NamaMenu, PlatNomor, Harga, CustomType);
            totalCar++;
        } else {
            newItem = new Menu(IDMenu, NamaMenu, PlatNomor, Harga);
            totalMotorcycles++;
        }

        menu.add(newItem);
        menu = mergeSort(menu);
        System.out.println("CREATE MENU SUCCESS: " + IDMenu + " " + NamaMenu + " " + PlatNomor);
    }

    private static List<Menu> mergeSort(List<Menu> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<Menu> left = new ArrayList<>(list.subList(0, mid));
        List<Menu> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    private static List<Menu> merge(List<Menu> left, List<Menu> right) {
        List<Menu> merged = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).IDMenu.compareTo(right.get(rightIndex).IDMenu) <= 0) {
                merged.add(left.get(leftIndex++));
            } else {
                merged.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex++));
        }

        return merged;
    }

    private static Menu binarySearchMenu(String id) {
        // Melakukan binary search pada list menu
        int left = 0, right = menu.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int cmp = menu.get(mid).IDMenu.compareTo(id);
            if (cmp == 0) {
                return menu.get(mid);
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }


    private static void processCreatePromo(String input, Scanner scanner) {
        String[] parts = input.split(" ");
        if (parts.length < 3) {
            // System.out.println("Invalid input format.");
            return;
        }

        String promoType = parts[2].trim();

        if (!scanner.hasNextLine()) {
            // System.out.println("No promo details provided.");
            return;
        }
        String promoDetails = scanner.nextLine();
        String[] details = promoDetails.split("\\|"); // Use "\\|" to split by the pipe character

        // Check if details array has the required number of elements
        if (details.length < 6) {
//            System.out.println("Invalid promo details format.");
            return;
        }

        String promoCode = details[0].trim();
        LocalDate startDate = LocalDate.parse(details[1].trim(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        LocalDate endDate = LocalDate.parse(details[2].trim(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        int percentPieces = Integer.parseInt(details[3].replace("%", "").trim());
        int maxPieces = Integer.parseInt(details[4].trim());
        int minPurchase = Integer.parseInt(details[5].trim());

        for (Promotion promo : promoList) {
            if (promo.promoCode.equals(promoCode)) {
                System.out.println("CREATE PROMO " + promoType + " FAILED: " + promoCode + " IS EXISTS");
                return;
            }
        }

        Promotion newPromo;
        switch (promoType.toUpperCase()) {
            case "CASHBACK":
                newPromo = new CashbackPromo(promoCode, startDate, endDate, percentPieces, maxPieces, minPurchase, promoType);
                break;
            case "DISCOUNT":
                newPromo = new PercentOffPromo(promoCode, startDate, endDate, percentPieces, maxPieces, minPurchase, promoType);
                break;
            default:
                System.out.println("Invalid promo type.");
                return;
        }

        promoList.add(newPromo);
        promoList = mergeSortPromos(promoList);
        System.out.println("CREATE PROMO " + promoType + " SUCCESS: " + newPromo);
    }


    private static List<Promotion> mergeSortPromos(List<Promotion> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        List<Promotion> left = new ArrayList<>(list.subList(0, mid));
        List<Promotion> right = new ArrayList<>(list.subList(mid, list.size()));

        left = mergeSortPromos(left);
        right = mergeSortPromos(right);

        return mergePromos(left, right);
    }

    private static List<Promotion> mergePromos(List<Promotion> left, List<Promotion> right) {
        List<Promotion> merged = new ArrayList<>();
        int leftIndex = 0, rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                merged.add(left.get(leftIndex++));
            } else {
                merged.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            merged.add(left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            merged.add(right.get(rightIndex++));
        }

        return merged;
    }

    private static void processAddToCart(String input) {
        String[] parts = input.split(" ");
        String IDPemesanan = parts[1];
        String IDMenu = parts[2];
        int qty = Integer.parseInt(parts[3]);
        String startDate = parts[4];

        // Mengecek apakah customer dan menu tersedia
        if (!customers.containsKey(IDPemesanan) || binarySearchMenu(IDMenu) == null) {
            System.out.println("ADD_TO_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
            return;
        }

        // Mendapatkan customer dari map
        Customer customer = customers.get(IDPemesanan);

        // Mencari menu berdasarkan ID
        Menu menuItem = binarySearchMenu(IDMenu);

        // Menambahkan item ke keranjang belanja customer
        boolean isNew = customer.addToCart(menuItem, qty, startDate);

        // Mendapatkan kuantitas terbaru dari keranjang
        CartItem updatedCartItem = customer.cart.get(menuItem.IDMenu + startDate);
        int updatedQty = updatedCartItem.qty;

        // Mengonversi qty menjadi days jika lebih dari 1
        String dayOrDays = updatedQty > 1 ? "days" : "day";

        // Mencetak output berdasarkan kondisi apakah item baru atau sudah ada sebelumnya
        if (isNew) {
            System.out.println("ADD_TO_CART SUCCESS: " + updatedQty + " " + dayOrDays + " " + menuItem.NamaMenu + " " + menuItem.PlatNomor + " (NEW)");
        } else {
            System.out.println("ADD_TO_CART SUCCESS: " + updatedQty + " " + dayOrDays + " " + menuItem.NamaMenu + " " + menuItem.PlatNomor + " (UPDATED)");
        }
    }

    private static void processApplyPromo(String input) {
        Order order = new Order();
        String[] parts = input.split(" ");
        String IDPemesan = parts[1];
        String KodePromo = parts[2];
        char userCategory = IDPemesan.charAt(0);
        // Check apakah pengguna adalah anggota atau tamu
        if (userCategory == 'A') {
            Promotion promo = null;
            for (Promotion p : promoList) {
                if (p.getPromoCode().equals(KodePromo)) {
                    promo = p;
                    break;
                }
            }

            Customer customer = customers.get(IDPemesan);
            int subtotal=0;
            for (Map.Entry<Menu, CartItem> subtotal1: customer.getCart().entrySet()) {
                CartItem menu = subtotal1.getValue();
                Menu menu1 = menu.menuItem;
                int temp = menu.qty;
                subtotal += temp*menu1.Harga;
            }
            order.subTotal = subtotal;
            if (promo == null || customer == null) {
                System.out.println("APPLY_PROMO FAILED: " + KodePromo);
                return;
            }

            LocalDate currentDate = LocalDate.now();
            if (currentDate.isBefore(promo.startDate) || currentDate.isAfter(promo.endDate)) {
                System.out.println("comel");
                System.out.println("APPLY_PROMO FAILED: " + KodePromo + " is EXPIRED");
                return;
            }

            if (!promo.isCustomerEligible(customer)) {
                // syarat tidak terpenuhi: umur akun kurang dari 30 hari
                System.out.println("APPLY_PROMO FAILED: " + KodePromo);
                return;
            }

            if (promo.isCustomerEligible(customer) && promo.isMinimumPriceEligible(order)) {
                // syarat terpenuhi: umur akun lebih dari 30 hari
                System.out.println("APPLY_PROMO SUCCESS: " + KodePromo);
                try {
                    customer.setPromo(promo);
//                    double totalDiscount = promo.calculateTotalDiscount(null); // Anda perlu menyesuaikan dengan parameter yang dibutuhkan
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Member member = (Member) customer;
                member.setOrder(order);
                return;
            }


        }
        else if (userCategory == 'G') {
            // syarat tidak terpenuhi: tamu gak bisa dapet promo
            System.out.println("APPLY_PROMO FAILED: " + KodePromo);
        }
        else {
            // syarat tidak terpenuhi: ID pemesanan tidak valid
            System.out.println("APPLY_PROMO FAILED: " + KodePromo);
        }

    }

    private static void processRemoveFromCart(String input) {
        String[] parts = input.split(" ");
        String IDPemesanan = parts[1];
        String IDMenu = parts[2];
        int qty = Integer.parseInt(parts[3]);

        if (!customers.containsKey(IDPemesanan)) {
            System.out.println("REMOVE_FROM_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
            return;
        }

        Customer customer = customers.get(IDPemesanan);
        Menu menuItem = binarySearchMenu(IDMenu);

        if (menuItem == null) {
            System.out.println("REMOVE_FROM_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
            return;
        }

        boolean isSuccess = customer.removeFromCart(menuItem, qty);
        if (!isSuccess) {
            System.out.println("REMOVE_FROM_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
        }
    }

    private static void processTopUp(String input) {
        String[] parts = input.split(" ");
        String IDPemesanan = parts[1];
        int topUpAmount = Integer.parseInt(parts[2]);

        Customer customer = customers.get(IDPemesanan);
        if (customer == null) {
            System.out.println("TOPUP FAILED: NON EXISTENT CUSTOMER");
            return;
        }

        int saldoAwal = customer.getBalance();
        customer.topUp(topUpAmount);
        int saldoAkhir = customer.balance;

        System.out.println("TOPUP SUCCESS: " + customer.getFullName() + " " + saldoAwal + " => " + saldoAkhir);
    }

    private static void processCheckout(String input) {
        String[] parts = input.split(" ");
        String customerId = parts[1];

        if (!customers.containsKey(customerId)) {
            System.out.println("CHECK_OUT FAILED: NON EXISTENT CUSTOMER OR MENU");
            return;
        }

        Customer customer = customers.get(customerId);
        Map<Menu, CartItem> cart = customer.getCart();

        double subTotal = 0;
        for (CartItem item : cart.values()) {
            subTotal += item.qty * item.menuItem.Harga;
        }

        if (customer.getBalance() < subTotal) {
            System.out.println("CHECK_OUT FAILED: " + customerId + " " + customer.getFullName() + " INSUFFICIENT BALANCE");
            return;
        }

        // Simulasi pembuatan pesanan
        LocalDate orderDate = LocalDate.now();
        LocalDate endDate = orderDate.plusDays(1);
        Order order = customer.makeOrder(orderDate, endDate, subTotal, subTotal);
        customer.orderHistory.add(order);
        customer.confirmPay(order.orderNumber);

        System.out.println("CHECK_OUT SUCCESS: " + customerId + " " + customer.getFullName());
    }
}
