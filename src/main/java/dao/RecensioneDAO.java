package dao;

import model.Recensione;
import exceptions.NoResultException;
import exceptions.DbOperationException;
import java.util.List;

public interface RecensioneDAO {
    void inserisciRecensione(Recensione recensione) throws DbOperationException;
    List<Recensione> trovaRecensioniRicevute(String emailRecensito) throws NoResultException;
    List<Recensione> trovaRecensioniPerViaggio(int idViaggio) throws NoResultException;
    double calcolaMediaVoti(String emailRecensito) throws NoResultException;

    // NUOVO: Metodo per controllare se l'utente ha già recensito questo viaggio!
    boolean esisteRecensione(String emailRecensore, int idViaggio) throws DbOperationException;
}