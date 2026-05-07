package utilities.other.mappers;

import beans.CredenzialiBean;
import model.Credentials; // (Assumendo che nel tuo Model tu abbia la classe Credenziali)

public class CredenzialiMapper implements BeanAndModelMapper<CredenzialiBean, Credentials> {

    @Override
    public Credentials fromBeanToModel(CredenzialiBean bean) {
        if(bean.getPassword() != null) {
            return new Credentials(bean.getEmail(), bean.getPassword());
        } else {
            return new Credentials(bean.getEmail());
        }
    }

    @Override
    public CredenzialiBean fromModelToBean(Credentials model) {
        if(model.getPassword() != null) {
            return new CredenzialiBean(model.getEmail(), model.getPassword());
        } else {
            return new CredenzialiBean(model.getEmail());
        }
    }
}