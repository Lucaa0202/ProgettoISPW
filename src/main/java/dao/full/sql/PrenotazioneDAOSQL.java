package dao.full.sql;

import dao.PrenotazioneDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Prenotazione;
import query.PrenotazioneQuery;
import utilities.enums.StatoPrenotazione;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAOSQL implements PrenotazioneDAO {

    private static final String ID = "id";
    private static final String EMAIL_PASSEGGERO = "email_passeggero";
    private static final String ID_VIAGGIO = "id_viaggio";
    private static final String STATO = "stato";
    private static final String DATA_PRENOTAZIONE = "data_prenotazione";

    @Override
    public void inserisciPrenotazione(Prenotazione prenotazione) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            PrenotazioneQuery.insertPrenotazione(conn, prenotazione);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante la creazione della prenotazione", e);
        }
    }

    // RINOMINATO: da recuperaPrenotazioniPasseggero a trovaPrenotazioniPasseggero
    @Override
    public List<Prenotazione> trovaPrenotazioniPasseggero(String emailPasseggero) throws NoResultException {
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = PrenotazioneQuery.retrievePrenotazioniByPasseggero(conn, emailPasseggero)) {

            while (rs.next()) {
                prenotazioni.add(mappaResultSetAPrenotazione(rs));
            }

            if (prenotazioni.isEmpty()) {
                throw new NoResultException("Nessuna prenotazione trovata per questo passeggero.");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return prenotazioni;
    }

    // RINOMINATO: da recuperaPrenotazioniViaggio a trovaPrenotazioniViaggio
    @Override
    public List<Prenotazione> trovaPrenotazioniPerViaggio(int idViaggio) throws NoResultException {
        List<Prenotazione> prenotazioni = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = PrenotazioneQuery.retrievePrenotazioniByViaggio(conn, idViaggio)) {

            while (rs.next()) {
                prenotazioni.add(mappaResultSetAPrenotazione(rs));
            }

            if (prenotazioni.isEmpty()) {
                throw new NoResultException("Nessuna prenotazione trovata per questo viaggio.");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return prenotazioni;
    }

    @Override
    public void aggiornaStatoPrenotazione(Prenotazione prenotazione) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            // Estraiamo l'ID della prenotazione e l'ID del nuovo stato direttamente dall'oggetto!
            PrenotazioneQuery.updateStatoPrenotazione(conn, prenotazione.getIdPrenotazione(), prenotazione.getStato().getId());
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'aggiornamento dello stato", e);
        }
    }

    @Override
    public void eliminaPrenotazione(int idPrenotazione) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            PrenotazioneQuery.deletePrenotazione(conn, idPrenotazione);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'eliminazione", e);
        }
    }

    // --- METODI PRIVATI DI UTILITY ---

    private Prenotazione mappaResultSetAPrenotazione(ResultSet rs) throws SQLException {
        return new Prenotazione(
                rs.getInt(ID),
                rs.getString(EMAIL_PASSEGGERO),
                rs.getInt(ID_VIAGGIO),
                StatoPrenotazione.convertIntToState(rs.getInt(STATO)),
                rs.getTimestamp(DATA_PRENOTAZIONE).toLocalDateTime()
        );
    }

    private void handleException(Exception e) {
        System.err.println(String.format("Errore DB Prenotazioni: %s", e.getMessage()));
    }
}