package dao.full.sql;

import dao.UtenteDAO;
import dao.full.sql.ConnectionSQL;
import exceptions.DbOperationException;
import exceptions.MailAlreadyExistsException;
import exceptions.WrongEmailOrPasswordException;
import exceptions.NoResultException;
import exceptions.LoginAndRegistrationException;
import model.Utente;
import model.Credentials;
import query.UtenteQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAOSQL implements UtenteDAO {

    // Costanti per le colonne del database (esattamente come il tuo amico)
    private static final String EMAIL = "email";
    private static final String NOME = "nome";
    private static final String COGNOME = "cognome";
    private static final String TELEFONO = "telefono";
    private static final String DATA_REGISTRAZIONE = "dataRegistrazione";

    @Override
    public boolean esisteEmail(String email) {
        try (Connection conn = ConnectionSQL.getConnection()) {
            int result = UtenteQuery.checkEmail(conn, email);
            return result != 0;
        } catch (SQLException | DbOperationException e) { // <-- Aggiunto DbOperationException qui!
            handleException(e);
        }
        return false;
    }

    @Override
    public void registraUtente(Utente utente) throws MailAlreadyExistsException, LoginAndRegistrationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            if (esisteEmail(utente.getCredentials().getEmail())) {
                throw new MailAlreadyExistsException("Mail già esistente");
            }
            // Ora basta chiamare il metodo, l'eccezione viene gestita dentro la Query!
            UtenteQuery.registerUtente(conn, utente);

        } catch (SQLException | DbOperationException e) {
            throw new LoginAndRegistrationException("Errore nella registrazione", e);
        }
    }

    @Override
    public Utente login(Credentials credentials) throws WrongEmailOrPasswordException, LoginAndRegistrationException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = UtenteQuery.logQuery(conn, credentials)) {

            if (rs.next()) {
                // Recuperiamo i dati e costruiamo l'utente
                Utente utente = new Utente(
                        credentials,
                        rs.getString(NOME),
                        rs.getString(COGNOME),
                        rs.getString(TELEFONO)
                        // Aggiungi la data di registrazione se serve, convertendo da SQL a LocalDateTime
                );
                return utente;
            } else {
                throw new WrongEmailOrPasswordException("Email o password errati.");
            }
        } catch (SQLException e) {
            throw new LoginAndRegistrationException("Errore del database durante il login", e);
        }
    }

    @Override
    public Utente recuperaUtente(String email) throws NoResultException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = UtenteQuery.retrieveUtente(conn, email)) {

            if (rs.next()) {
                return new Utente(
                        new Credentials(email),
                        rs.getString(NOME),
                        rs.getString(COGNOME),
                        rs.getString(TELEFONO)
                );
            } else {
                throw new NoResultException("Nessun utente trovato con questa email.");
            }
        } catch (SQLException e) {
            handleException(e);
            return null; // In alternativa possiamo lanciare un'eccezione
        }
    }

    // Metodo privato per gestire le eccezioni generiche (come il suo "handleException")
    private void handleException(Exception e) {
        // Al momento usiamo System.err, in futuro magari una classe Printer
        System.err.println(String.format("%s", e.getMessage()));
    }
}