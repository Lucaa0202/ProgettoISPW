package controller;

import beans.VeicoloBean;
import beans.ViaggioBean;
import dao.VeicoloDAO;
import dao.ViaggioDAO;
import dao.full.sql.VeicoloDAOSQL;
import dao.full.sql.ViaggioDAOSQL;
import exceptions.NoResultException;
import model.Veicolo;
import model.Viaggio;
import utilities.other.mappers.VeicoloMapper;
import utilities.other.mappers.ViaggioMapper;

import java.util.List;

public class GestioneViaggioController {

    // Quando creeremo il ViaggioDAO, toglieremo il commento a questa riga
    private final ViaggioDAO viaggioDAO = new ViaggioDAOSQL();

    public void creaViaggio(ViaggioBean viaggioBean) throws Exception {

        // 1. Il Controller chiama il Mapper per la traduzione
        ViaggioMapper mapper = new ViaggioMapper();
        Viaggio viaggioModel = mapper.fromBeanToModel(viaggioBean);

        // 2. Chiamata al database (DA ATTIVARE NEL PROSSIMO STEP)
        viaggioDAO.inserisciViaggio(viaggioModel);

        // Log di conferma per IntelliJ
        System.out.println("GestioneViaggioController: Viaggio da " + viaggioModel.getPartenza() +
                " verso " + viaggioModel.getDestinazione() + " arrivato al controller!");
    }

    // --- NUOVO METODO PER IL CARICAMENTO AUTO ---
    public void recuperaVeicoliUtente(String email, List<VeicoloBean> veicoliBeanList) throws Exception {

        // 1. Istanziamo il DAO (In futuro qui potremmo usare una FactoryDAO)
        VeicoloDAO veicoloDAO = new VeicoloDAOSQL();

        // 2. Chiamiamo il DB per avere la lista dei Model
        List<Veicolo> veicoliModel = veicoloDAO.trovaVeicoliPerProprietario(email);

        // 3. Trasformiamo i Model in Bean usando il Mapper
        VeicoloMapper mapper = new VeicoloMapper();

        for (Veicolo veicolo : veicoliModel) {
            veicoliBeanList.add(mapper.fromModelToBean(veicolo));
        }
    }
    public void recuperaViaggiGuidatore(String email, List<ViaggioBean> viaggiBeanList) throws Exception {
        // Peschiamo i viaggi dal DB
        List<Viaggio> viaggiModel = viaggioDAO.recuperaViaggiPerGuidatore(email);

        ViaggioMapper mapper = new ViaggioMapper();
        for (Viaggio viaggio : viaggiModel) {
            viaggiBeanList.add(mapper.fromModelToBean(viaggio));
        }
    }
}