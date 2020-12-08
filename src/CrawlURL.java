import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;


/**
 * Clasa CrawlURL creaza structura schelet a sitemap-ului
 * pornind de la URL-urile din fisierul de intrare
 *
 * @author Orăș Alexandra-Larisa
 */
public class CrawlURL {

    String sitemapDir;
    String inFile;
    Map<String, ArrayList<String>> robots;
    String USER_AGENT = " Mozilla";
    CrawlLog logger = new CrawlLog();

    /**
     * Constructorul clasei, responsabil cu
     * initializarea componentelor necesare
     * procesului de creare a structurii sitemap-ului
     *
     * @param inputFilename Fisier de intrare cu cate un URL pe fiecare linie
     * @param sitemapDir    Numele directorului parinte, in care se va crea structura arborescenta
     */
    public CrawlURL(String inputFilename, String sitemapDir) throws IOException {

        this.sitemapDir = sitemapDir; // sites
        this.inFile = inputFilename;

        File sitemapDirectory = new File(this.sitemapDir);
        if (!sitemapDirectory.exists()) {
            sitemapDirectory.mkdir();
        }
    }

    /**
     * Functie care returneaza numele site-ului curent
     * pentru a se crea un folder corespunzator
     *
     * @param URL URL-ul site-ului a carui sitemap urmeaza a se face
     * @return returneaza numele radacina al site-ului
     * <p>
     * Exemplu: URL - https://mta.ro/, returneaza mta.ro
     */
    public String formatURL(String URL) {
        String getSiteName = URL.split("//")[1];
        return getSiteName;
    }

    /**
     * Functie care modeleaza URL-urile din fisier de intrare
     * si creaza structura arborescenta corespunzatoare
     * resursei catre care indica fiecare link.
     * De asemenea, inainte de descarcarea resursei respective
     * se verifica existenta fisierului robots.txt
     * si permisiunile user-agentului dat ca parametru
     *
     * @throws IOException Eroare aruncata atunci cand fisierul nu poate fi deschis
     */
    public void startCrawl() throws IOException {
        FileReader inputFileReader = new FileReader(this.inFile);
        BufferedReader readInput = new BufferedReader(inputFileReader);

        String currentLine = readInput.readLine();

        while (currentLine != null) {
            String siteName = formatURL(currentLine);
            String[] splitSiteName = siteName.split("/");
            String resourceFileName = splitSiteName[splitSiteName.length - 1];
            File directory = null;

            //daca URL-ul indica catre root-ul site-lui(EX: https://mta.ro/)
            //resursa descarcata va fi index.html
            if (resourceFileName == splitSiteName[0]) {
                resourceFileName = "index.html";
                directory = new File(this.sitemapDir + "/" + siteName);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

            } else {
                // altfel, daca URL indica catre o alta pagina din cadrul site-ului
                //(EX: https://jetbrains.com/help/idea/configure-project-settings.html/)
                //resursa descarcata va fi configure-project-settings.html
                StringJoiner path = new StringJoiner("/");
                for (int i = 0; i < splitSiteName.length - 1; i++) {
                    path.add(splitSiteName[i]);
                }
                directory = new File(this.sitemapDir + "/" + path);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                if (resourceFileName.indexOf('.') == -1) {
                    resourceFileName += ".html";
                }
            }

            try {
                CheckRobots getRobots = new CheckRobots(currentLine);
                logger.sendDataToLogger(1, "Trying to access robots.txt on: " + currentLine);
                this.robots = getRobots.readRobots();
                //System.out.println(this.robots);
                boolean isDisallowed = false;
                if (this.robots.containsKey(this.USER_AGENT) == true) {
                    for (int i = 0; i < splitSiteName.length; i++) {
                        if (this.robots.containsValue(splitSiteName[i])) {
                            isDisallowed = true;
                            break;
                        }
                    }
                }
                if (isDisallowed == false) {
                    logger.sendDataToLogger(1, "Downloading " + currentLine);
                    download(currentLine, directory.toString() + "/" + resourceFileName);

                } else {
                    throw new Exception("Crawler could not access this resource!");
                }
            } catch (MalformedURLException e) {
                //de bagat in fisierul de log
                System.out.println("Format URL gresit!");
            } catch (Exception e) {
                //de bagat in fisierul de log
                logger.sendDataToLogger(1, e.getMessage());
                //System.out.println(e.getMessage());
                //e.printStackTrace();
            }

            currentLine = readInput.readLine();
        }

        readInput.close();
        logger.close();

    }

    /**
     * Functia care descarca continutul paginii catre care
     * indica fiecare URL din fisierul de intrare
     *
     * @param urlString        URL-ul respectiv
     * @param resourceFileName Numele resursei ce va fi descarcata(index.html, imagine.jpg, etc..)
     * @throws IOException Eroare aruncata atunci cand fisierul nu poate fi deschis
     */
    public void download(String urlString, String resourceFileName) throws MalformedURLException, FileNotFoundException, IOException {

        System.setProperty("http.agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection.getResponseCode() == 404) {
            //resursa respectiva nu exista
            //de logat in fisier
            logger.sendDataToLogger(2, "Resource not found at " + urlString);
           // System.out.println("404");
        } else {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new FileWriter(resourceFileName));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
