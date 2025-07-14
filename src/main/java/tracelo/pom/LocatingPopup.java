package tracelo.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import abstractComponent.AbstractComponent;


public class LocatingPopup extends AbstractComponent {

	WebDriver driver;

	public LocatingPopup(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "(//div[@class='selected-flag'])[2]")
	public WebElement countryCodeDropdown;

	@FindBy(xpath = "(//input[@placeholder='Enter a phone number'])[2]")
	public WebElement phoneNumberField;

	@FindBy(xpath = "(//div[@class='body-dash-modal'])//textarea")
	public WebElement descriptionField;

	@FindBy(xpath = "//button[@type='submit']")
	public WebElement submitBtn;

	@FindBy(xpath = "//div[@class='number display-inputs-rtl']/p")
	public WebElement numberOnLocatingPopupElement;

	@FindBy(xpath = "//button[normalize-space(text())='Continue with Google']")
	public WebElement continueWithGoogle;
	
	@FindBy(xpath = "//button[normalize-space(text())='Continue with Facebook']")
	public WebElement continueWithFb;

	@FindBy(xpath = "//a[normalize-space(text())='Terms and Conditions']")
	public WebElement termsConditionSingupPopup;
	
	@FindBy(xpath = "//input[@name='email']/following-sibling::div[1]")
	public WebElement invalidEmailErrorMessage;
	
}


