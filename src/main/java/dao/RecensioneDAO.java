package dao;

import model.Recensione;
import exceptions.NoResultException;
import exceptions.DbOperationException;
import java.util.List;

public interface RecensioneDAO {
    // Lascia una nuova recensione
    void inserisciRecensione(Recensione recensione) throws DbOperationException;

    // Trova tutte le recensioni che un utente ha RICEVUTO (utile per mostrare il suo profilo)
    List<Recensione> trovaRecensioniRicevute(String emailRecensito) throws NoResultException;

    // (Opzionale/Avanzato) Calcola la media voti direttamente tramite una query SQL (es. SELECT AVG(voto)...)
    double calcolaMediaVoti(String emailRecensito) throws NoResultException;
}