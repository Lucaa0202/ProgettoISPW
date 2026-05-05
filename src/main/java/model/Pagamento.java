package model;

public class Pagamento {
    private int idPagamento;
    private Prenotazione prenotazione; // Il pagamento è collegato a una prenotazione specifica
    private double importo;
    private String metodo;
    private String stato;
    private String dataPagamento;

    public Pagamento(int idPagamento, Prenotazione prenotazione, double importo, String metodo, String stato, String dataPagamento) {
        this.idPagamento = idPagamento;
        this.prenotazione = prenotazione;
        this.importo = importo;
        this.metodo = metodo;
        this.stato = stato;
        this.dataPagamento = dataPagamento;
    }

    public Pagamento() {}

    public int getIdPagamento() { return idPagamento; }
    public void setIdPagamento(int idPagamento) { this.idPagamento = idPagamento; }

    public Prenotazione getPrenotazione() { return prenotazione; }
    public void setPrenotazione(Prenotazione prenotazione) { this.prenotazione = prenotazione; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public String getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(String dataPagamento) { this.dataPagamento = dataPagamento; }
}