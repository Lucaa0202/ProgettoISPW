package utilities.other.mappers;

import beans.InvitoBean;
import model.Invito;

public class InvitoMapper implements BeanAndModelMapper<InvitoBean, Invito> {
    @Override
    public Invito fromBeanToModel(InvitoBean bean) {
        Invito i = new Invito(
                bean.getIdViaggio(),
                bean.getEmailPasseggero(),
                bean.getStato()
        );
        i.setIdInvito(bean.getIdInvito());
        return i;
    }

    @Override
    public InvitoBean fromModelToBean(Invito model) {
        return new InvitoBean(
                model.getIdInvito(),
                model.getIdViaggio(),
                model.getEmailPasseggero(),
                model.getStato()
        );
    }
}