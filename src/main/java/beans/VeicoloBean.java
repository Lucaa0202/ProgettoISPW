package beans;

public class VeicoloBean {
    private String targa;
    private String marca;
    private String modello;
    private int postiTotali;
    private String emailProprietario;

    public VeicoloBean(String targa, String marca, String modello, int postiTotali, String emailProprietario) {
        this.targa = targa;
        this.marca = marca;
        this.modello = modello;
        this.postiTotali = postiTotali;
        this.emailProprietario = emailProprietario;
    }

    public VeicoloBean() {}

    // --- GETTER E SETTER ---
    public String getTarga() { return targa; }
    public void setTarga(String targa) { this.targa = targa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public int getPostiTotali() { return postiTotali; }
    public void setPostiTotali(int postiTotali) { this.postiTotali = postiTotali; }

    public String getEmailProprietario() { return emailProprietario; }
    public void setEmailProprietario(String emailProprietario) { this.emailProprietario = emailProprietario; }
}