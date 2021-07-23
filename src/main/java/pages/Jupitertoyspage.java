package pages;

import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import commonlib.Runner;
import commonlib.Util;
import commonlib.WebDriverHelper;
import commonlib.Constants;
import commonlib.ExecutionLogger;
import scala.Console;

public class Jupitertoyspage extends Runner {

	private static final By tab_submit = By.xpath("//a[contains(text(),'Submit')]");
	private static final By validate_launch_message1 = By.xpath("//div[@id='header-message']/div[text()]");
	private static final By validate_launch_message2 = By.xpath("//div[@id='header-message']/div/strong[text()]");
	private static final By validate_Forename_Required = By.xpath("//span[@id='forename-err'][text()]");
	private static final By validate_Email_Required = By.xpath("//span[@id='email-err'][text()]");
	private static final By validate_Message_Required = By.xpath("//span[@id='message-err'][text()]");
	private static final By validate_launch_message3 = By.xpath("//div[@class='alert alert-success']/strong[text()]");
	private static final By validate_launch_message4 = By.xpath("//div[@class='alert alert-success'][text()]");    
	private static final By text_Forename = By.xpath("//*[@id=\"forename\"]");
	private static final By text_Email = By.xpath("//*[@id=\"email\"]");
	private static final By text_Message = By.xpath("//*[@id=\"message\"]");
	private static final By btn_Back = By.xpath("//a[@class = 'btn']");
	private static final By pop_up_Modal = By.xpath("//div[@class = 'progress progress-info wait']");
	private static final By go_Home = By.xpath("//*[@id='nav-home']/a");
	private static final By Cart_menu = By.xpath("//*[@id='nav-cart']/a");
	private static final By Cart_total = By.xpath("//table[@class='table table-striped cart-items']//tfoot/tr[1]/td/strong");
	private static final By Cart_table = By.xpath("//table[@class='table table-striped cart-items']//tbody");

	private Util util;
	private WebDriverHelper webDriverHelper;   
	private String forename = "";
	private String email = "";
	private String message = "";

	public Jupitertoyspage() {
		util = new Util();
		webDriverHelper = new WebDriverHelper();     
	} 

	public void verifyPrice() { 
		try
		{
			Double total = 0.0;
			webDriverHelper.click(Cart_menu);
			WebElement carttable = driver.findElement(Cart_table);	
			List<WebElement> rows=carttable.findElements(By.tagName("tr"));
			for(int rnum=1;rnum<=rows.size();rnum++)
			{
				String actualitem = driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[" + rnum + "]//td[1]")).getText();
				String price = driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[" + rnum + "]//td[2]")).getText();
				String subtotal = driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[" + rnum + "]//td[4]")).getText();
				int actualquantity = Integer.parseInt(driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[" + rnum + "]//td[3]//input")).getAttribute("value"));
				switch(actualitem) {
				case Constants.TEDDY_BEAR:
					Util.AssertEqualsString("", price,"$12.99");
					Console.println("Price of Teddy Bear is " + price);
					break;
				case Constants.STUFFED_FROG:
					Util.AssertEqualsString("", price,"$10.99");
					Console.println("Price of Stuffed Frog is " + price);
					break;
				case Constants.HANDMADE_DOLL:
					Util.AssertEqualsString("", price,"$10.99");
					Console.println("Price of Handmade Doll is " + price);
					break;
				case Constants.FLUFFY_BUNNY:
					Util.AssertEqualsString("", price,"$9.99");
					Console.println("Price of Fluffy Bunny is " + price);
					break;
				case Constants.SMILEY_BEAR:
					Util.AssertEqualsString("", price,"$14.99");
					Console.println("Price of Smiley Bear is " + price);
					break;
				case Constants.FUNNY_COW:
					Util.AssertEqualsString("", price,"$10.99");
					Console.println("Price of Funny Cow is " + price);
					break;
				case Constants.VALENTINE_BEAR:
					Util.AssertEqualsString("", price,"$14.99");
					Console.println("Price of Valentine Bear is " + price);
					break;
				case Constants.SMILEY_FACE:
					Util.AssertEqualsString("", price,"$9.99");
					Console.println("Price of Smiley Face is " + price);
					break;
				default:
					Util.AssertEqualsString("", price,"");
					Console.println("Price is " + price);
				}
				Double calculated_subtotal = Util.convertStrToDecimal(price,2) * actualquantity;
				String finalPrice = util.addcurrency(calculated_subtotal);
				Util.AssertEqualsString("", finalPrice,subtotal);
				total = total + calculated_subtotal;
				ExecutionLogger.root_logger.info("Subtotal of " + actualquantity + " " + actualitem + " is : " + finalPrice);
			}
			String totalamount = driver.findElement(Cart_total).getText();
			Util.AssertEqualsString("", totalamount, Constants.TOTAL + total);
			ExecutionLogger.root_logger.info("Total Amount is " + total);
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to verify price" + e.getMessage());
		}
	}



	public void checkCart(Map<String, Integer> itemMap) {  
		try
		{
			webDriverHelper.click(Cart_menu);
			WebElement carttable = driver.findElement(Cart_table);	
			List<WebElement> rows=carttable.findElements(By.tagName("tr"));
			itemMap.forEach(
					(k, v) -> {
						for(int rnum=1;rnum<=rows.size();rnum++)
						{
							String actualitem = driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[" + rnum + "]//td[1]")).getText();
							if ( actualitem.contains(k))
							{
								int actualquantity = Integer.parseInt(driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[" + rnum + "]//td[3]//input")).getAttribute("value"));
								Util.AssertEqualsInt("", v,actualquantity);
								ExecutionLogger.root_logger.info(actualquantity + " Quantity of "+ actualitem +" added");
							}
						}
					}
					);
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to check cart" + e.getMessage());
		}
	}

	public void buyItem(Map<String, Integer> itemMap) { 
		try
		{
			itemMap.forEach((k, v) -> System.out.println((k + ":" + v)));
			for (Map.Entry<String, Integer> e : itemMap.entrySet())
			{
				By btn_Item_buy = By.xpath("//li//div//h4[text()='" + e.getKey() +"']/following-sibling::p//a[text()='Buy']");
				Integer count = e.getValue();
				for (int i=0;i<count;i++) {
					webDriverHelper.click(btn_Item_buy);
				}

			}
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to buy item" + e.getMessage());
		}
	}

	public void populateMandatoryFields() { 
		try
		{	
			forename = util.generatetName(Constants.FORENAME);
			email = util.generateEmailId();
			message = util.generateMessage();
			webDriverHelper.click(text_Forename);
			webDriverHelper.clearAndSetText(text_Forename, forename);
			webDriverHelper.click(text_Email);
			webDriverHelper.clearAndSetText(text_Email, email);
			webDriverHelper.click(text_Message);
			webDriverHelper.clearAndSetText(text_Message, message);
			webDriverHelper.hardWait(1);
			webDriverHelper.click(tab_submit);
			webDriverHelper.waitForElementNotVisible(pop_up_Modal);	
			webDriverHelper.waitForText(validate_launch_message4, Constants.APPRECIATE_YOUR_FEEDBACK);
			webDriverHelper.waitForText(validate_launch_message3, Constants.THANKS + forename);
			webDriverHelper.click(btn_Back); 
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to populate mandatory fields" + e.getMessage());
		}

	}
	public void validateFailureMessages() { 
		try
		{
			webDriverHelper.waitForText(validate_launch_message1, Constants.WELCOME_YOUR_FEEDBACK); 
			webDriverHelper.waitForText(validate_launch_message2, Constants.COMPLETE_YOUR_FORM);
			webDriverHelper.waitForText(validate_Forename_Required, Constants.FORENAME_REQUIRED);
			webDriverHelper.waitForText(validate_Email_Required, Constants.EMAIL_REQUIRED);
			webDriverHelper.waitForText(validate_Message_Required, Constants.MESSAGE_REQUIRED);
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to validate failure messages" + e.getMessage());
		}
	}

	public void goToHome() { 

		try
		{
			webDriverHelper.click(go_Home);
		}catch (Exception e) {
			ExecutionLogger.root_logger.error(this.getClass().getName()+ "Unable to go home" + e.getMessage());
		}
	}
}
