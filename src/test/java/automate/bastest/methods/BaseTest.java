package automate.bastest.methods;

import org.testng.Assert;
import org.testng.ITestResult;
import org.apache.commons.io.FileUtils;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import com.qa.opencart.constants.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Frame;
import com.microsoft.playwright.JSHandle;
import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import com.microsoft.playwright.Video;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.MouseButton;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.factory.PlaywrightFactory;
import com.qa.opencart.pages.HomePage;
import com.qa.opencart.pages.LoginPage;

import automate.demotests.tests.CodelessTest;
import automate.demotests.tests.LoginPageTest;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import static org.testng.Assert.assertEquals;

import java.awt.Button;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.microsoft.playwright.ElementHandle;
import com.qa.constants.*;

public class BaseTest extends PlaywrightFactory {
	public static Logger log = Logger.getLogger(BaseTest.class);
	PlaywrightFactory pf;
	
	public static Page page;

	public ExtentReports extent;
	public static ExtentTest logger;
	protected HomePage homePage;
	protected LoginPage loginPage;
	protected CodelessTest codelessTest;
	protected LoginPageTest loginpageTest;
	public String folder = System.getProperty("user.dir") + "/screenshot/";

		
	

	@BeforeTest
	public void startReport(){
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");
		Date dt = new Date();
		String fileName;
		fileName = "report";
		createfolder("screens" + dateFormat.format(dt));
		String reportname = fileName + dateFormat.format(dt) + ".html";
		extent = new ExtentReports();
		ExtentSparkReporter spark = new ExtentSparkReporter(
				System.getProperty("user.dir") + "\\AutomationReports\\" + reportname);
		extent.attachReporter(spark);

		spark.config().setDocumentTitle("Smoke Testing");
		// Name of the report
		spark.config().setReportName("Automation Testing Report");
		// Dark Theme
		spark.config().setTheme(Theme.STANDARD);

	}

	public void createfolder(String name) {
		String parentDirectory = AppConstants.folderpath;
		AppConstants.folderpath = AppConstants.folderpath + name + "\\";
		// Create the folder
		File folder = new File(parentDirectory, name);
		if (folder.mkdir()) {
			System.out.println("Random folder created: " + folder.getAbsolutePath());
		} else {
			System.out.println("Failed to create random folder.");
		}
	}

	public void initializeBrowser(String Browser, String url)   {
		pf = new PlaywrightFactory();
		prop = pf.init_prop();
		
		String browserName = AppConstants.BROWSER;

		page = pf.initBrowser(Browser, url);
        // Maximize the window
       page.setDefaultTimeout(Constants.durationTimeOut);

	}

	public static void attachReport() throws IOException {
		String folderPath = AppConstants.folderpath;
		File folder = new File(folderPath);

		// Check if the folder exists
		if (folder.exists() && folder.isDirectory()) {
			// Get all the files in the folder
			File[] files = folder.listFiles();

			// Loop through each file in the folder
			for (File file : files) {
				if (file.isFile()) {
					// Perform operations on the file
					System.out.println(file.getName());
					logger.pass("Screenshot",
							MediaEntityBuilder.createScreenCaptureFromPath(file.getAbsolutePath()).build());
					// ...
					// Add your own logic here
					// ...
				}
			}
		} else {
			System.out.println("Folder does not exist or is not a directory.");
		}

	}

	public static void convertVideo(String videoPath) throws IOException {

		String ffmpegPath = System.getProperty("user.dir") + "/ffmpeg-6.0/";

		String inputFilePath = videoPath;
		String outputFilePath = videoPath.replace(".webm", ".mp4");
		String destinationFilePath = "/path/to/destination/file.mp4";

		File sourceFile = new File(videoPath);
		File destinationFile = new File(outputFilePath);

		try {
			Path sourcePath = sourceFile.toPath();
			Path destinationPath = destinationFile.toPath();
			Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File renamed successfully.");
		} catch (Exception e) {
			System.out.println("Failed to rename the file: " + e.getMessage());
		}

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

	public static void clickButtonAndAcceptAlert(String xpath) {
		log.info("Performing Click Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		page.click(xpath);
		takeScreenshot();
		page.waitForPopup(() -> {
			page.click("text=OK");
		});
	}

	public static void clickButtonAndDismissAlert(String xpath) {
		log.info("Performing Click Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		page.click(xpath);
		takeScreenshot();
		page.waitForPopup(() -> {
			page.click("text=Cancel");
		});
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
		page.isVisible(xpath);
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
		page.isVisible(xpath);
		highlightElement(xpath);
		if (page.isChecked(xpath)) {
			page.click(xpath);
		}
		takeScreenshot();
	}

	public static void enterText(String xpath, String value) {
		log.info("Performing Enter Text Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		page.fill(xpath, value);
		takeScreenshot();
	}
	
	public static void typeAndSelectValue(String xpath, String value,int index) {
		log.info("Performing Enter Text Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		page.fill(xpath, value);
		takeScreenshot();
		  Keyboard keyboard = page.keyboard();
		  
			  keyboard.press("ArrowDown");
		
		  keyboard.press("Enter");
	}
	
	public static void getScreenshotOfParticularElement(String xpath) {
		log.info("Performing clickCheckBox Action for" + xpath);
		page.isVisible(xpath);
		ElementHandle element = page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setTimeout(250));

		highlightElement(xpath);
		
		    
	 // Take a screenshot of the element.
		 
		  try { byte[] screenshotData = element.screenshot();
              String filePath = AppConstants.ELEMENT_SCREENSHOT_folderpath+ System.currentTimeMillis() + ".png";;
              try (FileOutputStream fos = new FileOutputStream(filePath)) {
                  fos.write(screenshotData);
                  System.out.println("Screenshot saved to: "+filePath);
              } catch (IOException e) {
                  e.printStackTrace();
              } } catch (Exception e) {
              e.printStackTrace();
          }
          
		    
		   
	}


	public static void verifyText(String xpath, String expectingTextValue) {
		log.info("Performing verifyText  Action for" + xpath);
	   page.waitForSelector(xpath);
		highlightElement(xpath);
		String getText = page.innerText(xpath).trim();
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
	
	public static void progressBar(String startXpath,String stopXpath,String ProgressbarXpath,int Percentage)
	{ 
		page.waitForLoadState(LoadState.NETWORKIDLE);
    page.waitForSelector(startXpath);
    page.waitForSelector(stopXpath);
    
    // Click the Start button
    page.click(startXpath);  
    // Wait for the progress bar to reach 
    page.waitForFunction("() => {\n"
            + "    const progressBar = document.querySelector('"+ProgressbarXpath+"');\n"
            + "    const value = progressBar.getAttribute('aria-valuenow');\n"
            + "    return value >= "+Percentage+";\n"
            + "}");
    
    // Click the Stop button
    page.click(stopXpath); 
    
    ElementHandle progressBarElement = page.querySelector(ProgressbarXpath); 
    String progressBarValue = progressBarElement.getAttribute("aria-valuenow");
    
    int difference = Math.abs(Integer.parseInt(progressBarValue) - 75);
    
    System.out.println("Difference: " + difference);}
	
	public static void compareTwoUIText(String xpath,String xpath2) {
		log.info("Performing get Text Action for" + xpath);
		page.isVisible(xpath);
		highlightElement(xpath);
		highlightElement(xpath2);
		takeScreenshot();
		String Text1 = page.innerText(xpath);
		String Text2 = page.innerText(xpath2);
	}
	

	public static void verifyLogo(String xpath) {
		log.info("Performing verifyLogo for" + xpath);
		ElementHandle element = page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setTimeout(250));

		boolean validateElement = false;
		takeScreenshot();
		try {
			try {
				page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
				highlightElement(xpath);
				validateElement = true;
			} catch (Exception e) {

			}
			Assert.assertEquals(validateElement, true);
		} catch (Exception e) {
			Exception();
		}

	}

	public static void isButtonVisible(String xpath) {
		boolean status = false;
		try {
			ElementHandle element = page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setTimeout(100));
			if (element == null) {
				status = false;
			} else {
				status = element.isVisible();
			}
		} catch (PlaywrightException ex) {
			status = false;
		}
		highlightElement(xpath);
		takeScreenshot();
		Assert.assertEquals(status, true);
	}
	
	
	public static void isLogoVisible(String xpath) {
		boolean status = false;
		try {
			ElementHandle element = page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setTimeout(100));
			if (element == null) {
				status = false;
			} else {
				status = element.isVisible();
			}
		} catch (PlaywrightException ex) {
			status = false;
		}
		highlightElement(xpath);
		takeScreenshot();
		Assert.assertEquals(status, true);
	}

	public static void isButtonNotVisible(String xpath) {
		boolean status = false;
		try {
			ElementHandle element = page.waitForSelector(xpath, new Page.WaitForSelectorOptions().setTimeout(30));
			if (element == null) {
				status = false;
			} else {
				status = element.isVisible();
			}
		} catch (PlaywrightException ex) {
			status = false;
		}
		highlightElement(xpath);
		takeScreenshot();
		Assert.assertEquals(status, false);

	}

	public static boolean isTextBoxReadable(String xpath) {
		log.info("Performing isTextBoxReadable Action for" + xpath);
		highlightElement(xpath);
		boolean isTextBoxReadable = (boolean) page
				.evaluate("selector => {" + "  const element = document.querySelector(selector);"
						+ "  return !element.readOnly && !element.disabled;" + "}", xpath);
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

	public static void dynamicScroll(String xpath) {
		ElementHandle button = page.querySelector(xpath);
		boolean isButtonVisible = button.isVisible();

		// Scroll to bring the button into view
		button.scrollIntoViewIfNeeded();
		highlightElement(xpath);
		takeScreenshot();
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
	public static void enterTextinShadowRootElement(String shadowHostXPath, String shadowInputXPath,String Value) {
		log.info("Performing selectDropdown Action for" + shadowHostXPath);
		highlightElement(shadowHostXPath);
		page.isVisible(shadowHostXPath);
		  // Use JavaScript to access the shadow DOM
 String script = "return document.evaluate('" + shadowHostXPath + "', document).iterateNext().shadowRoot";
        JSHandle shadowRootHandle = page.evaluateHandle(script);
        JSHandle inputElementHandle = shadowRootHandle.evaluateHandle("document.querySelector('" + shadowInputXPath + "')");

        // Type text into the input element within the shadow DOM
        inputElementHandle.asElement().type(Value);
        
	}
	
	public static void clickElementinShadowRootElement(String shadowHostXPath, String shadowClickXPath) {
		log.info("Performing selectDropdown Action for" + shadowHostXPath);
		highlightElement(shadowHostXPath);
		page.isVisible(shadowHostXPath);
		  // Use JavaScript to access the shadow DOM
 String script = "return document.evaluate('" + shadowHostXPath + "', document).iterateNext().shadowRoot";
        JSHandle shadowRootHandle = page.evaluateHandle(script);
        JSHandle elementHandle = shadowRootHandle.evaluateHandle("document.querySelector('" + shadowClickXPath + "')");

        // Type text into the input element within the shadow DOM
        elementHandle.asElement().click();
        
	}
	
	public static ElementHandle findShadowDomIframeElements(String shadowHostXPath, String iframeElementXpath) {
		log.info("Performing selectDropdown Action for" + shadowHostXPath);
		highlightElement(shadowHostXPath);
		page.isVisible(shadowHostXPath);

        // Use JavaScript to access the shadow DOM
        String script = "return document.evaluate('" + shadowHostXPath + "', document).iterateNext().shadowRoot";
        JSHandle shadowRootHandle = page.evaluateHandle(script);

        // Find all iframe elements within the shadow DOM
        // Find all iframe elements within the shadow DOM
        String iframeSearchScript = "return Array.from(arguments[0].shadowRoot.querySelectorAll('iframe'))";
        JSHandle iframeHandlesHandle = shadowRootHandle.evaluateHandle(iframeSearchScript);
        String findElementScript = "function findElementInIframes(args) { " +
                "const shadowHostXPath = args.shadowHostXPath;" +
                "const elementSelector = args.elementSelector;" +
                "const shadowHost = document.evaluate(shadowHostXPath, document).iterateNext();" +
                "if (!shadowHost) return null;" +
                "const shadowRoot = shadowHost.shadowRoot;" +
                "if (!shadowRoot) return null;" +
                "const iframes = shadowRoot.querySelectorAll('iframe');" +
                "for (const iframe of iframes) {" +
                "   const iframeDocument = iframe.contentDocument || iframe.contentWindow.document;" +
                "   const element = iframeDocument.querySelector(elementSelector);" +
                "   if (element) return element;" +
                "}" +
                "return null; }" +
                "return findElementInIframes(arguments[0]);";

        Object scriptArgs = new ScriptArgs(shadowHostXPath, iframeElementXpath);
        return page.evaluateHandle(findElementScript, scriptArgs).asElement();
    }
        
	  public static class ScriptArgs {
	        public String shadowHostXPath;
	        public String elementSelector;

	        public ScriptArgs(String shadowHostXPath, String elementSelector) {
	            this.shadowHostXPath = shadowHostXPath;
	            this.elementSelector = elementSelector;
	        }
	    }
	

	public static String getPageTitle() {
		String pageTitle = page.title();
		return pageTitle;
	}

	
	
	
	
	public static void clickIframeElement(String xpath) {
		log.info("Performing selectDropdown Action for" + xpath);
		highlightElement(xpath);
		page.isVisible(xpath);
		   ElementHandle foundElement = findElementInAllIframes(page, xpath);

           if (foundElement != null) {
               // Perform actions on the element (e.g., click)
               foundElement.click();
               System.out.println("Element found and clicked.");
           } else {
               System.out.println("Element not found in any iframe.");
           }        
	}
	
	public static void EnterTextIframeElement(String xpath,String value) {
		log.info("Performing selectDropdown Action for" + xpath);
		highlightElement(xpath);
		page.isVisible(xpath);
		   ElementHandle foundElement = findElementInAllIframes(page, xpath);

           if (foundElement != null) {
               // Perform actions on the element (e.g., click)
               foundElement.type(value);
               System.out.println("Element found and clicked.");
           } else {
               System.out.println("Element not found in any iframe.");
           }        
	}
	
	
	
	 public static ElementHandle findElementInAllIframes(Page page, String elementSelector) {     final ElementHandle[] foundElement = {null};

     // Function to recursively search for the element in iframes
     page.querySelectorAll("iframe").forEach(iframeHandle -> {
         Frame iframeFrame = iframeHandle.contentFrame();
         ElementHandle elementInIframe = iframeFrame.waitForSelector(elementSelector);
         if (elementInIframe != null) {
             foundElement[0] = elementInIframe;
         } else {
             // Recursively search in nested iframes
             findElementInIframes(iframeHandle, iframeFrame, elementSelector, foundElement);
         }
     });

     return foundElement[0];
}

	 private static void findElementInIframes(ElementHandle iframeHandle, Frame iframeFrame, String elementSelector, ElementHandle[] foundElement) {
	        iframeFrame.querySelectorAll("iframe").forEach(nestedIframeHandle -> {
	            ElementHandle elementInIframe = nestedIframeHandle.contentFrame().waitForSelector(elementSelector);
	            if (elementInIframe != null) {
	                foundElement[0] = elementInIframe;
	            } else {
	                // Recursively search in more nested iframes
	                findElementInIframes(nestedIframeHandle, nestedIframeHandle.contentFrame(), elementSelector, foundElement);
	            }
	        });
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
	
	
	  public static boolean visualTesting(String screenshotPath, String referenceImagePath) {
		
		  try {
          // Load the images
          BufferedImage image1 = ImageIO.read(new File(screenshotPath));
          BufferedImage image2 = ImageIO.read(new File(referenceImagePath));

          // Check if images have the same dimensions
          if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) {
              return true; // Images have different dimensions, consider them different
          }

          // Compare pixel by pixel
          for (int x = 0; x < image1.getWidth(); x++) {
              for (int y = 0; y < image1.getHeight(); y++) {
                  int pixel1 = image1.getRGB(x, y);
                  int pixel2 = image2.getRGB(x, y);

                  // Compare pixel colors
                  if (pixel1 != pixel2) {
                      return true; // Visual difference detected
                  }
              }
          }

          // No differences found
          return false;
      } catch (IOException e) {
          e.printStackTrace();
          return false; // Error occurred, consider it a difference
      }
  }

	  
	public static void navigateToBackPage() {
		page.goBack();
	}

	@AfterMethod
	public void getResult(ITestResult result) throws Exception {
		attachReport();
		page.close();
		Path a = getLatestModifiedFile(System.getProperty("user.dir") + "/testvideos/");
		// convertVideo(a.toAbsolutePath().toString());
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
			String dest = takeScreenshot();
			String destinatio = System.getProperty("user.dir") + "/screenshot/";
			logger.addScreenCaptureFromBase64String(dest);
			logger.log(Status.FAIL, MarkupHelper.createLabel("Screenshot Path: " + destinatio, ExtentColor.BLUE));
			logger.fail(result.getThrowable());

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
			takeScreenshot();

		} else {
			logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
			logger.skip(result.getThrowable());
		}

	}

	@AfterTest
	public void reportReady() throws IOException {
		extent.flush();

		System.out.println("Report is ready to be shared, with screenshots of tests");

		// addVideo();
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
