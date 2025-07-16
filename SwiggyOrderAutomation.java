import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SwiggyOrderAutomation {

	public static void main(String[] args) throws InterruptedException, IOException {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\DEEPAK\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
		driver.manage().window().maximize();

		// Step 1: Open Swiggy
		driver.get("https://www.swiggy.com/");
		System.out.println("Swiggy opened.");
		
		System.out.println("Title: " + driver.getTitle());
		System.out.println("URL: " + driver.getCurrentUrl());


		// Step 2: Wait and Click Login
		try {
			WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Sign in']")));
			loginBtn.click();
			System.out.println("Login button clicked.");
		} catch (TimeoutException e) {
			System.out.println("Login button not found. Exiting.");
			driver.quit();
			return;
		}

//		// Step 3: Enter Mobile Number
//		WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mobile")));
//		phoneInput.sendKeys("7903829763");
//		System.out.println("Enter OTP manually (within 25 seconds)");
//		Thread.sleep(25000);  // Manual OTP entry
		// Step 3: Enter Mobile Number
		WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mobile")));
		phoneInput.sendKeys("7903829763");

		// Wait manually to enter OTP
		System.out.println("Enter OTP manually (within 25 seconds)");
		Thread.sleep(22);  // Manual OTP entry

		// Step 4: Click the Login Button (by class name "lyOGZ")
		WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("lyOGZ")));
		//lyOGZ
		loginButton.click();

		System.out.println("Login button clicked after manual OTP entry.");



		// Step 4: Enter location
		WebElement locationInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("location")));
		locationInput.sendKeys("Bengaluru");
		Thread.sleep(3000);
		locationInput.sendKeys(Keys.ARROW_DOWN);
		locationInput.sendKeys(Keys.ENTER);
		System.out.println("Location entered.");

		// Step 5: Search for restaurant
		WebElement searchBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[1]/header/div/div/ul/li[5]/div/a")));
		searchBtn.click();
		
		

		// Corrected placeholder text from screenshot
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
		    By.xpath("//input[@placeholder='Search for restaurant, itemxx or more']")));

		searchBox.sendKeys("Domino's Pizza");
		Thread.sleep(3000); // Not ideal — prefer WebDriverWait if possible
		searchBox.sendKeys(Keys.ARROW_DOWN);
		searchBox.sendKeys(Keys.ENTER);


		// Step 6: Restaurant name print
		String restaurantName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class*='restaurant-name']"))).getText();
		System.out.println("Restaurant: " + restaurantName);

		// Step 7: Add Food Item
		List<WebElement> items = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'item-details')]//h3")));
		if (items.size() >= 2) {
			String foodItem = items.get(1).getText();
			System.out.println("Selected Food Item: " + foodItem);
			WebElement addBtn = driver.
					findElements(By.xpath("//button[contains(text(),'ADD')]")).get(1);
			addBtn.click();
		}

		// Step 8: Screenshot of cart
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File("./screenshots/addedItem.png"));

		// Step 9: View Cart
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='View Cart']"))).click();
		Thread.sleep(2000);

		// Step 10: Increase Quantity
		WebElement plusBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'quantity')]//div[contains(@class,'increase')]")));
		plusBtn.click();
		File qtyScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(qtyScreenshot, new File("./screenshots/increasedQty.png"));

		// Step 11: Enter Address
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Add New Address']"))).click();
		Thread.sleep(2000);
		driver.findElement(By.name("house")).sendKeys("123 ABC");
		driver.findElement(By.name("landmark")).sendKeys("Near XYZ Park");
		driver.findElement(By.xpath("//button[text()='Home']")).click();
		driver.findElement(By.xpath("//button[text()='Save Address & Proceed']")).click();
		File addressScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(addressScreenshot, new File("./screenshots/addressEntered.png"));

		// Step 12: Payment Page
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Proceed to Pay']"))).click();
		Thread.sleep(3000);

		// Step 13: Print Cart Total
		String cartTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'final-price')]"))).getText();
		System.out.println("Cart Total: " + cartTotal);

		// Close driver
		driver.quit();
	}
}
