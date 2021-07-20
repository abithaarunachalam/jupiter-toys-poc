package pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import commonlib.Configuration;
import commonlib.Runner;
import commonlib.Util;
import commonlib.WebDriverHelper;
import scala.Console;

public class Jupitertoyspage extends Runner {
	
//    private static final By ECHELON_USERNAME = By.xpath("//input[@name='user_username']");
//    private static final By ECHELON_PASSWORD = By.xpath("//input[@name='user_password']");
//    private static final By ECHELON_LOGIN = By.xpath("//button[@id='register-submit-btn']");
    private static final By Play_Button = By.xpath("//*[@id='mod-video-player_module-1']/div/div[6]/div/a");
    private static final By Playpause_Button = By.xpath("//button[@aria-label='Play']");
    private static final By time_Span = By.xpath("//*[@id=\"mod-video-player_module-1\"]/div/div[3]/nav/div[2]/span[1]");
    private static final By tab_contacts = By.xpath("//*[@id=\"nav-contact\"]/a");
    private static final By tab_shop = By.xpath("//*[@id=\"nav-shop\"]/a");
    private static final By tab_submit = By.xpath("//a[contains(text(),'Submit')]");
    
    //private static final By validate_launch_message1 = By.xpath("//div[@class='alert alert-info ng-scope']/strong[text()]");
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
    
    private static final By Cart_empty_check = By.xpath("//*[@id='nav-cart']/a//span");
    private static final By Cart_menu = By.xpath("//*[@id='nav-cart']/a");
    
    private static final By Cart_table = By.xpath("//table[@class='table table-striped cart-items']//tbody");
    
	private Util util;
    private WebDriverHelper webDriverHelper;   
    private Configuration conf;
    String forename = "peter";
    String email = "peter.paul@gmail.com";
    String message = "Nice website - peter";
    
    public Jupitertoyspage() {
        util = new Util();
        webDriverHelper = new WebDriverHelper();
        conf = new Configuration();        
    }
        
    public void launchjupiter() {    	
            driver.get(conf.getProperty("jupiter_URL"));  
           // webDriverHelper.hardWait(5);
    }   
    
    
    public void scenario1() { 
    	
    	webDriverHelper.click(tab_contacts);  
    	webDriverHelper.waitForText(validate_launch_message1,"We welcome your feedback"); 
    	webDriverHelper.waitForText(validate_launch_message2,"- tell it how it is."); 
    	webDriverHelper.click(tab_submit); 
    	validatefailuremessage();
    	populatemandatoryfields();
    	validatesuccessmessage();
    	Console.println("Scenario 1 completed");
    	webDriverHelper.hardWait(5);
    }
    
    public void scenario2() { 
    	
    	webDriverHelper.click(tab_contacts);  
    	webDriverHelper.waitForText(validate_launch_message1,"We welcome your feedback"); 
    	webDriverHelper.waitForText(validate_launch_message2,"- tell it how it is."); 
    	for (int i=0;i<5;i++) {
    	populatemandatoryfields();
    	validatesuccessmessage();
    	}
    	Console.println("Scenario 2 completed");
    	webDriverHelper.hardWait(5);
    }

    public void scenario3() { 
    	  
        webDriverHelper.click(tab_shop); 
        Map<String, Integer> itemMap = new HashMap<String, Integer>();
        itemMap.put("Funny Cow", 2);
        itemMap.put("Fluffy Bunny", 1);
        buyitem(itemMap);
        //buyitem(itemMap);
        //checkcart("Funny Cow", 2);
        //checkcart("Fluffy Bunny", 1);
        Console.println("Cart check completed");
        Console.println("Scenario 3 completed");
    	webDriverHelper.hardWait(5);
    }
    
    public void checkcart(String cart, int count) {  
    	
    	webDriverHelper.click(Cart_menu);
    	//WebElement carttable = driver.findElement(Cart_table);
    	String actualitem = driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[1]//td[1]")).getText();
    	Console.println("actual item " + actualitem);
    	Util.AssertEqualsString("",actualitem, cart);
    	int actualquantity = Integer.parseInt(driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[1]//td[3]//input")).getAttribute("value"));
    	Console.println("actual qty " + actualquantity);
    	Util.AssertEqualsInt("",actualquantity, count);
    	
    	/*
    	List<WebElement> rows=carttable.findElements(By.tagName("tr"));
    	for(int rnum=1;rnum<=rows.size();rnum++)
    	{
    		//WebElement rowvalues = driver.findElement(Cart_table);
    		
    	List<WebElement> columns=rows.get(rnum).findElements(By.tagName("td"));
    	//System.out.println("Number of columns:"+columns.size());
    	 
    	for(int cnum=1;cnum<=columns.size();cnum++)
    	{
    		System.out.println("column = " + cnum);
    		String actual = driver.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[" + rnum + "]//td[" + cnum + "]")).getText();
        	  //Assert.assertEquals("invalid", actual, expected)
      		
    		//Assert.equalscolumns.get(cnum).getText() == cart;
    		
    	System.out.println(actual);
    	
    	}
    	}
    	
    	
        List<WebElement> rowsNumber = driver.findElements(Cart_table);
        int rowCount = rowsNumber.size();
        Console.println(rowCount);
        WebElement cellAddress = rowsNumber.findElement(By.xpath("//table[@class='table table-striped cart-items']//tbody//tr[1]//td[4][text()]"));
        		String value = cellAddress.getText();
        		System.out.println(“The Cell Value is : “ +value);
        		*/
    }
    
    //Map<String, Integer> itemMap
    
    public void buyitem(Map<String, Integer> itemMap) {  
    	
    	itemMap.forEach((k, v) -> System.out.println((k + ":" + v)));
    	
    }
      
    /*public void buyitem(String cart, int count) {  
    	
    	for(int i=0;i<count;i++)  
        {  
        By btn_Item_buy = By.xpath("//li//div//h4[text()='" + cart +"']/following-sibling::p//a[text()='Buy']");
		webDriverHelper.click(btn_Item_buy);
        }
    }*/

    public void cartcount(String count) { 
    	
	if (webDriverHelper.getText(Cart_empty_check).toString() == count) {
		Console.println(" Cart contains " + count + "items");}
		else {
			Console.println(webDriverHelper.getText(Cart_empty_check));
			Console.println(count);}
    }
    
   
    
    public void populatemandatoryfields() { 
    	
	webDriverHelper.click(text_Forename);
	webDriverHelper.clearAndSetText(text_Forename, forename);
	webDriverHelper.click(text_Email);
	webDriverHelper.clearAndSetText(text_Email, email);
	webDriverHelper.click(text_Message);
	webDriverHelper.clearAndSetText(text_Message, message);
	webDriverHelper.hardWait(1);
	webDriverHelper.click(tab_submit);
	
}
    public void validatefailuremessage() { 
    	
    	webDriverHelper.waitForText(validate_launch_message1,"We welcome your feedback"); 
    	webDriverHelper.waitForText(validate_launch_message2," - but we won't get it unless you complete the form correctly.");
    	webDriverHelper.waitForText(validate_Forename_Required,"Forename is required");
    	webDriverHelper.waitForText(validate_Email_Required,"Email is required");
    	webDriverHelper.waitForText(validate_Message_Required,"Message is required");
    }
    
    public void validatesuccessmessage() { 
    
    webDriverHelper.waitForElementNotVisible(pop_up_Modal);	
	webDriverHelper.waitForText(validate_launch_message4,", we appreciate your feedback.");
	webDriverHelper.waitForText(validate_launch_message3,"Thanks "+ forename);
	webDriverHelper.click(btn_Back); 
    }
}
