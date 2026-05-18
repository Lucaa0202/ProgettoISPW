package dao.full.sql;

import dao.ViaggioDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Viaggio;
import query.ViaggioQuery;
import utilities.enums.StatoViaggio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViaggioDAOSQL implements ViaggioDAO {

    // Nomi delle colonne del tuo DB MySQL
    // NOTA: Se la colonna dell'ID su Workbench si chiama "idViaggio", cambia "id" in "idViaggio"!
    private static final String ID = "idViaggio";
    private static final String PARTENZA = "partenza";
    private static final String DESTINAZIONE = "destinazione";
    private static final String DATA_ORA = "data_ora";
    private static final String POSTI_DISPONIBILI = "posti_disponibili";
    private static final String PREZZO = "prezzo";
    private static final String STATO = "stato";
    private static final String GUIDATORE_EMAIL = "guidatore_email";
    // AGGIUNTO: il nome della colonna della targa su Workbench
    private static final String VEICOLO_TARGA = "veicolo_targa";

    @Override
    public void inserisciViaggio(Viaggio viaggio) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            ViaggioQuery.insertViaggio(conn, viaggio);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante la creazione del viaggio", e);
        }
    }

    @Override
    public List<Viaggio> cercaViaggi(String partenza, String destinazione) throws NoResultException {
        List<Viaggio> viaggiTrovati = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ViaggioQuery.searchViaggi(conn, partenza, destinazione)) {

            while (rs.next()) {
                viaggiTrovati.add(mappaResultSetAViaggio(rs));
            }

            if (viaggiTrovati.isEmpty()) {
                throw new NoResultException("Nessun viaggio disponibile trovato per questa tratta.");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return viaggiTrovati;
    }

    @Override
    public Viaggio recuperaViaggio(int idViaggio) throws NoResultException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = ViaggioQuery.retrieveViaggio(conn, idViaggio)) {

            if (rs.next()) {
                return mappaResultSetAViaggio(rs);
            } else {
                throw new NoResultException("Viaggio non trovato con questo ID.");
            }
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public void aggiornaPostiDisponibili(int idViaggio, int nuoviPosti) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            ViaggioQuery.updatePosti(conn, idViaggio, nuoviPosti);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'aggiornamento dei posti", e);
        }
    }

    // --- METODI PRIVATI DI UTILITY ---

    private Viaggio mappaResultSetAViaggio(ResultSet rs) throws SQLException {
        // Ora i parametri sono 9, esattamente come vuole il costruttore del tuo Model!
        return new Viaggio(
                rs.getInt(ID),
                rs.getString(PARTENZA),
                rs.getString(DESTINAZIONE),
                rs.getTimestamp(DATA_ORA).toLocalDateTime(),
                rs.getInt(POSTI_DISPONIBILI),
                rs.getDouble(PREZZO),
                StatoViaggio.convertIntToState(rs.getInt(STATO)),
                rs.getString(GUIDATORE_EMAIL),
                rs.getString(VEICOLO_TARGA) // <--- ECCO LA TARGA!
        );
    }

    private void handleException(Exception e) {
        System.err.println(String.format("Errore DB Viaggi: %s", e.getMessage()));
    }
}
