package automate.orangehrm.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.qa.opencart.constants.AppConstants;

import automate.bastest.methods.BaseTest;
import automate.remoteserver.methods.RemoteConnector;
import automate.utils.common.Locaters;
import automate.utils.common.TestData;

public class OHRM_LoginTests extends BaseTest {

	
	
	
	@Test(priority = 1)
	public void orangeHappyPatgFlow() throws IOException {
		//Step#1 
		initializeBrowser(TestData.browser,TestData.orangeHRMURL);
		//Step#2
		isLogoVisible(Locaters.orangeHRMLoginPageLogo);
		//Step#3
		enterText(Locaters.orangeHRNUserName, "admin");
		//Step#4
		enterText(Locaters.orangeHRMPassword, "admin123");
		//Step#5
		clickButton(Locaters.orangeHRMLoginButton);
	    isButtonVisible(Locaters.orangeHRMDashboard);
		
		
		
		//Step#6
		clickButton(Locaters.orangeHRMAdmin);
		//Step#7
		clickButton(Locaters.orangeHRMAddUser);
		//Step#8
		clickButton(Locaters.orangeHRMUserRole);
		//Step#9
		clickButton(Locaters.oraneHRMSelectUser);
		//Step#10
		clickButton(Locaters.orangeHRMStatus);		
		//Step#11
		clickButton(Locaters.oraneHRMSelectStatus);
		//Step#12
		typeAndSelectValue(Locaters.orangeHRMEmployeeName, "Imalsha Tester",1);
		//Step#13
		enterText(Locaters.orangeHRMUsername, "anand tester");
		//Step#14
		enterText(Locaters.orangeHRMUserNewPassword, "Anand@123");
		//Step15
		enterText(Locaters.orangeHRMUserConfirmNewPassword, "Anand@123");
		//Step16
		clickButton(Locaters.orangeHRMSaveUser);
		
	}

}
