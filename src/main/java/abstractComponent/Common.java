package abstractComponent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public class Common extends AbstractComponent {
	WebDriver driver;

	public Common(WebDriver driver) {

		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// Set to store already used numbers
    private Set<Integer> usedNumbers = new HashSet<>();
    private static final String FILE_NAME = System.getProperty("user.dir")
            + "\\src\\main\\java\\resources\\used_numbers.txt";

    // Constructor to load used numbers from the file when the object is created
    public void RandomEmailGenerator() {
        loadUsedNumbersFromFile();
    }

    public int randomNumber() {
        Random randomGenerator = new Random();
        int randomInt;

        // Keep generating a random number until we get a unique one
        do {
            randomInt = 2000 + randomGenerator.nextInt(1000); // Update range if needed
        } while (usedNumbers.contains(randomInt)); // Check if it's already used

        // Add the generated number to the set
        usedNumbers.add(randomInt);

        // Save the used number to the file after it's generated
        saveUsedNumbersToFile();

        return randomInt;
    }

    public String randomEmail() {
        String character = "seleniumg";
        String domain = "@yopmail.com";
        String randomEmail = character + randomNumber() + domain;
        return randomEmail;
    }

    // Load used numbers from file
    private void loadUsedNumbersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing file found, creating a new one.");
            return; // If file doesn't exist, no need to load anything
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                usedNumbers.add(Integer.parseInt(line));
            }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    // Save used numbers to file
    private void saveUsedNumbersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Integer num : usedNumbers) {
                writer.write(num.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
	
	public String currentLocation() throws InterruptedException {
		driver.get("https://www.google.com/");
		String country = driver.findElement(By.xpath("(//div[@role='contentinfo']//div)[1]")).getText();
		return country;
	}

	public static String getLastDigit(String text) {
		// Use regex to find the last digit in the string
		for (int i = text.length() - 1; i >= 0; i--) {
			if (Character.isDigit(text.charAt(i))) {
				return String.valueOf(text.charAt(i)); // Return the last found digit
			}
		}
		return ""; // Return empty if no digit is found
	}
	
	public void clearField(WebElement fieldName) {
		Actions actions = new Actions(driver);
		fieldName.click();
		actions.moveToElement(fieldName).click().keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL) 
				.sendKeys(Keys.BACK_SPACE) // Delete the selected text
				.perform();
	}
	
	public void yopmailEmailPass(String email) throws InterruptedException {
		driver.get("https://yopmail.com/en");
		driver.findElement(By.id("login")).clear();
		driver.findElement(By.id("login")).sendKeys(email);
		driver.findElement(By.xpath("//button[@Title='Check Inbox @yopmail.com']")).click();
		Thread.sleep(3000);
		WebElement iframeYopmail = driver.findElement(By.id("ifmail"));
		driver.switchTo().frame(iframeYopmail);
	}
}
