import java.io.*;
import java.net.URL;

public class Crawler {
    public static void main(String[] args) throws IOException {

        CrawlerConfig config;

        if(args[0].equals("crawl")){
            //crawler crawl config.conf;
//            config = new CrawlerConfig(args[1]);
//
//            CrawlURL firstCrawl = new CrawlURL(args[2], "sites");
//            firstCrawl.startCrawl();

            var crawler = new CrawlerInDepth(3,"https://www.mta.ro");
            crawler.startCrawl();

      
        } else if(args[0].equals("search")){
            Cautare cautare=new Cautare();
            cautare.cauta("precum");
        
            //crawler search document
            //parcurgere fisiere din sitemap
            //si afisare doar acele fisiere cu numele specificat(ex: search)

        } else if(args[0].equals("filter")){
            CrawlerFilter crawlerFilter = new CrawlerFilter(args[1],"sites/"+args[2]);
            crawlerFilter.showPath();
        }
		else if(args[0].equals("sitemap")){
        CrawlSitemap sitemap=new CrawlSitemap(new File("C:\\ip\\ProiectIPWebCrawler\\sites"));
        sitemap.setFunction();}

    }

}
