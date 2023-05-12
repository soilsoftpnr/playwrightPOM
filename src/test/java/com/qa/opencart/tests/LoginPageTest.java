package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

import CommonUtils.Locaters;
import CommonUtils.TestData;

public class LoginPageTest extends BaseTest {

	
	
	
	@Test(priority = 1)
	public void loginTest() {
		//Step#1
		initializeBrowser(TestData.browser,TestData.url);
		//Step#2
		enterText(Locaters.userName, TestData.userName);
		//Step#3
	    enterText(Locaters.password, TestData.password);
	    //Step#4
	    clickButton(Locaters.loginBtn);
	    //Step#5
	    verifyLogo(Locaters.logo);
	}

	

}
