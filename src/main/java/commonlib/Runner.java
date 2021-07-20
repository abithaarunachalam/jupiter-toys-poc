package commonlib;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


public class Runner {

    public static WebDriver driver;
    public static Configuration conf;
    public static String envNISP;
    private Util util;


    public Runner() {
        util = new Util();
    }

    public void setup() {
    	 String chromePath = "//usr//local//bin//";
    	 System.setProperty("webdriver.chrome.driver", chromePath+"chromedriver");
         driver = new ChromeDriver();
    }


    public void cleanup() {
        driver.quit();
    }   

}


