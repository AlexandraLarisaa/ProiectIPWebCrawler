import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Clasa CheckRobots verifica restrictiile user-agentilor
 * si le stocheaza intr-un obiect de tip map pentru a
 * putea fi verificat la descarcarea resurselor
 *
 * @author Orăș Alexandra-Larisa
 */
public class CheckRobots {

    private String robotsContent;
    private String robotsURL;
    private Map<String, ArrayList<String>> robots;
    CrawlLog logger;

    /**
     * Functia returneaza link-ul catre fisierul
     * robots.txt al fiecarui URL primit ca parametru
     *
     * @param URL link-ul catre site
     * @return returneaza calea catre fisierul robots.txt
     */
    public String parseURL(String URL) {
        String siteName = URL.split("//")[1];
        //daca e de forma wiki.mta.ro
        if (siteName.indexOf("/") == -1) {
            return URL + "/robots.txt";
        } else {
            String[] splitSiteURL = siteName.split("/");
            if (URL.contains("https")) {
                return "https://" + splitSiteURL[0] + "/robots.txt";
            } else {
                return "http://" + splitSiteURL[0] + "/robots.txt";
            }
        }
    }

    /**
     * Functia principala o programului care construieste obiectul
     * de tip map. Fiecarui user-agent i se atribuie o lista
     * de conditii de tip disallow.
     *
     * @return returneaza obiectul de tip map
     * @throws IOException exceptie returnata cand nu se poate deschide fisierul robots.txt
     */
    public Map<String, ArrayList<String>> readRobots() throws IOException {

        this.robots = new HashMap<String, ArrayList<String>>();
        ArrayList<String> userAgentDisallow = new ArrayList<String>();
        HashMap<String, ArrayList<String>> userDisallowMap = new HashMap<String, ArrayList<String>>();


        URL url = new URL(robotsURL);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // this.robotsContent = reader.readLine();
            //if (this.robotsContent != null) {
            String userAgent = null;

            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                if (line.contains("User-Agent:") || line.contains("User-agent:")) {
                    userAgent = line.split(":")[1];
                    userAgentDisallow = new ArrayList<String>();
                   // System.out.println(line);
                } else if (line.contains("Disallow:")) {
                    String disallow = line.split(":")[1];
                    userAgentDisallow.add(disallow);
                    this.robots.put(userAgent, userAgentDisallow);

                }
                this.robotsContent += line;
                line = reader.readLine();
            }
            //}
        } catch (IOException e) {
           logger.sendDataToLogger(3,"robots.txt doesn't exists..");
            //System.out.println("Fisierul robots.txt nu exista!");
        }
        return this.robots;
    }

    /**
     * Constructorul clasei initializeaza link-ul catre
     * locatia fisierului robots.txt in cadrul site-ului
     *
     * @param rootURL URL-ul site-ului
     * @throws IOException
     */
    public CheckRobots(String rootURL) throws IOException {

        this.robotsURL = this.parseURL(rootURL);

    }
}