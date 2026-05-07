package utilities.other.mappers;

import beans.PagamentoBean;
import model.Pagamento;

public class PagamentoMapper implements BeanAndModelMapper<PagamentoBean, Pagamento> {

    @Override
    public Pagamento fromBeanToModel(PagamentoBean bean) {
        // Usiamo il costruttore COMPLETO del tuo Model:
        // (int idPagamento, int idPrenotazione, double importo, String metodo, StatoPagamento stato, LocalDateTime dataPagamento)
        return new Pagamento(
                bean.getIdPagamento(),
                bean.getIdPrenotazione(),
                bean.getImporto(),
                bean.getMetodo(),
                bean.getStato(),
                bean.getDataPagamento()
        );
    }

    @Override
    public PagamentoBean fromModelToBean(Pagamento model) {
        // Dal Model ricreiamo il Bean per la grafica
        return new PagamentoBean(
                model.getIdPagamento(),
                model.getIdPrenotazione(),
                model.getImporto(),
                model.getMetodo(),
                model.getDataPagamento(),
                model.getStato()
        );
    }
}