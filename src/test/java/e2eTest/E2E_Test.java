package e2eTest;

import java.time.Duration;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import testComponent.BaseTest;

public class E2E_Test extends BaseTest {

//	(retryAnalyzer = testComponent.Retry.class)
	@Test
	public void e2e() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Step 1: Open the first URL
		driver.get("https://stage.tracelo.com/en");

		// Step 2: Open a new tab
		driver.switchTo().newWindow(WindowType.TAB);
		driver.get("https://onlinesim.io/free_numbers/russia");

		WebElement mobileNumberElement = driver.findElement(By.xpath("//div[@class='fw-items']//a[1]"));
		wait.until(ExpectedConditions.visibilityOf(mobileNumberElement));
		System.out.println(mobileNumberElement.getText());
		String mobileNumber = mobileNumberElement.getText();

		Assert.assertEquals(mobileNumber, 123);
		Set<String> g = driver.getWindowHandles();
		Iterator<String> s = g.iterator();

		String traceloSite = s.next();
		String virtualNumberSite = s.next();

		driver.switchTo().window(traceloSite);

		Thread.sleep(5000);
		wait.until(ExpectedConditions.visibilityOf(landingpage.flagDropdown));
		landingpage.enterPhoneNumberField.sendKeys(mobileNumber);
		landingpage.locateBtn.click();
		wait.until(ExpectedConditions.visibilityOf(landingpage.emailField));
		landingpage.emailField.sendKeys(common.randomEmail());
		landingpage.continueBtn.click();
		Thread.sleep(2000);
		driver.switchTo().frame(landingpage.iframeStripe);
		Thread.sleep(1000);
		SoftAssert softAssert = new SoftAssert();
		softAssert.assertEquals(landingpage.stripeCardNumberField.getAttribute("placeholder"), "1234 1234 1234 1234");
		landingpage.stripeCardNumberField.sendKeys("5555555555554444");
		landingpage.stripeCardExpireField.sendKeys("227");
		landingpage.stripeCardCvvField.sendKeys("424");
		driver.switchTo().defaultContent();
		landingpage.stripeCardSubmitBtn.click();

		Thread.sleep(5000);
		dashboard.firstNameField.sendKeys("First Name");
		dashboard.lastNameField.sendKeys("Last Name");
		dashboard.addressField.sendKeys("Address");
		dashboard.cityField.sendKeys("City");
		dashboard.zipcodeField.sendKeys("382350");
		dashboard.billingInfoSaveBtn.click();

		Thread.sleep(4000);
		locatingPopup.countryCodeDropdown.click();
		Thread.sleep(4000);
		WebElement indiaFlag = driver.findElement(By.xpath("(//span[text()='India'])[2]"));
		indiaFlag.click();

		Actions action = new Actions(driver);
		Thread.sleep(2000);

		action.click(locatingPopup.phoneNumberField).build().perform();
		Thread.sleep(2000);

		action.sendKeys(Keys.BACK_SPACE).build().perform();
		Thread.sleep(2000);

		locatingPopup.phoneNumberField.sendKeys("9725754645");

		locatingPopup.descriptionField.sendKeys("Hello, one of your loved ones is looking for you.");

		locatingPopup.submitBtn.click();

		Thread.sleep(3000);
		dashboard.countryCodeDropdown.click();

		Thread.sleep(2000);
		driver.findElement(By.xpath("(//span[text()='India'])[1]")).click();

		dashboard.numberWantToLocateField.sendKeys("9725754645");

		driver.findElement(By.xpath("//div[@class='input-suffix']")).click();

		Thread.sleep(3000);
		driver.findElement(By.xpath("(//div[@class='selected-flag'])[2]")).click();

		Thread.sleep(2000);
		driver.findElement(By.xpath("(//span[text()='India'])[2]")).click();

		driver.findElement(By.xpath("(//div[@class='body-dash-modal'])//input[@type='tel']")).sendKeys("9725754645");

		driver.findElement(By.xpath("(//div[@class='body-dash-modal'])//textarea"))
				.sendKeys("Hello, one of your loved ones is looking for you.");

		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	
	@Test
	public void TR_52() throws InterruptedException {
	    String emails[] = { 
	        "case121@yopmail.com", "case101@yopmail.com", "case102@yopmail.com", "case103@yopmail.com", 
	        "case104@yopmail.com", "case105@yopmail.com", "case106@yopmail.com", "case107@yopmail.com", 
	        "case108@yopmail.com", "case109@yopmail.com", "case110@yopmail.com", "case111@yopmail.com", 
	        "case112@yopmail.com", "case113@yopmail.com", "case114@yopmail.com", "case115@yopmail.com", 
	        "case116@yopmail.com", "case117@yopmail.com", "case118@yopmail.com", "case119@yopmail.com", 
	        "case120@yopmail.com"
	    };
	    
	    for (int i = 0; i < emails.length; i++) {
	        try {
	            // Step 1: Enter mobile number
	            landingpage.enterMobileNumber("+91 1234567890");
	            landingpage.waitForWebElementToAppear(landingpage.emailField);

	            // Step 2: Enter email from array
	            landingpage.emailField.clear(); // Clear the field before entering a new email
	            landingpage.emailField.sendKeys(emails[i]);
	            landingpage.continueBtn.click();
	            Thread.sleep(4000);

	            // Step 3: Complete Stripe submission
	            landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

	            // Step 4: Submit billing information
	            dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

	            // Step 5: Select country and enter phone number
	            locatingPopup.countryCodeDropdown.click();
	            Thread.sleep(2000);
	            WebElement indiaFlag = driver.findElement(By.xpath("(//span[text()='India'])[2]"));
	            indiaFlag.click();
	            Thread.sleep(2000);

	            locatingPopup.phoneNumberField.clear(); // Clear the phone number field
	            locatingPopup.phoneNumberField.sendKeys("1234567890");

	            // Step 6: Enter description and submit the form
	            locatingPopup.descriptionField.sendKeys("Hello, one of your loved ones is looking for you.");
	            locatingPopup.submitBtn.click();
	            Thread.sleep(3000);

	            // Step 7: Capture success message
	            WebElement successMessageElement = driver.findElement(By.xpath("//div[@class='ant-notification-notice-message']"));
	            String successMessage = successMessageElement.getText();
	            System.out.println("Success message for " + emails[i] + ": " + successMessage);
	            Thread.sleep(3000);

	        } catch (Exception e) {
	            // Handle any unexpected exceptions
	            System.err.println("Error occurred with email: " + emails[i] + " - " + e.getMessage());
	        } finally {
	            // Step 8: Log out after each iteration
	            Thread.sleep(2000);
	            dashboard.logout();
	        }
	    }
	}
}
