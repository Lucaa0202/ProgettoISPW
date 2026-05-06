package patterns.factory;

import dao.*;
import dao.full.sql.UtenteDAOSQL;
import dao.full.sql.ViaggioDAOSQL;
import dao.full.sql.PrenotazioneDAOSQL;
import dao.full.sql.VeicoloDAOSQL;
import dao.full.sql.PagamentoDAOSQL;
import dao.full.sql.InvitoDAOSQL;
import dao.full.sql.RecensioneDAOSQL;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

public class FactoryDAO {

    private FactoryDAO() {}

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();
    private static final String PERSISTENCE_TYPE = "persistence.type";

    // Istanze Singleton dei DAO
    private static UtenteDAO utenteDAO;
    private static ViaggioDAO viaggioDAO;
    private static PrenotazioneDAO prenotazioneDAO;
    private static VeicoloDAO veicoloDAO;
    private static PagamentoDAO pagamentoDAO;
    private static InvitoDAO invitoDAO;
    private static RecensioneDAO recensioneDAO;

    private static void loadProperties() {
        try (InputStream input = FactoryDAO.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Properties file not found: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
        }
    }

    private static String getPersistenceType() {
        if (properties.isEmpty()) {
            loadProperties();
        }
        String type = properties.getProperty(PERSISTENCE_TYPE);
        if (type == null) {
            throw new IllegalArgumentException("Persistence type not found in properties file.");
        }
        return type;
    }

    // Metodo aggiornato: se passi "null" per demo o json, ti avvisa senza far crashare Java
    private static <T> T createDAO(String type, Supplier<T> mysqlSupplier, Supplier<T> demoSupplier, Supplier<T> jsonSupplier) {
        return switch (type) {
            case "mysql" -> mysqlSupplier.get();
            case "demo" -> {
                if (demoSupplier == null) throw new IllegalArgumentException("DAO Demo non ancora implementato.");
                yield demoSupplier.get();
            }
            case "json" -> {
                if (jsonSupplier == null) throw new IllegalArgumentException("DAO JSON non supportato/implementato.");
                yield jsonSupplier.get();
            }
            default -> throw new IllegalArgumentException("Tipo di DAO non valido: " + type);
        };
    }

    // --- GETTER SINCRONIZZATI PER I DAO ---

    public static synchronized UtenteDAO getUtenteDAO() {
        if (utenteDAO == null) {
            utenteDAO = createDAO(getPersistenceType(), UtenteDAOSQL::new, null, null);
        }
        return utenteDAO;
    }

    public static synchronized ViaggioDAO getViaggioDAO() {
        if (viaggioDAO == null) {
            viaggioDAO = createDAO(getPersistenceType(), ViaggioDAOSQL::new, null, null);
        }
        return viaggioDAO;
    }

    public static synchronized PrenotazioneDAO getPrenotazioneDAO() {
        if (prenotazioneDAO == null) {
            prenotazioneDAO = createDAO(getPersistenceType(), PrenotazioneDAOSQL::new, null, null);
        }
        return prenotazioneDAO;
    }

    public static synchronized VeicoloDAO getVeicoloDAO() {
        if (veicoloDAO == null) {
            veicoloDAO = createDAO(getPersistenceType(), VeicoloDAOSQL::new, null, null);
        }
        return veicoloDAO;
    }

    public static synchronized PagamentoDAO getPagamentoDAO() {
        if (pagamentoDAO == null) {
            pagamentoDAO = createDAO(getPersistenceType(), PagamentoDAOSQL::new, null, null);
        }
        return pagamentoDAO;
    }

    public static synchronized InvitoDAO getInvitoDAO() {
        if (invitoDAO == null) {
            invitoDAO = createDAO(getPersistenceType(), InvitoDAOSQL::new, null, null);
        }
        return invitoDAO;
    }

    public static synchronized RecensioneDAO getRecensioneDAO() {
        if (recensioneDAO == null) {
            recensioneDAO = createDAO(getPersistenceType(), RecensioneDAOSQL::new, null, null);
        }
        return recensioneDAO;
    }
}