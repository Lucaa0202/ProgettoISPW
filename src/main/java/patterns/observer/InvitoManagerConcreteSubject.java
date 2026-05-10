package patterns.observer;

import model.Invito;
import java.util.ArrayList;
import java.util.List;

public class InvitoManagerConcreteSubject extends Subject {
    private static InvitoManagerConcreteSubject instance = null;
    private List<Invito> requests = new ArrayList<>(); // Manteniamo il nome 'requests' come il suo

    private InvitoManagerConcreteSubject() {}

    // Singleton - Identico al suo
    public static InvitoManagerConcreteSubject getInstance() {
        if (instance == null) {
            instance = new InvitoManagerConcreteSubject();
        }
        return instance;
    }

    public void addInvito(Invito invito) {
        requests.add(invito);
        notifyObservers();
    }

    public void removeInvito(Invito invito) {
        // Lui usa r.getID() == request.getID()
        // Noi usiamo l'ID del tuo model Invito
        requests.removeIf(r -> r.getIdInvito() == invito.getIdInvito());
        notifyObservers();
    }

    public List<Invito> getInviti() {
        return requests;
    }

    public void loadInviti(List<Invito> inviti) {
        // Copia speculare del suo metodo loadRequests
        this.requests = inviti;
    }
}
