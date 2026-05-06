package dao.full.sql;

import dao.VeicoloDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Veicolo;
import query.VeicoloQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VeicoloDAOSQL implements VeicoloDAO {

    private static final String TARGA = "targa";
    private static final String MARCA = "marca";
    private static final String MODELLO = "modello";
    private static final String ANNO_IMMATRICOLAZIONE = "anno_immatricolazione";
    private static final String EMAIL_PROPRIETARIO = "email_proprietario";

    @Override
    public void inserisciVeicolo(Veicolo veicolo) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            VeicoloQuery.insertVeicolo(conn, veicolo);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'inserimento del veicolo", e);
        }
    }

    @Override
    public Veicolo trovaVeicoloPerTarga(String targa) throws NoResultException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = VeicoloQuery.retrieveVeicoloByTarga(conn, targa)) {

            if (rs.next()) {
                return mappaResultSetAVeicolo(rs);
            } else {
                throw new NoResultException("Nessun veicolo trovato con questa targa.");
            }
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public List<Veicolo> trovaVeicoliPerProprietario(String emailProprietario) throws NoResultException {
        List<Veicolo> veicoli = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = VeicoloQuery.retrieveVeicoliByProprietario(conn, emailProprietario)) {

            while (rs.next()) {
                veicoli.add(mappaResultSetAVeicolo(rs));
            }

            if (veicoli.isEmpty()) {
                throw new NoResultException("Nessun veicolo trovato per questo proprietario.");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return veicoli;
    }

    @Override
    public void rimuoviVeicolo(String targa) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            VeicoloQuery.deleteVeicolo(conn, targa);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'eliminazione del veicolo", e);
        }
    }

    // --- METODI PRIVATI DI UTILITY ---

    private Veicolo mappaResultSetAVeicolo(ResultSet rs) throws SQLException {
        return new Veicolo(
                rs.getString(TARGA),
                rs.getString(MARCA),
                rs.getString(MODELLO),
                rs.getInt(ANNO_IMMATRICOLAZIONE),
                rs.getString(EMAIL_PROPRIETARIO)
        );
    }

    private void handleException(Exception e) {
        System.err.println(String.format("Errore DB Veicolo: %s", e.getMessage()));
    }
}