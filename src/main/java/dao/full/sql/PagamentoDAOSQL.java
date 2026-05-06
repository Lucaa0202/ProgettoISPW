package dao.full.sql;

import dao.PagamentoDAO;
import dao.full.sql.ConnectionSQL;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Pagamento;
import query.PagamentoQuery;
import utilities.enums.StatoPagamento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PagamentoDAOSQL implements PagamentoDAO {

    private static final String ID = "id";
    private static final String ID_PRENOTAZIONE = "id_prenotazione";
    private static final String IMPORTO = "importo";
    private static final String METODO = "metodo";
    private static final String STATO = "stato";
    private static final String DATA_PAGAMENTO = "data_pagamento";

    @Override
    public void inserisciPagamento(Pagamento pagamento) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            PagamentoQuery.insertPagamento(conn, pagamento);
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'inserimento del pagamento", e);
        }
    }

    @Override
    public Pagamento trovaPagamentoPerId(int idPagamento) throws NoResultException {
        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = PagamentoQuery.retrievePagamentoById(conn, idPagamento)) {

            if (rs.next()) {
                return mappaResultSetAPagamento(rs);
            } else {
                throw new NoResultException("Nessun pagamento trovato con questo ID.");
            }
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }

    @Override
    public List<Pagamento> trovaPagamentiPerPrenotazione(int idPrenotazione) throws NoResultException {
        List<Pagamento> pagamenti = new ArrayList<>();

        try (Connection conn = ConnectionSQL.getConnection();
             ResultSet rs = PagamentoQuery.retrievePagamentiByPrenotazione(conn, idPrenotazione)) {

            while (rs.next()) {
                pagamenti.add(mappaResultSetAPagamento(rs));
            }

            if (pagamenti.isEmpty()) {
                throw new NoResultException("Nessun pagamento trovato per questa prenotazione.");
            }

        } catch (SQLException e) {
            handleException(e);
        }

        return pagamenti;
    }

    @Override
    public void aggiornaStatoPagamento(int idPagamento, StatoPagamento nuovoStato) throws DbOperationException {
        try (Connection conn = ConnectionSQL.getConnection()) {
            PagamentoQuery.updateStatoPagamento(conn, idPagamento, nuovoStato.getId());
        } catch (SQLException e) {
            throw new DbOperationException("Errore di connessione durante l'aggiornamento del pagamento", e);
        }
    }

    // --- METODI PRIVATI DI UTILITY ---

    private Pagamento mappaResultSetAPagamento(ResultSet rs) throws SQLException {
        return new Pagamento(
                rs.getInt(ID),
                rs.getInt(ID_PRENOTAZIONE),
                rs.getDouble(IMPORTO),
                rs.getString(METODO),
                // Assumo che la tua Enum abbia un metodo statico per convertire l'intero
                StatoPagamento.convertIntToState(rs.getInt(STATO)),
                rs.getTimestamp(DATA_PAGAMENTO).toLocalDateTime()
        );
    }

    private void handleException(Exception e) {
        System.err.println(String.format("Errore DB Pagamenti: %s", e.getMessage()));
    }
}