package query;

import exceptions.DbOperationException;
import model.Recensione;

import java.sql.*;

public class RecensioneQuery {

    private RecensioneQuery() {}

    public static void insertRecensione(Connection conn, Recensione recensione) throws SQLException, DbOperationException {
        String query = "INSERT INTO Recensione (email_recensore, email_recensito, id_viaggio, voto, commento, data_recensione) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, recensione.getEmailRecensore());
            pstmt.setString(2, recensione.getEmailRecensito());
            pstmt.setInt(3, recensione.getIdViaggio());
            pstmt.setInt(4, recensione.getVoto());
            pstmt.setString(5, recensione.getCommento());
            pstmt.setTimestamp(6, Timestamp.valueOf(recensione.getDataRecensione()));

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: La recensione non è stata salvata.");
            }
        }
    }

    // Molto utile: Trova tutte le recensioni ricevute da un utente
    public static ResultSet retrieveRecensioniByRecensito(Connection conn, String emailRecensito) throws SQLException {
        String query = "SELECT id, email_recensore, email_recensito, id_viaggio, voto, commento, data_recensione FROM Recensione WHERE email_recensito = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailRecensito);
        return pstmt.executeQuery();
    }

    // Utile: Trova le recensioni lasciate per un viaggio specifico
    public static ResultSet retrieveRecensioniByViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT id, email_recensore, email_recensito, id_viaggio, voto, commento, data_recensione FROM Recensione WHERE id_viaggio = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void deleteRecensione(Connection conn, int idRecensione) throws SQLException, DbOperationException {
        String query = "DELETE FROM Recensione WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idRecensione);
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile eliminare la recensione.");
            }
        }
    }
    public static ResultSet retrieveMediaVoti(Connection conn, String emailRecensito) throws SQLException {
        String query = "SELECT AVG(voto) AS media FROM Recensione WHERE email_recensito = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailRecensito);
        return pstmt.executeQuery();
    }
}