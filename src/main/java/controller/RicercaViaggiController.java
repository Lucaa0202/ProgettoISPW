package controller;

import beans.ViaggioBean;
import dao.ViaggioDAO;
import exceptions.NoResultException;
import model.Viaggio;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;

import java.util.ArrayList;
import java.util.List;

public class RicercaViaggiController {
    private final BeanAndModelMapperFactory factory;
    private final ViaggioDAO viaggioDAO;

    public RicercaViaggiController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.viaggioDAO = FactoryDAO.getViaggioDAO();
    }

    // Basato sul searchSchedules del tuo amico, ma ottimizzato per il tuo DAO
    public void cercaViaggi(List<ViaggioBean> viaggiTrovatiBean, String partenza, String destinazione, String emailPasseggero) throws NoResultException {
        viaggiTrovatiBean.clear();

        try {
            // CORRETTO: Ora passiamo solo 2 argomenti e salviamo il risultato nella lista!
            List<Viaggio> viaggiModel = viaggioDAO.cercaViaggi(partenza, destinazione,emailPasseggero);

            if (viaggiModel.isEmpty()) {
                throw new NoResultException("Nessun viaggio trovato per questa tratta.");
            }

            // Convertiamo i Model in Bean per la grafica JavaFX
            for (Viaggio v : viaggiModel) {
                ViaggioBean vBean = factory.fromModelToBean(v, Viaggio.class);
                viaggiTrovatiBean.add(vBean);
            }

        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new NoResultException("Errore durante la ricerca dei viaggi: " + e.getMessage());
        }
    }
}
