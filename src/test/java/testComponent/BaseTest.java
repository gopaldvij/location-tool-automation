package testComponent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import abstractComponent.Common;
import e2eTest.SkipSetup;
import tracelo.pom.Dashboard;
import tracelo.pom.LandingPage;
import tracelo.pom.LocatingPopup;

public class BaseTest {

	public WebDriver driver;
	public Common common;
	public Dashboard dashboard;
	public LandingPage landingpage;
	public LocatingPopup locatingPopup;

	public WebDriver initializeDriver() throws IOException {
		// Load the properties file
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\resources\\GlobalData.properties");
		prop.load(fis);

		// Get the browser name from properties
		String browserName = prop.getProperty("browser");
		System.out.println("Selected Browser: " + browserName); // Debug log to confirm browser value

		// Check if the browser is set to 'browserstack'
		if (browserName.equalsIgnoreCase("browserstack")) {
			// Initialize BrowserStack driver
			return getBrowserStackDriver(prop);
		} else {
			// Initialize local browser (e.g., Chrome, Firefox, etc.)
			return initializeLocalDriver(browserName); // Call a helper function for local browsers
		}
	}

	private WebDriver initializeLocalDriver(String browserName) {
		WebDriver driver = null;

		switch (browserName.toLowerCase()) {
		case "chrome":
			ChromeOptions options = new ChromeOptions();
			if (browserName.contains("headless")) {
				options.addArguments("--headless", "--disable-gpu", "--ignore-certificate-errors",
						"--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");
			}
			driver = new ChromeDriver(options);
			break;

		case "firefox":
			driver = new FirefoxDriver();
			break;

		case "edge":
			driver = new EdgeDriver();
			break;

		case "safari":
			driver = new SafariDriver();
			break;

		default:
			throw new IllegalArgumentException("Browser not supported: " + browserName);
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		return driver;
	}

	@SuppressWarnings("deprecation")
	public WebDriver getBrowserStackDriver(Properties prop) throws MalformedURLException {
		// Read BrowserStack credentials from properties
		String username = prop.getProperty("BROWSERSTACK_USERNAME");
		String accessKey = prop.getProperty("BROWSERSTACK_ACCESS_KEY");
		String browserstackURL = prop.getProperty("BROWSERSTACK_URL");
		String browserstackBrowser = prop.getProperty("browserstack"); // Get the browser type for BrowserStack

		// Set BrowserStack capabilities
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browser", browserstackBrowser);
		caps.setCapability("browser_version", "latest");
		caps.setCapability("os", "Windows");
		caps.setCapability("os_version", "10");
		caps.setCapability("name", "Tracelo Test Automation");

		// Create a Remote WebDriver for BrowserStack
		return new RemoteWebDriver(new URL(browserstackURL), caps);
	}

	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {
		// read json to string
		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

		// String to HashMap- Jackson Databind
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {
				});
		return data;
	}

	public String getScreenshot(String testcasename, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File Source = ts.getScreenshotAs(OutputType.FILE);
		File File = new File(System.getProperty("user.dir") + "//reports//" + testcasename + ".png");
		FileUtils.copyFile(Source, File);
		return System.getProperty("user.dir") + "//reports//" + testcasename + ".png";
	}

	@BeforeClass(alwaysRun = true)
	public void launchApplication() throws IOException {
		driver = initializeDriver();
		common = new Common(driver);
		dashboard = new Dashboard(driver);
		landingpage = new LandingPage(driver);
		locatingPopup = new LocatingPopup(driver);
	}

	@BeforeMethod
	public void openWebsite(Method method) throws InterruptedException {
		driver.get("https://stage.location-tool.com/en");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		landingpage.waitForWebElementToAppear(landingpage.flagDropdown);
		Thread.sleep(2000);
	}

	@AfterClass
	public void tearDown() {
		driver.close();
	}
}
