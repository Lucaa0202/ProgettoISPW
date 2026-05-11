package controller;

import beans.UtenteBean;
import dao.UtenteDAO;
import exceptions.LoginAndRegistrationException;
import exceptions.MailAlreadyExistsException;
import model.Utente;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;

public class UserRegistrationController {

    private final UtenteDAO utenteDAO;
    private final BeanAndModelMapperFactory mapperFactory;

    public UserRegistrationController() {
        this.utenteDAO = FactoryDAO.getUtenteDAO();
        this.mapperFactory = BeanAndModelMapperFactory.getInstance();
    }

    public void registraUtente(UtenteBean utenteBean) throws MailAlreadyExistsException, LoginAndRegistrationException {
        // Trasformiamo il Bean in Model (assicurati che il mapper per Utente sia registrato!)
        Utente utente = mapperFactory.fromBeanToModel(utenteBean, UtenteBean.class);

        // Chiamiamo il DAO per scrivere nel DB
        utenteDAO.registraUtente(utente);
    }
}