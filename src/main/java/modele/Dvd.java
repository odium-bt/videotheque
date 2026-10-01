package modele;

public class Dvd extends FichierVideo {
    private String numero;
    private int zone;

    public Dvd(String numero, int zone) {
        this.numero = numero;
        this.zone = zone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getSupport() {
        return "DVD";
    }

    public void lire() {
        System.out.println("Prenez le DVD DVD-001 \"Amélie\" et insérez-le dans un lecteur zone 2.");
    }
}
