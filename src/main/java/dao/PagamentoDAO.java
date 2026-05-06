package dao;

import model.Pagamento;
import utilities.enums.StatoPagamento;
import exceptions.NoResultException;
import exceptions.DbOperationException;
import java.util.List;

public interface PagamentoDAO {
    // Registra un nuovo tentativo di pagamento
    void inserisciPagamento(Pagamento pagamento) throws DbOperationException;

    // Aggiorna lo stato (es. da IN_SOSPESO a COMPLETATO)
    void aggiornaStatoPagamento(int idPagamento, StatoPagamento nuovoStato) throws DbOperationException;

    // Recupera lo storico pagamenti di una specifica prenotazione
    List<Pagamento> trovaPagamentiPerPrenotazione(int idPrenotazione) throws NoResultException;

    // Metodo default per prevenire frodi (i log dei pagamenti non si cancellano)
    default void eliminaPagamento(Pagamento pagamento) {
        throw new UnsupportedOperationException("L'eliminazione fisica di un pagamento non è consentita per motivi di sicurezza fiscale.");
    }
}