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

import static org.testng.Assert.assertEquals;

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

	public void initializeBrowser(String Browser,String url) {
		pf = new PlaywrightFactory();

		prop = pf.init_prop();


	 String	browserName=AppConstants.BROWSER;


		page = pf.initBrowser(Browser,url);
		
		}
		
	

	@AfterTest
	public void tearDown() {
		page.context().browser().close();
	}
	public static void highlightElement(String selector) {
        try {
        	ElementHandle element = page.querySelector(selector);
        	 element.evaluate("element => { element.style.border = '5px solid orange'; }");	
		} catch (Exception e) {
		}
    }
	
	public static void clickButton(String XpathName) {
		String finalXpath =XpathName;
		    log.info("Performing Click Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			highlightElement(finalXpath);
			page.querySelector(finalXpath).click();
			
	}
	
	public static void clickRadioButton(String XpathName) {
		String finalXpath =XpathName;
		    log.info("Performing ClickRadioButton Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			highlightElement(finalXpath);
			page.querySelector(finalXpath).click();
	}
	
	public static void clickCheckBox(String XpathName) {
		String finalXpath =XpathName;
		    log.info("Performing clickCheckBox Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			highlightElement(finalXpath);
			page.querySelector(finalXpath).click();
	}
	
	public static void enterText(String XpathName,String value) {
		String finalXpath =XpathName;
		log.info("Performing Enter Text Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			highlightElement(finalXpath);
			page.querySelector(finalXpath).fill(value);
			

	}
	
	public static void verifyText(String xpathName,String expectingTextValue) {
		String finalXpath =xpathName;
		log.info("Performing Enter Text Action for"+xpathName);
		highlightElement(finalXpath);
			String getText=page.querySelector(finalXpath).textContent();
			
			try {
				assertEquals(expectingTextValue, getText);
			} catch (Exception e) {
				Exception();
			}
			

	}

	public static String getText(String XpathName) {
		String finalXpath =XpathName;
		log.info("Performing get Text Action for"+XpathName);
			page.querySelector(finalXpath).isVisible();
			highlightElement(finalXpath);
			String fetchedText=page.querySelector(finalXpath).textContent();
			
           return fetchedText;
	}
	
	public static void verifyLogo(String XpathName) {
		    log.info("Performing is Visible for"+XpathName);
		    highlightElement(XpathName);
			page.querySelector(XpathName).isVisible();
			boolean validateElement=page.querySelector(XpathName).isVisible();
			
			try {
				assertEquals(validateElement, true);
			} catch (Exception e) {
				Exception();
			}

	}
	
	
	public static void dragAndDrop(String source,String destination) {
		log.info("Performing drag and drop");
		highlightElement(source);
		highlightElement(destination);
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
	
	public static void Exception() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 0) {
            StackTraceElement element = stackTrace[0];
            String className = element.getClassName();
            String fileName = element.getFileName();
            int lineNumber = element.getLineNumber();
            System.out.println("Exception occurred in class " + className + ", file " + fileName + ", line " + lineNumber);
        }
	}
}
