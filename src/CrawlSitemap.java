
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
/**
 * Clasa CrwalSitemap creeaza structura fisierului
 * in functie de paginile existente in fiecare
 * site
 *
 * @author Robert
 */
public class CrawlSitemap {
    File mDir;
    FileWriter mOut;

    /**
     * Constructorul clasei, responsabil cu
     * initializarea componentelor necesare
     * procesului de creare a fisierului sitemap
     */
    public CrawlSitemap(File dir) throws IOException {
        this.mDir=dir;
        System.out.println("fghj");
        File s=new File("C:\\ip\\ProiectIPWebCrawler\\stmap.txt");
        this.mOut=new FileWriter (s);

    }

    public File getmDir() {
        return mDir;
    }

    /**
     * Functie care seteaza calea fisierului
     * sitemap si in care apelam scrierea
     * in acest fisier
     *
     *
     */
    public void setFunction() throws IOException {
        File s=new File("C:\\ip\\ProiectIPWebCrawler\\stmap.txt");
        FileWriter w =new FileWriter (s);
        this.displayDirectoryContents(this.mDir,w);
        w.close();
    }

    /**
     * Functie care scrie in fisierul de log
     * mesajul corespunzator codului de eroare/
     * warning/download
     *
     * @param dir       Directorul in care se afla
     *                  site-urile
     * @param w         Fisierul in care vom scrie
     *                  sitemap-ul
     *
     *
     */
    public  void displayDirectoryContents(File dir, FileWriter w) throws IOException {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    w.write("directory:" + file.getCanonicalPath()+'\n');
                    // mOut.append("directory:" + file.getCanonicalPath());
                    displayDirectoryContents(file,w);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                    w.write("     file:" + file.getCanonicalPath()+'\n');
                    // mOut.append("     file:" + file.getCanonicalPath());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
