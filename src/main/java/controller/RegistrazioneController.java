package controller;

import beans.UtenteBean;
import dao.UtenteDAO;
import exceptions.LoginAndRegistrationException;
import exceptions.MailAlreadyExistsException;
import model.Utente;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;

public class RegistrazioneController {

    private final BeanAndModelMapperFactory beanAndModelMapperFactory;
    private final UtenteDAO utenteDAO;

    public RegistrazioneController() {
        this.beanAndModelMapperFactory = BeanAndModelMapperFactory.getInstance();
        this.utenteDAO = FactoryDAO.getUtenteDAO();
    }

    public void registerUser(UtenteBean utenteBean) throws LoginAndRegistrationException, MailAlreadyExistsException {
        Utente utente = beanAndModelMapperFactory.fromBeanToModel(utenteBean, UtenteBean.class);

        try {
            // CORRETTO: Ora chiama esattamente il metodo definito nella tua interfaccia
            utenteDAO.registraUtente(utente);

        } catch (MailAlreadyExistsException e) {
            throw new MailAlreadyExistsException(e.getMessage());
        } catch (Exception e) {
            throw new LoginAndRegistrationException("Errore durante la registrazione: " + e.getMessage());
        }
    }
}