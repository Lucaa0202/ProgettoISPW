package controller;

import beans.InvitoBean;
import dao.InvitoDAO;
import exceptions.DbOperationException;
import model.Invito;
import utilities.enums.StatoInvito; // <-- Aggiunto per gestire la risposta
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;
import patterns.observer.InvitoManagerConcreteSubject;

public class InvitoController {

    private final BeanAndModelMapperFactory factory;
    private final InvitoDAO invitoDAO;

    public InvitoController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.invitoDAO = FactoryDAO.getInvitoDAO();
    }

    // Il Guidatore invia un invito al passeggero
    public void inviaInvito(InvitoBean invitoBean) throws DbOperationException {
        Invito invito = factory.fromBeanToModel(invitoBean, InvitoBean.class);

        try {
            // CORRETTO: Usiamo il nome esatto del tuo DAO
            invitoDAO.creaInvito(invito);

            // Avvisiamo l'Observer degli Inviti per la notifica
            InvitoManagerConcreteSubject subject = InvitoManagerConcreteSubject.getInstance();
            subject.addInvito(invito);

        } catch (Exception e) {
            throw new DbOperationException("Errore durante l'invio dell'invito: " + e.getMessage());
        }
    }

    // Il passeggero risponde all'invito (es. Accetta o Rifiuta)
    public void rispondiInvito(InvitoBean invitoBean, StatoInvito risposta) throws DbOperationException {
        Invito invito = factory.fromBeanToModel(invitoBean, InvitoBean.class);

        try {
            // CORRETTO: Aggiorniamo lo stato nel Database passando l'ID e la risposta (es. StatoInvito.ACCETTATO)
            invitoDAO.rispondiInvito(invito.getIdInvito(), risposta);

            // Indipendentemente dalla risposta, togliamo la notifica dalle schermate "pendenti"
            InvitoManagerConcreteSubject subject = InvitoManagerConcreteSubject.getInstance();
            subject.removeInvito(invito);

        } catch (Exception e) {
            throw new DbOperationException("Errore durante la gestione dell'invito: " + e.getMessage());
        }
    }
}