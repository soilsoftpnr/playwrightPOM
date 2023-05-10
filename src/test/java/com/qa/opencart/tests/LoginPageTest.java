package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

public class LoginPageTest extends BaseTest {

	
	public static String userName="//input[@name=\"username\"]";
	public static String password="//input[@id=\"passwordtxt\"]";
	public static String loginBtn="//span[text()=\" Login \"]/..";
	public static String logo="//img[@class=\"app-logo-text ng-star-inserted\"]";
	
	
	
	
	
	
	
	
	
	@Test(priority = 1)
	public void loginTest() {
		//Step#1
		initializeBrowser("chromium","https://aquilaqa.pattesalabs.ai/signin");
		//Step#2
		enterText(userName, "namratha.pattesa@gmail.com");
		//Step#3
	    enterText(password, "Asdf@1230");
	    //Step#4
	    clickButton(loginBtn);
	    //Step#5
	    verifyLogo(logo);
	}

	

}
