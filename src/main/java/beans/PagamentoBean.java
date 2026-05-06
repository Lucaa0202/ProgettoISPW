package beans;

import utilities.enums.StatoPagamento;
import java.time.LocalDateTime;

public class PagamentoBean {
    private int idPagamento;
    private int idPrenotazione;
    private double importo;
    private LocalDateTime dataPagamento;
    private StatoPagamento stato;

    // Costruttore completo
    public PagamentoBean(int idPagamento, int idPrenotazione, double importo, LocalDateTime dataPagamento, StatoPagamento stato) {
        this.idPagamento = idPagamento;
        this.idPrenotazione = idPrenotazione;
        this.importo = importo;
        this.dataPagamento = dataPagamento;
        this.stato = stato;
    }

    // Costruttore per un nuovo pagamento appena tentato
    public PagamentoBean(int idPrenotazione, double importo, StatoPagamento stato) {
        this.idPrenotazione = idPrenotazione;
        this.importo = importo;
        this.stato = stato;
        this.dataPagamento = LocalDateTime.now(); // Imposta l'ora attuale
    }

    public PagamentoBean() {}

    // --- GETTER E SETTER ---
    public int getIdPagamento() { return idPagamento; }
    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }

    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public StatoPagamento getStato() { return stato; }
    public void setStato(StatoPagamento stato) { this.stato = stato; }
}
