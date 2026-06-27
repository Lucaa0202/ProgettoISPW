package query;

import exceptions.DbOperationException;
import model.Recensione;

import java.sql.*;

public class RecensioneQuery {

    private RecensioneQuery() {}

    public static void insertRecensione(Connection conn, Recensione recensione) throws SQLException, DbOperationException {
        // Nomi corretti: recensore_email, recensito_email, viaggio_id
        String query = "INSERT INTO Recensione (recensore_email, recensito_email, viaggio_id, voto, commento, data_recensione) VALUES (?,?,?,?,?,?)";
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

    public static ResultSet retrieveRecensioniByRecensito(Connection conn, String emailRecensito) throws SQLException {
        String query = "SELECT idRecensione, recensore_email, recensito_email, viaggio_id, voto, commento, data_recensione FROM Recensione WHERE recensito_email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailRecensito);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveRecensioniByViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT idRecensione, recensore_email, recensito_email, viaggio_id, voto, commento, data_recensione FROM Recensione WHERE viaggio_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void deleteRecensione(Connection conn, int idRecensione) throws SQLException, DbOperationException {
        String query = "DELETE FROM Recensione WHERE idRecensione = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idRecensione);
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile eliminare la recensione.");
            }
        }
    }

    public static ResultSet retrieveMediaVoti(Connection conn, String emailRecensito) throws SQLException {
        String query = "SELECT AVG(voto) AS media FROM Recensione WHERE recensito_email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailRecensito);
        return pstmt.executeQuery();
    }

    // NUOVO: Query anti-spam corretta
    public static ResultSet checkRecensioneEsistente(Connection conn, String emailRecensore, int idViaggio) throws SQLException {
        String query = "SELECT COUNT(*) AS conteggio FROM Recensione WHERE recensore_email = ? AND viaggio_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailRecensore);
        pstmt.setInt(2, idViaggio);
        return pstmt.executeQuery();
    }
}