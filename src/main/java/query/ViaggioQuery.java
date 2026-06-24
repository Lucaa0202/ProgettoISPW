package query;

import exceptions.DbOperationException;
import model.Viaggio;

import java.sql.*;

public class ViaggioQuery {

    // Costruttore privato per SonarCloud (Utility class)
    private ViaggioQuery() {}

    public static void insertViaggio(Connection conn, Viaggio viaggio) throws SQLException, DbOperationException {
        String query = "INSERT INTO Viaggio (partenza, destinazione, data_ora, posti_disponibili, prezzo, stato, guidatore_email) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, viaggio.getPartenza());
            pstmt.setString(2, viaggio.getDestinazione());
            // Convertiamo LocalDateTime in Timestamp per MySQL
            pstmt.setTimestamp(3, Timestamp.valueOf(viaggio.getDataOra()));
            pstmt.setInt(4, viaggio.getPostiDisponibili());
            pstmt.setDouble(5, viaggio.getPrezzo());
            pstmt.setInt(6, viaggio.getStato().getId());
            pstmt.setString(7, viaggio.getEmailGuidatore());

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: Il viaggio non è stato inserito nel database.");
            }
        }
    }

    public static ResultSet searchViaggi(Connection conn, String partenza, String destinazione) throws SQLException {
        // Cerchiamo i viaggi per tratta che sono "ATTIVI" (stato = 1) e hanno posti disponibili
        String query = "SELECT id, partenza, destinazione, data_ora, posti_disponibili, prezzo, stato, guidatore_email FROM Viaggio WHERE partenza = ? AND destinazione = ? AND stato = 1 AND posti_disponibili > 0";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, partenza);
        pstmt.setString(2, destinazione);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT id, partenza, destinazione, data_ora, posti_disponibili, prezzo, stato, guidatore_email FROM Viaggio WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void updatePosti(Connection conn, int idViaggio, int nuoviPosti) throws SQLException, DbOperationException {
        String query = "UPDATE Viaggio SET posti_disponibili = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, nuoviPosti);
            pstmt.setInt(2, idViaggio);

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile aggiornare i posti, viaggio non trovato.");
            }
        }
    }
}