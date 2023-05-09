package com.qa.opencart.base;

import java.util.Properties;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.apache.log4j.Logger;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;

import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.MouseButton;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.factory.PlaywrightFactory;
import com.qa.opencart.pages.HomePage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.tests.CodelessTest;

import bsh.org.objectweb.asm.Constants;

import java.awt.Button;
import java.awt.event.InputEvent;
import com.microsoft.playwright.ElementHandle;


public class BaseTest {
	public static Logger log = Logger.getLogger(BaseTest.class);
	PlaywrightFactory pf;
	static Page page;
	protected Properties prop;

	protected HomePage homePage;
	protected LoginPage loginPage;
	protected CodelessTest codelessTest;

	@Parameters({ "browser" })
	@BeforeTest
	public void setup() {
		pf = new PlaywrightFactory();

		prop = pf.init_prop();


	 String	browserName=AppConstants.BROWSER;
		if (browserName != null) {
			prop.setProperty("browser", browserName);

         
		if (AppConstants.BROWSER != null) {
			prop.setProperty("browser", AppConstants.BROWSER);
		}

		page = pf.initBrowser(prop);
		homePage = new HomePage(page);
		}
	}
	
	

	@AfterTest
	public void tearDown() {
		page.context().browser().close();
	}
	
	
	public static void clickButton(String XpathName) {
		String finalXpath =XpathName;
		    log.info("Performing Click Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			page.querySelector(finalXpath).click();
	}
	
	public static void enterText(String XpathName,String value) {
		String finalXpath =XpathName;
		log.info("Performing Enter Text Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			page.querySelector(finalXpath).fill(value);
			

	}
	
	public static String getText(String XpathName) {
		String finalXpath =XpathName;
		log.info("Performing get Text Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			String fetchedText=page.querySelector(finalXpath).textContent();
			
           return fetchedText;
	}
	
	public static boolean validate(String XpathName) {
		log.info("Performing is Visible for"+XpathName);
			page.querySelector(XpathName).isVisible();
			boolean validateElement=page.querySelector(XpathName).isVisible();
			
           return validateElement;
	}
	
	
	public static void dragAndDrop(String source,String destination) {
		log.info("Performing drag and drop");
	// Drag and drop source element onto destination element
    page.dragAndDrop(source, destination);
    // Close the browser
   }
	
	public static void scrollUp(int scrollLength) {
		 page.evaluate("window.scrollBy(0, -"+scrollLength+")");
	}
	
	public static void scrollDown(int scrollLength) {
		 page.evaluate("window.scrollBy(0, "+scrollLength+")");
	}
	
	
}
