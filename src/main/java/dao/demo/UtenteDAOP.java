package dao.demo;

import dao.UtenteDAO;
import dao.demo.shared.SharedResources;
import model.Utente;
import model.Credentials;
import exceptions.LoginAndRegistrationException;
import exceptions.MailAlreadyExistsException;
import exceptions.WrongEmailOrPasswordException;
import exceptions.NoResultException;

public class UtenteDAOP implements UtenteDAO {

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    @Override
    public boolean esisteEmail(String email) {
        return SharedResources.getInstance().getUtenti().containsKey(normalizeEmail(email));
    }

    @Override
    public void registraUtente(Utente utente) throws MailAlreadyExistsException, LoginAndRegistrationException {
        String email = normalizeEmail(utente.getCredentials().getEmail());
        if (esisteEmail(email)) {
            throw new MailAlreadyExistsException("Mail già esistente: " + email);
        }
        // Inserisco nella mappa usando l'email come chiave
        SharedResources.getInstance().getUtenti().put(email, utente);
    }

    @Override
    public Utente login(Credentials credentials) throws WrongEmailOrPasswordException, LoginAndRegistrationException {
        String email = normalizeEmail(credentials.getEmail());
        Utente storedUtente = SharedResources.getInstance().getUtenti().get(email);

        // Controllo se l'utente esiste e se la password corrisponde
        if (storedUtente == null || !storedUtente.getCredentials().getPassword().equals(credentials.getPassword())) {
            throw new WrongEmailOrPasswordException("Email o password errati");
        }
        return storedUtente;
    }

    @Override
    public Utente recuperaUtente(String email) throws NoResultException {
        Utente storedUtente = SharedResources.getInstance().getUtenti().get(normalizeEmail(email));
        if (storedUtente == null) {
            throw new NoResultException("Utente non trovato per l'email: " + email);
        }
        return storedUtente;
    }
}