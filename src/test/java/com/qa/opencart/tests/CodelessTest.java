package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;
import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.factory.PlaywrightFactory;

import java.util.ArrayList;
import java.util.Properties;

public class CodelessTest extends BaseTest {

	PlaywrightFactory pf;
	Page page;
	protected Properties prop;
	@Test
	public void executeTest() {
		
		ArrayList<String> steps=new ArrayList<String>();
		steps.add("Launch application https://google.com");
		steps.add("Enter keyword 'what is date in USA?' in search box");;
		
		for(String step:steps) {
			if(step.contains("Launch")&&step.contains("application")) {
				String locator=step.split("application")[1].trim().toString();
			page.navigate(locator);	
			}
			
			else if(step.contains("Enter")) {
				page.fill(step.split("in")[1], step.split("keyword")[1].split("in")[0].trim());
			}
		}
		
		
		
	}

	@Test
	public void homePageURLTest() {
		String actualURL = homePage.getHomePageURL();
		Assert.assertEquals(actualURL, prop.getProperty("url"));
	}

	@DataProvider
	public Object[][] getProductData() {
		return new Object[][] {
				{ "Macbook" }, 
				{ "iMac" }, 
				{ "Samsung" }
		};
	}

	@Test(dataProvider = "getProductData")
	public void searchTest(String productName) throws InterruptedException {
		Thread.sleep(5000);
		String actualSearchHeader = homePage.doSearch(productName);
		Assert.assertEquals(actualSearchHeader, "Search - " + productName);
	}

}
