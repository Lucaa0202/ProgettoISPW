package model;

import utilities.enums.StatoPagamento;
import java.time.LocalDateTime;

public class Pagamento {
    private int idPagamento;
    private int idPrenotazione; // <-- Appiattito!
    private double importo;
    private String metodo; // Es: "Carta", "PayPal"
    private StatoPagamento stato;
    private LocalDateTime dataPagamento;

    // Costruttore CON ID (dal Database)
    public Pagamento(int idPagamento, int idPrenotazione, double importo, String metodo, StatoPagamento stato, LocalDateTime dataPagamento) {
        this.idPagamento = idPagamento;
        this.idPrenotazione = idPrenotazione;
        this.importo = importo;
        this.metodo = metodo;
        this.stato = stato;
        this.dataPagamento = dataPagamento;
    }

    // Costruttore SENZA ID (Nuovo pagamento da inserire)
    public Pagamento(int idPrenotazione, double importo, String metodo, StatoPagamento stato, LocalDateTime dataPagamento) {
        this.idPrenotazione = idPrenotazione;
        this.importo = importo;
        this.metodo = metodo;
        this.stato = stato;
        this.dataPagamento = dataPagamento;
    }

    // --- GETTER E SETTER ---
    public int getIdPagamento() { return idPagamento; }
    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }

    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public StatoPagamento getStato() { return stato; }
    public void setStato(StatoPagamento stato) { this.stato = stato; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}