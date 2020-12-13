import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cautare{




   public static void cauta(String keyword) {

        File dir = new File("C:\\Users\\andre\\Desktop\\cautare\\ProiectIPWebCrawler\\sites"); // directory = target directory.
        if (dir.exists()) // Directory exists then proceed.
        {
            Pattern p = Pattern.compile(keyword); // keyword = keyword to search in files.
            ArrayList<String> list = new ArrayList<String>(); // list of files.

            for (File folder: dir.listFiles()) {
                for (File f : folder.listFiles()) {
                    if (!f.isFile()) continue;
                    try {
                        FileInputStream fis = new FileInputStream(f);
                        byte[] data = new byte[fis.available()];
                        fis.read(data);
                        String text = new String(data);
                        Matcher m = p.matcher(text);
                        if (m.find()) {
                            list.add(f.getName()); // add file to found-keyword list.
                        }
                        fis.close();
                    } catch (Exception e) {
                        System.out.print("\n\t Error processing file : " + f.getName());
                    }

                }
            }

            System.out.print("\n\t List : " + list); // list of files containing keyword.
        } // IF directory exists then only process.
        else {
            System.out.print("\n Directory doesn't exist.");
        }
    }
}