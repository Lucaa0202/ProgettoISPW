package beans;

public class RecensioneBean {
    private int idRecensione;
    private int voto;
    private String commento;
    private String emailAutore;
    private String emailDestinatario;
    private int idViaggio; // Aggiunto per coerenza con il Model

    // Costruttore completo
    public RecensioneBean(int idRecensione, int voto, String commento, String emailAutore, String emailDestinatario, int idViaggio) {
        this.idRecensione = idRecensione;
        this.voto = voto;
        this.commento = commento;
        this.emailAutore = emailAutore;
        this.emailDestinatario = emailDestinatario;
        this.idViaggio = idViaggio;
    }

    public RecensioneBean() {}

    // --- GETTER E SETTER ---
    public int getIdRecensione() { return idRecensione; }
    public void setIdRecensione(int idRecensione) { this.idRecensione = idRecensione; }

    public int getVoto() { return voto; }
    public void setVoto(int voto) { this.voto = voto; }

    public String getCommento() { return commento; }
    public void setCommento(String commento) { this.commento = commento; }

    public String getEmailAutore() { return emailAutore; }
    public void setEmailAutore(String emailAutore) { this.emailAutore = emailAutore; }

    public String getEmailDestinatario() { return emailDestinatario; }
    public void setEmailDestinatario(String emailDestinatario) { this.emailDestinatario = emailDestinatario; }

    public int getIdViaggio() { return idViaggio; }
    public void setIdViaggio(int idViaggio) { this.idViaggio = idViaggio; }
}