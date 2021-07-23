package pages;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.By;
import commonlib.Constants;
import commonlib.ExecutionLogger;
import commonlib.Runner;
import commonlib.WebDriverHelper;

/**
 * @author abitha
 * @implNote This class is contains functions for different scenarios
 *
 */

public class Scenarios extends Runner {

	private static final By tab_contacts = By.xpath("//*[@id=\"nav-contact\"]/a");
	private static final By tab_shop = By.xpath("//*[@id=\"nav-shop\"]/a");
	private static final By tab_submit = By.xpath("//a[contains(text(),'Submit')]");
	private static final By validate_launch_message1 = By.xpath("//div[@id='header-message']/div[text()]");
	private static final By validate_launch_message2 = By.xpath("//div[@id='header-message']/div/strong[text()]");
	private WebDriverHelper webDriverHelper; 
	private Jupitertoyspage jupitertoyspage;

	public Scenarios() {
		webDriverHelper = new WebDriverHelper();
		jupitertoyspage = new Jupitertoyspage();
	}

	public void launchjupiter() {   
		try {
		//driver.get(conf.getProperty("jupiter_URL"));  
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to launch URL" + e.getMessage());
		}
	}   

	public void scenario1_Comments_Validation() { 

		try
		{
			ExecutionLogger.root_logger.info("Scenario1 - Comments Validation Started");
			ExecutionLogger.root_logger.info("---------------------------------------");
			driver.get(Constants.JUPITER_URL);
			webDriverHelper.click(tab_contacts);  
			webDriverHelper.waitForText(validate_launch_message1, Constants.WELCOME_YOUR_FEEDBACK); 
			webDriverHelper.waitForText(validate_launch_message2, Constants.TELL_HOW_IT_IS); 
			webDriverHelper.click(tab_submit); 
			jupitertoyspage.validateFailureMessages();
            webDriverHelper.hardWait(3);
			jupitertoyspage.populateMandatoryFields();
			jupitertoyspage.goToHome();
			webDriverHelper.hardWait(5);
			ExecutionLogger.root_logger.info("Scenario1 - Comments Validation Completed");
			ExecutionLogger.root_logger.info("-----------------------------------------");
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to complete Scenario1" + e.getMessage());
		}
	}

	public void scenario2_Multiple_Comments() 
	{ 
		try {
			ExecutionLogger.root_logger.info("Scenario2 - Multiple Comments Started");
			ExecutionLogger.root_logger.info("-------------------------------------");
			driver.get(Constants.JUPITER_URL);
			webDriverHelper.click(tab_contacts);  
			webDriverHelper.waitForText(validate_launch_message1, Constants.WELCOME_YOUR_FEEDBACK); 
			webDriverHelper.waitForText(validate_launch_message2, Constants.TELL_HOW_IT_IS); 
			for (int i=0;i<5;i++) {
				jupitertoyspage.populateMandatoryFields();
			}
			webDriverHelper.hardWait(5);
			jupitertoyspage.goToHome();
			ExecutionLogger.root_logger.info("Scenario2 - Multiple Comments Completed");
			ExecutionLogger.root_logger.info("---------------------------------------");
		}
		catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+"Unable to complete Scenario2" + e.getMessage());
		}
	}

	public void scenario3_Add_Item_To_Cart() { 
		try
		{
		ExecutionLogger.root_logger.info("Scenario3 - Add Items To Cart Started");
		ExecutionLogger.root_logger.info("-------------------------------------");
		driver.get(Constants.JUPITER_URL);
		webDriverHelper.click(tab_shop); 
		Map<String, Integer> itemMap = new HashMap<String, Integer>();
		itemMap.put(Constants.FLUFFY_BUNNY, 1);
		itemMap.put(Constants.FUNNY_COW, 2);
		jupitertoyspage.buyItem(itemMap);
		jupitertoyspage.checkCart(itemMap);
		jupitertoyspage.goToHome();
		webDriverHelper.hardWait(5);
		ExecutionLogger.root_logger.info("Scenario3 - Add Items To Cart Completed");
		ExecutionLogger.root_logger.info("---------------------------------------");
		}
		catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+"Unable to complete Scenario3" + e.getMessage());
		}
	}

	public void scenario4_Check_Price() {	
		try
		{
		ExecutionLogger.root_logger.info("Scenario4 - Check Price Started");
		ExecutionLogger.root_logger.info("-------------------------------");
		driver.get(Constants.JUPITER_URL);
		webDriverHelper.click(tab_shop); 
		Map<String, Integer> itemMap = new HashMap<String, Integer>();
		itemMap.put(Constants.STUFFED_FROG, 2);
		itemMap.put(Constants.FLUFFY_BUNNY, 5);
		itemMap.put(Constants.VALENTINE_BEAR, 3);
		jupitertoyspage.buyItem(itemMap);
		jupitertoyspage.checkCart(itemMap);
		jupitertoyspage.verifyPrice();
		jupitertoyspage.goToHome();
        webDriverHelper.hardWait(5);
		ExecutionLogger.root_logger.info("Scenario4 - Check Price Completed");
        ExecutionLogger.root_logger.info("---------------------------------");
		}
		catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+"Unable to complete Scenario3" + e.getMessage());
		}
	}
}