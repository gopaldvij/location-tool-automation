package e2eTest;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import testComponent.BaseTest;

public class DashboardTest extends BaseTest {

	@Test(description = "Verify that dashboard should arrives after login.", priority = 1)
	public void TR_68() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			String loginSuccessMessage = driver.findElement(By.xpath("//div[@Role='alert']")).getText();
			softAssert.assertEquals(driver.getCurrentUrl(), "https://stage.location-tool.com/en/dash");
			softAssert.assertTrue(dashboard.sideMenu.isDisplayed(), "Dashboard is not open after loginDash");
			softAssert.assertEquals(loginSuccessMessage, "Successfully logged in.");
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that Tracelo Logo, Language, and Side Bar should be visible.", priority = 2)
	public void TR_69() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			WebElement dashLogo = driver.findElement(By.xpath("//div[@class='main-logo-img']//img"));
			WebElement langOption = driver.findElement(By.xpath("//div[@class='header-lang-dropdown']//img[1]"));
			softAssert.assertTrue(dashLogo.isDisplayed());
			softAssert.assertEquals(dashLogo.getAttribute("src"),
					"https://stage.location-tool.com/static/media/mainLogo.a871e2d4315b6695b99125b58e48e991.svg");
			softAssert.assertTrue(dashboard.sideMenu.isDisplayed());
			softAssert.assertTrue(langOption.isDisplayed());
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Make sure that phone number field should be visible.", priority = 3)
	public void TR_70() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			Assert.assertTrue(dashboard.numberWantToLocateField.isDisplayed());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that map section should be visible.", priority = 4)
	public void TR_72() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			WebElement mapSection = driver.findElement(By.xpath("//div[@class='dashboard-map dashboard-box']"));
			Assert.assertTrue(mapSection.isDisplayed());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that 'Phone Number You Want to Locate' placeholder should be visible in the phone number field.", priority = 5)
	public void TR_73() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			Assert.assertEquals(dashboard.numberWantToLocateField.getAttribute("placeholder"), "Enter a phone number");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that 'Daily limit of 10 requests' text should be visible below the phone number field.", priority = 6)
	public void TR_74() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			Assert.assertEquals(
					driver.findElement(By.xpath("//div[@class='dashboard-find-number dashboard-box']//p[1]")).getText(),
					"Daily limit of 10 requests");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that country code should visible in phone number field.", priority = 7)
	public void TR_75() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			WebElement countryCode = driver.findElement(By.xpath("(//div[@class='selected-flag'])[1]"));
			Assert.assertTrue(countryCode.isDisplayed());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that Locate button should be visible besides of phone number field.", priority = 8)
	public void TR_76() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			Assert.assertTrue(dashboard.searchButton.isDisplayed());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that user should able to enter the mobile number.", priority = 9)
	public void TR_77() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			dashboard.numberWantToLocateField.sendKeys("1234567890");
			Assert.assertEquals(dashboard.numberWantToLocateField.getAttribute("value").replaceAll("\\s", ""),
					"1234567890");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that error should arrives when i click on the locate button without entering a number.", priority = 10)
	public void TR_78() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			dashboard.searchButton.click();
			WebElement invalidNumberError = dashboard.invalidNumberError;
			softAssert.assertEquals(invalidNumberError.getText(), "Please Enter Number");
			softAssert.assertTrue(invalidNumberError.isDisplayed());
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Make sure that country code should be changed according to the country code.", priority = 11)
	public void TR_79() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			String[] countryCodesString = landingpage.totalCountryCodes();
			Assert.assertEquals(Arrays.toString(countryCodesString), Arrays.toString(landingpage.expectedCountryCode));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(enabled = false, description = "Verify that country code and flag should changed if i paste the mobile number.", priority = 12)
	public void TR_81() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			for (int i = 0; i < landingpage.expectedCountryCode.length; i++) {
				dashboard.numberWantToLocateField.sendKeys(landingpage.phoneNumbers[i]);
				softAssert.assertEquals(landingpage.actualCountryCode(), landingpage.expectedCountryCode[i]);
				dashboard.resetEnterPhoneNumberField();
			}
		} catch (AssertionError e) {
			// TODO: handle exception
			System.err.println("Assertion failed: " + e.getMessage());
			System.out.println("Mobile Number123 : " + dashboard.numberWantToLocateField.getAttribute("value"));
			throw e;
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Make sure that alphabets should not allow in the phone number field.", priority = 13)
	public void TR_82() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			dashboard.numberWantToLocateField.sendKeys("abcdefghijklmnopqrstuvwxyz");
			Assert.assertEquals(dashboard.numberWantToLocateField.getText(), "");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Make sure that special characters should not allow in the phone number filed.", priority = 14)
	public void TR_83() throws InterruptedException {
		try {
			landingpage.loginDash("selenium122@yopmail.com", "4KXbuA");
			dashboard.numberWantToLocateField.sendKeys("!~`@#$%^&*()_-={}[];:''<>?/");
			Assert.assertEquals(dashboard.numberWantToLocateField.getText(), "");
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Locating popup should be arrives if you search the valid number.", priority = 15)
	public void TR_85() throws InterruptedException {
		try {
			landingpage.loginDash("case100@yopmail.com", "NZT03D");
			dashboard.numberWantToLocateField.sendKeys("1234567890");
			dashboard.searchButton.click();
			landingpage.waitForWebElementToAppear(dashboard.numberWhereYouWillReceiveResultField);
			Assert.assertTrue(dashboard.numberWhereYouWillReceiveResultField.isDisplayed());
			Thread.sleep(2000);
			landingpage.popupCloseBtn.click();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that you got the locating popup where you have three fields : Phone Number field, Message, Continue", priority = 16)
	public void TR_86() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("case101@yopmail.com", "v8Fk7C");
			dashboard.numberWantToLocateField.sendKeys("1234567890");
			dashboard.searchButton.click();
			landingpage.waitForWebElementToAppear(dashboard.numberWhereYouWillReceiveResultField);
			Thread.sleep(2000);
			softAssert.assertTrue(dashboard.numberWhereYouWillReceiveResultField.isDisplayed());
			softAssert.assertTrue(dashboard.description.isDisplayed());
			softAssert.assertTrue(dashboard.submitBtn.isDisplayed());
			landingpage.popupCloseBtn.click();
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that you cannot continue with the blank information in the locating popup.", priority = 17)
	public void TR_87() throws InterruptedException {
		try {
			landingpage.loginDash("case102@yopmail.com", "zRYQr4");
			dashboard.locateNumber("1234567890", " ", " ");
			Thread.sleep(2000);
			WebElement numberFieldError = driver.findElement(By.xpath("(//div[@class='dashboard-locate']//p)[1]"));
			Assert.assertEquals(numberFieldError.getText(), "Please Enter Number");
			landingpage.popupCloseBtn.click();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that when you click on the 'Cancel' button then locating popup should close.", priority = 18)
	public void TR_88() throws InterruptedException {
		try {
			landingpage.loginDash("case103@yopmail.com", "EcCzhi");
			dashboard.numberWantToLocateField.sendKeys("1234567890");
			Thread.sleep(2000);
			dashboard.searchButton.click();
			Thread.sleep(3000);
			landingpage.popupCloseBtn.click();
			Thread.sleep(1000);
			Assert.assertTrue(dashboard.sideMenu.isDisplayed());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that same number should be visible on the locating which we have added in number want to locate field.", priority = 19)
	public void TR_89() throws InterruptedException {
		try {
			landingpage.loginDash("case104@yopmail.com", "35bqUs");
			dashboard.numberWantToLocateField.sendKeys("+911234567890");
			Thread.sleep(2000);
			dashboard.searchButton.click();
			Thread.sleep(2000);
			String numberOnLocatingPopup = driver
					.findElement(By.xpath("//span[text()='Locating']/following-sibling::p")).getText().trim()
					.replaceAll(" ", "");
			Assert.assertEquals(numberOnLocatingPopup, "+911234567890");
			landingpage.popupCloseBtn.click();
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		} finally {
			dashboard.logout();
		}
	}

	@Test(description = "Verify that 'Location request sent successfully!' message arrives if you click Continue after entering the valid details", priority = 20)
	public void TR_90() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("case105@yopmail.com", "MMyL7Z");
			Thread.sleep(3000);
			dashboard.numberWantToLocateField.sendKeys("+911234567890");
			dashboard.searchButton.click();
			dashboard.numberWhereYouWillReceiveResultField.sendKeys("+911234567890");
			dashboard.description.sendKeys("Hello, one of your loved ones is looking for you.");
			dashboard.submitBtn.click();
			Thread.sleep(2000);

			WebElement successMessageElement = driver
					.findElement(By.xpath("//div[@class='ant-notification-notice-message']"));
			String successMessage = successMessageElement.getText();
			Thread.sleep(5000);
			softAssert.assertEquals(successMessage, "Location request sent successfully!");
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that Number you want to locate should be visible in the history listing.", priority = 21)
	public void TR_93() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("case106@yopmail.com", "G7XjWi");
			dashboard.locateNumber("+911234567890", "+13434804619",
					"Hello, one of your loved ones is looking for you.");
			Thread.sleep(5000);
			String firstNumberHistory = driver.findElement(By.xpath("(//div[@class='items-scrollable']//span)[1]"))
					.getText();
			softAssert.assertEquals(firstNumberHistory, "+911234567890");
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that location status should be pending in the history listing until user don't click on the location url.", priority = 22)
	public void TR_94() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("case107@yopmail.com", "XnYw2D");
			dashboard.locateNumber("+911234567890", "+13434804619",
					"Hello, one of your loved ones is looking for you.");
			Thread.sleep(5000);
			String firstNumberStatus = driver.findElement(By.xpath("(//div[@class='items-scrollable']//span)[3]"))
					.getText();
			String firstNumberHistory = driver.findElement(By.xpath("(//div[@class='items-scrollable']//span)[1]"))
					.getText();
			softAssert.assertEquals(firstNumberStatus, "Pending");
			softAssert.assertEquals(firstNumberHistory, "+911234567890");
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Make sure that Daily 10 request should be allowed for location.", priority = 27)
	public void TR_96() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			Thread.sleep(3000);
			landingpage.enterMobileNumber("+91 1234567890");
			landingpage.waitForWebElementToAppear(landingpage.emailField);
			landingpage.emailField.sendKeys(common.randomEmail());
			landingpage.continueBtn.click();
			Thread.sleep(2000);

			landingpage.stripeSubmit("5555 5555 5555 4444", "229", "231");

			dashboard.billingInfoSubmit("First Name", "Last Name", "Address", "City", "382350");
			Thread.sleep(2000);

			locatingPopup.phoneNumberField.sendKeys("+911234567890");
			locatingPopup.descriptionField.sendKeys("Hello, one of your loved ones is looking for you.");
			locatingPopup.submitBtn.click();
			Thread.sleep(2000);

			for (int i = 0; i < 9; i++) {
				dashboard.locateNumber("+911234567890", "+13434804619",
						"Hello, one of your loved ones is looking for you.");
			}

			Thread.sleep(2000);
			dashboard.numberWantToLocateField.sendKeys("+911234567890");
			Thread.sleep(2000);
			dashboard.searchButton.click();
			Thread.sleep(2000);

			String errorMessage = driver
					.findElement(By.xpath("//div[@class='dashboard-find-number dashboard-box']//p[1]")).getText();
			softAssert.assertEquals(errorMessage, "Your daily limit has been reached. Please try again later.");
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(2000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that Dashboard , Account and Logout button should be visible in the Side Menu.", priority = 23)
	public void TR_97() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("case108@yopmail.com", "Z9b6QT");
			Thread.sleep(3000);
			dashboard.sideMenu.click();
			Thread.sleep(2000);
			softAssert.assertTrue(dashboard.dashboardBtn.isDisplayed(),
					"Dashboard Button is not visible in the sidebar");
			softAssert.assertTrue(dashboard.accountBtn.isDisplayed(), "Account Button is not visible in the sidebar");
			softAssert.assertTrue(dashboard.logoutBtn.isDisplayed(), "Logout Button is not visible in the sidebar");
		} catch (Exception e) {
			softAssert.fail(e.getMessage());
		} finally {
			dashboard.logoutBtn.click();
			softAssert.assertAll();
		}
	}

	@Test(description = "Verify that Account page should be open when you click on the Account Page.", priority = 24)
	public void TR_98() throws InterruptedException {
		try {
			landingpage.loginDash("case109@yopmail.com", "v8ZY2h");
			Thread.sleep(3000);
			dashboard.sideMenu.click();
			Thread.sleep(2000);
			dashboard.accountBtn.click();
			Thread.sleep(2000);
			WebElement accountInfoHeader = driver
					.findElement(By.xpath("//span[normalize-space(text())='Account Information']"));
			Assert.assertEquals(accountInfoHeader.getText(), "Account Information");
		} catch (AssertionError e) {
			// TODO: handle exception
			Assert.fail(e.getMessage());
		} finally {
			Thread.sleep(2000);
			dashboard.logout();
		}
	}

	@Test(description = "Make sure that user should logged out when you click on the logout button.", priority = 25)
	public void TR_99() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("case110@yopmail.com", "nX2xkw");
			Thread.sleep(3000);
			dashboard.logout();
			Thread.sleep(2000);
			softAssert.assertTrue(landingpage.loginBtn.isDisplayed(), "Logout btn is not worked");

		} catch (AssertionError e) {
			softAssert.fail(e.getMessage());
		}
		softAssert.assertAll();
	}

	@Test(description = "Verify that all the languages should be visible in the langagues list.", priority = 26)
	public void TR_100() throws InterruptedException {
		SoftAssert softAssert = new SoftAssert();
		try {
			landingpage.loginDash("case111@yopmail.com", "PGolFN");
			Thread.sleep(3000);
			softAssert.assertTrue(dashboard.langDropdown.isDisplayed());
			softAssert.assertEquals(Arrays.toString(landingpage.totalLanguages()),
					Arrays.toString(landingpage.expectedLanguages));
			landingpage.langListCloseBtn.click();
		} catch (AssertionError e) {
			// TODO: handle exception
			softAssert.fail(e.getMessage());
		} finally {
			Thread.sleep(2000);
			dashboard.logout();
			softAssert.assertAll();
		}
	}
}
