package com.mytests;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import com.saucedemo.common.Common_Utilities;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SauceTest {	
	
	WebDriver driver = null;
	Common_Utilities Testdata = new Common_Utilities();
	
	@Parameters({"browser"})
	@BeforeMethod
	public void setup(String browserName, Method name) throws IOException {
	
		System.out.println("browser name is:"+browserName);
		String methodName = name.getName();
		
		String remote = Testdata.getpropertyvalue("remote", "Test_config.properties");
		
		if (remote.matches("true")) {

			//Zalenium: Selenium Grid: HubUrl / Selenium Remote Url
			/*String remoteUrl = "http://" + "naveenautomation" + ":" + "Zalenium2020" + "@" + "104.198.174.10" + "/wd/hub";
			System.out.println("Zalenium hub url us:"+remoteUrl);*/
			
			//Selenoid: Selenium Grid: HubUrl / Selenium Remote Url
			String remoteUrl = "http://34.125.121.70:4444/wd/hub";
			System.out.println("Selenoid hub url us:"+remoteUrl);
	
			//Zalenium: Capabilities
			/*DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("name", methodName);
			cap.setCapability(CapabilityType.PLATFORM_NAME, Platform.LINUX);
			cap.setCapability("build", 1.1);
			cap.setCapability("idleTimeout", 180);
			cap.setCapability("recordVideo", true);
			cap.setCapability("tz", "Asia/Kolkata");*/
			
			//Selenoid: Capabilities
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("name", methodName);
			cap.setCapability("enableVNC", true);
			cap.setCapability("enableVideo", false);
			
			if (browserName.equalsIgnoreCase("chrome")) {
				WebDriverManager.chromedriver().setup();
				cap.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);				
			}
			else if(browserName.equalsIgnoreCase("firefox")) {
				WebDriverManager.firefoxdriver().setup();
				cap.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
			}
			else if(browserName.equalsIgnoreCase("ie")) {
				WebDriverManager.iedriver().setup();
				cap.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
			}
			else if(browserName.equalsIgnoreCase("Edge")) {
				WebDriverManager.edgedriver().setup();
				cap.setCapability(CapabilityType.BROWSER_NAME, BrowserType.EDGE);
			}
			//Selenium Grid: will run on perticular Cloud(AWS, Azure, GCP) Grid
			try {			
				driver = new RemoteWebDriver(new URL(remoteUrl),cap);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);			
			} catch(MalformedURLException e) {
				e.printStackTrace();
			}
		
	    } 
		else if (remote.matches("false")) {
	    	
			if (browserName.equalsIgnoreCase("chrome")) {
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
			}
			else if(browserName.equalsIgnoreCase("firefox")) {
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
			}
			else if(browserName.equalsIgnoreCase("ie")) {
				WebDriverManager.iedriver().setup();
				driver = new InternetExplorerDriver();
			}
			else if(browserName.equalsIgnoreCase("Edge")) {
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
			}
	    }
	    	
	}
	
	@Test
	public void doLogin() throws IOException {
		
		String URL = Testdata.getpropertyvalue("URL", "Test_config.properties");
		String UserName = Testdata.getpropertyvalue("UserName", "Test_config.properties");
		String Password = Testdata.getpropertyvalue("Password", "Test_config.properties");
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get(URL);
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.findElement(By.id("user-name")).sendKeys(UserName);
		driver.findElement(By.id("password")).sendKeys(Password);
		driver.findElement(By.id("login-button")).click();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}
	
	@Test(priority = 1)
	public void checkInventoryItemTest() throws IOException {
		doLogin();
		Assert.assertTrue(driver.findElements(By.xpath("//div[@class='inventory_item']")).size() == 6);
	}
	
	@Test(priority = 2)
	public void checkAddToCartButtonTest() throws IOException {
		doLogin();
		Assert.assertTrue(driver.findElements(By.xpath("//button[text()='Add to cart']")).size() == 6);
	}
	
	@Test(priority = 3)
	public void appLogoTest() throws IOException {
		doLogin();
		Assert.assertTrue(driver.findElement(By.xpath("//div[@class='app_logo']")).isDisplayed());
	}
	
	@Test(priority = 4)
	public void socialTwitterTest() throws IOException {
		doLogin();
		Assert.assertTrue(driver.findElement(By.className("social_twitter")).isDisplayed());
	}
	
	@Test(priority = 5)
	public void socialFacebookTest() throws IOException {
		doLogin();
		Assert.assertTrue(driver.findElement(By.className("social_facebook")).isDisplayed());
	}
	
	@Test(priority = 6)
	public void socialLinkedInTest() throws IOException {
		doLogin();
		Assert.assertTrue(driver.findElement(By.className("social_linkedin")).isDisplayed());
	}
	
	@AfterMethod
	public void Lougout() throws InterruptedException {
		Thread.sleep(2000);
		driver.quit();
		Thread.sleep(2000);
	}
	
}
