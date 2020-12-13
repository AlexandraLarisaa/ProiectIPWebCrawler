import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clasa CrawlerConfig seteaza valorile parametrilor
 * de configurare, specificate in fisierul de config dat ca argument
 * config.conf
 *
 * @author Orăș Alexandra-Larisa
 */
public class CrawlerConfig {

    private int n_threads;
    private int delay;
    private String root_dir;
    private int log_level;
    private String configContent = "";

    /**
     * Functie responsabila cu asocierea valorilor fiecarui
     * parametru de configurare. Verifica linia primita ca argument
     * si identifica a carei variabila trebuie sa-i seteze valoarea
     *
     * @param configLine Linie din fisierul de configurare
     */
    private void setConfigurationVariables(String configLine) {
        String[] splitConfig = configLine.split("=");
        String configName = splitConfig[0];
        String configValues = splitConfig[1];

        switch (configName) {
            case "n_threads":
                this.n_threads = Integer.parseInt(configValues);
                break;
            case "delay":
                this.delay = Integer.parseInt(configValues);
                break;
            case "root_dir":
                this.root_dir = configValues;
                break;
            case "log_level":
                this.log_level = Integer.parseInt(configValues);
                break;
            default:
                System.out.println("Variabila de configurare nu poate fi asociata!");
        }
    }

    /**
     * Functie care citese continutul fisierului de intrare config.conf
     * Il parcurge si trimite catre functia setConfigurationVariables cate o linie
     * reprezentand configuratia si valoarea(EX: configuratie=valoare)
     *
     * @param configFilename Fisierul de configurare (config.conf)
     * @throws Exception Eroare aruncata atunci cand fisierul este gol
     * @throws IOException Eroare aruncata atunci cand fisierul nu poate fi deschis
     * @throws FileNotFoundException Eroare aruncata atunci cand fisierul nu exista
     */

    private void readConfigContent(String configFilename) throws Exception, IOException, FileNotFoundException {
        FileReader inputFileReader = new FileReader(configFilename);
        BufferedReader readInput = new BufferedReader(inputFileReader);

        this.configContent += readInput.readLine();

        if (this.configContent == "") {
            throw new Exception("Fisier de configurare gol!");
        } else {
            String bufferLine = this.configContent;

            while (bufferLine != null) {
                setConfigurationVariables(bufferLine);
                bufferLine = readInput.readLine();
                this.configContent += bufferLine;
            }
        }

    }

    /**
     * Constructorul clasei, responsabil cu apelarea
     * functiei ce va citi si seta valorile parametrilor
     * de configurare
     *
     * @param configFilename Numele fisierului de configurare (config.conf)
     * @throws IOException Exceptie aruncata daca fisierul nu poate fi deschis
     * @throws Exception Exceptie aruncata daca fisierul de configurare este gol
     */
    public CrawlerConfig(String configFilename){

        try {
            readConfigContent(configFilename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Fisierul de configurare este gol!");
            System.exit(-1);
        }
    }

    public int getN_threads() {
        return n_threads;
    }

    public int getDelay() {
        return delay;
    }

    public String getRoot_dir() {
        return root_dir;
    }

    public int getLog_level() {
        return log_level;
    }

}
