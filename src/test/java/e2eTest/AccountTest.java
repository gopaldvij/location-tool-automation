package e2eTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import testComponent.BaseTest;

public class AccountTest extends BaseTest {

	@Test(description = "Verify the url of the Account page.")
	public void TR_102() throws InterruptedException {
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		Assert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/settings");
		dashboard.logout();
	}

	@Test(description = "Verify that Account information section should be visible in the Account page.")
	public void TR_103() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		WebElement accountInformationHeader = driver
				.findElement(By.xpath("//span[normalize-space(text())='Account Information']"));
		WebElement billingInfoHeader = driver.findElement(By.xpath("//span[normalize-space(text())='Billing Info']"));

		softAssert.assertEquals(accountInformationHeader.getText(), "Account Information");
		softAssert.assertEquals(billingInfoHeader.getText(), "Billing Info");
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that Email and Creation Date should be visible in the Account page.")
	public void TR_104() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		WebElement emailElement = driver.findElement(By.xpath("(//div[@class='wi-50 account_address']//a)[1]"));
		WebElement dateElement = driver.findElement(By.xpath("//div[@class='wi-50 account_address']//time[1]"));
		softAssert.assertEquals(emailElement.getText(), "case120@yopmail.com");
		softAssert.assertTrue(dateElement.isDisplayed(), "Date is not visible in the account page");
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that Billing Info section should be visible in the Account page.")
	public void TR_105() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		WebElement billingInfoHeader = driver.findElement(By.xpath("//span[normalize-space(text())='Billing Info']"));
		softAssert.assertEquals(billingInfoHeader.getText(), "Billing Info");
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that following six field should be visible in Billing Info Section - First Name, Last Name, Street Address, Country, City, Postal Code.")
	public void TR_106() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		softAssert.assertTrue(dashboard.firstNameField.isDisplayed(), "First Name is not displayed");
		softAssert.assertTrue(dashboard.lastNameField.isDisplayed(), "Last Name is not displayed");
		softAssert.assertTrue(dashboard.addressField.isDisplayed(), "Address is not displayed");
		softAssert.assertTrue(dashboard.cityField.isDisplayed(), "City field is not displayed");
		softAssert.assertTrue(dashboard.zipcodeField.isDisplayed(), "Zipcode field is not displayed");
		softAssert.assertTrue(dashboard.selectCountry.isDisplayed(), "Country Field is not displayed");

		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Make sure that all the billing info field should be mandatory.")
	public void TR_107() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		dashboard.billingInfoClear();
		Thread.sleep(3000);
		dashboard.billingInfoSubmit(" ", " ", " ", " ", " ");

		WebElement firstNameErrorElement = driver
				.findElement(By.xpath("//input[@name='first_name']/following-sibling::span"));
		WebElement lastNameErrorElement = driver
				.findElement(By.xpath("//input[@name='last_name']/following-sibling::span"));
		WebElement addressErrorElement = driver
				.findElement(By.xpath("//input[@name='address']/following-sibling::span"));
		WebElement cityErrorElement = driver.findElement(By.xpath("//input[@name='city']/following-sibling::span"));
		WebElement postalCodeErrorElement = driver
				.findElement(By.xpath("//input[@name='zipcode']/following-sibling::span"));

		softAssert.assertTrue(firstNameErrorElement.isDisplayed());
		softAssert.assertTrue(lastNameErrorElement.isDisplayed());
		softAssert.assertTrue(addressErrorElement.isDisplayed());
		softAssert.assertTrue(cityErrorElement.isDisplayed());
		softAssert.assertTrue(postalCodeErrorElement.isDisplayed());
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that error should arrives when you click on the Save button with the empty data.")
	public void TR_108() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		dashboard.billingInfoClear();
		Thread.sleep(3000);
		dashboard.billingInfoSubmit(" ", " ", " ", " ", " ");

		WebElement firstNameErrorElement = driver
				.findElement(By.xpath("//input[@name='first_name']/following-sibling::span"));
		WebElement lastNameErrorElement = driver
				.findElement(By.xpath("//input[@name='last_name']/following-sibling::span"));
		WebElement addressErrorElement = driver
				.findElement(By.xpath("//input[@name='address']/following-sibling::span"));
		WebElement cityErrorElement = driver.findElement(By.xpath("//input[@name='city']/following-sibling::span"));
		WebElement postalCodeErrorElement = driver
				.findElement(By.xpath("//input[@name='zipcode']/following-sibling::span"));

		softAssert.assertTrue(firstNameErrorElement.isDisplayed());
		softAssert.assertTrue(lastNameErrorElement.isDisplayed());
		softAssert.assertTrue(addressErrorElement.isDisplayed());
		softAssert.assertTrue(cityErrorElement.isDisplayed());
		softAssert.assertTrue(postalCodeErrorElement.isDisplayed());
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Make sure that detail should be save and success message 'Settings have been successfully updated' should arrives when you click on the save.")
	public void TR_109() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		dashboard.billingInfoClear();
		Thread.sleep(3000);
		dashboard.billingInfoSubmit("Case", "120", "Nikol", "Ahmedabad", "382350");

		String updateMessage = driver.findElement(By.xpath("//div[@class='ant-notification-notice-message']"))
				.getText();

		softAssert.assertEquals(dashboard.firstNameField.getAttribute("value"), "Case");
		softAssert.assertEquals(dashboard.lastNameField.getAttribute("value"), "120");
		softAssert.assertEquals(dashboard.addressField.getAttribute("value"), "Nikol");
		softAssert.assertEquals(dashboard.cityField.getAttribute("value"), "Ahmedabad");
		softAssert.assertEquals(dashboard.zipcodeField.getAttribute("value"), "382350");
		softAssert.assertEquals(updateMessage, "Settings have been successfully updated");
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that user should be loggedout when you click on the Logout button.")
	public void TR_110() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.logout();
		softAssert.assertTrue(landingpage.loginBtn.isDisplayed());
		softAssert.assertAll();
	}

	@Test(description = "Verify that Unsubscribe popup should be open when you click on the Unsubscribe button.")
	public void TR_111() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		dashboard.unsubscribeBtn.click();
		Thread.sleep(2000);
		WebElement unsubscribeText = driver.findElement(By.cssSelector(".unsubscribe-text"));
		WebElement unsubscribeHeader = driver.findElement(By.xpath("//p[normalize-space(text())='Unsubscribe']"));
		softAssert.assertEquals(unsubscribeText.getText(),
				"By clicking submit you will be unsubscribed from LocationTool and will no longer be able to use the service.");
		softAssert.assertEquals(unsubscribeHeader.getText(), "Unsubscribe");
		driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
		Thread.sleep(1000);
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that unsubscribe popup should be close when you click on the cancel.")
	public void TR_112() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("case120@yopmail.com", "onAVOl");
		dashboard.openAccountPage();
		dashboard.unsubscribeBtn.click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
		Thread.sleep(1000);
		dashboard.logout();
		softAssert.assertTrue(landingpage.loginBtn.isDisplayed());
		softAssert.assertAll();
	}

	@Test(description = "Verify that User should unsubscribed and logged out automatically when he clicks on the Unsubscribe button.")
	public void TR_113() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailField);
			landingpage.emailField.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");
			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			dashboard.popupCloseBtn.click();

			Thread.sleep(4000);
			dashboard.cancelSubscription();
			System.out.println("test1");
			softAssert.assertTrue(landingpage.loginBtn.isDisplayed());
			System.out.println("test2");
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(1000);
			softAssert.assertAll();
		}
	}

	@Test(description = "Make sure that user cannot login with the unsubscribed account.")
	public void TR_114() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailField);
			final String email = common.randomEmail();
			landingpage.emailField.sendKeys(email);
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");
			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			dashboard.popupCloseBtn.click();

			driver.get("https://yopmail.com/en");
			driver.findElement(By.id("login")).clear();
			driver.findElement(By.id("login")).sendKeys(email);
			driver.findElement(By.xpath("//button[@Title='Check Inbox @yopmail.com']")).click();
			Thread.sleep(3000);
			WebElement iframeYopmail = driver.findElement(By.id("ifmail"));
			driver.switchTo().frame(iframeYopmail);
			Thread.sleep(2000);
			final String username = driver.findElement(By.xpath("//p[contains(text(), 'Username:')]/span")).getText();
			final String password = driver.findElement(By.xpath("//p[contains(text(), 'Password:')]/span")).getText();

			Thread.sleep(2000);

			driver.get("https://stage.location-tool.com/en/");
			Thread.sleep(2000);

			dashboard.cancelSubscription();
			landingpage.login(username, password);
			WebElement errorUnsubscribe = driver.findElement(By.xpath(
					"//p[normalize-space(text())='You have already unsubscribed from Location-tool.com service and Charges on your credit card will cease.']"));
			softAssert.assertEquals(errorUnsubscribe.getText(),
					"You have already unsubscribed from Location-tool.com service and Charges on your credit card will cease.");
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(1000);
			softAssert.assertAll();
		}
	}

	@Test(description = "Make sure that user cannot register with the unsubscribed account.")
	public void TR_115() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailField);
			final String email = common.randomEmail();
			landingpage.emailField.sendKeys(email);
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");
			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			dashboard.popupCloseBtn.click();

			driver.get("https://yopmail.com/en");
			driver.findElement(By.id("login")).clear();
			driver.findElement(By.id("login")).sendKeys(email);
			driver.findElement(By.xpath("//button[@Title='Check Inbox @yopmail.com']")).click();
			Thread.sleep(3000);
			WebElement iframeYopmail = driver.findElement(By.id("ifmail"));
			driver.switchTo().frame(iframeYopmail);
			Thread.sleep(2000);
			final String username = driver.findElement(By.xpath("//p[contains(text(), 'Username:')]/span")).getText();
			final String password = driver.findElement(By.xpath("//p[contains(text(), 'Password:')]/span")).getText();
			Thread.sleep(2000);

			driver.get("https://stage.location-tool.com/en/");
			Thread.sleep(2000);

			dashboard.cancelSubscription();

			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailField);
			landingpage.emailField.sendKeys(email);
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			WebElement errorUnsubscribe = driver.findElement(By.xpath("//div[@class='modal-login-form']//div[1]"));
			softAssert.assertEquals(errorUnsubscribe.getText(),
					"You already have an account with us. If you can't remember it, please contact support.");
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(1000);
			softAssert.assertAll();
		}
	}
}
