package patterns.factory;

import dao.*;
/* Scommenterai questi import man mano che creeremo i pacchetti
import il.tuo.pacchetto.dao.demo.*;
import il.tuo.pacchetto.dao.sql.*;
import il.tuo.pacchetto.dao.json.*;
*/

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;

public class FactoryDAO {

    // Costruttore privato identico a quello del tuo amico
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

    // Caricamento delle proprietà una sola volta
    private static void loadProperties() {
        try (InputStream input = FactoryDAO.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("Properties file not found: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            // Se in futuro replichi la classe "Printer" del tuo amico, puoi sostituire questa riga
            System.err.println("Error loading properties file: " + e.getMessage());
        }
    }

    // Verifica che il tipo di persistenza sia valido
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

    // Metodo generico per creare DAO (Esattamente identico al suo)
    private static <T> T createDAO(String type, Supplier<T> mysqlSupplier, Supplier<T> demoSupplier, Supplier<T> jsonSupplier) {
        return switch (type) {
            case "mysql" -> mysqlSupplier.get();
            case "demo" -> demoSupplier.get();
            case "json" -> {
                if (jsonSupplier == null) {
                    throw new IllegalArgumentException("DAO JSON non supportato.");
                }
                yield jsonSupplier.get();
            }
            default -> throw new IllegalArgumentException("Tipo di DAO non valido: " + type);
        };
    }

    // --- GETTER SINCRONIZZATI PER I DAO ---

    public static synchronized UtenteDAO getUtenteDAO() {
        if (utenteDAO == null) {
            /* SCOMMENTARE QUANDO LE CLASSI SARANNO PRONTE
            utenteDAO = createDAO(
                    getPersistenceType(),
                    UtenteDAOSQL::new,
                    UtenteDAODemo::new,
                    UtenteDAOJSON::getInstance // Usiamo getInstance per il JSON come faceva il tuo amico
            );
            */
        }
        return utenteDAO;
    }

    public static synchronized ViaggioDAO getViaggioDAO() {
        if (viaggioDAO == null) {
            /*
            viaggioDAO = createDAO(
                    getPersistenceType(),
                    ViaggioDAOSQL::new,
                    ViaggioDAODemo::new,
                    null // Non supportato in JSON, come richiesto dai vincoli
            );
            */
        }
        return viaggioDAO;
    }

    public static synchronized PrenotazioneDAO getPrenotazioneDAO() {
        if (prenotazioneDAO == null) {
            /*
            prenotazioneDAO = createDAO(
                    getPersistenceType(),
                    PrenotazioneDAOSQL::new,
                    PrenotazioneDAODemo::new,
                    null
            );
            */
        }
        return prenotazioneDAO;
    }

    public static synchronized VeicoloDAO getVeicoloDAO() {
        if (veicoloDAO == null) {
            /*
            veicoloDAO = createDAO(
                    getPersistenceType(),
                    VeicoloDAOSQL::new,
                    VeicoloDAODemo::new,
                    null
            );
            */
        }
        return veicoloDAO;
    }

    public static synchronized PagamentoDAO getPagamentoDAO() {
        if (pagamentoDAO == null) {
            /*
            pagamentoDAO = createDAO(
                    getPersistenceType(),
                    PagamentoDAOSQL::new,
                    PagamentoDAODemo::new,
                    null
            );
            */
        }
        return pagamentoDAO;
    }

    public static synchronized InvitoDAO getInvitoDAO() {
        if (invitoDAO == null) {
            /*
            invitoDAO = createDAO(
                    getPersistenceType(),
                    InvitoDAOSQL::new,
                    InvitoDAODemo::new,
                    null
            );
            */
        }
        return invitoDAO;
    }

    public static synchronized RecensioneDAO getRecensioneDAO() {
        if (recensioneDAO == null) {
            /*
            recensioneDAO = createDAO(
                    getPersistenceType(),
                    RecensioneDAOSQL::new,
                    RecensioneDAODemo::new,
                    null
            );
            */
        }
        return recensioneDAO;
    }
}