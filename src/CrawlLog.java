import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Clasa CrawlLog creeaza structura fisierului de log
 * in functie de anumite valori si de data si ora
 * curenta
 *
 * @author Robert
 */
public class CrawlLog {
    private FileWriter mLogFile;
    private BufferedWriter writer;

    /**
     * Constructorul clasei, responsabil cu
     * initializarea componentelor necesare
     * procesului de creare a fisierului de log
     */
    public CrawlLog() throws IOException {
        this.mLogFile = new FileWriter("logger.txt", true);
        this.writer = new BufferedWriter(this.mLogFile);
    }

    /**
     * Functie care scrie in fisierul de log
     * mesajul corespunzator codului de eroare/
     * warning/download
     *
     * @param log_id       Id-ul mesajului de log corespunzator
     *                     err/wrn/download
     * @param messageToLog mesajul pe baza caruia se va face
     *                     logul
     */
    public void sendDataToLogger(int log_id, String messageToLog) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd '-' HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        String basicMessage;
        if (log_id == 1) {
            basicMessage = "[ " + dateFormat.format(date) + "] " + "INFO: " + messageToLog + "\n";
            //System.out.println(basicMessage);
            writer.write(basicMessage);
        } else if (log_id == 2) {
            basicMessage = "[ " + dateFormat.format(date) + "] " + "WARNING:" + messageToLog + "\n";
            writer.write(basicMessage);
        } else if (log_id == 3) {
            basicMessage = "[ " + dateFormat.format(date) + "] " + "ERROR:" + messageToLog + "\n";
            writer.write(basicMessage);
        }

    }

    public void close() throws IOException {
        this.writer.close();
    }
}
