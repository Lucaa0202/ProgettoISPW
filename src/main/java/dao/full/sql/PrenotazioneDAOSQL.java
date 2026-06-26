package dao.full.sql;

import dao.PrenotazioneDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Prenotazione;
import query.PrenotazioneQuery;
import utilities.enums.StatoPrenotazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAOSQL implements PrenotazioneDAO {

    // COSTANTI AGGIORNATE PER COMBACIARE CON IL DB
    private static final String ID = "idPrenotazione";
    private static final String EMAIL_PASSEGGERO = "passeggero_email";
    private static final String ID_VIAGGIO = "viaggio_id";
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

    // --- NUOVO METODO: GESTIONE OVERBOOKING ---
    @Override
    public void rifiutaAltreRichieste(int idViaggio) throws DbOperationException {
        String query = "UPDATE prenotazione SET stato = ? WHERE viaggio_id = ? AND stato = ?";

        try (Connection conn = ConnectionSQL.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Rifiutiamo (stato 3) le richieste che sono ancora IN_ATTESA (stato 1) per questo specifico viaggio
            ps.setInt(1, StatoPrenotazione.RIFIUTATA.getId());
            ps.setInt(2, idViaggio);
            ps.setInt(3, StatoPrenotazione.IN_ATTESA.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante il rifiuto automatico delle richieste (Overbooking)", e);
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