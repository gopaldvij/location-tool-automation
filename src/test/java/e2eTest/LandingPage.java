package e2eTest;

import java.awt.AWTException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import testComponent.BaseTest;

public class LandingPage extends BaseTest {

	@Test(description = "Verify that user should navigate to the Tracelo Landingpage when you open the 'https://stage.location-tool.com/'")
	public void TR_01() {
		Assert.assertEquals(driver.getTitle(), "LocationTool");
	}

	@Test(description = "Verify the Tracelo logo should available on the top of the left side.")
	public void TR_02() {
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(landingpage.logo.isDisplayed());
		softAssert.assertEquals(landingpage.logo.getAttribute("src"),
				"https://stage.location-tool.com/static/media/mainLogo.a871e2d4315b6695b99125b58e48e991.svg");
		softAssert.assertAll();
	}

	@Test(description = "Verify that FAQ, Pricing, Contact, Unsubscribe, Languages and Login button should visible on the right side of the header.", groups = {
			"smoke", "regression" })
	public void TR_03() {
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(landingpage.FAQ.getText(), "F.A.Q");
		softAssert.assertEquals(landingpage.Pricing.getText(), "Pricing");
		softAssert.assertEquals(landingpage.Contact.getText(), "Contact");
		softAssert.assertEquals(
				driver.findElement(By.xpath("//div[starts-with(@class, 'header-lang-drop')]//img")).getAttribute("src"),
				"https://cdn.jsdelivr.net/gh/lipis/flag-icons/flags/4x3/gb.svg");

		softAssert.assertEquals(landingpage.loginBtn.getText(), "Login");
		softAssert.assertAll();
	}

	@Test(description = "Verify that FAQ, Pricing, Contact, Unsubscribe, Languages and Login button should accessible on the right side of the header.", groups = {
			"smoke", "regression" })
	public void TR_04() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.FAQ.click();
		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/faq");
		landingpage.waitForWebElementToAppear(landingpage.Pricing);

		landingpage.Pricing.click();
		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/pricing");
		landingpage.waitForWebElementToAppear(landingpage.Contact);

		landingpage.Contact.click();
		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/contact");
		landingpage.waitForWebElementToAppear(landingpage.Unsubscribe);

		landingpage.Unsubscribe.click();
		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/unsubscribe");
		landingpage.waitForWebElementToAppear(landingpage.langDropdown);

		landingpage.langDropdown.click();
		landingpage.waitForWebElementToAppear(landingpage.langListCloseBtn);
		softAssert.assertEquals(landingpage.langListTitle.getText(), "Language");
		landingpage.langListCloseBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.loginBtn);

		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.loginPopup);
		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/login");
		softAssert.assertAll();
	}

	@Test(description = "Verify that all the languages should be visible in the langagues list.", groups = { "smoke",
			"regression" })
	public void TR_05() {
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(landingpage.langDropdown.isDisplayed());
		softAssert.assertEquals(Arrays.toString(landingpage.totalLanguages()),
				Arrays.toString(landingpage.expectedLanguages));
		softAssert.assertAll();
	}

	@Test(description = "Make sure that insert phone number field should be visible.", groups = { "smoke",
			"regression" })
	public void TR_06() {
		Assert.assertTrue(landingpage.enterPhoneNumberField.isDisplayed());
	}

	@Test(description = "Verify that 'Enter a phone number' placeholder should be visible in the phone number field.", groups = {
			"smoke", "regression" })
	public void TR_07() {
		Assert.assertEquals(landingpage.enterPhoneNumberField.getAttribute("placeholder"), "Enter a phone number");
	}

	@Test(description = "Verify that country name should visible in phone number field.", groups = { "smoke",
			"regression" })
	public void TR_08() throws InterruptedException {
		Assert.assertEquals(landingpage.actualCountryName(), common.currentLocation());
	}

	@Test(description = "Verify that Locate button should be visible besides of phone number field.", groups = {
			"smoke", "regression" })
	public void TR_09() {
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(landingpage.locateBtn.getText(), "Locate");
		softAssert.assertTrue(landingpage.locateBtn.isEnabled(), "Locate button is not enabled");
		softAssert.assertAll();
	}

	@Test(description = "Verify that user should able to enter the mobile number.", groups = { "smoke", "regression" })
	public void TR_10() {
		landingpage.enterPhoneNumberField.sendKeys("1234567890");
		Assert.assertEquals(landingpage.enterPhoneNumberField.getAttribute("value").replaceAll("\\s", ""),
				"1234567890");
	}

	@Test(description = "Verify that error should arrives when i click on the locate button without entering a number.", groups = {
			"smoke", "regression" })
	public void TR_11() {
		SoftAssert softAssert = new SoftAssert();
		landingpage.locateBtn.click();
		softAssert.assertEquals(landingpage.invalidNumberError.getText(), "Please Enter Number");
		softAssert.assertTrue(landingpage.invalidNumberError.isDisplayed());
		softAssert.assertAll();
	}

	@Test(description = "Make sure that country code should be changed according to the country.", groups = { "noSetup",
			"smoke", "regression" })
	public void TR_12() {
		String[] countryCodesString = landingpage.totalCountryCodes();
		Assert.assertEquals(Arrays.toString(countryCodesString), Arrays.toString(landingpage.expectedCountryCode));
	}

	@Test(description = "Verify that country code and flag should changed if i paste the mobile number.", groups = {
			"smoke", "regression" })
	public void TR_14() throws AWTException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			for (int i = 0; i < landingpage.expectedCountryCode.length; i++) {
				landingpage.enterPhoneNumberField.sendKeys(landingpage.phoneNumbers[i]);
				softAssert.assertEquals(landingpage.actualCountryCode(), landingpage.expectedCountryCode[i]);
				landingpage.resetEnterPhoneNumberField();
			}

		} catch (AssertionError e) {
			// TODO: handle exception
			System.err.println("Assertion failed: " + e.getMessage());
			System.out.println("Mobile Number123 : " + landingpage.enterPhoneNumberField.getAttribute("value"));
			throw e;

		} finally {
			softAssert.assertAll(); // Aggregate all assertion results
		}
	}

	@Test(description = "Make sure that alphabets should not allow in the phone numer field.", groups = { "smoke",
			"regression" })
	public void TR_15() {
		landingpage.enterPhoneNumberField.sendKeys("abcdefghijklmnopqrstuvwxyz");
		Assert.assertEquals(landingpage.enterPhoneNumberField.getText(), "");
	}

	@Test(description = "Make sure that special characters should not allow in the phone number filed.", groups = {
			"smoke", "regression" })
	public void TR_16() {
		landingpage.enterPhoneNumberField.sendKeys("!~`@#$%^&*()_-={}[];:''<>?/");
		Assert.assertEquals(landingpage.enterPhoneNumberField.getText(), "");
	}

	@Test(description = "Verify that 3 subscription options should be visible in the landing page.", groups = { "smoke",
			"regression" })
	public void TR_17() throws IOException, InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		Thread.sleep(5000);
		String specialOfferText = "Special offer!\n"
				+ "Test out LocationTool's capabilities for just ₹99.00 for a full day!\n"
				+ "LocationTool offers its services through its easy-to-manage monthly subscription.";

		String trialPeriodText = "Trial period\n" + "₹99.00\n" + "24 Hour Trial\n"
				+ "LocationTool operates on a monthly subscription basis. Your initial payment grants unrestricted access "
				+ "to all platform services for 24 hours. After that period, the subscription fee of ₹2495 per month applies.\n"
				+ "Start Trial";

		String subscriptionText = "Subscription\n" + "₹2495\n" + "Monthly fee\n"
				+ "Once the 24 hour trial period has ended, the offer will automatically renew to a monthly subscription. "
				+ "Remember that you can cancel the subscription whenever you want.\n" + "Subscribe Now";

		softAssert.assertTrue(landingpage.pricingBoxes.isDisplayed());
		softAssert.assertEquals(landingpage.specialOfferBox.getText(), specialOfferText);
		softAssert.assertEquals(landingpage.trialPeriodBox.getText(), trialPeriodText);
		softAssert.assertEquals(landingpage.subscriptionBox.getText(), subscriptionText);
		softAssert.assertAll();
	}

	@Test(description = "Make sure that 'Start Trial' and 'Subscribe Now' button should accessible.", groups = {
			"smoke", "regression" })
	public void TR_18() {
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(landingpage.startTrialBtn.isEnabled());
		softAssert.assertTrue(landingpage.subscribeNowBtn.isEnabled());

		WebElement[] pricingBtn = { landingpage.startTrialBtn, landingpage.subscribeNowBtn };

		for (int i = 0; i < pricingBtn.length; i++) {
			pricingBtn[i].click();
			WebElement activeElement = driver.switchTo().activeElement();

			if (activeElement.equals(landingpage.enterPhoneNumberField)) {
				softAssert.assertTrue(true, "Focus is correctly on the 'enterPhoneNumberField'.");
			} else {
				softAssert
						.fail("Focus is not on the 'enterPhoneNumberField' after clicking " + pricingBtn[i].getText());
			}
		}
		softAssert.assertAll();
	}

	@Test(description = "Verify that 'How Does It Work?' section should be visible in the landingpage", groups = {
			"smoke", "regression" })
	public void TR_19() {
		SoftAssert softAssert = new SoftAssert();
		String howDoesItWorkTitle = "How Does It Work?";
		softAssert.assertEquals(howDoesItWorkTitle, landingpage.howDoesWorkTitle.getText());
		softAssert.assertTrue(landingpage.howDoesWorkSections.isDisplayed());
		softAssert.assertAll();
	}

	@Test(description = "Make sure that 'How Does It Work' have a three cards.", groups = { "smoke", "regression" })
	public void TR_20() {
		String howDoesWorkSections = "01\n"+"Enter a Phone Number\n"
				+ "Enter the phone number you want to locate and customize the SMS message you want the recipient to receive.\n"
				+"02\n"+ "Send Tracking SMS\n"
				+ "LocationTool will send the recipient your custom SMS that will request the phones location.\n"
				+"03\n"+ "Receive Precise Location\n"
				+ "Get notified as soon as the recipient's consent to share their location. View the exact position on the map in your dashboard.";

		Assert.assertEquals(landingpage.howDoesWorkSections.getText(), howDoesWorkSections);		
	}

	@Test(description = "Verify that 'Why choose Tracelo' section should be visible in the landingpage", groups = {
			"smoke", "regression" })
	public void TR_21() {
		Assert.assertTrue(landingpage.chooseTraceloSections.isDisplayed());
	}

	@Test(description = "Make sure that content of the 'Why choose Tracelo' should be visible.", groups = { "smoke",
			"regression" })
	public void TR_22() {
		String whyChooseTraceloText = "Why choose LocationTool?\n"
				+ "One click and zero effort to find anyone's location across the globe\n" + "Discreet Service\n"
				+ "While the phone's owner will be informed of a location request, the requester's identity remains confidential. "
				+ "All geolocation requests are made with respect to privacy, and transmitted data is encrypted for security.\n"
				+ "No installation required\n"
				+ "There's no software to install on your phone or the targeted device. Everything operates remotely! "
				+ "Just input the phone number you wish to track. As soon as the SMS recipient provides consent to share their location, "
				+ "you'll have immediate access to it.\n" + "Worldwide coverage\n"
				+ "LocationTool works with all networks worldwide. However, successful tracking requires the phone to be on, "
				+ "service availability, and the SMS recipient's explicit consent to share their location.\n"
				+ "iOS and Android compatible\n"
				+ "LocationTool seamlessly supports both iOS and Android mobile devices. As long as you have the phone number "
				+ "and the individual's explicit approval to track, you can locate them using our service.\n"
				+ "Locate Phone";
		

		Assert.assertEquals(landingpage.chooseTraceloSections.getText(), whyChooseTraceloText);
	}

	@Test(description = "Verify that 'Locate Phone' button should be visible below the 'Why choose Tracelo Content.'", groups = {
			"smoke", "regression" })
	public void TR_23() {
		int whyChooseTraceloYPosition = landingpage.chooseTraceloSections.getLocation().getY();
		int locatePhoneButtonYPosition = landingpage.locatePhoneBtn.getLocation().getY();
		Assert.assertTrue(locatePhoneButtonYPosition > whyChooseTraceloYPosition,
				"'Locate Phone' button is not positioned below the 'Why choose Tracelo' content.");
	}

	@Test(description = "Make sure that Page should scroll to top if user click on the 'Locate Phone'.", groups = {
			"smoke", "regression" })
	public void TR_24() throws InterruptedException {
		// Scroll down the page to simulate the user has scrolled down
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", landingpage.locatePhoneBtn);
		landingpage.locatePhoneBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.enterPhoneNumberField);
		Thread.sleep(3000);

		// Use JavaScriptExecutor to get the current scroll position
		Number scrollPositionY = (Number) js.executeScript("return window.scrollY;");

		Thread.sleep(3000);
		// Assert that the scroll position is at the top (0)
		Assert.assertEquals(scrollPositionY.doubleValue(), 0.0, "did not scroll the page to the top.");
	}

	@Test(description = "Verify that FAQ section should be visible in the landing page.", groups = { "smoke",
			"regression" })
	public void TR_25() {
		SoftAssert softAssert = new SoftAssert();
		String expecteedFaqText = "Frequently Asked Questions\n"
				+ "Here you can find all the answers to the questions we are most often asked about our mobile geolocation service, LocationTool.";
		softAssert.assertTrue(landingpage.faqSection.isDisplayed());
		softAssert.assertEquals(landingpage.faqTitle.getText(), expecteedFaqText);
		softAssert.assertAll();
	}

	@Test(description = "Make sure that FAQ listing should be visible.", groups = { "smoke", "regression" })
	public void TR_26() {
		SoftAssert softAssert = new SoftAssert();
		String[] expectedFaqs = { "What is LocationTool?",
				"How to track a phone number without physical access to the target device?",
				"Is it even legal to use the LocationTool phone number tracker?", "What networks does LocationTool support?",
				"Can I track the location of the phone number if I don’t know its model?",
				"Does LocationTool support both iOS and Android operated mobile devices?",
				"How fast can LocationTool find the location of a phone number?",
				"Can I track someone by cell phone number?", "Can I track someone by cell phone number for free?",
				"I want to pay for tracking a phone number, but my payment is declined. What now?" };

		String[] faqList = new String[landingpage.faqList.size()];

		for (int i = 0; i < landingpage.faqList.size(); i++) {
			landingpage.waitForAllWebElementElementToAppear(landingpage.faqList);
			faqList[i] = landingpage.faqList.get(i).getText();
		}
		softAssert.assertEquals(landingpage.faqList.size(), 10);
		System.out.println("faq List : "+faqList);
		softAssert.assertEquals(Arrays.toString(faqList), Arrays.toString(expectedFaqs));
		softAssert.assertAll();
	}

	@Test(description = "Verify that user should able to open the FAQs.", groups = { "smoke", "regression" })
	public void TR_27() throws InterruptedException {
		// Create a list to store the paragraphs
		List<String> expectedFaqParagraph = new ArrayList<>();

		// Add paragraphs to the list
		expectedFaqParagraph.add(
				"LocationTool offers a service that enables you to determine a person's location using their phone number. Whether you are seeking to locate a family member or any individual, our service can assist you, all while ensuring ethical practices.");
		expectedFaqParagraph.add(
				"LocationTool operates ethically, respecting privacy and consent. It does not require physical access to the target phone. To track another person's location, all you need is their phone number. LocationTool functions entirely online and remotely, eliminating the need for app installations on your device or the targeted mobile device. You won't have to install any apps; simply access LocationTool via a web browser, enter the phone number you wish to locate, and ensure you have the individual's consent to use our service.");
		expectedFaqParagraph.add(
				"Your concerns about the location tracker are natural. Our primary focus is ensuring that tracking is conducted responsibly and with the prior agreement of the person being located. To maintain compliance and respect for privacy, we require users to send SMS location requests only to individuals who've given explicit prior consent to be tracked. Rest assured, LocationTool's phone number-based location method is entirely legal and aligned with the Information Commissioner’s Office recommendations.");
		expectedFaqParagraph.add(
				"You don’t need to be a tech-savvy person to track a cell phone location by number. You don’t even need to know what network they use, as LocationTool works on any network, meaning it can locate anyone anywhere.");
		expectedFaqParagraph.add(
				"Yes, LocationTool allows you to track a phone using only their phone number, without any need to know their phone model. However, it's important to note that this tracking is contingent upon obtaining the individual's explicit consent to be tracked, ensuring ethical use. LocationTool operates online, accessible through any web browser. Once you've created an account and input their phone number, you can access their precise location, provided that the SMS recipient agrees to share their location.");
		expectedFaqParagraph.add(
				"LocationTool works on all major OS and phone brands such as Android, iOS, Windows phone and Blackberry. And is supported by all carrier companies around the globe.");
		expectedFaqParagraph.add(
				"After entering their phone number, please allow approximately 1 minute for LocationTool to initiate location tracking by phone number. This is the typical timeframe for the location request message to reach their phone. You will receive an instant notification once the third party has granted consent for their geo-location to be located, ensuring an ethical and consent-based process.");
		expectedFaqParagraph.add(
				"To geolocate a phone, please ensure that you provide us with the phone number associated with the mobile device for which you have obtained prior consent to track. The device you are seeking will then receive a custom SMS message, courteously requesting it to transmit its position. LocationTool will subsequently send you its location by SMS, maintaining a process that strictly adheres to consent-based tracking practices.");
		expectedFaqParagraph.add(
				"Since LocationTool is an advanced solution for cell phone tracking by number, its services are paid. LocationTool works on a monthly basis. With a first payment of just ₹99.00 you will have unlimited access to all the platform's services for 24 hours. After this period, the subscription fee of ₹2495 per month applies. Please note that your subscription automatically renews every month. If you no longer wish to use LocationTool, you can cancel your active subscription at any time from the website.");
		expectedFaqParagraph.add(
				"Your payment might have been declined because there are insufficient funds on your credit card. There also might have been some problems with your transaction. We recommend that you contact your bank and ask for help. As an option, you can write to us at support@location-tool.com and provide us with the details.");

		List<String> faqParagraph = new ArrayList<>();
		for (int i = 0; i < landingpage.faqList.size(); i++) {
			landingpage.collapseBtn.get(i).click();
			Thread.sleep(1000);
			faqParagraph.add(landingpage.faqParagraph.getText());
		}
		Assert.assertEquals(expectedFaqParagraph, faqParagraph);
	}

	@Test(description = "Verify that all the bottom bar options should be visible.", groups = { "smoke", "regression" })
	public void TR_28() {
		SoftAssert softAssert = new SoftAssert();
		WebElement[] elementsToCheckDisplayed = { landingpage.footerLogo, landingpage.footerDescription,
				landingpage.footerTraceloText, landingpage.footerLegalInfoTitle, landingpage.footerAccountTitle,
				landingpage.footerPricing, landingpage.footerContact, landingpage.footerFaq, landingpage.footerLogin,
				landingpage.footerSignup, landingpage.footerUnsubscribe, landingpage.footerCookiesPolicy,
				landingpage.footerPrivacyPolicy, landingpage.footerTermsPolicy };

		for (WebElement element : elementsToCheckDisplayed) {
			softAssert.assertTrue(element.isDisplayed(), "Element not displayed: " + element);
		}

		// Assert text or attribute values
		Map<String, String> elementsToCheckText = new HashMap<>();
		elementsToCheckText.put(landingpage.footerLogo.getAttribute("src"),
				"https://stage.location-tool.com/static/media/mainLogo.a871e2d4315b6695b99125b58e48e991.svg");
		elementsToCheckText.put(landingpage.footerDescription.getText(),
				"LocationTool let's you get a mobile phone's geo location and works on all phone types, networks and countries.");
		elementsToCheckText.put(landingpage.footerTraceloText.getText(), "LocationTool - 2024");
		elementsToCheckText.put(landingpage.footerLegalInfoTitle.getText(), "Legal info");
		elementsToCheckText.put(landingpage.footerAccountTitle.getText(), "Account");
		elementsToCheckText.put(landingpage.footerPricing.getText(), "Pricing");
		elementsToCheckText.put(landingpage.footerContact.getText(), "Contact");
		elementsToCheckText.put(landingpage.footerFaq.getText(), "F.A.Q");
		elementsToCheckText.put(landingpage.footerLogin.getText(), "Login");
		elementsToCheckText.put(landingpage.footerSignup.getText(), "Sign up");
		elementsToCheckText.put(landingpage.footerUnsubscribe.getText(), "Unsubscribe");
		elementsToCheckText.put(landingpage.footerCookiesPolicy.getText(), "Cookies Policy");
		elementsToCheckText.put(landingpage.footerPrivacyPolicy.getText(), "Privacy Policy");
		elementsToCheckText.put(landingpage.footerTermsPolicy.getText(), "Terms and Conditions");

		// Iterate through the entries (key-value pairs) in the elementsToCheckText map
		for (Map.Entry<String, String> entry : elementsToCheckText.entrySet()) {
			softAssert.assertEquals(entry.getKey(), entry.getValue(),
					"Text/Attribute mismatch for element with expected value: " + entry.getValue());
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that all the bottom bar options should be accessible.", groups = { "smoke",
			"regression" })
	public void TR_29() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		Map<WebElement, String> map = new HashMap<>();
		map.put(landingpage.FAQ, "FAQ");
		map.put(landingpage.Pricing, "Pricing");
		map.put(landingpage.Contact, "Contact");
		map.put(landingpage.Unsubscribe, "Unsubscribe");
		map.put(landingpage.headerLogo, "Landing Page");

		String expectedFooterLogoUrl = "https://stage.location-tool.com/en/";

		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			landingpage.footerLogo.click();
			String errorMessage = String.format("Footer logo click event is not working in the %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterLogoUrl, errorMessage);
			softAssert.assertTrue(landingpage.enterPhoneNumberField.isDisplayed(), errorMessage);
		}

		String expectedFooterDescription = "LocationTool let's you get a mobile phone's geo location and works on all phone types, networks and countries.";
		softAssert.assertEquals(landingpage.footerDescription.getText(), expectedFooterDescription);
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			String errorMessage = String.format("Footer description text is not visible %s", entry.getValue());
			softAssert.assertEquals(landingpage.footerDescription.getText(), expectedFooterDescription, errorMessage);
		}

		String expectedFooterTraceloText = "LocationTool - 2024";
		softAssert.assertEquals(landingpage.footerTraceloText.getText(), expectedFooterTraceloText);
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			String errorMessage = String.format("LocationTool - 2024 text is not visible in footer %s", entry.getValue());
			softAssert.assertEquals(landingpage.footerTraceloText.getText(), expectedFooterTraceloText, errorMessage);
		}

		String expectedFooterLegalInforTitle = "Legal info";
		softAssert.assertEquals(landingpage.footerLegalInfoTitle.getText(), expectedFooterLegalInforTitle);
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			String errorMessage = String.format("Legal info text is not visible in footer %s", entry.getValue());
			softAssert.assertEquals(landingpage.footerLegalInfoTitle.getText(), expectedFooterLegalInforTitle,
					errorMessage);
		}

		String expectedFooterAccountTitle = "Account";
		softAssert.assertEquals(landingpage.footerAccountTitle.getText(), expectedFooterAccountTitle);
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			String errorMessage = String.format("Account text is not visible in footer %s", entry.getValue());
			softAssert.assertEquals(landingpage.footerAccountTitle.getText(), expectedFooterAccountTitle, errorMessage);
		}

		String expectedFooterPricingUrl = "https://stage.location-tool.com/en/pricing";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerPricing.click();
			String errorMessage = String.format("Pricing button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterPricingUrl, errorMessage);
		}

		String expectedFooterContactUrl = "https://stage.location-tool.com/en/contact";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerContact.click();
			String errorMessage = String.format("Contact button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterContactUrl, errorMessage);
		}

		String expectedFooterFaqUrl = "https://stage.location-tool.com/en/faq";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerFaq.click();
			String errorMessage = String.format("FAQ button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterFaqUrl, errorMessage);
		}

		String expectedFooterLoginUrl = "https://stage.location-tool.com/en/login";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerLogin.click();
			Thread.sleep(2000);
			String errorMessage = String.format("Login button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterLoginUrl, errorMessage);
			landingpage.popupCloseBtn.click();
		}

		String expectedFooterUnsubscribeUrl = "https://stage.location-tool.com/en/unsubscribe";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerUnsubscribe.click();
			String errorMessage = String.format("Unsubscribe button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterUnsubscribeUrl, errorMessage);
		}

		String expectedFooterCookiesUrl = "https://stage.location-tool.com/en/cookie-policy";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerCookiesPolicy.click();
			String errorMessage = String.format("Cookies button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterCookiesUrl, errorMessage);
		}

		String expectedFooterPrivacyUrl = "https://stage.location-tool.com/en/privacy-policy";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerPrivacyPolicy.click();
			String errorMessage = String.format("Privacy button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterPrivacyUrl, errorMessage);
		}

		String expectedFooterTermsUrl = "https://stage.location-tool.com/en/terms";
		for (Map.Entry<WebElement, String> entry : map.entrySet()) {
			entry.getKey().click();
			Thread.sleep(2000);
			landingpage.footerTermsPolicy.click();
			String errorMessage = String.format("Terms button is not working in footer %s", entry.getValue());
			softAssert.assertEquals(driver.getCurrentUrl(), expectedFooterTermsUrl, errorMessage);
		}
		softAssert.assertAll();
	}

	@Test(description = "Verify all the broken link", groups = { "smoke", "regression" })
	public void TR_30() {
		List<WebElement> links = driver.findElements(By.tagName("a"));

		for (WebElement link : links) {
			String url = link.getAttribute("href");

			if (url == null || url.isEmpty()) {
				System.out.println("URL is either not configured for anchor tag or it is empty.");
				continue;
			}
			try {
				HttpURLConnection httpURLConnection = (HttpURLConnection) (new URL(url).openConnection());
				httpURLConnection.setRequestMethod("HEAD");
				httpURLConnection.connect();
				int responseCode = httpURLConnection.getResponseCode();

				if (responseCode >= 400) {
					System.out.println(url + " is a broken link. Response code: " + responseCode);
				} else {
					System.out.println(url + " is a valid link. Response code: " + responseCode);
				}
			} catch (Exception e) {
				System.out.println(url + " is a broken link. Exception: " + e.getMessage());
			}
		}
	}
}
