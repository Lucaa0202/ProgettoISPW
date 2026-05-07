package beans;

import utilities.enums.StatoPagamento;
import java.time.LocalDateTime;

public class PagamentoBean {
    private int idPagamento;
    private int idPrenotazione;
    private double importo;
    private String metodo; // <-- Aggiunto!
    private LocalDateTime dataPagamento;
    private StatoPagamento stato;

    // Costruttore completo
    public PagamentoBean(int idPagamento, int idPrenotazione, double importo, String metodo, LocalDateTime dataPagamento, StatoPagamento stato) {
        this.idPagamento = idPagamento;
        this.idPrenotazione = idPrenotazione;
        this.importo = importo;
        this.metodo = metodo;
        this.dataPagamento = dataPagamento;
        this.stato = stato;
    }

    public PagamentoBean() {}

    // --- GETTER E SETTER ---
    public int getIdPagamento() { return idPagamento; }
    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }

    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public StatoPagamento getStato() { return stato; }
    public void setStato(StatoPagamento stato) { this.stato = stato; }
}
