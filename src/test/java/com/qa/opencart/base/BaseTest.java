package com.qa.opencart.base;

import org.testng.Assert;
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
import com.microsoft.playwright.options.SelectOption;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.factory.PlaywrightFactory;
import com.qa.opencart.pages.HomePage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.tests.CodelessTest;
import com.qa.opencart.tests.LoginPageTest;

import static org.testng.Assert.assertEquals;

import java.awt.Button;
import java.awt.event.InputEvent;
import com.microsoft.playwright.ElementHandle;
import com.qa.constants.*;

public class BaseTest extends PlaywrightFactory {
	public static Logger log = Logger.getLogger(BaseTest.class);
	PlaywrightFactory pf;
	static Page page;
	protected Properties prop;

	protected HomePage homePage;
	protected LoginPage loginPage;
	protected CodelessTest codelessTest;
	protected LoginPageTest loginpageTest;

	public void initializeBrowser(String Browser, String url) {
		pf = new PlaywrightFactory();

		prop = pf.init_prop();

		String browserName = AppConstants.BROWSER;

		page = pf.initBrowser(Browser, url);
		page.setDefaultTimeout(Constants.durationTimeOut);
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

	public static void clickButton(String xpath) {
		log.info("Performing Click Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		page.click(xpath);
		takeScreenshot();

	}

	public static void selectRadioButton(String xpath) {
		log.info("Performing ClickRadioButton Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		if (!page.isChecked(xpath)) {
			page.click(xpath);
		}
		takeScreenshot();
	}

	public static void selectCheckBox(String xpath) {
		page.querySelector(xpath).isVisible();
		highlightElement(xpath);
		if (!page.isChecked(xpath)) {
			page.click(xpath);
		}
		takeScreenshot();
	}

	public static void unSelectRadioButton(String xpath) {
		log.info("Performing ClickRadioButton Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		if (page.isChecked(xpath)) {
			page.click(xpath);
		}
		takeScreenshot();
	}

	public static void unSelectCheckBox(String xpath) {
		log.info("Performing clickCheckBox Action for" + xpath);
		page.querySelector(xpath).isVisible();
		highlightElement(xpath);
		if (page.isChecked(xpath)) {
			page.click(xpath);
		}
		takeScreenshot();
	}

	public static void enterText(String xpath, String value) {
		log.info("Performing Enter Text Action for" + xpath);
		page.querySelector(xpath).isVisible();
		highlightElement(xpath);
		page.querySelector(xpath).fill(value);
		takeScreenshot();
	}

	public static void verifyText(String xpath, String expectingTextValue) {
		log.info("Performing verifyText  Action for" + xpath);
		highlightElement(xpath);
		String getText = page.querySelector(xpath).textContent();
		takeScreenshot();
		try {
			assertEquals(expectingTextValue, getText);
		} catch (Exception e) {
			Exception();
		}
	}

	public static String getText(String xpath) {
		log.info("Performing get Text Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		takeScreenshot();
		String fetchedText = page.innerText(xpath);
		return fetchedText;
	}

	public static void verifyLogo(String xpath) {
		log.info("Performing verifyLogo for" + xpath);
		highlightElement(xpath);
		boolean validateElement = page.isVisible(xpath);
		takeScreenshot();
		try {
			Assert.assertEquals(validateElement, true);
		} catch (Exception e) {
			Exception();
		}

	}

	public static boolean isButtonVisible(String xpath) {
		log.info("Performing isButtonVisible Action for" + xpath);
		highlightElement(xpath);
		boolean isvisible = page.isVisible(xpath);
		takeScreenshot();
		return isvisible;
	}
	public static boolean isTextBoxReadable(String xpath) {
		log.info("Performing isTextBoxReadable Action for" + xpath);
		highlightElement(xpath);
		boolean isTextBoxReadable = (boolean) page.evaluate("selector => {"
                + "  const element = document.querySelector(selector);"
                + "  return !element.readOnly && !element.disabled;"
                + "}", xpath);
		 takeScreenshot();
		return isTextBoxReadable;
	}
	public static boolean isButtonEnabled(String xpath) {
		log.info("Performing isButtonEnabled Action for" + xpath);
		highlightElement(xpath);
		boolean isvisible = page.isEnabled(xpath);
		takeScreenshot();
		return isvisible;
	}
	public static boolean isCheckboxSelected(String xpath) {
		log.info("Performing isCheckboxSelected Action for" + xpath);
		highlightElement(xpath);
		boolean isChecked = page.isChecked(xpath);
		takeScreenshot();
		return isChecked;
	}
	
	

	public static boolean isRadioButtonSelected(String xpath) {
		log.info("Performing isRadioButtonSelected Action for" + xpath);
		highlightElement(xpath);
		boolean isRadioChecked = page.isChecked(xpath);
		takeScreenshot();
		return isRadioChecked;
	}

	public static boolean isRadioUnSelected(String xpath) {
		log.info("Performing isRadioUnSelected Action for" + xpath);
		highlightElement(xpath);
		boolean isUnChecked = page.isChecked(xpath);
		if (isUnChecked)
			isUnChecked = false;
		else
			isUnChecked = true;
		takeScreenshot();
		return isUnChecked;
	}

	public static boolean isCheckboxunSelected(String xpath) {
		log.info("Performing isCheckboxunSelected Action for" + xpath);
		highlightElement(xpath);
		boolean isUnChecked = page.isChecked(xpath);
		if (isUnChecked)
			isUnChecked = false;
		else
			isUnChecked = true;
		takeScreenshot();
		return isUnChecked;
	}

	public static void dragAndDrop(String source, String destination) {
		log.info("Performing drag and drop");
		highlightElement(source);
		highlightElement(destination);
		// Drag and drop source element onto destination element
		page.dragAndDrop(source, destination);
		takeScreenshot();
		// Close the browser
	}

	public static void scrollUp(int scrollLength) {
		page.evaluate("window.scrollBy(0, -" + scrollLength + ")");
	}

	public static void scrollDown(int scrollLength) {
		page.evaluate("window.scrollBy(0, " + scrollLength + ")");
	}

	public static void selectDropdown(String xpath, String value) {
		log.info("Performing selectDropdown Action for" + xpath);
		highlightElement(xpath);
		page.isVisible(xpath);
		page.selectOption(xpath, value);
		takeScreenshot();
	}

	public static String getPageTitle() {
		String pageTitle = page.title();
		return pageTitle;
	}

	public static String getUrl() {
		String url = page.url();
		return url;
	}

	public static String getHyperLink(String xpath) {
		highlightElement(xpath);
		page.isVisible(xpath);
		String link = page.getAttribute(xpath, "href");
		takeScreenshot();
		return link;

	}

	public static void Exception() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		if (stackTrace.length > 0) {
			StackTraceElement element = stackTrace[0];
			String className = element.getClassName();
			String fileName = element.getFileName();
			int lineNumber = element.getLineNumber();
			System.out.println(
					"Exception occurred in class " + className + ", file " + fileName + ", line " + lineNumber);
		}
	}
}
