package dao.demo;

import dao.RecensioneDAO;
import dao.demo.shared.SharedResources;
import model.Recensione;
import exceptions.NoResultException;
import exceptions.DbOperationException;

import java.util.ArrayList;
import java.util.List;

public class RecensioneDAOP implements RecensioneDAO {

    @Override
    public void inserisciRecensione(Recensione recensione) throws DbOperationException {
        if (recensione == null) {
            throw new DbOperationException("Impossibile inserire una recensione nulla.");
        }
        SharedResources.getInstance().getRecensioni().put(recensione.getIdRecensione(), recensione);
    }

    @Override
    public List<Recensione> trovaRecensioniRicevute(String emailRecensito) throws NoResultException {
        List<Recensione> trovate = new ArrayList<>();
        for (Recensione r : SharedResources.getInstance().getRecensioni().values()) {
            if (r.getEmailRecensito().equals(emailRecensito)) {
                trovate.add(r);
            }
        }
        if (trovate.isEmpty()) {
            throw new NoResultException("Nessuna recensione trovata per: " + emailRecensito);
        }
        return trovate;
    }

    @Override
    public List<Recensione> trovaRecensioniPerViaggio(int idViaggio) throws NoResultException {
        List<Recensione> trovate = new ArrayList<>();
        for (Recensione r : SharedResources.getInstance().getRecensioni().values()) {
            if (r.getIdViaggio() == idViaggio) {
                trovate.add(r);
            }
        }
        if (trovate.isEmpty()) {
            throw new NoResultException("Nessuna recensione trovata per il viaggio: " + idViaggio);
        }
        return trovate;
    }

    @Override
    public double calcolaMediaVoti(String emailRecensito) throws NoResultException {
        double somma = 0;
        int contatore = 0;

        for (Recensione r : SharedResources.getInstance().getRecensioni().values()) {
            if (r.getEmailRecensito().equals(emailRecensito)) {
                somma += r.getVoto();
                contatore++;
            }
        }

        if (contatore == 0) {
            throw new NoResultException("Nessuna recensione per calcolare la media di: " + emailRecensito);
        }

        return somma / contatore;
    }

    @Override
    public boolean esisteRecensione(String emailRecensore, int idViaggio) throws DbOperationException {
        for (Recensione r : SharedResources.getInstance().getRecensioni().values()) {
            if (r.getEmailRecensore().equals(emailRecensore) && r.getIdViaggio() == idViaggio) {
                return true;
            }
        }
        return false;
    }
}