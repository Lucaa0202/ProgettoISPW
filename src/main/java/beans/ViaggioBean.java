package beans;

import java.time.LocalDateTime;

public class ViaggioBean {
    private int idViaggio;
    private String emailGuidatore;
    private String partenza;
    private String arrivo;
    private LocalDateTime dataOraPartenza;
    private int postiDisponibili;
    private double contributoCondiviso;

    public ViaggioBean(int idViaggio, String emailGuidatore, String partenza, String arrivo, LocalDateTime dataOraPartenza, int postiDisponibili, double contributoCondiviso) {
        this.idViaggio = idViaggio;
        this.emailGuidatore = emailGuidatore;
        this.partenza = partenza;
        this.arrivo = arrivo;
        this.dataOraPartenza = dataOraPartenza;
        this.postiDisponibili = postiDisponibili;
        this.contributoCondiviso = contributoCondiviso;
    }

    public ViaggioBean() {}

    // --- GETTER E SETTER ---
    public int getIdViaggio() { return idViaggio; }
    public void setIdViaggio(int idViaggio) { this.idViaggio = idViaggio; }

    public String getEmailGuidatore() { return emailGuidatore; }
    public void setEmailGuidatore(String emailGuidatore) { this.emailGuidatore = emailGuidatore; }

    public String getPartenza() { return partenza; }
    public void setPartenza(String partenza) { this.partenza = partenza; }

    public String getArrivo() { return arrivo; }
    public void setArrivo(String arrivo) { this.arrivo = arrivo; }

    public LocalDateTime getDataOraPartenza() { return dataOraPartenza; }
    public void setDataOraPartenza(LocalDateTime dataOraPartenza) { this.dataOraPartenza = dataOraPartenza; }

    public int getPostiDisponibili() { return postiDisponibili; }
    public void setPostiDisponibili(int postiDisponibili) { this.postiDisponibili = postiDisponibili; }

    public double getContributoCondiviso() { return contributoCondiviso; }
    public void setContributoCondiviso(double contributoCondiviso) { this.contributoCondiviso = contributoCondiviso; }
}