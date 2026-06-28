package dao.full.sql;

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

    // NOMI DELLE COLONNE ALLINEATI AL TUO WORKBENCH
    private static final String ID = "idInvito";
    private static final String ID_VIAGGIO = "viaggio_id";
    private static final String EMAIL_PASSEGGERO = "passeggero_email";
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

    // =========================================================================
    // NUOVO METODO: RESTITUISCE LA LISTA DI EMAIL DEI PASSEGGERI STORICI
    // =========================================================================
    public List<String> trovaStoricoPasseggeri(String emailGuidatore) throws NoResultException {
        List<String> passeggeri = new ArrayList<>();
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = InvitoQuery.retrieveStoricoPasseggeri(conn, emailGuidatore)) {

            while (rs.next()) {
                // Prende la prima colonna della query (che è passeggero_email)
                passeggeri.add(rs.getString(1));
            }
            if (passeggeri.isEmpty()) {
                throw new NoResultException("Nessun passeggero storico trovato.");
            }
        } catch (SQLException e) {
            handleException(e);
        }
        return passeggeri;
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