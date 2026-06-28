package query;

import exceptions.DbOperationException;
import model.Invito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvitoQuery {

    private InvitoQuery() {}

    public static void insertInvito(Connection conn, Invito invito) throws SQLException, DbOperationException {
        // Nomi colonne aggiornati: viaggio_id, passeggero_email, stato
        String query = "INSERT INTO Invito (viaggio_id, passeggero_email, stato) VALUES (?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, invito.getIdViaggio());
            pstmt.setString(2, invito.getEmailPasseggero());
            pstmt.setInt(3, invito.getStato().getId());

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: L'invito non è stato salvato.");
            }
        }
    }

    public static ResultSet retrieveInvitoById(Connection conn, int idInvito) throws SQLException {
        String query = "SELECT idInvito, viaggio_id, passeggero_email, stato FROM Invito WHERE idInvito = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idInvito);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveInvitiByPasseggero(Connection conn, String emailPasseggero) throws SQLException {
        String query = "SELECT idInvito, viaggio_id, passeggero_email, stato FROM Invito WHERE passeggero_email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailPasseggero);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveInvitiByViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT idInvito, viaggio_id, passeggero_email, stato FROM Invito WHERE viaggio_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void updateStatoInvito(Connection conn, int idInvito, int nuovoStatoId) throws SQLException, DbOperationException {
        String query = "UPDATE Invito SET stato = ? WHERE idInvito = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, nuovoStatoId);
            pstmt.setInt(2, idInvito);
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile aggiornare lo stato, invito non trovato.");
            }
        }
    }

    public static void deleteInvito(Connection conn, int idInvito) throws SQLException, DbOperationException {
        String query = "DELETE FROM Invito WHERE idInvito = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idInvito);
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile eliminare l'invito.");
            }
        }
    }

    // =========================================================================
    // NUOVA QUERY: ESTRAE LO STORICO DEI PASSEGGERI PER IL MENU A TENDINA
    // =========================================================================
    public static ResultSet retrieveStoricoPasseggeri(Connection conn, String emailGuidatore) throws SQLException {
        // N.B: Modifica "passeggero_email", "emailGuidatore", "idViaggio" se nelle tue tabelle Prenotazione/Viaggio si chiamano diversamente
        String query = "SELECT DISTINCT P.passeggero_email " +
                "FROM Prenotazione P " +
                "JOIN Viaggio V ON P.viaggio_id = V.idViaggio " +
                "WHERE V.guidatore_email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailGuidatore);
        return pstmt.executeQuery();
    }
}