package query;

import exceptions.DbOperationException;
import exceptions.MailAlreadyExistsException;
import model.Utente;
import model.Credentials;

import java.sql.*;

public class UtenteQuery {

    private UtenteQuery() {}

    // Allineato al suo registerCustomer
    public static void registerUtente(Connection conn, Utente utente) throws SQLException, MailAlreadyExistsException, DbOperationException {
        String query = "INSERT INTO Utente (email, password, nome, cognome, telefono) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, utente.getCredentials().getEmail());
            pstmt.setString(2, utente.getCredentials().getPassword());
            pstmt.setString(3, utente.getNome());
            pstmt.setString(4, utente.getCognome());
            pstmt.setString(5, utente.getTelefono());
            // Se avevi la data nel DB, aggiungi: pstmt.setDate(6, Date.valueOf(utente.getDataRegistrazione().toLocalDate()));

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new MailAlreadyExistsException("Email già esistente");
            }
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella registrazione", e);
        }
    }

    // Allineato al suo modifyCustomer (Utile per il futuro)
    public static void modifyUtente(Connection conn, Utente utente) throws DbOperationException {
        String query = "UPDATE Utente SET nome = ?, cognome = ?, telefono = ? WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, utente.getNome());
            pstmt.setString(2, utente.getCognome());
            pstmt.setString(3, utente.getTelefono());
            pstmt.setString(4, utente.getCredentials().getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DbOperationException("Errore nella modifica del profilo", e);
        }
    }

    // Allineato al suo retrieveCustomer
    public static ResultSet retrieveUtente(Connection conn, String email) throws SQLException {
        String query = "SELECT email, password, nome, cognome, telefono FROM Utente WHERE email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, email);
        return pstmt.executeQuery();
    }

    // Aggiungiamo i metodi per il Login (che lui aveva in CredentialsQuery)
    public static ResultSet logQuery(Connection conn, Credentials credentials) throws SQLException {
        String query = "SELECT nome, cognome, telefono FROM Utente WHERE email = ? AND password = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, credentials.getEmail());
        pstmt.setString(2, credentials.getPassword());
        return pstmt.executeQuery();
    }

    public static int checkEmail(Connection conn, String email) throws DbOperationException {
        String query = "SELECT 1 FROM Utente WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return 1;
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DbOperationException("Errore controllo email", e);
        }
    }
}