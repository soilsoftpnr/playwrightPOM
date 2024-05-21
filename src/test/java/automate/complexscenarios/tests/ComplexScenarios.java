package automate.complexscenarios.tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.qa.opencart.constants.AppConstants;

import automate.bastest.methods.BaseTest;
import automate.utils.common.Locaters;
import automate.utils.common.TestData;

public class ComplexScenarios extends BaseTest {

	
	
	
	/**
	 * @throws IOException
	 * 
	 * 
	 */
	@Test(priority = 1)
	public void complexScenarios() throws IOException {
		logger=extent.createTest("Login to Aquila");
		//Step#1
		initializeBrowser(TestData.browser,TestData.url);
		//Step#2
		clickButton(Locaters.dynamicidlink);
		//Step#3
		isButtonVisible(Locaters.buttonWithdynamicId,true);
		//Step#4
		clickButton(Locaters.buttonWithdynamicId);
		//Step#5
		navigateToBackPage();
		
		
		//Step#6
		isButtonVisible(Locaters.classAttributeLink,true);
		//step#7
		clickButton(Locaters.classAttributeLink);
		//Step#8
		isButtonVisible(Locaters.btnPrimary, false);
		//Step#8
		isButtonVisible(Locaters.btnSuccess, false);
		//Step#9
		isButtonVisible(Locaters.btnWarning, false);
		//Step#10
		clickButton(Locaters.btnPrimary);
		//Step#11
		navigateToBackPage();
		
		
		
		//Step#12
		isButtonVisible(Locaters.hiddenLayersLink, false);
		//Step#13
		clickButton(Locaters.hiddenLayersLink);
		//Step#14
		isButtonVisible(Locaters.greenButton, false);
		//Step#15
		clickButton(Locaters.greenButton);
		//Step#16
		isButtonEnabled(Locaters.greenButton);
		//Step#17
		navigateToBackPage();
		
		
		//Step#18
		isButtonVisible(Locaters.loadDelayLink, false);
		//Step#19
		clickButton(Locaters.loadDelayLink);
		//Step#20
		isButtonVisible(Locaters.buttonAppearingaterDelay, false);
		//Step#21
		navigateToBackPage();
		
		
		
		//Step#22
		isButtonVisible(Locaters.ajaxDataLink, false);
		//Step#23
		clickButton(Locaters.ajaxDataLink);
		//Step#24
		isButtonVisible(Locaters.buttonTriggeringAJAXRequest, false);
		//Step#25
		clickButton(Locaters.buttonTriggeringAJAXRequest);
		//Step#26
		verifyText(Locaters.dataloadedwithAJAXGetrequest, "Data loaded with AJAX get request.", false);
		//Step#27
		navigateToBackPage();
		
		
		//Step#28
		isButtonVisible(Locaters.clientSideDelayLink, false);
		//Step#29
		clickButton(Locaters.clientSideDelayLink);
		//Step#30
		isButtonVisible(Locaters.buttonTriggeringClientSideLogic, false);
		//Step#31
		clickButton(Locaters.buttonTriggeringClientSideLogic);
		//Step#32
		verifyText(Locaters.dataCalculatedOnTheClientSide, "Data calculated on the client side.", false);
		//Step#33
		navigateToBackPage();
				
		

		//Step#34
		isButtonVisible(Locaters.clickLink, false);
		//Step#35
		clickButton(Locaters.clickLink);
		//Step#36
		isButtonVisible(Locaters.buttonThatIgnoresDOMClickEvent, false);
		//Step#37
		clickButton(Locaters.buttonThatIgnoresDOMClickEvent);
		//Step#38
		isButtonVisible(Locaters.buttonThatIgnoresDOMClickEventsuccuess, false);
		//Step#39
		navigateToBackPage();
		
		
		//Step#40
		isButtonVisible(Locaters.textInputLink, false);
		//Step#41
		clickButton(Locaters.textInputLink);
		//Step#42
		isButtonVisible(Locaters.buttonThatIgnoresDOMClickEvent, false);
		//Step#43
		enterText(Locaters.textInput, "Demo Testing");
		//Step#44
		isButtonVisible(Locaters.updatingButton, false);
	    //Step#45
		clickButton(Locaters.updatingButton);
		//Step#46
		verifyText(Locaters.updatingButton, "Demo Testing", false);
		//Step#47
		navigateToBackPage();
		
		
		
				
		//Step#48
		isButtonVisible(Locaters.scrollbarsLink, false);
		//Step#49
		clickButton(Locaters.scrollbarsLink);
		//Step#50
		dynamicScroll(Locaters.hidingButton);
		//Step#51
		navigateToBackPage();
		
		
		
		
		//Step#52
		isButtonVisible(Locaters.dynamicTableLink, false);
		//Step#53
		clickButton(Locaters.dynamicTableLink);
		//Step#54
       	compareTwoUIText(Locaters.chromeCPU,Locaters.actualChromeCPU);
		//Step#55
       	navigateToBackPage();
       	
       	
        //Step#52
      	isButtonVisible(Locaters.verifytextLink, false);
      	//Step#53
      	clickButton(Locaters.verifytextLink);
      	//Step#54
        verifyText(Locaters.verifyText,"Welcome", false);
      	//Step#55
        navigateToBackPage();
        
        
        //Step#52
      	isButtonVisible(Locaters.progressBar, false);
      	//Step#53
      	clickButton(Locaters.progressBar);
      	//Step#54
      	progressBar(Locaters.startButton,Locaters.stopButton,Locaters.progressBar,75);
      	//Step#55
        navigateToBackPage();
        
        
        
       //Step#56
      	isButtonVisible(Locaters.visibilityLink, false);
      	//Step#57
      	clickButton(Locaters.visibilityLink);
      	//Step#58
    	isButtonVisible(Locaters.hideButton, false);
      	//Step#59
    	isButtonVisible(Locaters.removedButton, false);
    	//Step#60
    	isButtonVisible(Locaters.zeroWidthButton, false);
    	//Step#61
    	isButtonVisible(Locaters.overlappedButton, false);
    	//Step#62
    	isButtonVisible(Locaters.transparentButton, false);
    	//Step#63
    	isButtonVisible(Locaters.invisibleButton, false);
    	//Step#64
    	isButtonVisible(Locaters.notdisplayedButton, false);
    	//Step#65
    	isButtonVisible(Locaters.offscreenButton, false);
    	//Step#66
    	isButtonVisible(Locaters.verifytextLink, false);
    	//Step#67
    	clickButton(Locaters.hideButton);
    	//Step#68
    	isButtonNotVisible(Locaters.removedButton, false);
    	//Step#69
    	isButtonNotVisible(Locaters.zeroWidthButton, false);
    	//Step#70
    	isButtonNotVisible(Locaters.overlappedButton, false);
    	//Step#71
    	isButtonNotVisible(Locaters.transparentButton, false);
    	//Step#72
    	isButtonNotVisible(Locaters.invisibleButton, false);
    	//Step#73
    	isButtonNotVisible(Locaters.notdisplayedButton, false);
    	//Step#74
    	isButtonNotVisible(Locaters.offscreenButton, false);
    	//Step#75
    	isButtonNotVisible(Locaters.verifytextLink, false);
    	//Step#76
    	 navigateToBackPage();
    	 
    	 
    	//Step#77
       	isButtonVisible(Locaters.sampleAppLink, false);
       	//Step#78
       	clickButton(Locaters.sampleAppLink);
       	//Step#79
        enterText(Locaters.userNameui, "anand");
       	//Step#80
        enterText(Locaters.passwordui, "anand");
        //Step#81
        clickButton(Locaters.logIN);
        //Step#82
        verifyText(Locaters.loginStatus, "Invalid username/password", false);
        //Step#83
        navigateToBackPage();
        
        
        //Step#84
       	isButtonVisible(Locaters.mousehoverLink, false);
       	//Step#85
       	clickButton(Locaters.mousehoverLink);
       	//Step#86
       	clickButton(Locaters.clickMe);
       	//Step#87
       	clickButton(Locaters.clickMe);
       	//Step#88
       	clickButton(Locaters.clickMe);
       	//Step#89
        verifyText(Locaters.linkedClickedTimes, "3", false);
        
        
        
        
        //Step#90
       	isButtonVisible(Locaters.nonBreakingSpaceLink, false);
       	//Step#91
       	clickButton(Locaters.nonBreakingSpaceLink);
       	//Step#92
       	isButtonVisible(Locaters.myButton, false);
        //Step#93
        navigateToBackPage();
        
        
        //Step#94
       	isButtonVisible(Locaters.overlappedButton, false);
       	//Step#95
       	clickButton(Locaters.overlappedButton);
       	//Step#96
       	enterText(Locaters.inputId, "12345");
    	//Step#97
       	dynamicScroll(Locaters.inputName);
    	//Step#98
       	enterText(Locaters.inputName, "testing");
        //Step#99
        navigateToBackPage();
        
       }

	@Test(priority = 2)
	public void loginTest1() {
		logger=extent.createTest("Login to Aquila");
		//Step#1
		initializeBrowser(TestData.browser,TestData.url);
		//Step#2
		enterText(Locaters.userName, TestData.userName);
		//Step#3
	    enterText(Locaters.password, TestData.password);
	    //Step#4
	    clickButton(Locaters.loginBtn);
	    //Step#5
	    verifyLogo(Locaters.userName, false);
	}


}
