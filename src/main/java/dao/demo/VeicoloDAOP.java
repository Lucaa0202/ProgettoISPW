package dao.demo;

import dao.VeicoloDAO;
import dao.demo.shared.SharedResources;
import model.Veicolo;
import exceptions.NoResultException;
import exceptions.DbOperationException;

import java.util.ArrayList;
import java.util.List;

public class VeicoloDAOP implements VeicoloDAO {

    @Override
    public void inserisciVeicolo(Veicolo veicolo) throws DbOperationException {
        if (veicolo == null) {
            throw new DbOperationException("Impossibile inserire un veicolo nullo.");
        }
        SharedResources.getInstance().getVeicoli().put(veicolo.getTarga(), veicolo);
    }

    @Override
    public List<Veicolo> trovaVeicoliPerProprietario(String emailProprietario) throws NoResultException {
        List<Veicolo> trovati = new ArrayList<>();
        for (Veicolo v : SharedResources.getInstance().getVeicoli().values()) {
            if (v.getEmailProprietario().equals(emailProprietario)) {
                trovati.add(v);
            }
        }
        if (trovati.isEmpty()) {
            throw new NoResultException("Nessun veicolo trovato per il proprietario: " + emailProprietario);
        }
        return trovati;
    }

    @Override
    public void rimuoviVeicolo(String targa) throws DbOperationException {
        if (!SharedResources.getInstance().getVeicoli().containsKey(targa)) {
            throw new DbOperationException("Impossibile rimuovere. Veicolo con targa " + targa + " non trovato.");
        }
        SharedResources.getInstance().getVeicoli().remove(targa);
    }

    @Override
    public Veicolo trovaVeicoloPerTarga(String targa) throws NoResultException {
        Veicolo v = SharedResources.getInstance().getVeicoli().get(targa);
        if (v == null) {
            throw new NoResultException("Veicolo con targa " + targa + " non trovato.");
        }
        return v;
    }
}