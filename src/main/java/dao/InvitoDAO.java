package dao;

import model.Invito;
import utilities.enums.StatoInvito;
import exceptions.NoResultException;
import exceptions.DbOperationException;
import java.util.List;

public interface InvitoDAO {
    // Invia un nuovo invito da un guidatore a un passeggero
    void creaInvito(Invito invito) throws DbOperationException;

    // Trova tutti gli inviti pendenti ricevuti da un utente
    List<Invito> trovaInvitiRicevuti(String emailPasseggero) throws NoResultException;

    List<String> trovaStoricoPasseggeri(String emailGuidatore) throws exceptions.NoResultException;

    // Aggiorna lo stato dell'invito quando il passeggero accetta o rifiuta
    void rispondiInvito(int idInvito, StatoInvito risposta) throws DbOperationException;
}