package com.qa.opencart.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Base64;
import java.util.Properties;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserType.LaunchOptions;
import com.qa.constants.Constants;
import com.qa.opencart.constants.AppConstants;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;


public class PlaywrightFactory {

	Playwright playwright;
	Browser browser;
	protected BrowserContext browserContext;
	Page page;
	Properties prop;

	private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
	protected static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
	protected static ThreadLocal<Page> tlPage = new ThreadLocal<>();
	private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();

	public static Playwright getPlaywright() {
		return tlPlaywright.get();
	}

	public static Browser getBrowser() {
		return tlBrowser.get();
	}

	public static BrowserContext getBrowserContext() {
		return tlBrowserContext.get();
	}

	public static Page getPage() {
		return tlPage.get();
	}
	
	protected static Path getLatestModifiedFile(String folderPath) throws IOException {
        Path latestModifiedFile = null;
        FileTime latestModifiedTime = FileTime.fromMillis(0);

        Path folder = Paths.get(folderPath);
        if (Files.isDirectory(folder)) {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folder);
            for (Path filePath : directoryStream) {
                BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                FileTime modifiedTime = attributes.lastModifiedTime();
                if (modifiedTime.compareTo(latestModifiedTime) > 0) {
                    latestModifiedTime = modifiedTime;
                    latestModifiedFile = filePath;
                }
            }
        }
        return latestModifiedFile;
    }

	public Page initBrowser(String browser,String url) {

		String browserName = browser;
		System.out.println("browser name is : " + browserName);

		// playwright = Playwright.create();
		tlPlaywright.set(Playwright.create());

		switch (browserName.toLowerCase()) {
		case "chromium":
			tlBrowser.set(getPlaywright().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "firefox":
			tlBrowser.set(getPlaywright().firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "safari":
			tlBrowser.set(getPlaywright().webkit().launch(new BrowserType.LaunchOptions().setHeadless(false)));
			break;
		case "chrome":
			tlBrowser.set(
					getPlaywright().chromium().launch(new LaunchOptions().setChannel("chrome").setHeadless(false)));
			break;
		case "edge":
			tlBrowser.set(
					getPlaywright().chromium().launch(new LaunchOptions().setChannel("msedge").setHeadless(false)));
			break;	

		default:
			System.out.println("please pass the right browser name......");
			break;
		}

		tlBrowserContext.set(getBrowser().newContext(new Browser.NewContextOptions().setRecordVideoDir(Paths.get("testvideos/")).setRecordVideoSize(640,480)));
		tlPage.set(getBrowserContext().newPage());
		getPage().navigate(url);
		return getPage();

	}

	/**
	 * this method is used to initialize the properties from config file
	 */
	public Properties init_prop() {

		try {
			FileInputStream ip = new FileInputStream("./src/test/resources/config/config.properties");
			prop = new Properties();
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;

	}
	
	public void addVideo() throws IOException {
		String path=getLatestModifiedFile(System.getProperty("user.dir") + "/AutomationReports/").toString();
		String Vide0Path=getLatestModifiedFile(System.getProperty("user.dir") + "/testvideos/").toString();
		  // Load the HTML file
		 // Load the HTML file
       File htmlFile = new File(path);
        Document doc = Jsoup.parse(htmlFile, "UTF-8");

        Element infoDiv = doc.select("div.info").first();

        // Create the <video> element
        Element videoElement = new Element("video");
        videoElement.attr("src", Vide0Path);
        videoElement.attr("type", "video/webm");
        videoElement.attr("controls", "");
        videoElement.attr("width", "400");
        videoElement.attr("height", "300");
        videoElement.attr("style", "border: 1px solid red;");
        videoElement.attr("autoplay", ""); // Add the autoplay attribute
        // Append the <video> element to the <div> element
        infoDiv.appendChild(videoElement);

        // Write the modified HTML back to the file
        String modifiedHtml = doc.html();
        org.apache.commons.io.FileUtils.writeStringToFile(htmlFile, modifiedHtml, "UTF-8");

        System.out.println("Video tag added successfully.");


	}
	/**
	 * take screenshot
	 * 
	 */

	public static String takeScreenshot() {
		String path = AppConstants.folderpath + System.currentTimeMillis() + ".png";
		//getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
		byte[] buffer=null;
		try {
			 buffer = getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		String base64Path = Base64.getEncoder().encodeToString(buffer);
		return base64Path;
	}

}
