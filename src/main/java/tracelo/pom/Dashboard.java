package tracelo.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import abstractComponent.AbstractComponent;
import abstractComponent.Common;

public class Dashboard extends AbstractComponent {
	WebDriver driver;

	public Dashboard(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(name = "first_name")
	public WebElement firstNameField;

	@FindBy(name = "last_name")
	public WebElement lastNameField;

	@FindBy(name = "address")
	public WebElement addressField;

	@FindBy(name = "city")
	public WebElement cityField;

	@FindBy(name = "zipcode")
	public WebElement zipcodeField;

	@FindBy(xpath = "//select[@name='country']")
	public WebElement selectCountry;

	@FindBy(xpath = "//button[text()='Save']")
	public WebElement billingInfoSaveBtn;

	@FindBy(xpath = "(//div[@class='selected-flag'])[1]")
	public WebElement countryCodeDropdown;

	@FindBy(xpath = "(//input[@placeholder='Enter a phone number'])[2]")
	public WebElement numberWhereYouWillReceiveResultField;
	
	@FindBy(xpath = "//button[@aria-label='Close']")
	public WebElement popupCloseBtn;

	@FindBy(xpath = "//div[@class='hum-burger-menu']")
	public WebElement sideMenu;

	@FindBy(xpath = "//a[.='Logout']")
	public WebElement logoutBtn;

	@FindBy(xpath = "//input[@placeholder='Enter a phone number']")
	public WebElement numberWantToLocateField;

	@FindBy(xpath = "//div[@class='input-suffix']")
	public WebElement searchButton;

	@FindBy(xpath = "//div[@class='dashboard-locate']//textarea")
	public WebElement description;

	@FindBy(xpath = "//button[@type='submit']")
	public WebElement submitBtn;

	@FindBy(xpath = "//div[@class='dashboard-find-number dashboard-box']//p[1]")
	public WebElement invalidNumberError;

	@FindBy(xpath = "//span[normalize-space(text())='Dashboard']")
	public WebElement dashboardBtn;

	@FindBy(xpath = "(//span[@class='ant-dropdown-menu-title-content']//a)[2]")
	public WebElement accountBtn;

	@FindBy(xpath = "//div[starts-with(@class, 'header-lang-drop')]")
	public WebElement langDropdown;

	@FindBy(xpath = "//button[text()='Unsubscribe']")
	public WebElement unsubscribeBtn;
	
	@FindBy(xpath="//img[@alt='Main Logo']")
	public WebElement logo;
	
	@FindBy(css  = ".cancel_btn")
	public WebElement cancelSubscriptionBtn;
	

	public void locateNumber(String number_WantToLocateField, String number_WhereYouWillReceiveResultField,
			String Description) throws InterruptedException {
		Thread.sleep(2000);
		numberWantToLocateField.sendKeys(number_WantToLocateField);
		searchButton.click();
		Thread.sleep(2000);
		numberWhereYouWillReceiveResultField.sendKeys(number_WhereYouWillReceiveResultField);
		description.sendKeys(Description);
		submitBtn.click();
		waitForWebElementToAppear(sideMenu);
//		Thread.sleep(5000);
	}

	public void openWebsite(String URL) {
		driver.get(URL);
	}

	public void billingInfoSubmit(String firstname, String lastname, String address, String city, String zipcode)
			throws InterruptedException {
		waitForWebElementToAppear(firstNameField);
		firstNameField.sendKeys(firstname);
		lastNameField.sendKeys(lastname);
		addressField.sendKeys(address);
		cityField.sendKeys(city);
		zipcodeField.sendKeys(zipcode);
		billingInfoSaveBtn.click();
//		waitForWebElementToAppear(countryCodeDropdown);
		Thread.sleep(3000);
	}

	public void billingInfoClear() throws InterruptedException {
		Common common = new Common(driver);
		waitForWebElementToAppear(firstNameField);
		common.clearField(firstNameField);
		common.clearField(lastNameField);
		common.clearField(addressField);
		common.clearField(cityField);
		common.clearField(zipcodeField);
		billingInfoSaveBtn.click();
//		waitForWebElementToAppear(countryCodeDropdown);
		Thread.sleep(3000);
	}

	public void logout() throws InterruptedException {
		waitForWebElementToDisappear(popupCloseBtn);
		sideMenu.click();
		waitForWebElementToAppear(logoutBtn);
		logoutBtn.click();
		Thread.sleep(2000);
	}

	public void resetEnterPhoneNumberField() {
		// Ensure the element is focused
		Actions actions = new Actions(driver);
		numberWantToLocateField.click();
		actions.moveToElement(numberWantToLocateField).click().keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
				.sendKeys(Keys.BACK_SPACE) // Delete the selected text
				.perform();
	}

	public void openAccountPage() throws InterruptedException {
		sideMenu.click();
		Thread.sleep(2000);
		accountBtn.click();
		Thread.sleep(2000);
	}

	public void unsubscribeAccount() throws InterruptedException {
		openAccountPage();
		unsubscribeBtn.click();
		Thread.sleep(2000);
		WebElement submitBtn = driver.findElement(By.xpath("//button[normalize-space(text())='Submit']"));
		submitBtn.click();
		Thread.sleep(10000);
	}
	
	public void cancelSubscription() throws InterruptedException {
		openAccountPage();
		cancelSubscriptionBtn.click();
		Thread.sleep(2000);
		WebElement submitBtn = driver.findElement(By.xpath("//button[normalize-space(text())='Submit']"));
		submitBtn.click();
		Thread.sleep(10000);
	}
}
