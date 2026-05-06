package utilities.enums;

public enum StatoPrenotazione {
    IN_ATTESA(1),
    CONFERMATA(2),
    RIFIUTATA(3);

    private final int id;

    private StatoPrenotazione(int id) {
        this.id = id;
    }

    public static StatoPrenotazione convertIntToState(int id) {
        for (StatoPrenotazione stato : values()) {
            if (stato.getId() == id) {
                return stato;
            }
        }
        return null; // Oppure lanciare un'eccezione se preferisci
    }

    public int getId() {
        return id;
    }
}