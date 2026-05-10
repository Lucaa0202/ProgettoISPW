package controller;

import beans.RecensioneBean;
import beans.UtenteBean;
import dao.RecensioneDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Recensione;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;

import java.util.ArrayList;
import java.util.List;

public class RecensioneController {

    private final BeanAndModelMapperFactory factory;
    private final RecensioneDAO recensioneDAO;

    public RecensioneController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.recensioneDAO = FactoryDAO.getRecensioneDAO(); // Assicurati di avere questo nella Factory
    }

    // L'utente lascia una recensione a fine viaggio
    public void lasciaRecensione(RecensioneBean recensioneBean) throws DbOperationException {
        Recensione recensione = factory.fromBeanToModel(recensioneBean, RecensioneBean.class);

        try {
            // Salva la recensione nel DB
            recensioneDAO.inserisciRecensione(recensione);
        } catch (Exception e) {
            throw new DbOperationException("Errore durante il salvataggio della recensione: " + e.getMessage());
        }
    }

    // Recupera tutte le recensioni ricevute da un utente (es. per calcolare la media nel suo profilo)
    public void visualizzaRecensioniRicevute(UtenteBean utenteBean, List<RecensioneBean> recensioniTrovate) throws NoResultException {
        recensioniTrovate.clear();

        try {
            // Passiamo l'email (o l'identificativo) dell'utente per trovare le recensioni a lui destinate
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
}