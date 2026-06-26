package query;

import exceptions.DbOperationException;
import model.Prenotazione;

import java.sql.*;

public class PrenotazioneQuery {

    // Costruttore privato per Utility Class (SonarCloud lo adora)
    private PrenotazioneQuery() {}

    public static void insertPrenotazione(Connection conn, model.Prenotazione prenotazione) throws SQLException, exceptions.DbOperationException {
        // Facciamo solo l'inserimento! Niente UPDATE dei posti qui.
        String insertQuery = "INSERT INTO prenotazione (passeggero_email, viaggio_id, stato, data_prenotazione) VALUES (?,?,?,?)";

        try (PreparedStatement psInsert = conn.prepareStatement(insertQuery)) {
            psInsert.setString(1, prenotazione.getEmailPasseggero());
            psInsert.setInt(2, prenotazione.getIdViaggio());

            // Forza l'inserimento allo stato IN_ATTESA (che nel tuo Enum ha ID 1)
            psInsert.setInt(3, utilities.enums.StatoPrenotazione.IN_ATTESA.getId());
            psInsert.setTimestamp(4, Timestamp.valueOf(prenotazione.getDataPrenotazione()));

            int rs = psInsert.executeUpdate();
            if (rs == 0) {
                throw new exceptions.DbOperationException("Errore imprevisto: La prenotazione non è stata salvata.");
            }
        }
    }

    // --- METODI AGGIORNATI CON I NOMI COLONNE CORRETTI ---

    public static ResultSet retrievePrenotazioniByPasseggero(Connection conn, String emailPasseggero) throws SQLException {
        String query = "SELECT idPrenotazione, passeggero_email, viaggio_id, stato, data_prenotazione FROM Prenotazione WHERE passeggero_email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailPasseggero);
        return pstmt.executeQuery();
    }

    public static ResultSet retrievePrenotazioniByViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT idPrenotazione, passeggero_email, viaggio_id, stato, data_prenotazione FROM Prenotazione WHERE viaggio_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void updateStatoPrenotazione(Connection conn, int idPrenotazione, int idStato) throws SQLException, DbOperationException {
        String query = "UPDATE Prenotazione SET stato = ? WHERE idPrenotazione = ?";
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
        String query = "DELETE FROM Prenotazione WHERE idPrenotazione = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idPrenotazione);

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile eliminare la prenotazione.");
            }
        }
    }
}
