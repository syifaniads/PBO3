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

public class MainFilkomTravel {
    private static List<Member> members = new ArrayList<>();
    private static List<Guest> guests = new ArrayList<>();
    private static List<Menu> menu = new ArrayList<>();
    private static List<Promotion> promoList = new ArrayList<>();
    private static Map<String, Customer> customers = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //main loop (loop 1)
        while (true) {
//            try {
                System.out.println("=".repeat(52));
                System.out.println("=".repeat(20) + "FILKOM TRAVEL" + "=".repeat(19));
                System.out.println("=".repeat(52));
                System.out.println("[Type 1] Member");
                System.out.println("[Type 2] Guest");
                System.out.println("[Type 3] Admin");
                System.out.println("[Type 0] Exit Program");
                System.out.print("Enter option: ");
                int choice1 = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice1) {
                    case 1:         //member
                        loginMember(scanner);
                        break;
                    case 2:         //guest
                        loginGuest(scanner);
                        continue;
                    case 3:
                        loginAdmin(scanner);
                        continue;
                    case 0:
                        System.out.print("You've logged out of the program.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                        break;
                }

                //loop 2
                while (true) {
                    System.out.println("=".repeat(52));
                    System.out.println("List of Option:");
                    System.out.println("[Type 1] Rent Vehicle");
                    System.out.println("[Type 2] See Order List");
                    System.out.println("[Type 3] Balance Top Up");
                    System.out.println("[Type 4] Balance Check");
                    System.out.println("[Type 0] Back");
                    System.out.print("Enter option: ");
                    int choice2 = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice2) {
                        case 1:
                            rentVehicle(scanner);
                            continue;
                        case 2:
                            //lihat list pesanan
                            while (scanner.hasNextLine()) {
                                String command = scanner.nextLine();
                                String[] parts = command.split(" ");
                                if (parts[0].equals("PRINT_HISTORY")) {
                                    String idPemesan = parts[1];
                                    if (customers.containsKey(idPemesan)) {
                                        customers.get(idPemesan).printOrderHistory();
                                    }
                                    else {
                                        System.out.println("PRINT FAILED: NON EXISTENT CUSTOMER");
                                    }
                                }
                            }
                            continue;
                        case 3:
                            //top up balance
                            String topUpInput = scanner.nextLine();
                            if (topUpInput.startsWith("TOPUP")) {
                                processTopUp(topUpInput);
                            }
                            continue;
                        case 4:
                            //cek saldo
                            String balanceInput = scanner.nextLine();
                            if (balanceInput.startsWith("VIEW_BALANCE")) {
                                processViewBalance(balanceInput);
                            }
                            continue;
//                        case 0:
//                            System.exit(0);
//                            break;
                        default:
//                            System.out.println("Invalid option.");
                            break;
                    }
                    break;
                }
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }

        }
//        scanner.close();
    }


    private static void loginAdmin(Scanner scanner){
        System.out.print("Password: ");
        int password = scanner.nextInt();
        if(password == 12345678) {
            while (true) {
                System.out.println("Daftar Pilihan \n[Type 1] Create Promo \n[Type 2] Create Menu \n[Type 0] Back");
                System.out.print("Enter option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        //create promo
                        String inputPromo = scanner.nextLine();
                        if (inputPromo.startsWith("CREATE PROMO")) {
                            processCreatePromo(inputPromo, scanner);
                        }
                        continue;
                    case 2:
                        //create menu
                        String inputMenu = scanner.nextLine();
                        if (inputMenu.startsWith("CREATE MENU")) {
                            processCreateMenu(inputMenu);
                        }
                        continue;
                    case 0:
                        continue; // Kembali ke looping utama di main
                    default:
                        System.out.println("Invalid option.");
                        break;
                }
                return;
            }
        }
        else System.out.println("Invalid password.");
    }
    private static void loginMember(Scanner scanner) {
        String input = scanner.nextLine();
        Member pelanggan = null;
        if (input.startsWith("CREATE MEMBER ")) {
            String[] part = input.substring(14).split("\\|");
            if (part.length == 4) {
                String id = part[0];
                String name = part[1];
                LocalDate membershipDate = LocalDate.parse(part[2], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                int startBalance = Integer.parseInt(part[3]);

                if (!isMemberExist(id)) {
                    String[] fullname = name.split(" ");
                    String firstName = fullname[0];
                    String lastName = fullname.length > 1 ? fullname[1] : "";
                    Member newMember = new Member(id, firstName, lastName, membershipDate, startBalance);
                    members.add(newMember);
                    members = mergeSortMembers(members);
                    customers.put(id, newMember);
                    System.out.println(newMember.toString());
//                    System.out.println(members.get(members.size() - 1).toString());
                }
                else System.out.println("CREATE MEMBER FAILED: " + id + " IS EXISTS");
            }
            else System.out.println("Invalid input format. Please use: CREATE MEMBER IDAnggota|Nama|TanggalDaftar|SaldoAwal");
        }
        else System.out.println("Invalid input. Please start with 'CREATE MEMBER '");
    }

    private static void loginGuest(Scanner scanner) {
        String input = scanner.nextLine();
        Guest tamu = null;
        if (input.startsWith("CREATE GUEST ")) {
            String[] part = input.substring(13).split("\\|");
            if (part.length == 2) {
                String id = part[0];
                int startBalance = Integer.parseInt(part[1]);
                if (!isGuestExist(id)) {
                    tamu = new Guest(id, startBalance);
                    guests.add(tamu);
                    guests = mergeSortGuests(guests);
                    customers.put(id, tamu);
                    System.out.println(tamu.toString());
                } else System.out.println("CREATE GUEST FAILED:" + id + " IS EXISTS");
            } else System.out.println("Invalid input format. Please use: CREATE GUEST IDTamu|SaldoAwal");
        } else System.out.println("Invalid input. Please start with 'CREATE GUEST '");
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

    private static void rentVehicle(Scanner scanner) {
        //loop 3
        while (true) {
            System.out.println("=".repeat(52));

            //list menu kendaraan
            // -> add to cart -> apply promo -> check out -> cetak pesanan
            System.out.println("[Type 1] Add to Cart");
            System.out.println("[Type 2] Delete Cart");
            System.out.println("[Type 0] Back");
            System.out.print("Enter option: ");
            int choice3 = scanner.nextInt();
            scanner.nextLine();

            switch (choice3) {
                case 1:
                    // add to cart
                    System.out.println("List of Vehicles:");
                    for (Menu item : menu) {
                        System.out.println(item.IDMenu + " - " + item.NamaMenu + " (" + item.PlatNomor + ") - Rp" + item.Harga + " per day");
                    }
                    String inputCart = scanner.nextLine();
                    if (inputCart.startsWith("ADD_TO_CART")) {
                        processAddToCart(inputCart);
                    }

                    //promo
                    System.out.println("List of Available Promotions:");
                    printPromoCodes();
                    System.out.println("=========Apply Promo=========");
                    String inputPromo = scanner.nextLine();
                    processApplyPromo(inputPromo);

                    //check out
                    System.out.println("=========Check Out==========");
                    String checkoutInput = scanner.nextLine();
                    if (checkoutInput.startsWith("CHECK_OUT")) {
                        processCheckout(checkoutInput, scanner);
                    }

                    System.out.println("=========Print Order=========");
                    while (scanner.hasNextLine()) {
                        String input = scanner.nextLine();
                        String[] parts = input.split(" ");

                        if (parts.length != 2 || !parts[0].equals("PRINT")) {
                            System.out.println("Invalid command");
                            continue;
                        }

                        String customerId = parts[1];
                        if (!customers.containsKey(customerId)) {
                            System.out.println("PRINT FAILED: NON EXISTENT CUSTOMER");
                            continue;
                        }

                        Customer customer = customers.get(customerId);
                        customer.printOrder();
                    }
                    scanner.close();

                    break;
                case 2:
                    //delete cart
                    String inputDelete = scanner.nextLine();
                    if (inputDelete.startsWith("REMOVE_FROM_CART")) {
                        processRemoveFromCart(inputDelete);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
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
        } else {
            newItem = new Menu(IDMenu, NamaMenu, PlatNomor, Harga);
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
        String promoType = parts[2];

        String promoDetails = scanner.nextLine();
        String[] details = promoDetails.split("\\|");

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
            case "FREESHIPPING":
                newPromo = new FreeShippingPromo(promoCode, startDate, endDate, percentPieces, maxPieces, minPurchase, promoType);
                break;
            case "PERCENTOFF":
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
        int Qty = Integer.parseInt(parts[3]);
        String StartDate = parts[4];

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
        boolean isNew = customer.addToCart(menuItem, Qty, StartDate);

        // Mengonversi Qty menjadi days jika lebih dari 1
        String dayOrDays = Qty > 1 ? "days" : "day";

        // Mencetak output berdasarkan kondisi apakah item baru atau sudah ada sebelumnya
        if (isNew) {
            System.out.println("ADD_TO_CART SUCCESS: " + Qty + " " + dayOrDays + " " + menuItem.NamaMenu + " " + menuItem.PlatNomor + " (NEW)");
        } else {
            System.out.println("ADD_TO_CART SUCCESS: " + Qty + " " + dayOrDays + " " + menuItem.NamaMenu + " " + menuItem.PlatNomor + " (UPDATED)");
        }
    }


    private static void processApplyPromo(String input) {
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
            if (promo == null || customer == null) {
                System.out.println("APPLY_PROMO FAILED: " + KodePromo);
                return;
            }

            LocalDate currentDate = LocalDate.now();
            if (currentDate.isBefore(promo.startDate) || currentDate.isAfter(promo.endDate)) {
                System.out.println("APPLY_PROMO FAILED: " + KodePromo + " is EXPIRED");
                return;
            }

            if (!promo.isCustomerEligible(customer)) {
                System.out.println("APPLY_PROMO FAILED: " + KodePromo + " is not applicable for this customer");
                return;
            }

            // Hitung diskon, cashback, atau potongan biaya pengiriman sesuai jenis promo
            double totalDiscount = 0;
            try {
                totalDiscount = promo.calculateTotalDiscount(null); // Anda perlu menyesuaikan dengan parameter yang dibutuhkan
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (userCategory == 'G') {
            // Tamu
            System.out.println("APPLY_PROMO FAILED: Guests are not eligible to apply promo.");
        }
        else {
            // ID pemesanan tidak valid
            System.out.println("APPLY_PROMO FAILED: Invalid user ID.");
        }
    }

    private static void printPromoCodes() {
        if (promoList.isEmpty()) {
            return; // Do nothing if the promo list is empty
        }
        for (Promotion promo : promoList) {
            System.out.println(promo.getPromoCode());
        }
    }

    private static void processRemoveFromCart(String input) {
         String[] parts = input.split(" ");
         String IDPemesanan = parts[1];
         String IDMenu = parts[2];
         int Qty = Integer.parseInt(parts[3]);
         String StartDate = parts.length > 4 ? parts[4] : null;

         Customer customer = customers.get(IDPemesanan);
         if (customer == null) {
             System.out.println("REMOVE_FROM_CART FAILED: NON EXISTENT CUSTOMER OR MENU");
             return;
         }

         Menu menuItem = binarySearchMenu(IDMenu);
         boolean removed = customer.removeFromCart(menuItem);
         if (!removed) {
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
        int saldoAkhir = customer.getBalance();

        System.out.println("TOPUP SUCCESS: " + customer.getFullName() + " " + saldoAwal + " => " + saldoAkhir);
    }

    private static void processViewBalance(String input) {
        String[] parts = input.split(" ");
        String IDPemesanan = parts[2];

        Customer customer = customers.get(IDPemesanan);
        if (customer == null) {
            System.out.println("VIEW BALANCE FAILED: NON EXISTENT CUSTOMER");
            return;
        }

        int saldo = customer.getBalance();
        System.out.println("CURRENT BALANCE: " + customer.getFullName() + " " + saldo);
    }

    private static void processCheckout(String input, Scanner scanner) {
        String[] parts = input.split(" ");
        String IDPemesan = parts[1];

        Customer customer = customers.get(IDPemesan);
        if (customer == null) {
            System.out.println("CHECK_OUT FAILED: NON EXISTENT CUSTOMER");
            return;
        }

        double subTotal = 0;
        for (Map.Entry<Menu, CartItem> entry : customer.getCart().entrySet()) {
            Menu menuItem = entry.getKey();
            CartItem cartItem = entry.getValue();
            subTotal += menuItem.Harga * cartItem.qty;
        }

        // Pertanyaan untuk biaya pengiriman kendaraan
        System.out.println("Need Vehicle Shipping Cost to your address? ");
        System.out.println("[Type 1] Yes, Deliver the Vehicle to my address");
        System.out.println("[Type 2] No, Pick up myself");
        System.out.print("Enter option: ");
        int serviceOption = scanner.nextInt();
        scanner.nextLine();

        double shippingCost = 0;
        if (serviceOption == 1) {
            System.out.println("[Type 1] Rent Car");
            System.out.println("[Type 2] Rent Motorcycle");
            System.out.print("Enter option: ");
            int choiceTypeVehicle = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter range (in km): ");
            int range = scanner.nextInt();
            scanner.nextLine();
            if (choiceTypeVehicle == 1) {
                if (range <= 10) shippingCost = 20000;
                else if (range > 10 && range <= 20) shippingCost = 40000;
                else if (range > 20 && range <= 30) shippingCost = 60000;
                else if (range > 30) shippingCost = 80000;
            } else if (choiceTypeVehicle == 2) {
                if (range <= 10) shippingCost = 10000;
                else if (range > 10 && range <= 20) shippingCost = 20000;
                else if (range > 20 && range <= 30) shippingCost = 40000;
                else if (range > 30) shippingCost = 60000;
            }
        } else {
            shippingCost = 0;
        }

        double discount = 0; // Implementasikan penghitungan diskon jika ada
        double total = subTotal + shippingCost - discount;

        if (customer.getBalance() >= total) {
            // Checkout berhasil
            Order order = customer.makeOrder(LocalDate.now(), LocalDate.now().plusDays(1), subTotal, shippingCost, discount, total);
            customer.balance -= total;
            customer.getCart().clear();
            System.out.println("CHECK_OUT SUCCESS: " + IDPemesan + " " + customer.getFullName());
        }
        else {
            // Saldo tidak mencukupi
            System.out.println("CHECK_OUT FAILED: " + IDPemesan + " " + customer.getFullName() + " INSUFFICIENT BALANCE");
        }
    }
}
