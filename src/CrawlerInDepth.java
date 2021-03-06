import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerInDepth {
    private int inputLevel;
    private String inputURL;
    private List<List<String>> urlsPerLevel;

    public CrawlerInDepth(int inputLevel, String inputURL) {
        this.inputLevel = inputLevel;
        this.inputURL = inputURL;
        urlsPerLevel = new ArrayList<>();
        for (int i = 0; i <= inputLevel; i++) {
            urlsPerLevel.add(new ArrayList<>());
        }
    }

    public List<String> getContainingUrls(String inputUrl) throws IOException {
        URL url = new URL(inputUrl);
        var containedUrls = new ArrayList<String>();

        // read text returned by server
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = in.readLine()) != null) {
            for (var extractedUrl: extractUrls(line)) {
                containedUrls.add(extractedUrl);
            }
        }
        in.close();
        return containedUrls;
    }
    public static long findSize(String path) {
        long totalSize = 0;
        ArrayList<String> directory = new ArrayList<String>();
        File file = new File(path);

        if(file.isDirectory()) {
            directory.add(file.getAbsolutePath());
            while (directory.size() > 0) {
                String folderPath = directory.get(0);

                directory.remove(0);
                File folder = new File(folderPath);
                File[] filesInFolder = folder.listFiles();
                int noOfFiles = filesInFolder.length;

                for(int i = 0 ; i < noOfFiles ; i++) {
                    File f = filesInFolder[i];
                    if(f.isDirectory()) {
                        directory.add(f.getAbsolutePath());
                    } else {
                        totalSize += f.length();
                    }
                }
            }
        } else {
            totalSize = file.length();
        }
        return totalSize;
    }
    public void startCrawl(){
        urlsPerLevel.get(0).add(inputURL);
        for (int i = 0; i < urlsPerLevel.size(); i++) {
            System.out.println("Processing layer "+ i + " of " + urlsPerLevel.get(i).size() + " websites");

            long folderSize ;

            for(var url:urlsPerLevel.get(i)){
                try{

                    var crawledUrls = getContainingUrls(url);
                    for (var crawledUrl: crawledUrls) {
                        if(i+1 < inputLevel){
                            urlsPerLevel.get(i+1).add(crawledUrl);
                        }
                    }
                    folderSize = findSize("D:/Download");
                    if(folderSize>=1073741824) {
                                break;
                    }
                    var downloadPath = "D:/Download";
                    downloadResource(url,downloadPath);
                } catch (IOException e) {
                    System.out.println("Couldn't crawl url: "+url);
                }
            }
        }

        for (int i = 0; i < inputLevel; i++) {
            System.out.println("LEVEL" + i + ":");

            for (var urlInLevel:urlsPerLevel.get(i)) {
                System.out.println(urlInLevel);
            }
        }
    }


    public void downloadResource(String url,String path) throws IOException {

        ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
        var formattedUrl = url.replace('/', '_').replace(':','_');
        String extension="";
        if(url.length()>3){
            extension=url.substring(url.length()-3);

        }
        if(extension.equals("png") || extension.equals("jpg")||extension.equals("pdf")||extension.equals("gif") || extension.equals("doc")) {

        }else
        {
            formattedUrl = formattedUrl + ".html";
        }




        formattedUrl = path+'/'+formattedUrl;
        File newFile = new File(formattedUrl);

        if(!newFile.exists())
        {
            System.out.println("Downloading new file "+ formattedUrl);
            newFile.createNewFile();
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(formattedUrl)) {

                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                // handle exception
            }
        }
        else{
            System.out.println("File "+ formattedUrl + " already exists");
        }

    }

    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}
