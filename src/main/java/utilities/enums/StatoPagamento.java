package utilities.enums;

public enum StatoPagamento {
    IN_SOSPESO(1),
    COMPLETATO(2),
    FALLITO(3),
    RIMBORSATO(4);

    private final int id;

    private StatoPagamento(int id) {
        this.id = id;
    }

    public static StatoPagamento convertIntToState(int id) {
        for (StatoPagamento stato : values()) {
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