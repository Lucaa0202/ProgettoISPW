package dao;

import model.Veicolo;
import exceptions.NoResultException;
import exceptions.DbOperationException;
import java.util.List;

public interface VeicoloDAO {
    // Registra un nuovo veicolo nel sistema
    void inserisciVeicolo(Veicolo veicolo) throws DbOperationException;

    // Trova tutti i veicoli posseduti da uno specifico guidatore
    List<Veicolo> trovaVeicoliPerProprietario(String emailProprietario) throws NoResultException;

    // Rimuove un veicolo dal sistema
    void rimuoviVeicolo(String targa) throws DbOperationException;
}
