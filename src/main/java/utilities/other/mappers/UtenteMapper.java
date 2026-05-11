package utilities.other.mappers;

import beans.CredenzialiBean;
import beans.UtenteBean;
import model.Credentials;
import model.Utente;
import patterns.factory.BeanAndModelMapperFactory;

public class UtenteMapper implements BeanAndModelMapper<UtenteBean, Utente> {

    @Override
    public Utente fromBeanToModel(UtenteBean bean) {
        BeanAndModelMapperFactory factory = BeanAndModelMapperFactory.getInstance();

        // 1. Convertiamo le credenziali
        Credentials credenzialiModel = factory.fromBeanToModel(bean.getCredenziali(), CredenzialiBean.class);

        // 2. TRADUZIONE VERSO IL MODEL (per il Database)
        // Usiamo bean.getTelefono() al posto della vecchia "StringaMancante"
        return new Utente(credenzialiModel, bean.getNome(), bean.getCognome(), bean.getTelefono());
    }

    @Override
    public UtenteBean fromModelToBean(Utente model) {
        BeanAndModelMapperFactory factory = BeanAndModelMapperFactory.getInstance();

        // Recuperiamo le credenziali dal model
        Credentials credenzialiModel = model.getCredentials();
        CredenzialiBean credenzialiBean = factory.fromModelToBean(credenzialiModel, Credentials.class);

        // 3. TRADUZIONE VERSO IL BEAN (per la Grafica)
        // Usiamo il nuovo costruttore dell'UtenteBean che abbiamo creato prima
        return new UtenteBean(credenzialiBean, model.getNome(), model.getCognome(), model.getTelefono());
    }
}