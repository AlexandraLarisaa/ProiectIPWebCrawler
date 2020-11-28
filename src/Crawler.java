import java.io.*;
import java.net.URL;

public class Crawler {
    public static void main(String[] args) throws IOException {

        CrawlerConfig config;

        if(args[0].equals("crawl")){
            //crawler crawl config.conf;
            config = new CrawlerConfig(args[1]);

            CrawlURL firstCrawl = new CrawlURL(args[2], "sites");
            firstCrawl.startCrawl();

        } else if(args[0].equals("list")){
            //crawler list png
            //parcurgere fisiere din sitemap
            //si afisare doar acele fisiere cu tipul specificat(ex: png)

        } else if(args[0].equals("search")){
            //crawler search document
            //parcurgere fisiere din sitemap
            //si afisare doar acele fisiere cu numele specificat(ex: search)

        }


    }

}
