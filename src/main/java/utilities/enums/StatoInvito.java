package utilities.enums;

public enum StatoInvito {
    PENDING(1),
    ACCETTATO(2),
    RIFIUTATO(3);

    private final int id;

    private StatoInvito(int id) {
        this.id = id;
    }

    public static StatoInvito convertIntToState(int id) {
        for (StatoInvito stato : values()) {
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
