package dao.full.sql;

// Guarda gli import: ora sono esattamente quelli del tuo progetto!
import dao.InvitoDAO;
import model.Invito;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import query.InvitoQuery;
import utilities.enums.StatoInvito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvitoDAOSQL implements InvitoDAO {

    private static final String ID = "id";
    private static final String ID_VIAGGIO = "id_viaggio";
    private static final String EMAIL_PASSEGGERO = "email_passeggero";
    private static final String STATO = "stato";

    @Override
    public void creaInvito(Invito invito) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            InvitoQuery.insertInvito(conn, invito);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante la creazione dell'invito", e);
        }
    }

    @Override
    public List<Invito> trovaInvitiRicevuti(String emailPasseggero) throws NoResultException {
        List<Invito> inviti = new ArrayList<>();
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = InvitoQuery.retrieveInvitiByPasseggero(conn, emailPasseggero)) {

            while (rs.next()) {
                inviti.add(mappaResultSetAInvito(rs));
            }
            if (inviti.isEmpty()) {
                throw new NoResultException("Nessun invito trovato per questo utente.");
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return inviti;
    }

    @Override
    public void rispondiInvito(int idInvito, StatoInvito risposta) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            InvitoQuery.updateStatoInvito(conn, idInvito, risposta.getId());
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante la risposta all'invito", e);
        }
    }

    // --- METODI PRIVATI DI UTILITY ---

    private Invito mappaResultSetAInvito(ResultSet rs) throws SQLException {
        return new Invito(
                rs.getInt(ID),
                rs.getInt(ID_VIAGGIO),
                rs.getString(EMAIL_PASSEGGERO),
                StatoInvito.convertIntToState(rs.getInt(STATO))
        );
    }

    private void handleException(Exception e) {
        System.err.println(String.format("Errore DB Inviti: %s", e.getMessage()));
    }
}