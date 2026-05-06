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
        String query = "INSERT INTO Invito (id_viaggio, email_passeggero, stato) VALUES (?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, invito.getIdViaggio());
            pstmt.setString(2, invito.getEmailPasseggero());
            pstmt.setInt(3, invito.getStato().getId()); // Come per i pagamenti, salviamo l'ID dell'Enum

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: L'invito non è stato salvato.");
            }
        }
    }

    public static ResultSet retrieveInvitoById(Connection conn, int idInvito) throws SQLException {
        String query = "SELECT id, id_viaggio, email_passeggero, stato FROM Invito WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idInvito);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveInvitiByPasseggero(Connection conn, String emailPasseggero) throws SQLException {
        String query = "SELECT id, id_viaggio, email_passeggero, stato FROM Invito WHERE email_passeggero = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailPasseggero);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveInvitiByViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT id, id_viaggio, email_passeggero, stato FROM Invito WHERE id_viaggio = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void updateStatoInvito(Connection conn, int idInvito, int nuovoStatoId) throws SQLException, DbOperationException {
        String query = "UPDATE Invito SET stato = ? WHERE id = ?";
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
        String query = "DELETE FROM Invito WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idInvito);
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile eliminare l'invito.");
            }
        }
    }
}