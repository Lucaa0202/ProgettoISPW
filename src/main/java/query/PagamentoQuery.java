package query;

import exceptions.DbOperationException;
import model.Pagamento;

import java.sql.*;

public class PagamentoQuery {

    private PagamentoQuery() {}

    public static void insertPagamento(Connection conn, Pagamento pagamento) throws SQLException, DbOperationException {
        // Ipotizzo che la colonna dello stato salvi un intero (es. 0 = In Attesa, 1 = Completato, 2 = Fallito)
        String query = "INSERT INTO Pagamento (id_prenotazione, importo, metodo, stato, data_pagamento) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, pagamento.getIdPrenotazione());
            pstmt.setDouble(2, pagamento.getImporto());
            pstmt.setString(3, pagamento.getMetodo());
            pstmt.setInt(4, pagamento.getStato().getId()); // Assumo che la tua Enum abbia un metodo getId()
            pstmt.setTimestamp(5, Timestamp.valueOf(pagamento.getDataPagamento()));

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: Il pagamento non è stato salvato.");
            }
        }
    }

    // Trova un pagamento tramite il suo ID
    public static ResultSet retrievePagamentoById(Connection conn, int idPagamento) throws SQLException {
        String query = "SELECT id, id_prenotazione, importo, metodo, stato, data_pagamento FROM Pagamento WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idPagamento);
        return pstmt.executeQuery();
    }

    // Trova i pagamenti associati a una determinata prenotazione
    public static ResultSet retrievePagamentiByPrenotazione(Connection conn, int idPrenotazione) throws SQLException {
        String query = "SELECT id, id_prenotazione, importo, metodo, stato, data_pagamento FROM Pagamento WHERE id_prenotazione = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, idPrenotazione);
        return pstmt.executeQuery();
    }

    // Aggiorna lo stato di un pagamento (es. da "In Attesa" a "Completato")
    public static void updateStatoPagamento(Connection conn, int idPagamento, int nuovoStatoId) throws SQLException, DbOperationException {
        String query = "UPDATE Pagamento SET stato = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, nuovoStatoId);
            pstmt.setInt(2, idPagamento);
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile aggiornare lo stato, pagamento non trovato.");
            }
        }
    }
}
