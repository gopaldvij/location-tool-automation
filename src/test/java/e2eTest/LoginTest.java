package e2eTest;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import testComponent.BaseTest;

public class LoginTest extends BaseTest {

	@Test(description = "Verify that Login popup should open when i click on the Login button.")
	public void TR_54() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(driver.findElement(By.xpath("//div[normalize-space(text())='Login']")));
		WebElement headerLoginPopup = driver.findElement(By.xpath("//div[normalize-space(text())='Login']"));
		softAssert.assertEquals(headerLoginPopup.getText(), "Login");
		softAssert.assertTrue(driver.getCurrentUrl().endsWith("login"), "Login Button does not works");
		softAssert.assertAll();
	}

	@Test(description = "Verify that below social login options should available in the popup.")
	public void TR_55() {
		SoftAssert softAssert = new SoftAssert();
		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.emailField);
		softAssert.assertEquals(locatingPopup.continueWithGoogle.getText(), "Continue with Google");
		softAssert.assertEquals(locatingPopup.continueWithFb.getText(), "Continue with Facebook");
		softAssert.assertTrue(locatingPopup.continueWithGoogle.isEnabled(),
				"Continue with Google button is not visible");
		softAssert.assertTrue(locatingPopup.continueWithFb.isEnabled(), "Continue with Facebook button is not visible");
		softAssert.assertAll();
	}

	@Test(description = "Verify with the empty email and password field.")
	public void TR_56() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.loginDash("", "");
		String emailError = driver.findElement(By.xpath("//div[normalize-space(text())='Please Enter Email']"))
				.getText();
		String passError = driver.findElement(By.xpath("//div[normalize-space(text())='Password Is Required']"))
				.getText();

		softAssert.assertEquals(emailError, "Please Enter Email");
		softAssert.assertEquals(passError, "Password Is Required");
		softAssert.assertAll();
	}

	@Test(description = "Verify with the valid email and invalid password.")
	public void TR_57() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("smoke2@yopmail.com", "Password@123");
		String errorEmail = driver
				.findElement(By.xpath("//p[normalize-space(text())='Email and password do not match.']")).getText();
		softAssert.assertEquals(errorEmail, "Email and password do not match.");
		softAssert.assertAll();
	}

	@Test(description = "Verify with the invalid email and valid password.")
	public void TR_58() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("seleniu12@yopmail.com", "djg8Ut");
		String errorEmail = driver
				.findElement(By.xpath("//p[normalize-space(text())='There is no account associated with this email.']"))
				.getText();
		softAssert.assertEquals(errorEmail, "There is no account associated with this email.");
		softAssert.assertAll();
	}

	@Test(description = "Verify with the invalid email and invalid password.")
	public void TR_59() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.login("seleniu12@yopmail.com", "Password@123");
		String errorMessage = driver
				.findElement(By.xpath("//p[normalize-space(text())='There is no account associated with this email.']"))
				.getText();
		softAssert.assertEquals(errorMessage, "There is no account associated with this email.");
		softAssert.assertAll();
	}

	@Test(description = "Verify that user should navigated to the dashboard after login.")
	public void TR_60() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.emailField);
		landingpage.emailField.sendKeys("smoke2@yopmail.com");
		landingpage.passwordField.sendKeys("p9aXcf");
		landingpage.loginContinueBtn.click();
		landingpage.waitForWebElementToAppear(driver.findElement(By.xpath("//div[@Role='alert']")));
		String loginSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();

		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/dash");
		softAssert.assertTrue(dashboard.sideMenu.isDisplayed(), "Dashboard is not open after login");
		softAssert.assertEquals(loginSuccessMessage, "Successfully logged in.");
		Thread.sleep(2000);
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that the user should able to close the Login Popup.")
	public void TR_61() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.emailField);
		landingpage.popupCloseBtn.click();
		Thread.sleep(2000);
		softAssert.assertTrue(landingpage.loginBtn.isDisplayed());
		softAssert.assertAll();
	}

	@Test(description = "Verify that forgot password should be accessible.")
	public void TR_62() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.emailField);
		landingpage.forgotPassword.click();
		Thread.sleep(2000);
		softAssert.assertTrue(landingpage.headerResetPass.isDisplayed(), "Forgot Password button is not working");
		softAssert.assertAll();
	}

	@Test(description = "Make sure that new password should be sent to your email account when you reset the password.")
	public void TR_63() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.resetPassword("smoke1@yopmail.com");
		String resetPassSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();
		softAssert.assertEquals(resetPassSuccessMessage,
				"You will receive an email with password reset instructions shortly");

		common.yopmailEmailPass("selenium26@yopmail.com");
		String emailInResetPasswordMail = driver.findElement(By.xpath("//p[contains(text(), 'Username:')]/span"))
				.getText();
		WebElement passwordInResetPasswordMailElement = driver
				.findElement(By.xpath("//p[contains(text(), 'Password:')]/span"));
		String passwordInResetPasswordMail = driver.findElement(By.xpath("//p[contains(text(), 'Password:')]/span"))
				.getText();

		softAssert.assertEquals(emailInResetPasswordMail, "selenium26@yopmail.com",
				"Email is wrong in the forgot password mail");
		softAssert.assertTrue(passwordInResetPasswordMailElement.isDisplayed(), "Successfully Password Got.");
		softAssert.assertAll();
	}

	@Test(description = "Verify that user should not able to login with the old password.")
	public void TR_64() throws InterruptedException, IOException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.enterMobileNumber("+91 9090407368");
		landingpage.waitForWebElementToAppear(landingpage.emailField);
		final String newEmail = common.randomEmail();
		landingpage.emailField.sendKeys(newEmail);
		System.out.println(newEmail);
		landingpage.continueBtn.click();
		Thread.sleep(2000);

		landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");
		
		dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");

		Thread.sleep(3000);
		dashboard.popupCloseBtn.click();
		Thread.sleep(5000);
		dashboard.logout();
		
		common.yopmailEmailPass(newEmail);
		final String oldEmailInMail = driver.findElement(By.xpath("//p[contains(text(), 'Username:')]/span")).getText();
		final String oldPasswordInMail = driver.findElement(By.xpath("//p[contains(text(), 'Password:')]/span")).getText();
		Thread.sleep(2000);

		driver.get("https://stage.location-tool.com/en/");
		Thread.sleep(2000);

		landingpage.resetPassword(newEmail);
		String resetPassSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();
		softAssert.assertEquals(resetPassSuccessMessage,
				"You will receive an email with password reset instructions shortly");

		Thread.sleep(2000);

		landingpage.login(oldEmailInMail, oldPasswordInMail);
		String errorEmail = driver
				.findElement(By.xpath("//p[normalize-space(text())='Email and password do not match.']")).getText();
		softAssert.assertTrue(dashboard.logo.isDisplayed());
		softAssert.assertEquals(dashboard.logo.getAttribute("src"),
				"https://stage.location-tool.com/static/media/mainLogo.a871e2d4315b6695b99125b58e48e991.svg");
		
		softAssert.assertEquals(errorEmail, "Email and password do not match.");
		softAssert.assertAll();
	}

	@Test(description = "Verify that user should able to login with the new password.")
	public void TR_65() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.resetPassword("smoke1@yopmail.com");
		String resetPassSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();
		softAssert.assertEquals(resetPassSuccessMessage,
				"You will receive an email with password reset instructions shortly");
		
		common.yopmailEmailPass("smoke1@yopmail.com");
		String emailInResetPasswordMail = driver.findElement(By.xpath("//p[contains(text(), 'Username:')]/span")).getText();
		String passwordInResetPasswordMail = driver.findElement(By.xpath("//p[contains(text(), 'Password:')]/span")).getText();
		Thread.sleep(2000);

		driver.get("https://stage.location-tool.com/en/");
		Thread.sleep(2000);

		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.emailField);
		landingpage.emailField.sendKeys("smoke1@yopmail.com");
		landingpage.passwordField.sendKeys(passwordInResetPasswordMail);
		landingpage.loginContinueBtn.click();
		Thread.sleep(2000);
		String loginSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();
		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/dash");
		softAssert.assertTrue(dashboard.sideMenu.isDisplayed(), "Dashboard is not open after login");
		softAssert.assertEquals(loginSuccessMessage, "Successfully logged in.");
		Thread.sleep(5000);
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that email should be case sensitive in login popup.")
	public void TR_66() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.loginBtn.click();
		landingpage.waitForWebElementToAppear(landingpage.emailField);
		landingpage.emailField.sendKeys("SMOKE2@yopmail.com");
		landingpage.passwordField.sendKeys("p9aXcf");
		landingpage.loginContinueBtn.click();
		Thread.sleep(2000);
		String loginSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();

		softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/dash");
		softAssert.assertTrue(dashboard.sideMenu.isDisplayed(), "Dashboard is not open after login");
		softAssert.assertEquals(loginSuccessMessage, "Successfully logged in.");
		Thread.sleep(5000);
		dashboard.logout();
		softAssert.assertAll();
	}

	@Test(description = "Verify that email should be case sensitive in forgot password popup.")
	public void TR_67() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		landingpage.resetPassword("SELENIUM26@YOPMAIL.COM");
		String resetPassSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();
		softAssert.assertEquals(resetPassSuccessMessage,
				"You will receive an email with password reset instructions shortly");

		common.yopmailEmailPass("selenium26@yopmail.com");
		String emailInResetPasswordMail = driver.findElement(By.xpath("//p[contains(text(), 'Username:')]/span")).getText();
		WebElement passwordInResetPasswordMail = driver.findElement(By.xpath("//p[contains(text(), 'Password:')]/span"));

		softAssert.assertEquals(emailInResetPasswordMail, "selenium26@yopmail.com", "Email is wrong in the forgot password mail");
		softAssert.assertTrue(passwordInResetPasswordMail.isDisplayed(), "Successfully Password Got.");
		softAssert.assertAll();
	}
}
