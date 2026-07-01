package dao.demo;

import model.Viaggio;
import dao.InvitoDAO;
import dao.demo.shared.SharedResources;
import model.Invito;
import utilities.enums.StatoInvito;
import exceptions.NoResultException;
import exceptions.DbOperationException;

import java.util.ArrayList;
import java.util.List;

public class InvitoDAOP implements InvitoDAO {

    @Override
    public void creaInvito(Invito invito) throws DbOperationException {
        if (invito == null) {
            throw new DbOperationException("Impossibile creare un invito nullo.");
        }
        SharedResources.getInstance().getInviti().put(invito.getIdInvito(), invito);
    }

    @Override
    public List<Invito> trovaInvitiRicevuti(String emailPasseggero) throws NoResultException {
        List<Invito> trovati = new ArrayList<>();
        for (Invito i : SharedResources.getInstance().getInviti().values()) {
            if (i.getEmailPasseggero().equals(emailPasseggero)) {
                trovati.add(i);
            }
        }
        if (trovati.isEmpty()) {
            throw new NoResultException("Nessun invito ricevuto trovato per: " + emailPasseggero);
        }
        return trovati;
    }

    @Override
    public List<String> trovaStoricoPasseggeri(String emailGuidatore) throws NoResultException {
        List<String> storico = new ArrayList<>();

        for (Invito i : SharedResources.getInstance().getInviti().values()) {
            // 1. Recuperiamo il viaggio associato a questo invito
            Viaggio v = SharedResources.getInstance().getViaggi().get(i.getIdViaggio());

            // 2. Se il viaggio esiste in memoria E il suo guidatore è quello cercato...
            if (v != null && v.getEmailGuidatore().equals(emailGuidatore)) {
                // 3. Aggiungiamo il passeggero allo storico (se non c'è già)
                if (!storico.contains(i.getEmailPasseggero())) {
                    storico.add(i.getEmailPasseggero());
                }
            }
        }

        if (storico.isEmpty()) {
            throw new NoResultException("Nessuno storico passeggeri trovato per il guidatore: " + emailGuidatore);
        }
        return storico;
    }

    @Override
    public void rispondiInvito(int idInvito, StatoInvito risposta) throws DbOperationException {
        Invito i = SharedResources.getInstance().getInviti().get(idInvito);
        if (i == null) {
            throw new DbOperationException("Impossibile rispondere. Invito " + idInvito + " inesistente.");
        }
        i.setStato(risposta);
    }
}