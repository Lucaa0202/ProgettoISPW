package dao;

import exceptions.LoginAndRegistrationException;
import model.Utente;
import model.Credentials;
import exceptions.MailAlreadyExistsException;
import exceptions.WrongEmailOrPasswordException;
import exceptions.NoResultException;

public interface UtenteDAO {
    // Controlla se l'email è già registrata
    boolean esisteEmail(String email);

    // Registra un nuovo utente (lancia eccezione se l'email esiste già)
    void registraUtente(Utente utente) throws MailAlreadyExistsException, LoginAndRegistrationException;

    // Effettua il login (restituisce l'Utente completo se va bene, altrimenti lancia eccezione)
    Utente login(Credentials credentials) throws WrongEmailOrPasswordException, LoginAndRegistrationException;

    // Recupera i dati di un utente tramite email
    Utente recuperaUtente(String email) throws NoResultException;


    default void modificaUtente(Utente utente) {
        throw new UnsupportedOperationException("Modifica dell'utente non supportata da questa implementazione.");
    }
}