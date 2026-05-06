package model;

import utilities.enums.StatoPagamento;
import java.time.LocalDateTime;

public class Pagamento {
    private int idPagamento;
    private Prenotazione prenotazione;
    private double importo;
    private String metodo; // Es: "Carta", "PayPal"
    private StatoPagamento stato;
    private LocalDateTime dataPagamento;

    public Pagamento(int idPagamento, Prenotazione prenotazione, double importo, String metodo, StatoPagamento stato, LocalDateTime dataPagamento) {
        this.idPagamento = idPagamento;
        this.prenotazione = prenotazione;
        this.importo = importo;
        this.metodo = metodo;
        this.stato = stato;
        this.dataPagamento = dataPagamento;
    }

    public Pagamento(Prenotazione prenotazione, double importo, String metodo, StatoPagamento stato, LocalDateTime dataPagamento) {
        this.prenotazione = prenotazione;
        this.importo = importo;
        this.metodo = metodo;
        this.stato = stato;
        this.dataPagamento = dataPagamento;
    }

    // --- GETTER E SETTER ---
    public int getIdPagamento() { return idPagamento; }
    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }

    public Prenotazione getPrenotazione() { return prenotazione; }
    public void setPrenotazione(Prenotazione prenotazione) { this.prenotazione = prenotazione; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public StatoPagamento getStato() { return stato; }
    public void setStato(StatoPagamento stato) { this.stato = stato; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}