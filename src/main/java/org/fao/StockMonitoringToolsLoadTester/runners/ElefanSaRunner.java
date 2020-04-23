package org.fao.StockMonitoringToolsLoadTester.runners;

import java.net.MalformedURLException;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class ElefanSaRunner extends Runner implements Runnable {
	
	private String page = "elefan-sa";
	
	
	public ElefanSaRunner(String baseUrl, Boolean shinyproxy, String chromedriver, String testFile) {
		super(baseUrl, shinyproxy, chromedriver, testFile);
	}

	@Override
	public void run() {
		System.setProperty("webdriver.chrome.driver", this.chromedriver);
		
		HashMap<String, Object> prefs = new HashMap<String, Object>(); 
        prefs.put("profile.managed_default_content_settings.images", 2); 

        ChromeOptions options =new ChromeOptions(); 
        options.setExperimentalOption("prefs", prefs);
        
		
        WebDriver driver=new ChromeDriver(options);
        
        Boolean hasQueryParams;
        try {
			hasQueryParams = org.fao.StockMonitoringToolsLoadTester.utils.Utils.hasQueryParams(BASE_URL);
		} catch (MalformedURLException e) { return; }
        
        String queryDivider = hasQueryParams ? "&" : "?";
        
        String URL = String.format("%s%spage=%s", this.BASE_URL, queryDivider, this.page);
            
        driver.navigate().to(URL);
        if (this.shinyproxy) driver.switchTo().frame("shinyframe");
              
        WebElement uploadFile = new FluentWait<WebDriver>(driver)
        		.withTimeout(Duration.ofSeconds(60))
        		.pollingEvery(Duration.ofMillis(30))
        		.ignoring(NoSuchElementException.class)
        		.until(ExpectedConditions.presenceOfElementLocated(By.id("elefanGaModule-fileSa")));
        

        uploadFile.sendKeys(this.testFile);
        
        
        WebElement runButton = new FluentWait<WebDriver>(driver)
        		.withTimeout(Duration.ofSeconds(60))
        		.pollingEvery(Duration.ofMillis(30))
        		.ignoring(NoSuchElementException.class, ElementClickInterceptedException.class)
        		.until(ExpectedConditions.elementToBeClickable(By.id("elefanGaModule-go_sa")));
        
        
        if(runButton.isEnabled()) {
        	JavascriptExecutor executor = (JavascriptExecutor)driver;
        	executor.executeScript("arguments[0].click();", runButton);
        }
		
	}

}
