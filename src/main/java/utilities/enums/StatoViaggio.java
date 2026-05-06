package utilities.enums;

public enum StatoViaggio {
    ATTIVO(1),
    ANNULLATO(2),
    COMPLETATO(3);

    private final int id;

    private StatoViaggio(int id) {
        this.id = id;
    }

    public static StatoViaggio convertIntToState(int id) {
        for (StatoViaggio stato : values()) {
            if (stato.getId() == id) {
                return stato;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}