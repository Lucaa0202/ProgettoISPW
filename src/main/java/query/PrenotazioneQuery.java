package query;

import exceptions.DbOperationException;
import model.Prenotazione;

import java.sql.*;

public class PrenotazioneQuery {

    // Costruttore privato per Utility Class (SonarCloud lo adora)
    private PrenotazioneQuery() {}

    public static void insertPrenotazione(Connection conn, Prenotazione prenotazione) throws SQLException, DbOperationException {
        // QUERY 1: Nomi delle colonne CORRETTI (passeggero_email e viaggio_id)
        String insertQuery = "INSERT INTO prenotazione (passeggero_email, viaggio_id, stato, data_prenotazione) VALUES (?,?,?,?)";

        // QUERY 2: Scala il posto (NOTA: assicurati che l'ID nella tabella Viaggio si chiami "idViaggio". Se si chiama solo "id", cambia "WHERE idViaggio = ?" in "WHERE id = ?")
        String updatePostiQuery = "UPDATE Viaggio SET posti_disponibili = posti_disponibili - 1 WHERE idViaggio = ?";

        try (PreparedStatement psInsert = conn.prepareStatement(insertQuery);
             PreparedStatement psUpdate = conn.prepareStatement(updatePostiQuery)) {

            // Esegue l'inserimento
            psInsert.setString(1, prenotazione.getEmailPasseggero());
            psInsert.setInt(2, prenotazione.getIdViaggio());
            psInsert.setInt(3, prenotazione.getStato().getId());
            psInsert.setTimestamp(4, Timestamp.valueOf(prenotazione.getDataPrenotazione()));

            int rs = psInsert.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: La prenotazione non è stata salvata.");
            }

            // Esegue lo scalo del posto
            psUpdate.setInt(1, prenotazione.getIdViaggio());
            psUpdate.executeUpdate();
        }
    }

    // --- DA QUI IN GIÙ TUTTI I TUOI METODI VECCHI INTATTI ---

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
