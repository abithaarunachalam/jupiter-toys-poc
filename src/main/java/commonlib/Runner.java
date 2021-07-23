package commonlib;


import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author abitha
 * @implNote This class is used to for setting up drivers
 *
 */

public class Runner {

    public static WebDriver driver;
    public static Configuration conf;
    public static String envNISP;


    public Runner() {}

    public void setup() {
    	// Two ways of instantiating the chrome driver
    	
    	//The below code has been commented. If we have the latest version of the 
    	// chrome driver downloaded, please copy paste the driver in /usr/local/bin 
    	// and uncomment the below code
    	 // String chromePath = "//usr//local//bin//";
    	 // System.setProperty("webdriver.chrome.driver", chromePath+"chromedriver");

    	// I have downloaded the chrome driver and copied in to project folder - 
    	// src/main/resources/config/drivers. 
    	
    	System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + 
    			File.separator + "resources" + File.separator +"config" + 
    			File.separator + "drivers" + File.separator + "chromedriver");
    	driver = new ChromeDriver();
    }

    public void cleanup() {
        driver.quit();
    }   

}


