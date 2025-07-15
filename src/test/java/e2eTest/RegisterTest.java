package e2eTest;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import testComponent.BaseTest;

public class RegisterTest extends BaseTest {

	String[] validEmails = { "abc-d@mail.com", "abc.def@mail.com", "abc@mail.com", "abc_def@mail.com" };

	String[] invalidEmails = { "abc..def@mail.com", "abc-@mail.com", ".abc@mail.com", "abc#def@mail.com" };

//	@Test(description = "Verify that user able to enter the phone number in the phone number field")
//	public void TR_31() throws InterruptedException {
//		SoftAssert softAssert = new SoftAssert();
//		try {
//			for (int i = 0; i < landingpage.expectedCountryCode.length; i++) {
//				landingpage.enterPhoneNumberField.sendKeys(landingpage.phoneNumbers[i]);
//				softAssert.assertTrue(landingpage.enterPhoneNumberField.getAttribute("value")
//						.endsWith(common.getLastDigit(landingpage.phoneNumbers[i])));
//				landingpage.resetEnterPhoneNumberField();
//			}
//		} catch (AssertionError e) {
//			// TODO: handle exception
//			System.err.println("Assertion failed: " + e.getMessage());
//			System.out.println("Mobile Number123 : " + landingpage.enterPhoneNumberField.getAttribute("value"));
//			throw e;
//		} finally {
//			softAssert.assertAll();
//		}
//	}

	@Test(description = "Verify that max number limit should be 20.")
	public void TR_32() {
		String longNumber = "12345678901234567890123234234234";
		landingpage.enterPhoneNumberField.sendKeys(longNumber);
		String actualNumber = landingpage.enterPhoneNumberField.getAttribute("value");
		Assert.assertTrue(actualNumber.length() <= 21, "Phone number field did not enforce the maximum length");
	}

	@Test(description = "Make sure that locating popup should be open if user click on the locate button after the adding valid phone number.")
	public void TR_33() throws InterruptedException {
		landingpage.enterMobileNumber("1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
		WebElement locatingPopup = driver.findElement(By.xpath("//div[@role='dialog']"));
		Assert.assertTrue(locatingPopup.isDisplayed(), "locating popup does not arrvies");
	}

	@Test(description = "Verify that phone number should be visible with the country code on the locating popup")
	public void TR_34() throws InterruptedException {
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage
				.waitForWebElementToAppear(driver.findElement(By.xpath("//p[@class='checkout-payment-inputs']")));
		WebElement numberLocatingPopupElement = driver
				.findElement(By.xpath("//p[@class='checkout-payment-inputs']"));
		String numberOnLocatingPopup = numberLocatingPopupElement.getText().trim().replaceAll(" ", "");
		Assert.assertEquals(numberOnLocatingPopup, "+911234567890");
	}

	@Test(description = "Verify that 'Create Account' title should be visible on the locating popup.")
	public void TR_35() throws InterruptedException {
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
		Assert.assertEquals(driver.findElement(By.cssSelector(".body-modal-ceate-account")).getText(),
				"Create Account");
	}

	@Test(description = "Verify that 'Continue with Google' and 'Continue with Facebook' button is visible.")
	public void TR_36() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);

		softAssert.assertEquals(locatingPopup.continueWithGoogle.getText(), "Continue with Google");
		softAssert.assertEquals(locatingPopup.continueWithFb.getText(), "Continue with Facebook");
		softAssert.assertTrue(locatingPopup.continueWithGoogle.isEnabled(),
				"Continue with Google button is not visible");
		softAssert.assertTrue(locatingPopup.continueWithFb.isEnabled(), "Continue with Facebook button is not visible");
		softAssert.assertAll();
	}

	@Test(description = "Verify that signup with email option should be visible with the Continue button.")
	public void TR_37() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
		Thread.sleep(2000);
		softAssert.assertTrue(landingpage.continueBtn.isEnabled(), "Continue Button is not visible");
		softAssert.assertEquals(landingpage.continueBtn.getText(), "Continue");
		softAssert.assertTrue(landingpage.emailFieldRegister.isEnabled(), "Email field is not visible");
		softAssert.assertAll();
	}

	@Test(description = "Verify that 'terms and conditions' link is visible and accessible on the locating popup.")
	public void TR_38() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
		softAssert.assertEquals(locatingPopup.termsConditionSingupPopup.getText(), "Terms and Conditions");
		locatingPopup.termsConditionSingupPopup.click();

		Set<String> g = driver.getWindowHandles();
		Iterator<String> s = g.iterator();

		String traceloSite = s.next();
		String termsConditionPage = s.next();
		driver.switchTo().window(termsConditionPage);

		Thread.sleep(5000);
		softAssert.assertEquals(driver.getCurrentUrl(), "https://location-tool.com/en/terms");
		softAssert.assertAll();
	}

	@Test(description = "Verify that invalid emails should not work.")
	public void TR_39() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		landingpage.enterMobileNumber("+91 1234567890");

		// Use an enhanced for loop for readability
		for (String invalidEmail : invalidEmails) {
//			driver.get("https://stage.location-tool.com/en");
			// Wait until the email field is visible
			wait.until(ExpectedConditions.visibilityOf(landingpage.emailFieldRegister));

			// Clear any pre-filled content before entering invalid email
			landingpage.emailFieldRegister.clear();
			landingpage.emailFieldRegister.sendKeys(invalidEmail);

			// Click the continue button
			landingpage.continueBtn.click();

			// Wait for error message to appear (replace with relevant locator)
			try {
				wait.until(ExpectedConditions.visibilityOf(locatingPopup.invalidEmailErrorMessage));

				// Assert if the error message is displayed
				softAssert.assertTrue(locatingPopup.invalidEmailErrorMessage.isDisplayed(),
						"Expected error message for invalid email: " + invalidEmail + " but it was not found.");
			} catch (TimeoutException e) {
				softAssert.fail("No error message appeared for invalid email: " + invalidEmail);
			}
		}
		softAssert.assertAll();
	}

	@Test(description = "Verify that valid emails should works.")
	public void TR_40() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		for (int i = 0; i < validEmails.length; i++) {
			try {
				driver.get("https://stage.location-tool.com/en/sign-up?D=91&n=1234567890");
				landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
				landingpage.emailFieldRegister.sendKeys(validEmails[i]);
				Thread.sleep(2000);
				landingpage.continueBtn.click();
				Thread.sleep(5000);
				landingpage.waitForWebElementToAppear(landingpage.spreedlyCardSubmitBtn);
				softAssert.assertTrue(landingpage.spreedlyCardSubmitBtn.isDisplayed(),
						"Expected successful login, but it failed.");
			} catch (AssertionError e) {
				softAssert.fail("Focus is not on the 'enterPhoneNumberField' after clicking " + validEmails[i]);
			}
		}
		softAssert.assertAll();
	}

	@Test(description = "Verify that Checkout popup should be arrives when i click on continue after entering valid email")
	public void TR_41() throws InterruptedException {
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
		landingpage.emailFieldRegister.sendKeys(common.randomEmail());
		landingpage.continueBtn.click();
		Thread.sleep(2000);
		Assert.assertTrue(landingpage.spreedlyCardSubmitBtn.isDisplayed(), "Checkout popup is not visible");
	}

	@Test(description = "Verify that amount of payment should be visible on the checkout popup.")
	public void TR_42() throws InterruptedException {
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
		landingpage.emailFieldRegister.sendKeys("auto12607@gmail.com");
		landingpage.continueBtn.click();
		Thread.sleep(2000);
		String actualTrialFee = landingpage.actualTrialAmountElement.getText();
		Assert.assertEquals(actualTrialFee, "₹99.00");
	}

	@Test(description = "Verify that user should get error if they click on the Submit after entering the invalid payment details.")
	public void TR_43() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.enterMobileNumber("+91 1234567890");
		landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
		landingpage.emailFieldRegister.sendKeys("b2auto2607@gmail.com");
		landingpage.continueBtn.click();
		Thread.sleep(2000);

		landingpage.stripeSubmit("5555555355554444", "223", "");

		driver.switchTo().defaultContent();

		softAssert.assertEquals(landingpage.spreedlyCardNumberError.getText(), "Your card number is invalid.");
		softAssert.assertEquals(landingpage.spreedlyCardExpireDateError.getText(),
				"Your card's expiration year is in the past.");
//		softAssert.assertEquals(landingpage.spreedlyCardCvvError.getText(), "Your card's security code is incomplete.");
		softAssert.assertAll();
	}

	@Test(description = "Verify that billing info popup should arrives after submit the valid payment details.")
	public void TR_44() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			softAssert.assertEquals(landingpage.billingInfoElement.getText(), "Billing Info");
			
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			dashboard.popupCloseBtn.click();
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that billing info should not blank.")
	public void TR_45() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			Thread.sleep(3000);
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");
			dashboard.billingInfoSubmit("", "", "", "", "");

			WebElement firstNameErrorElement = driver
					.findElement(By.xpath("//input[@name='first_name']/following-sibling::span"));
			softAssert.assertTrue(firstNameErrorElement.isDisplayed());
			Thread.sleep(3000);

		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			dashboard.popupCloseBtn.click();
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Make sure that blank space should not allowed in the billing info.")
	public void TR_46() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			Thread.sleep(3000);
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit(" ", " ", "  ", " ", " ");

			WebElement firstNameErrorElement = driver
					.findElement(By.xpath("//input[@name='first_name']/following-sibling::span"));
			softAssert.assertTrue(firstNameErrorElement.isDisplayed());
			Thread.sleep(3000);
		}
		catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			dashboard.popupCloseBtn.click();
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that after saving the billing info with the save details user will redirected to the locating popup and get the success message \"You have updated settings successfully\"")
	public void TR_47() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			Thread.sleep(3000);
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

			WebElement billingInfoSuccessMessageElement = driver
					.findElement(By.xpath("//div[@class='ant-notification-notice-message']"));

			softAssert.assertTrue(locatingPopup.countryCodeDropdown.isDisplayed(), "Locating Popup is not arrives");
			softAssert.assertTrue(billingInfoSuccessMessageElement.isDisplayed(),
					"You have updated settings successfully message is not arrives");
			Thread.sleep(3000);

		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(3000);
			dashboard.popupCloseBtn.click();
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that you got the locating popup where you have three fields :Phone number field, Message, Continue button")
	public void TR_48() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

			softAssert.assertTrue(locatingPopup.phoneNumberField.isDisplayed(),
					"Phone Number field is not visible in the Locating Popup");
			softAssert.assertTrue(locatingPopup.descriptionField.isDisplayed(),
					"Description field is not visible in the Locating Popup");
			softAssert.assertTrue(locatingPopup.submitBtn.isDisplayed(),
					"Submit Button is not visible in the Locating Popup");
			Thread.sleep(3000);
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(3000);
			dashboard.popupCloseBtn.click();
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that you cannot continue with the blank information in the locating popup.")
	public void TR_49() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

			locatingPopup.phoneNumberField.sendKeys(" ");
			locatingPopup.descriptionField.sendKeys(" ");
			locatingPopup.submitBtn.click();

			Thread.sleep(2000);
			WebElement blankNumberFieldErrorElement = driver
					.findElement(By.xpath("//input[@inputmode='tel']/following-sibling::p[1]"));

			softAssert.assertTrue(blankNumberFieldErrorElement.isDisplayed(),
					"Phone error message is not displayed on Locating Popup");
			Thread.sleep(3000);

		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(3000);
			dashboard.popupCloseBtn.click();
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that when you click on the 'Cancel' button then locating popup should close.")
	public void TR_50() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");
			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			dashboard.popupCloseBtn.click();

			softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/dash");
			Thread.sleep(3000);
			
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that same number should be visible on the locating which we have added while signup.")
	public void TR_51() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

			WebElement phoneNumberLocatingPopupElement = driver
					.findElement(By.xpath("//div[@class='login-modal-title']/p"));
			String phoneNumberLocatingPopup = phoneNumberLocatingPopupElement.getText().trim().replaceAll(" ", "");

			System.out.println(phoneNumberLocatingPopup);
			softAssert.assertEquals(phoneNumberLocatingPopup, "+911234567890");
			Thread.sleep(3000);

		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(3000);
			dashboard.popupCloseBtn.click();
			Thread.sleep(5000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that 'Location request sent successfully!' message arrives if you click Continue after entering the valid details")
	public void TR_52() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

			locatingPopup.countryCodeDropdown.click();
			Thread.sleep(2000);
			WebElement indiaFlag = driver.findElement(By.xpath("(//span[text()='India'])[2]"));
			indiaFlag.click();
			Thread.sleep(2000);

			locatingPopup.phoneNumberField.sendKeys("1234567890");
			locatingPopup.descriptionField.sendKeys("Hello, one of your loved ones is looking for you.");
			locatingPopup.submitBtn.click();

			Thread.sleep(3000);
			WebElement successMessageElement = driver
					.findElement(By.xpath("//div[@class='ant-notification-notice-message']"));
			String successMessage = successMessageElement.getText();

			System.out.println(successMessage);
			softAssert.assertEquals(successMessage, "Location request sent successfully!");
			Thread.sleep(3000);

		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(2000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that same number should be visible in the history listing which we have added while signup.")
	public void TR_53() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

			locatingPopup.countryCodeDropdown.click();
			Thread.sleep(4000);
			WebElement indiaFlag = driver.findElement(By.xpath("(//span[text()='India'])[2]"));
			indiaFlag.click();

			locatingPopup.phoneNumberField.sendKeys("1234567890");
			locatingPopup.descriptionField.sendKeys("Hello, one of your loved ones is looking for you.");
			locatingPopup.submitBtn.click();
			Thread.sleep(3000);

			List<WebElement> historyListingElements = driver
					.findElements(By.xpath("(//div[@class='items-scrollable']//span)[1]"));
			for (WebElement element : historyListingElements) {
				String elementText = element.getText(); // Get the text of the current element

				if (elementText.contains("+911234567890")) { // Check if the text contains the target number
					System.out.println("Found the number: " + "+911234567890");
					softAssert.assertTrue(elementText.contains("+911234567890"));
					break; // Exit the loop once the number is found
				}
			}
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		}
		softAssert.assertAll();
	}
	
<<<<<<< HEAD
	//Reveiw Test
	@Test(description = "Verify that review modal opens, rating can be selected, and submission confirmation appears correctly.")
	public void TR_54_verifyReviewFlow() throws InterruptedException {
		    SoftAssert softAssert = new SoftAssert();
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailFieldRegister);
			landingpage.emailFieldRegister.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

			locatingPopup.countryCodeDropdown.click();
			Thread.sleep(4000);
			WebElement indiaFlag = driver.findElement(By.xpath("(//span[text()='India'])[2]"));
			indiaFlag.click();

		    // Test data
		    String modalTitle = "Thanks for joining us!";
		    String modalDescription = "We’d truly appreciate it if you could leave us a review. Your feedback is incredibly valuable to us.";
		    int rating = 3;

		    String submittedTitle = "Review submitted!";
		    String submittedDescription = "Thanks for taking the time to share your feedback!";

	    // Step 1: Open the modal (assume action is required before this test or included in preconditions)
	    // e.g., click a button to open the modal if needed
	    // reviewButton.click(); // ← Add if modal is not already open

		    // Step 2: Verify review modal and set rating
		    landingpage.verifyReviewModalAndSetRating(modalTitle, modalDescription, rating);
	
		    // Step 3: Verify the success screen and close it
		    landingpage.verifyAndCloseReviewSubmitted(submittedTitle, submittedDescription);
		}
=======
	
>>>>>>> 9796fad3ee3d2f0a59d617a82705fc60559a8b3f
}
