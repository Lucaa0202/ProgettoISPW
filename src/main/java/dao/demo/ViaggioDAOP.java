package dao.demo;

import dao.ViaggioDAO;
import dao.demo.shared.SharedResources;
import model.Viaggio;
import exceptions.NoResultException;
import exceptions.DbOperationException;

import java.util.ArrayList;
import java.util.List;

public class ViaggioDAOP implements ViaggioDAO {

    @Override
    public void inserisciViaggio(Viaggio viaggio) throws DbOperationException {
        if (viaggio == null) {
            throw new DbOperationException("Impossibile inserire un viaggio nullo.");
        }
        SharedResources.getInstance().getViaggi().put(viaggio.getIdViaggio(), viaggio);
    }

    @Override
    public List<Viaggio> cercaViaggi(String partenza, String destinazione, String emailPasseggero) throws NoResultException {
        List<Viaggio> trovati = new ArrayList<>();
        for (Viaggio v : SharedResources.getInstance().getViaggi().values()) {
            // Controlla partenza, destinazione e che il guidatore non sia il passeggero stesso
            if (v.getPartenza().equalsIgnoreCase(partenza) &&
                    v.getDestinazione().equalsIgnoreCase(destinazione) &&
                    !v.getEmailGuidatore().equals(emailPasseggero)) {
                trovati.add(v);
            }
        }
        if (trovati.isEmpty()) {
            throw new NoResultException("Nessun viaggio trovato da " + partenza + " a " + destinazione);
        }
        return trovati;
    }

    @Override
    public Viaggio recuperaViaggio(int idViaggio) throws NoResultException {
        Viaggio v = SharedResources.getInstance().getViaggi().get(idViaggio);
        if (v == null) {
            throw new NoResultException("Viaggio con ID " + idViaggio + " non trovato.");
        }
        return v;
    }

    @Override
    public void aggiornaPostiDisponibili(int idViaggio, int nuoviPosti) throws DbOperationException {
        Viaggio v = SharedResources.getInstance().getViaggi().get(idViaggio);
        if (v == null) {
            throw new DbOperationException("Impossibile aggiornare i posti. Viaggio con ID " + idViaggio + " non trovato.");
        }
        v.setPostiDisponibili(nuoviPosti);
    }

    @Override
    public List<Viaggio> recuperaViaggiPerGuidatore(String emailGuidatore) throws NoResultException {
        List<Viaggio> trovati = new ArrayList<>();
        for (Viaggio v : SharedResources.getInstance().getViaggi().values()) {
            if (v.getEmailGuidatore().equals(emailGuidatore)) {
                trovati.add(v);
            }
        }
        if (trovati.isEmpty()) {
            throw new NoResultException("Nessun viaggio trovato per il guidatore: " + emailGuidatore);
        }
        return trovati;
    }
}