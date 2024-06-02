import java.time.LocalDate;

abstract class Promotion implements Applicable, Comparable<Promotion> {
    String promoCode;
    LocalDate startDate;
    LocalDate endDate;
    double percentPieces;
    int maxPieces;
    int minPurchase;
    String promoType;

    Promotion(String promoCode, LocalDate startDate, LocalDate endDate, int percentPieces, int maxPieces, int minPurchase, String promoType) {
        this.promoCode = promoCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percentPieces = percentPieces * 0.01;
        this.maxPieces = maxPieces;
        this.minPurchase = minPurchase;
        this.promoType = promoType;
    }

    @Override
    public abstract boolean isCustomerEligible(Customer customer) ;
    @Override
    public abstract boolean isMinimumPriceEligible(Order order);
    @Override
    public abstract double calculateTotalDiscount(Order order) throws Exception;
    @Override
    public abstract double calculateTotalCashback(Order order) throws Exception;

    public int compareTo(Promotion o) {
        // Implementasi perbandingan berdasarkan tanggal mulai
        return this.startDate.compareTo(o.startDate);
    }

    public String toString() {
        return promoCode;
    }
}
