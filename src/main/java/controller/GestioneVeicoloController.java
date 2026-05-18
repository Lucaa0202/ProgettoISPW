package controller;

import beans.VeicoloBean;
import dao.full.sql.VeicoloDAOSQL;
import model.Veicolo;
import dao.VeicoloDAO;
import utilities.other.mappers.VeicoloMapper;
import exceptions.DbOperationException;

public class GestioneVeicoloController {

    // Istanziamo il DAO usando la sua implementazione concreta
    // Ricordati di fare Alt+Invio per importare dao.full.sql.VeicoloDAOSQL
    private final VeicoloDAO veicoloDAO = new VeicoloDAOSQL();

    public void aggiungiVeicolo(VeicoloBean veicoloBean) throws DbOperationException, Exception {

        // 1. Il Controller chiama il Mapper (il traduttore)
        VeicoloMapper mapper = new VeicoloMapper();

        // Trasformiamo il Bean (lingua della GUI) in Model (lingua del DB/Backend)
        Veicolo veicoloModel = mapper.fromBeanToModel(veicoloBean);

        // 2. Chiamiamo il DAO per inserire fisicamente i dati in MySQL Workbench
        // Questo metodo lancia una DbOperationException se qualcosa va storto con la query
        veicoloDAO.inserisciVeicolo(veicoloModel);

        // Log di controllo su IntelliJ per confermare l'operazione
        System.out.println("GestioneVeicoloController: Veicolo " + veicoloModel.getTarga() + " inserito con successo!");
    }
}