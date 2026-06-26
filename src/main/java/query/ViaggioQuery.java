package query;

import exceptions.DbOperationException;
import model.Viaggio;

import java.sql.*;

public class ViaggioQuery {

    // Costruttore privato per SonarCloud (Utility class)
    private ViaggioQuery() {}

    public static void insertViaggio(Connection conn, Viaggio viaggio) throws SQLException, DbOperationException {
        // Nomi colonne allineati perfettamente al tuo DB
        String query  = "INSERT INTO Viaggio (guidatore_email, partenza, destinazione, data_ora, posti_disponibili, prezzo, veicolo_targa, stato) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            // ORDINE CORRETTO DEI PARAMETRI (Speculare ai punti interrogativi qui sopra)
            pstmt.setString(1, viaggio.getEmail()); // Se hai chiamato il getter in modo diverso (es. getEmailGuidatore), cambialo qui
            pstmt.setString(2, viaggio.getPartenza());
            pstmt.setString(3, viaggio.getDestinazione());
            pstmt.setTimestamp(4, Timestamp.valueOf(viaggio.getDataOra()));
            pstmt.setInt(5, viaggio.getPostiDisponibili());
            pstmt.setDouble(6, viaggio.getPrezzo());
            pstmt.setString(7, viaggio.getTarga()); // Se hai chiamato il getter in modo diverso, cambialo qui
            pstmt.setInt(8, 1); // 1 = ATTIVO (visto che lo usi in searchViaggi)

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: Il viaggio non è stato inserito nel database.");
            }
        }
    }

    public static ResultSet searchViaggi(Connection conn, String partenza, String destinazione, String emailPasseggero) throws SQLException {
        // Aggiungiamo il controllo NOT IN sulle prenotazioni dell'utente
        String query = "SELECT idViaggio, partenza, destinazione, data_ora, posti_disponibili, prezzo, stato, guidatore_email, veicolo_targa " +
                "FROM Viaggio " +
                "WHERE partenza = ? AND destinazione = ? AND stato = 1 AND posti_disponibili > 0 " +
                "AND guidatore_email != ? " +
                "AND idViaggio NOT IN (SELECT viaggio_id FROM Prenotazione WHERE passeggero_email = ?)";

        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, partenza);
        pstmt.setString(2, destinazione);
        pstmt.setString(3, emailPasseggero); // Esclude i viaggi dove sono il guidatore
        pstmt.setString(4, emailPasseggero); // Esclude i viaggi dove sono già prenotato
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveViaggio(Connection conn, int idViaggio) throws SQLException {
        String query = "SELECT idViaggio, partenza, destinazione, data_ora, posti_disponibili, prezzo, stato, guidatore_email, veicolo_targa FROM Viaggio WHERE idViaggio = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idViaggio);
        return pstmt.executeQuery();
    }

    public static void updatePosti(Connection conn, int idViaggio, int nuoviPosti) throws SQLException, DbOperationException {
        String query = "UPDATE Viaggio SET posti_disponibili = ? WHERE idViaggio = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, nuoviPosti);
            pstmt.setInt(2, idViaggio);

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile aggiornare i posti, viaggio non trovato.");
            }
        }
    }
    public static ResultSet searchViaggiPerGuidatore(Connection conn, String emailGuidatore) throws SQLException {
        String query = "SELECT idViaggio, partenza, destinazione, data_ora, posti_disponibili, prezzo, stato, guidatore_email, veicolo_targa FROM Viaggio WHERE guidatore_email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailGuidatore);
        return pstmt.executeQuery();
        }
    }
