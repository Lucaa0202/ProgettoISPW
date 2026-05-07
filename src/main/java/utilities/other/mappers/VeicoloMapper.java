package utilities.other.mappers;

import beans.VeicoloBean;
import model.Veicolo;

public class VeicoloMapper implements BeanAndModelMapper<VeicoloBean, Veicolo> {

    @Override
    public Veicolo fromBeanToModel(VeicoloBean bean) {
        return new Veicolo(
                bean.getTarga(),
                bean.getMarca(),
                bean.getModello(),
                bean.getPostiTotali(),
                bean.getEmailProprietario()
        );
    }

    @Override
    public VeicoloBean fromModelToBean(Veicolo model) {
        return new VeicoloBean(
                model.getTarga(),
                model.getMarca(),
                model.getModello(),
                model.getPostiTotali(),
                model.getEmailProprietario()
        );
    }
}