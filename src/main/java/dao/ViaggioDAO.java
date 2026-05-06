package dao;

import model.Viaggio;
import exceptions.NoResultException;
import exceptions.DbOperationException;
import java.util.List;

public interface ViaggioDAO {
    // Aggiunge un nuovo viaggio
    void inserisciViaggio(Viaggio viaggio) throws DbOperationException;

    // Cerca viaggi in base a partenza e destinazione (SonarCloud friendly: restituisce la lista!)
    List<Viaggio> cercaViaggi(String partenza, String destinazione) throws NoResultException;

    // Recupera un singolo viaggio tramite ID
    Viaggio recuperaViaggio(int idViaggio) throws NoResultException;

    // Aggiorna i posti disponibili dopo una prenotazione
    void aggiornaPostiDisponibili(int idViaggio, int nuoviPosti) throws DbOperationException;

    // Metodo default per eliminare o annullare un viaggio
    default void annullaViaggio(Viaggio viaggio) {
        throw new UnsupportedOperationException("Annullamento del viaggio non supportato da questa implementazione.");
    }
}