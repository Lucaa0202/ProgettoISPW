package dao.full.sql;

import dao.RecensioneDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Recensione;
import query.RecensioneQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAOSQL implements RecensioneDAO {

    private static final String ID = "id";
    private static final String EMAIL_RECENSORE = "email_recensore";
    private static final String EMAIL_RECENSITO = "email_recensito";
    private static final String ID_VIAGGIO = "id_viaggio";
    private static final String VOTO = "voto";
    private static final String COMMENTO = "commento";
    private static final String DATA_RECENSIONE = "data_recensione";

    @Override
    public void inserisciRecensione(Recensione recensione) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            RecensioneQuery.insertRecensione(conn, recensione);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'inserimento della recensione", e);
        }
    }

    @Override
    public List<Recensione> trovaRecensioniRicevute(String emailRecensito) throws NoResultException {
        List<Recensione> recensioni = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = RecensioneQuery.retrieveRecensioniByRecensito(conn, emailRecensito)) {

            while (rs.next()) {
                recensioni.add(mappaResultSetARecensione(rs));
            }

            if (recensioni.isEmpty()) {
                throw new NoResultException("Nessuna recensione trovata per questo utente.");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return recensioni;
    }

    @Override
    public List<Recensione> trovaRecensioniPerViaggio(int idViaggio) throws NoResultException {
        List<Recensione> recensioni = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = RecensioneQuery.retrieveRecensioniByViaggio(conn, idViaggio)) {

            while (rs.next()) {
                recensioni.add(mappaResultSetARecensione(rs));
            }

            if (recensioni.isEmpty()) {
                throw new NoResultException("Nessuna recensione trovata per questo viaggio.");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return recensioni;
    }
    @Override
    public double calcolaMediaVoti(String emailRecensito) throws NoResultException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = RecensioneQuery.retrieveMediaVoti(conn, emailRecensito)) {

            // Controlla se il DB ha restituito un valore valido
            if (rs.next() && rs.getObject("media") != null) {
                return rs.getDouble("media");
            } else {
                throw new NoResultException("Nessuna recensione trovata per questo utente. Impossibile calcolare la media.");
            }

        } catch (SQLException e) {
            handleException(e);
            return 0.0;
        }
    }


    // --- METODI PRIVATI DI UTILITY ---

    private Recensione mappaResultSetARecensione(ResultSet rs) throws SQLException {
        return new Recensione(
                rs.getInt(ID),
                rs.getString(EMAIL_RECENSORE),
                rs.getString(EMAIL_RECENSITO),
                rs.getInt(ID_VIAGGIO),
                rs.getInt(VOTO),
                rs.getString(COMMENTO),
                rs.getTimestamp(DATA_RECENSIONE).toLocalDateTime()
        );
    }

    private void handleException(Exception e) {
        System.err.println(String.format("Errore DB Recensioni: %s", e.getMessage()));
    }
}