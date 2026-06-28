package controller;

import beans.RecensioneBean;
import beans.UtenteBean;
import dao.RecensioneDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Recensione;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;
import utilities.enums.StatoPrenotazione;

import java.time.LocalDateTime;
import java.util.List;

public class RecensioneController {

    private final BeanAndModelMapperFactory factory;
    private final RecensioneDAO recensioneDAO;

    public RecensioneController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.recensioneDAO = FactoryDAO.getRecensioneDAO();
    }

    public void lasciaRecensione(RecensioneBean recensioneBean) throws DbOperationException {
        // Controllo di sicurezza lato controller prima di inserire
        if (recensioneDAO.esisteRecensione(recensioneBean.getEmailAutore(), recensioneBean.getIdViaggio())) {
            throw new DbOperationException("Hai già lasciato una recensione per questo viaggio!");
        }

        Recensione recensione = factory.fromBeanToModel(recensioneBean, RecensioneBean.class);

        try {
            recensioneDAO.inserisciRecensione(recensione);
        } catch (Exception e) {
            throw new DbOperationException("Errore durante il salvataggio della recensione: " + e.getMessage());
        }
    }

    public void visualizzaRecensioniRicevute(UtenteBean utenteBean, List<RecensioneBean> recensioniTrovate) throws NoResultException {
        recensioniTrovate.clear();

        try {
            String emailDestinatario = utenteBean.getCredenziali().getEmail();
            List<Recensione> recensioniModel = recensioneDAO.trovaRecensioniRicevute(emailDestinatario);

            if (recensioniModel.isEmpty()) {
                throw new NoResultException("Nessuna recensione trovata per questo utente.");
            }

            for (Recensione r : recensioniModel) {
                RecensioneBean rBean = factory.fromModelToBean(r, Recensione.class);
                recensioniTrovate.add(rBean);
            }

        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new NoResultException("Errore nel recupero delle recensioni: " + e.getMessage());
        }
    }

    // =========================================================================
    // METODO MAGICO: Decide se sbloccare il bottone "Lascia Recensione" nella GUI
    // =========================================================================
    public boolean puoLasciareRecensione(String emailRecensore, int idViaggio, LocalDateTime dataOraViaggio, StatoPrenotazione statoPrenotazione) {

        // 1. Il viaggio non è ancora avvenuto?
        if (dataOraViaggio.isAfter(LocalDateTime.now())) {
            return false;
        }

        // 2. La prenotazione non è andata a buon fine?
        if (statoPrenotazione != StatoPrenotazione.CONFERMATA) {
            return false;
        }

        // 3. Ha già recensito questo viaggio?
        try {
            if (recensioneDAO.esisteRecensione(emailRecensore, idViaggio)) {
                return false;
            }
        } catch (DbOperationException e) {
            System.err.println("Errore controllo spam: " + e.getMessage());
            return false; // Nel dubbio, blocchiamo per sicurezza
        }

        // Se sopravvive a tutti i controlli, ha il diritto di recensire!
        return true;
    }
    // Aggiungi questo metodo nel tuo RecensioneController!
    public void visualizzaRecensioniRicevute(String emailDestinatario, List<RecensioneBean> recensioniTrovate) throws NoResultException {
        recensioniTrovate.clear();
        try {
            List<Recensione> recensioniModel = recensioneDAO.trovaRecensioniRicevute(emailDestinatario);

            if (recensioniModel.isEmpty()) {
                throw new NoResultException("Nessuna recensione trovata per questo utente.");
            }

            for (Recensione r : recensioniModel) {
                RecensioneBean rBean = factory.fromModelToBean(r, Recensione.class);
                recensioniTrovate.add(rBean);
            }
        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new NoResultException("Errore nel recupero delle recensioni: " + e.getMessage());
        }
    }
}