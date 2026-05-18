package query;

import exceptions.DbOperationException;
import model.Veicolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VeicoloQuery {

    private VeicoloQuery() {}

    public static void insertVeicolo(Connection conn, Veicolo veicolo) throws SQLException, DbOperationException {
        // Corretto in proprietario_email
        String query = "INSERT INTO Veicolo (targa, marca, modello, posti_totali, proprietario_email) VALUES (?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, veicolo.getTarga());
            pstmt.setString(2, veicolo.getMarca());
            pstmt.setString(3, veicolo.getModello());
            pstmt.setInt(4, veicolo.getPostiTotali());
            pstmt.setString(5, veicolo.getEmailProprietario());

            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore imprevisto: Il veicolo non è stato salvato.");
            }
        }
    }

    public static ResultSet retrieveVeicoloByTarga(Connection conn, String targa) throws SQLException {
        // Corretto in proprietario_email
        String query = "SELECT targa, marca, modello, posti_totali, proprietario_email FROM Veicolo WHERE targa = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, targa);
        return pstmt.executeQuery();
    }

    public static ResultSet retrieveVeicoliByProprietario(Connection conn, String emailProprietario) throws SQLException {
        // Corretto in proprietario_email
        String query = "SELECT targa, marca, modello, posti_totali, proprietario_email FROM Veicolo WHERE proprietario_email = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, emailProprietario);
        return pstmt.executeQuery();
    }

    public static void deleteVeicolo(Connection conn, String targa) throws SQLException, DbOperationException {
        String query = "DELETE FROM Veicolo WHERE targa = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, targa);
            int rs = pstmt.executeUpdate();
            if (rs == 0) {
                throw new DbOperationException("Errore: impossibile eliminare il veicolo, targa non trovata.");
            }
        }
    }
}