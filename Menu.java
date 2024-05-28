public class Menu {
    String IDMenu;
    String NamaMenu;
    String PlatNomor;
    int Harga;
    String CustomType;

    Menu(String IDMenu, String NamaMenu, String PlatNomor, int Harga, String CustomType) {
        this.IDMenu = IDMenu;
        this.NamaMenu = NamaMenu;
        this.PlatNomor = PlatNomor;
        this.Harga = Harga;
        this.CustomType = CustomType;
    }

    Menu(String IDMenu, String NamaMenu, String PlatNomor, int Harga) {
        this(IDMenu, NamaMenu, PlatNomor, Harga, null);
    }

    @Override
    public String toString() {
        return IDMenu + " " + NamaMenu + " " + PlatNomor;
    }
}
