package utilities.other.mappers;

import beans.*;
import model.*;
import patterns.factory.BeanAndModelMapperFactory;

public class MapperRegistration {

    private MapperRegistration() {}

    // Ricordati di chiamare questo metodo all'inizio del tuo Main.java!
    public static void registerMappers() {
        BeanAndModelMapperFactory factory = BeanAndModelMapperFactory.getInstance();

        // Mappers già fatti prima
        factory.registerMapper(CredenzialiBean.class, Credentials.class, new CredenzialiMapper());
        factory.registerMapper(UtenteBean.class, Utente.class, new UtenteMapper());

        // Nuovi Mappers appena aggiunti
        factory.registerMapper(VeicoloBean.class, Veicolo.class, new VeicoloMapper());
        factory.registerMapper(ViaggioBean.class, Viaggio.class, new ViaggioMapper());
        factory.registerMapper(PrenotazioneBean.class, Prenotazione.class, new PrenotazioneMapper());
        factory.registerMapper(RecensioneBean.class, Recensione.class, new RecensioneMapper());
        factory.registerMapper(InvitoBean.class, Invito.class, new InvitoMapper());
        factory.registerMapper(PagamentoBean.class, Pagamento.class, new PagamentoMapper());

    }
}