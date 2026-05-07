package utilities.other.mappers;

import beans.CredenzialiBean;
import beans.UtenteBean;
import model.Credentials; // (Se l'hai chiamato Credentials o Credenziali, usa il tuo nome esatto)
import model.Utente;
import patterns.factory.BeanAndModelMapperFactory;

public class UtenteMapper implements BeanAndModelMapper<UtenteBean, Utente> {

    @Override
    public Utente fromBeanToModel(UtenteBean bean) {
        BeanAndModelMapperFactory factory = BeanAndModelMapperFactory.getInstance();

        // 1. Convertiamo il Bean delle credenziali in un Model delle credenziali
        Credentials credenzialiModel = factory.fromBeanToModel(bean.getCredenziali(), CredenzialiBean.class);

        // 2. Passiamo l'INTERO oggetto credenzialiModel al costruttore!
        // Nota: Assicurati che il 4° parametro String (es. documento o patente) sia corretto.
        // Se nel Bean non ce l'hai, passa null o stringa vuota, oppure aggiungilo al Bean.
        return new Utente(credenzialiModel, bean.getNome(), bean.getCognome(), "StringaMancante");
    }

    @Override
    public UtenteBean fromModelToBean(Utente model) {
        BeanAndModelMapperFactory factory = BeanAndModelMapperFactory.getInstance();

        // Invece di fare model.getEmail(), ora prendiamo l'intero oggetto Credentials dal model!
        Credentials credenzialiModel = model.getCredentials(); // <- Assicurati di avere questo getter in Utente

        CredenzialiBean credenzialiBean = factory.fromModelToBean(credenzialiModel, Credentials.class);

        return new UtenteBean(credenzialiBean, model.getNome(), model.getCognome());
    }
}