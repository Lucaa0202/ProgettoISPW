package dao.demo;

import dao.PagamentoDAO;
import dao.demo.shared.SharedResources;
import model.Pagamento;
import utilities.enums.StatoPagamento;
import exceptions.NoResultException;
import exceptions.DbOperationException;

import java.util.ArrayList;
import java.util.List;

public class PagamentoDAOP implements PagamentoDAO {

    @Override
    public void inserisciPagamento(Pagamento pagamento) throws DbOperationException {
        if (pagamento == null) {
            throw new DbOperationException("Impossibile inserire un pagamento nullo.");
        }
        SharedResources.getInstance().getPagamenti().put(pagamento.getIdPagamento(), pagamento);
    }

    @Override
    public Pagamento trovaPagamentoPerId(int idPagamento) throws NoResultException {
        Pagamento p = SharedResources.getInstance().getPagamenti().get(idPagamento);
        if (p == null) {
            throw new NoResultException("Pagamento non trovato con ID: " + idPagamento);
        }
        return p;
    }

    @Override
    public void aggiornaStatoPagamento(int idPagamento, StatoPagamento nuovoStato) throws DbOperationException {
        Pagamento p = SharedResources.getInstance().getPagamenti().get(idPagamento);
        if (p == null) {
            throw new DbOperationException("Impossibile aggiornare lo stato. Pagamento " + idPagamento + " inesistente.");
        }
        p.setStato(nuovoStato);
    }

    @Override
    public List<Pagamento> trovaPagamentiPerPrenotazione(int idPrenotazione) throws NoResultException {
        List<Pagamento> trovati = new ArrayList<>();
        for (Pagamento p : SharedResources.getInstance().getPagamenti().values()) {
            if (p.getIdPrenotazione() == idPrenotazione) {
                trovati.add(p);
            }
        }
        if (trovati.isEmpty()) {
            throw new NoResultException("Nessun pagamento trovato per la prenotazione: " + idPrenotazione);
        }
        return trovati;
    }
}