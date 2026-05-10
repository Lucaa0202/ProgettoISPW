package controller;

import beans.CredenzialiBean;
import beans.UtenteBean;
import dao.UtenteDAO;
import exceptions.LoginAndRegistrationException;
import exceptions.NoResultException;
import exceptions.WrongEmailOrPasswordException;
import model.Credentials;
import model.Utente;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;

public class LoginController {

    private final BeanAndModelMapperFactory beanAndModelMapperFactory;
    private final UtenteDAO utenteDAO;

    public LoginController() {
        this.beanAndModelMapperFactory = BeanAndModelMapperFactory.getInstance();
        this.utenteDAO = FactoryDAO.getUtenteDAO();
    }

    public void login(CredenzialiBean credenzialiBean) throws WrongEmailOrPasswordException, LoginAndRegistrationException {
        try {
            Credentials credentials = beanAndModelMapperFactory.fromBeanToModel(credenzialiBean, CredenzialiBean.class);

            // CORRETTO: Usa il tuo metodo login (che lancia eccezioni se fallisce)
            utenteDAO.login(credentials);

        } catch (WrongEmailOrPasswordException e) {
            throw new WrongEmailOrPasswordException(e.getMessage());
        } catch (LoginAndRegistrationException e) {
            throw new LoginAndRegistrationException(e.getMessage());
        }
    }

    public void retrieveUtente(UtenteBean utenteBean) throws NoResultException {
        try {
            // Prendo l'email dal Bean (assicurati che il tuo UtenteBean abbia getEmail()
            // o getCredenzialiBean().getEmail() a seconda di come l'hai strutturato)
            // Usa getCredenziali() perché così è scritto nel tuo UtenteBean
            String email = utenteBean.getCredenziali().getEmail();

            // CORRETTO: Chiamo il tuo metodo passandogli la Stringa
            Utente utenteTrovato = utenteDAO.recuperaUtente(email);

            // Imposto i dati nel Bean per la grafica
            utenteBean.setNome(utenteTrovato.getNome());
            utenteBean.setCognome(utenteTrovato.getCognome());
            // utenteBean.setTelefono(utenteTrovato.getTelefono()); <-- aggiungi altri campi se serve

        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        } catch (Exception e) {
            throw new NoResultException("Errore nel recupero dei dati dell'utente.");
        }
    }
}