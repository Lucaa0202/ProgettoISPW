package query;

import exceptions.DbOperationException;
import model.Prenotazione;

import java.sql.*;

public class PrenotazioneQuery {

    // Costruttore privato per Utility Class (SonarCloud lo adora)
    private PrenotazioneQuery() {}

    public static void insertPrenotazione(Connection conn, Prenotazione prenotazione) throws SQLException, DbOperationException {
        String query = "INSERT INTO Prenotazione (email_passeggero, id_viaggio, stato, data_prenotazione) VALUES (?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, prenotazione.getEmailPasseggero());
            pstmt.setInt(2, prenotazione.getIdViaggio());
            pstmt.setInt(3, prenotazione.getStato().getId());
            pstmt.setTimestamp(4, Timestamp.valueOf(prenotazione.getDataPrenotazione()));

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: La prenotazione non è stata salvata.");
            }
        }
    }

    public static ResultSet retrievePrenotazioniByPasseggero(Connection conn, String emailPasseggero) throws SQLException {
        String query = "SELECT id, email_passeggero, id_viaggio, stato, data_prenotazione FROM Prenotazione WHERE email_passeggero = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailPasseggero);
        return pstmt.executeQuery();
    }

    public static ResultSet retrievePrenotazioniByViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT id, email_passeggero, id_viaggio, stato, data_prenotazione FROM Prenotazione WHERE id_viaggio = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void updateStatoPrenotazione(Connection conn, int idPrenotazione, int idStato) throws SQLException, DbOperationException {
        String query = "UPDATE Prenotazione SET stato = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idStato);
            pstmt.setInt(2, idPrenotazione);

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile aggiornare lo stato, prenotazione non trovata.");
            }
        }
    }

    public static void deletePrenotazione(Connection conn, int idPrenotazione) throws SQLException, DbOperationException {
        String query = "DELETE FROM Prenotazione WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idPrenotazione);

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile eliminare la prenotazione.");
            }
        }
    }
}
