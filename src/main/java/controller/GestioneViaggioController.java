package controller;


import beans.ViaggioBean;
import model.Viaggio;
import utilities.other.mappers.ViaggioMapper;

public class GestioneViaggioController {

    // Quando creeremo il ViaggioDAO, toglieremo il commento a questa riga
    // private final ViaggioDAO viaggioDAO = new ViaggioDAOSQL();

    public void creaViaggio(ViaggioBean viaggioBean) throws Exception {

        // 1. Il Controller chiama il Mapper per la traduzione
        ViaggioMapper mapper = new ViaggioMapper();
        Viaggio viaggioModel = mapper.fromBeanToModel(viaggioBean);

        // 2. Chiamata al database (DA ATTIVARE NEL PROSSIMO STEP)
        // viaggioDAO.inserisciViaggio(viaggioModel);

        // Log di conferma per IntelliJ
        System.out.println("GestioneViaggioController: Viaggio da " + viaggioModel.getPartenza() +
                " verso " + viaggioModel.getDestinazione() + " arrivato al controller!");
    }
}
