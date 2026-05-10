package controller;

import beans.PagamentoBean;
import dao.PagamentoDAO;
import exceptions.DbOperationException;
import model.Pagamento;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;

public class PagamentoController {

    private final BeanAndModelMapperFactory factory;
    private final PagamentoDAO pagamentoDAO;

    public PagamentoController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.pagamentoDAO = FactoryDAO.getPagamentoDAO();
    }

    // Il passeggero effettua il pagamento del contributo
    public void effettuaPagamento(PagamentoBean pagamentoBean) throws DbOperationException {
        Pagamento pagamento = factory.fromBeanToModel(pagamentoBean, PagamentoBean.class);

        try {
            // Salva il record del pagamento nel Database
            pagamentoDAO.inserisciPagamento(pagamento);

            // Qui in futuro potresti aggiungere logiche extra,
            // come aggiornare lo stato della Prenotazione a "PAGATA"

        } catch (Exception e) {
            throw new DbOperationException("Errore durante l'elaborazione del pagamento: " + e.getMessage());
        }
    }
}