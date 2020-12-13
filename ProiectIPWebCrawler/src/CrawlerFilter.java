import java.io.File;

public class CrawlerFilter {

	private String type;
	private String sitemapDir;

	public CrawlerFilter() {
	}

	public CrawlerFilter(String type, String sitemapDir) {
		this.type = type;
		this.sitemapDir = sitemapDir;
		File sitemapDirectory = new File(this.sitemapDir);
		if (!sitemapDirectory.exists()) {
			System.out.println("Nu exista acest director.");
		}
	}

	public void showPath() {
		File directory = new File(this.sitemapDir);
		File[] directors = directory.listFiles();
		find(directors);
	}

	public void find(File[] directory) {
		if (directory != null) {
			for (int i = 0; i < directory.length; i++) {
				File f = directory[i];
				if (f.isDirectory()) {
					find(f.listFiles());
				} else {
					String typeOfFile = f.getAbsolutePath();
					if (typeOfFile.endsWith(type)) {
						System.out.println(f.getAbsolutePath());
					}
				}

			}
		}
	}
}
