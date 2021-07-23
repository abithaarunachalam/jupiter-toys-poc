package commonlib;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author abitha
 * @implNote This class is a container for different functions of web driver
 *
 */

public class WebDriverHelper extends Runner {

	public enum Locators {xpath, id, name, classname, linktext, tagname, paritallinktext, cssLocator}

	public static String parentWindow;
	public static String childWindow;
	public static String time = "";
	private static final long DEFAULT_TIME_OUT = 20;
	private static final long IMPLICIT_WAIT = 10;
	private static final long FLUENT_WAIT = 120;
	public static String screenShotPath;
	public static Configuration conf = new Configuration();

	public WebElement getWebElement(Locators locator, String element) throws Exception {
		By byElement;
		switch (locator) {            //determine which locator item we are interested in
			case xpath:
				byElement = By.xpath(element);
				break;
			case id:
				byElement = By.id(element);
				break;
			case name:
				byElement = By.name(element);
				break;
			case classname:
				byElement = By.className(element);
				break;
			case linktext:
				byElement = By.linkText(element);
				break;
			case paritallinktext:
				byElement = By.partialLinkText(element);
				break;
			case tagname:
				byElement = By.tagName(element);
				break;
			default:
				throw new Exception();
		}
		WebElement query = driver.findElement(byElement);    //grab our element based on the locator
		return query;    //return our query
	}

	public WebElement findElement(By by) {
		WebElement element = null;
		try {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
			element = driver.findElement(by);
			ExecutionLogger.file_logger.info(by + " FOUND");
		} catch (NoSuchElementException e) {
			ExecutionLogger.root_logger.error(by + "NOT FOUND " + e);
			Assert.fail(by + " NOT FOUND!");
		}
		return element;
	}

	public void click(WebElement element) {
		if (element.isDisplayed()) {
			element.click();
			try {
				wait(1);
			} catch (InterruptedException e) {
				Assert.fail();
			}
			//System.out.println("Element click " + element + " successful");
		} else {
			ExecutionLogger.root_logger.error("Element " + element + " is not displayed in browser");
			Assert.fail();
		}
	}

	public boolean isElementExist(By by, long d) {
		@SuppressWarnings("unused")
		WebElement element = null;
		try {
			driver.manage().timeouts().implicitlyWait(d, TimeUnit.SECONDS);
			element = driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void verifyElementNotDisplayed(By by, long d) {
		@SuppressWarnings("unused")
		WebElement element = null;
		try {
			driver.manage().timeouts().implicitlyWait(d, TimeUnit.SECONDS);
			element = driver.findElement(by);
			Assert.fail("HTTP Request Failed Error occurred!"); //if Element is found
		} catch (NoSuchElementException e) {
			// Element is NOT found
		}
	}

	public boolean isElementDisplayed(final By locator, long d) {
		try {
			new FluentWait<>(driver)
				.withTimeout(d, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return (driver.findElement(locator).isDisplayed());	
		} catch (Exception e) {
			//ExecutionLogger.root_logger.error("Element "+locator+" not found"+e);
			// Should not fail is not displayed, just return false
			//Assert.fail("Element " + locator + " not found");
		}
		return false;
	}

	public boolean isElementEnabled(final By locator, long d) {
		try {
			new FluentWait<>(driver)
				.withTimeout(d, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class)
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return (driver.findElement(locator).isDisplayed());	
		} catch (Exception e) {
			//ExecutionLogger.root_logger.error("Element "+locator+" not found"+e);
			// Should not fail is not displayed, just return false
			//Assert.fail("Element " + locator + " not found");
		}
		return false;
	}

	public void click(By by) {
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		//WebElement element = null;
		try {
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
			//WebElement element = driver.findElement(by);
			waitForElementClickable(by);
			waitForElementEnabled(by);
			//if (element.isDisplayed()) {
			element.getTagName();
			element.click();
			ExecutionLogger.file_logger.info("Element click " + element + " successful");
		} catch (TimeoutException te) {
			ExecutionLogger.root_logger.error("Element " + by + " is not displayed " + te);
			//extentReport.createFailStepWithScreenshot("Element " + by + " is not displayed ");
			Assert.fail("Element " + by + " is not displayed");
		} catch (NoSuchElementException ne) {
			System.err.println("Element " + by + " cannot be found");
			ExecutionLogger.root_logger.error("Element " + by + " cannot be found " + ne);
			Assert.fail("Element " + by + " cannot be found");
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + by + " is not displayed " + e);
			Assert.fail("Element " + by + " is not displayed");
		}
	}

	public void clickByJavaScript(final WebElement element) {
		try {
			if(System.getProperty("os.name").contains("Windows 10")) {
				Actions builder = new Actions(driver);
				builder.moveToElement(element, 10, 10).click(element);
				builder.build().perform();
			}
			else
			{
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
				ExecutionLogger.file_logger.info("Element click  " + element + " successful by JavaScript");
			}
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + element + "not clickable by JavaScript " + e);
			Assert.fail();
		}
	}

	public void clickByJavaScript(By by) {
		WebElement element = null;
		element = findElement(by);
		waitForElementDisplayed(by);
		waitForElementClickable(by);
		waitForElementEnabled(by);
		//waitForStaleStatus(by);
		try {
			if(System.getProperty("os.name").contains("Windows 10"))
			{
				Actions builder = new Actions(driver);
				builder.moveToElement(element,10,10).click(element);
				builder.build().perform();

			}
			else
			{
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
				ExecutionLogger.file_logger.info("Element click  " + element + " successful by JavaScript");
			}
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + element + "not clickable by JavaScript " + e);
			Assert.fail();
		}
	}



	public String getValue(By by) {
		WebElement element = null;
		String strText = "";
		element = findElement(by);
		if (element != null) {
			if (element.isDisplayed()) {
				strText = element.getAttribute("value");
				ExecutionLogger.file_logger.info("Element value is " + strText);
			} else {
				ExecutionLogger.root_logger.error("Element " + element + " is not displayed");
				Assert.fail("Element " + element + " is not displayed");
			}
		} else {
			ExecutionLogger.file_logger.info("Element " + by + " is not found.");
		}
		return strText;
	}

	public String getText(By by) {
		WebElement element = null;
		String strText = "";
		element = findElement(by);
		if (element != null) {
			if (element.isDisplayed()) {
				strText = element.getText();
				ExecutionLogger.file_logger.info("Element text is " + strText);
			} else {
				ExecutionLogger.root_logger.error("Element " + element + " is not displayed");
				Assert.fail("Element " + element + " is not displayed");
			}
		} else {
			ExecutionLogger.file_logger.info("Element " + by + " is not found.");
		}
		return strText;
	}

	/* Function to get the text in a string array from list of web elements*/
	public String[] getWebElementsText(By by) {
		List<WebElement> elementlist;
		String[] strText = {};
		elementlist = driver.findElements(by);
		if (elementlist != null) {
			strText = new String[elementlist.size()];
			ExecutionLogger.file_logger.info("List of Elements Found");
			for (int i = 0; i < elementlist.size(); i++) {
				strText[i] = elementlist.get(i).getText().trim();
				ExecutionLogger.file_logger.info("Element: " + strText[i]);
			}
		} else {
			ExecutionLogger.file_logger.info("Elementlist " + elementlist + " is empty.");
		}
		ExecutionLogger.file_logger.info(" ");
		return strText;
	}

	public void setCheck(By by, String value) {
		if (!value.equals("IGNORE")) {
			if (!value.isEmpty()) {
				WebElement element = null;
				element = findElement(by);
				if (element != null) {
					if (element.isDisplayed()) {
						// check checkbox state before doing anything
						if (value.equals("Yes")) {
							if (!element.isSelected()) {
								element.click();
							}
						} else if (value.equals("No")) {
							if (element.isSelected()) {
								element.click();
							}
						}
						ExecutionLogger.file_logger.info("Element " + element + "set to " + value);
					} else {
						ExecutionLogger.root_logger.error("Element " + element + " is not displayed");
						Assert.fail("Element " + element + " is not displayed");
					}
				}
			}
		}
	}

	// generic selenium click functionality implemented
	public void click(Locators locator, String element) throws Exception {
		if (!element.equals("IGNORE")) {
			try {
				click(getWebElement(locator, element));
				ExecutionLogger.file_logger.info("Element " + element + "click is successfull");
			} catch (Exception e) {
				ExecutionLogger.root_logger.error("Element " + element + "not clickable " + e);
				Assert.fail();
			}
		}
	}

	//to wait for set amount of time
	public void wait(int seconds) throws InterruptedException {
		Thread.sleep(seconds * 1000);
	}

	public void wait(double seconds) throws InterruptedException {
		Thread.sleep(Double.doubleToLongBits(seconds * 1000));
	}


	public void waitForElementDisplayed(WebElement element, int seconds) throws Exception {
		//wait for up to XX seconds for our error message
		long end = System.currentTimeMillis() + (seconds * 1000);
		while (System.currentTimeMillis() < end) {
			// If results have been returned, the results are displayed in a drop down.
			if (element.isDisplayed()) {
				break;
			}
		}
	}

	public void sleep(double seconds) {
		try {
			Thread.sleep((long) (1000 * seconds));
		} catch (InterruptedException e) {
		}
	}

	//a method for checking id an element is displayed
	public void checkElementDisplayed(Locators locator, String element) throws Exception {
		if (!element.equals("IGNORE")) {
			checkElementDisplayed(getWebElement(locator, element));
		}
	}

	public void checkElementDisplayed(WebElement element) throws Exception {
		assertTrue(element.isDisplayed());
	}

	//a method to simulate the mouse hovering over an element
	public void hover(Locators locator, String element) throws Exception {
		hover(getWebElement(locator, element));
	}

	public void hover(WebElement element) throws Exception {
		Actions selAction = new Actions(driver);
		selAction.moveToElement(element).perform();
	}

	//our generic selenium type functionality
	public void setText(Locators locator, String element, String text) throws Exception {
		if (!text.equals("IGNORE")) {
			setText(getWebElement(locator, element), text);
		}
	}

	public void setText(WebElement element, String text) {
		if (!text.equals("IGNORE")) {
			if (element.isEnabled()) {
				element.sendKeys(text);
				ExecutionLogger.file_logger.info("Element set - " + element + " - " + text);
			} else {
				ExecutionLogger.root_logger.error("Element: " + element + " not enabled");
				Assert.fail("Element: " + element + " not enabled");
			}
		}
	}

	public void setText(By by, String text) {
		if (!text.equals("IGNORE")) {
			if (!text.isEmpty()) {

				driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
				WebElement element = null;
				try {
					element = findElement(by);
					if (element.isDisplayed()) {
						if (element.isEnabled()) {
							//element.clear();
							element.sendKeys(text);
							ExecutionLogger.file_logger.info("Element set - " + element + " - " + text);
						} else {
							ExecutionLogger.root_logger.error("Element: " + element + " not enabled");
							Assert.fail("Element: " + element + " not enabled");
						}
					} else {
						ExecutionLogger.root_logger.error("Element " + element + " is not displayed");
						Assert.fail("Element " + element + " is not displayed");
					}
				} catch (NoSuchElementException e) {
					ExecutionLogger.root_logger.error("Element " + element + " cannot be found");
					Assert.fail("Element " + element + " cannot be found");
				}
			}
		}
	}


	public void typeKeys(By by, String text) {
		if (!text.equals("IGNORE")) {
			driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
			WebElement element = null;
			try {
				element = findElement(by);
				if (element.isDisplayed()) {
					if (element.isEnabled()) {
						//element.clear();
						Actions Builder = new Actions(driver);
						Action EditEnter = Builder.sendKeys(element, text).build();
						EditEnter.perform();
						ExecutionLogger.file_logger.info("Element set - " + element + " - " + text);
					} else {
						ExecutionLogger.root_logger.error("Element: " + element + " not enabled");
						Assert.fail("Element: " + element + " not enabled");
					}
				} else {
					ExecutionLogger.root_logger.error("Element " + element + " is not displayed");
					Assert.fail("Element " + element + " is not displayed");
				}
			} catch (NoSuchElementException e) {
				ExecutionLogger.root_logger.error("Element " + element + " cannot be found");
				Assert.fail("Element " + element + " cannot be found");
			}
		}
	}

	public void select(By by, String text) {
		if (!text.equals("IGNORE")) {
			if (!text.isEmpty()) {
				driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
				WebElement element;
				try {
					element = findElement(by);
					if (element.isDisplayed()) {
						if (element.isEnabled()) {
							element.sendKeys(text);
							ExecutionLogger.file_logger.info("Element select - " + element + " - " + text);
						} else {
							ExecutionLogger.root_logger.error("Element: " + element + " not enabled");
							Assert.fail("Element: " + element + " not enabled");
						}
					} else {
						ExecutionLogger.root_logger.error("Element " + element + " is not displayed");
						Assert.fail("Element " + element + " is not displayed");
					}
				} catch (NoSuchElementException e) {
					ExecutionLogger.root_logger.error("Element " + by + " cannot be found");
					Assert.fail("Element " + by + " cannot be found");
				}
			}
		}
	}

	public void select(WebElement element, String text) {
		if (!text.equals("IGNORE")) {
			if (element.isEnabled()) {
				element.sendKeys(text);
				ExecutionLogger.file_logger.info("Element select - " + element + " - " + text);
			} else {
				ExecutionLogger.root_logger.error("Element: " + element + " not enabled");
				Assert.fail("Element: " + element + " not enabled");
			}
		}
	}

	//	public void selectionFromDropdown(String htmltagName, String toClick) {
	public void listSelectByTagName(String htmltagName, String toSelect) {
		if (!toSelect.equals("IGNORE")) {
			Boolean found = false;
//		driver=justForDriver();
			List<WebElement> options = driver.findElements(By.tagName(htmltagName));
			for (WebElement option : options) {
				if (toSelect.equals(option.getText())) {
					option.click();
					found = true;
					break;
				}
			}
			if (!found) {
				ExecutionLogger.root_logger.error("Item: " + toSelect + " not found");
				Assert.fail("Item: " + toSelect + " not found");
			}
		}
	}

	//Ramya
	public void listSelectByTagAndObjectName(By by, String htmltagName, String toSelect) {
		if (!toSelect.equals("IGNORE")) {
			click(by);
			Boolean found = false;
			if (toSelect.equalsIgnoreCase("ANYVALUE")) {
				List<WebElement> options = driver.findElements(By.tagName(htmltagName));
				for (WebElement option : options) {
					if (!option.getText().equals("<none>")) {
						option.click();
						found = true;
						break;
					}
				}
			} else {
				List<WebElement> options = driver.findElements(By.tagName(htmltagName));
				for (WebElement option : options) {
					if (toSelect.equals(option.getText())) {
						option.click();
						hardWait(1);
						if(isElementExist(by,1)) {
							String press = Keys.chord(Keys.SHIFT, Keys.TAB);
							enterTextByActions(by, press);
						}
						found = true;
						break;
					}
				}
			}
			if (!found) {
				ExecutionLogger.root_logger.error("Item: " + toSelect + " not found");
				Assert.fail("Item: " + toSelect + " not found");
			}
		}
	}

	

	public void radioSelect(WebDriver driver, String rgName, String option) {
		if (!option.equals("IGNORE")) {
			By by = By.cssSelector("[name*=" + rgName + "][value='" + option + "']");
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);

			try {
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
				element.click();
				ExecutionLogger.file_logger.info("Element click " + element + " successful");
			} catch (TimeoutException te) {
				ExecutionLogger.root_logger.error("Element " + by + " is not displayed");
				Assert.fail("Element " + by + " is not displayed");
			} catch (NoSuchElementException e) {
				ExecutionLogger.root_logger.error("Element " + by + " cannot be found");
				Assert.fail("Element " + by + " cannot be found");
			}
		}
	}

	public void waitForObjectDisplayed(final WebElement Object) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (Object != null);
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(pageLoadCondition);
	}

	public void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(pageLoadCondition);
	}

	public void waitForElementLoad(String idPart) {
		final String idPartStr = idPart;
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (driver.findElement(By.cssSelector("[id*=" + idPartStr + "]")) != null);
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(pageLoadCondition);
	}

	public void waitForLinkLoad(String linkText) {
		final String linkTextStr = linkText;
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return (driver.findElement(By.linkText(linkTextStr)) != null);
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(pageLoadCondition);
	}

	public void clearAndSetText(WebElement element, String text) {
		if (!text.equals("IGNORE")) {
			element.clear();
			setText(element, text);
		}
	}

	public void clearAndSetText(By by, String text) {
		if (!text.equals("IGNORE")) {
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
			try {
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
				if(conf.getProperty("ChromeVersion").equalsIgnoreCase("72")) {
					((JavascriptExecutor) driver).executeScript("arguments[0].value='" + text + "';", element);
				}
				else {
					element.clear();
					setText(element, text);
				}
			} catch (TimeoutException te) {
				ExecutionLogger.root_logger.error("Element " + by + " is not displayed");
				Assert.fail("Element " + by + " is not displayed");
			} catch (NoSuchElementException e) {
				ExecutionLogger.root_logger.error("Element " + by + " cannot be found");
				Assert.fail("Element " + by + " cannot be found");
			}
		}
	}

	public void clearWaitAndSetText(By by, String text) {
		if (!text.equals("IGNORE")) {
			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
			try {
				hardWait(2);
				WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
				element.clear();
				hardWait(1);
				String press = Keys.chord(Keys.SHIFT, Keys.TAB);
				setText(element, text + press);
			} catch (TimeoutException te) {
				ExecutionLogger.root_logger.error("Element " + by + " is not displayed");
				Assert.fail("Element " + by + " is not displayed");
			} catch (NoSuchElementException e) {
				ExecutionLogger.root_logger.error("Element " + by + " cannot be found");
				Assert.fail("Element " + by + " cannot be found");
			}
		}
	}


	// This method accepts a string which may contain multiple numeric values representing financial/non financial amounts with decimal/no decimal.
	// This method extracts out all the numeric values only, removing any comma and dollar signs etc. appearing before or within the numeric value
	// and returns pure numeric values as a list in same sequence in list as the values appear in the input string
	// @param any string containing any number of financial/non financial numeric values
	// @return List<String> containing only numeric values
	//
	public List<String> getAllNumericValuesFromNonNumericString(String strVal) {
		String patternStr1 = "([\\D]*)([\\d.]*)";
		String patternStr2 = "([\\D]*)([\\d.,]*)";
		Pattern pattern = Pattern.compile(patternStr2);
		Matcher matcher = pattern.matcher(strVal);
		List<String> numLst = new ArrayList<String>();
		String num = null;
		while (matcher.find()) {
			num = matcher.group(2);
			if (num != null && !num.isEmpty()) {
				num = num.replaceAll(patternStr1, "$2");
				numLst.add(num);
			}
		}
		return numLst;
	}

	public boolean isLink(String link) {
		return link.contains("http://") || link.contains("https://");
	}

	// This method can be used to click on the difficult web elements which are found/searched properly, but when try to
	// click the webelement, it throws error like - Element is not visible and can not be interacted with - even if the
	// element is properly visible and displayed on the web page. In such situations,using JQuery is one of the best solutions
	// to perform click or interact with web element. Selenium inherently supports JQuery
	//
	public void clickWebElementUsingJQuery(String cssSelectorStr) {
		//cssSelectorStr can be any normal cssSelector which can identify the element uniquely like cssSelector="input[id$=_chkAgree][type=checkbox]"
		((JavascriptExecutor) driver).executeScript("$('" + cssSelectorStr + "').click()");
	}

	/*public void clickOnElement(final By locator) {
		waitForElementClickable(locator).click();
	}*/
	public void clickOnElement(String xpath) {
		click(By.xpath(xpath));
	}

	public WebElement waitForElementClickable(final By locator) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		WebElement element = null;
		try {
			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + locator + " not found");
			Assert.fail("Element " + locator + " not found");
		}
		return element;
	}

	public void waitForElementClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void waitForElementVisible(WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void waitForElementVisible(By by) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public void waitForElementNotVisible(By by) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}

	public void selectDropDownOptionEquals(By locator, String text) {
		if (!text.equals("IGNORE")) {

			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			clickByJavaScript(element);

			try {
				List<WebElement> allOptions = element.findElements(By.tagName("option"));

				for (WebElement allOption : allOptions) {
					// System.out.println(allOptions.get(i).getText());
					if ((allOption.getText().trim().toLowerCase().equalsIgnoreCase(text.trim().toLowerCase()))) {
						ExecutionLogger.file_logger.info("Option " + text + " found");
						allOption.click();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ExecutionLogger.root_logger.error("Option " + text + " not found in " + element.getText());
				Assert.fail("Option " + text + " not found");
			}
		}
	}

	public void selectDropDownOption(By locator, String text) {
		if (!text.equals("IGNORE")) {

			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			clickByJavaScript(element);

			try {
				List<WebElement> allOptions = element.findElements(By.tagName("option"));

				for (WebElement allOption : allOptions) {
					// System.out.println(allOptions.get(i).getText());
					if ((allOption.getText().trim().toLowerCase().matches(text.trim().toLowerCase()))) {
						ExecutionLogger.file_logger.info("Option " + text + " found");
						allOption.click();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ExecutionLogger.root_logger.error("Option " + text + " not found in " + element.getText());
				Assert.fail("Option " + text + " not found");
			}
		}
	}

	public void selectDropDownOptionContains(By locator, String text) {
		if (!text.equals("IGNORE")) {

			WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			clickByJavaScript(element);

			try {
				List<WebElement> allOptions = element.findElements(By.tagName("option"));

				for (WebElement allOption : allOptions) {
					// System.out.println(allOptions.get(i).getText());
					if ((allOption.getText().trim().toLowerCase().contains(text.trim().toLowerCase()))) {
						ExecutionLogger.file_logger.info("Option " + text + " found");
						allOption.click();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ExecutionLogger.root_logger.error("Option " + text + " not found in " + element.getText());
				Assert.fail("Option " + text + " not found");
			}
		}
	}

	public String waitAndGetText(By by) {
		waitForText(by);
		return getText(by);
	}

	public void waitForText(final By locator) {
		try {
			new FluentWait<>(driver)
					.withTimeout(FLUENT_WAIT, TimeUnit.SECONDS)
					.pollingEvery(10, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.visibilityOfElementLocated(locator));			
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + locator + " not found" + e);
			Assert.fail("Element " + locator + " not found");
		}
	}

	public void waitForElement(final By locator) {
		try {
			new FluentWait<>(driver)
					.withTimeout(FLUENT_WAIT, TimeUnit.SECONDS)
					.pollingEvery(10, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.visibilityOfElementLocated(locator));		
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + locator + " not found" + e);
			Assert.fail("Element " + locator + " not found");
		}
	}

	public void waitForElementAndHardWait(final By locator, int waittime) {
		try {
			new FluentWait<>(driver)
					.withTimeout(FLUENT_WAIT, TimeUnit.SECONDS)
					.pollingEvery(10, TimeUnit.MILLISECONDS)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class)
					.until(ExpectedConditions.visibilityOfElementLocated(locator));		
			hardWait(waittime);
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + locator + " not found" + e);
			Assert.fail("Element " + locator + " not found");
		}
	}

	public void scrollToView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void pressKeys(By by, String pressKeys) {
		WebElement element;
		element = findElement(by);
		element.sendKeys(pressKeys);
	}

	public void pressESCKey(WebElement element) {
		element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.ESCAPE));
	}

	public void pressESCKey(By by) {
		WebElement element;
		element = findElement(by);
		element.sendKeys(Keys.chord(Keys.ESCAPE));
	}

	public void pressEnterKey(By by) {
		WebElement element;
		element = findElement(by);
		element.sendKeys(Keys.chord(Keys.ENTER));
	}

	public void pressDOWNkey(By by) {
		WebElement element;
		element = findElement(by);
		element.sendKeys(Keys.DOWN);
	}

	public void hardWait() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ie) {

		}
	}

	public void hardWait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException ie) {

		}
	}



	public void doubleClickByAction(By by) {
		WebElement element = null;
		try {
			element = findElement(by);
			Actions action = new Actions(driver);
			action.doubleClick(element).click().perform();
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + element + " not found" + e);
			Assert.fail("Element " + element + " not found");
		}
	}

	public void clickByAction(By by) {
		WebElement element = null;
		try {
			element = findElement(by);
			waitForElementDisplayed(by);
			waitForElementClickable(by);
			Actions action = new Actions(driver);
			action.click(element).perform();
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + element + " not found" + e);
			Assert.fail("Element " + element + " not found");
		}
	}

	public void waitAndClickByAction(By by, Integer wait) {
		waitForElementAndHardWait(by, wait);
		clickByAction(by);
	}

	public void enterTextByJavaScript(final WebElement element, String text) {
		if (!text.equals("IGNORE")) {
            if(System.getProperty("os.name").contains("Windows 10"))
            {        element.clear();
            element.sendKeys(text);
            }
            else {
 	((JavascriptExecutor) driver).executeScript("arguments[0].value='" + text + "';", element);
            }
        }
	}

	public void enterTextByJavaScript(By by, String text) {
		if (!text.equals("IGNORE")) {
			WebElement element;
			element = findElement(by);
            if(System.getProperty("os.name").contains("Windows 10"))
            {
            	if(conf.getProperty("ChromeVersion").equalsIgnoreCase("72")) {
					((JavascriptExecutor) driver).executeScript("arguments[0].value='"+ text + "';", element);
				} else {
					element.clear();
					element.sendKeys(text);
				}
            }
            else{
				((JavascriptExecutor) driver).executeScript("arguments[0].value='"+ text + "';", element);
            }
        }
	}

	public void waitForText(final By locator, final String text) {
		new FluentWait<>(driver)
				.withTimeout(FLUENT_WAIT, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.MILLISECONDS)
				.until(ExpectedConditions.visibilityOfElementLocated(locator));		
	}

	public WebElement waitForElementExists(By by, long timeoutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		return wait.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	public void enterTextByActions(final By locator, String text) {
		if (!text.equals("IGNORE")) {
			Actions act1 = new Actions(driver);
			act1.moveToElement(findElement(locator)).sendKeys(text).build().perform();
		}
	}



	public void doubleClickByJavaScript(By by) {
		WebElement element = null;
		element = findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].dblclick();", element);
	}

	/*public void setTextByJavaScript(By by) {
		WebElement element = null;
		element = findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].setAttribute('innerHTML','yahoo');", element);

	}*/

	public void waitForStaleStatus(final By by) {
		new WebDriverWait(driver, DEFAULT_TIME_OUT).ignoring(
				StaleElementReferenceException.class).until(ExpectedConditions.visibilityOfElementLocated(by)).click();	}

	public void waitForStaleStatusClick(final By by) {
		new WebDriverWait(driver, DEFAULT_TIME_OUT).ignoring(
				StaleElementReferenceException.class).until(ExpectedConditions.visibilityOfElementLocated(by)).click();	}
	

	public void waitForElementDisplayed(final By by) {

		WebElement element = null;
		element = findElement(by);

		//wait for up to XX seconds for our error message
		long end = System.currentTimeMillis() + (DEFAULT_TIME_OUT * 1000);
		while (System.currentTimeMillis() < end) {
			// If results have been returned, the results are displayed in a drop down.
			if (element.isDisplayed()) {
				break;
			}
		}
	}

	public void waitForElementEnabled(final By by) {

		WebElement element = null;
		element = findElement(by);

		//wait for up to XX seconds for our error message
		long end = System.currentTimeMillis() + (DEFAULT_TIME_OUT * 10000);
		while (System.currentTimeMillis() < end) {
			// If results have been returned, the results are displayed in a drop down.
			if (element.isEnabled()) {
				break;
			}
		}
	}

	public boolean isElementDisplayed(By by) {
		return driver.findElement(by).isDisplayed();
	}

	public void mouseHover(By rootelement, By childelement) {
		WebElement element = driver.findElement(rootelement);
		Actions action = new Actions(driver);
		action.moveToElement(element).moveToElement(driver.findElement(childelement)).click().build().perform();
	}


	public void waitForExpectedText(By by, String text) {
		WebDriverWait wait = new WebDriverWait(driver, DEFAULT_TIME_OUT);
		try {
			wait.until(ExpectedConditions.textToBePresentInElementLocated(by, text));
		} catch (Exception e) {
			Assert.fail("Expected Text " + text + "not found");
		}
	}

	public List<WebElement> returnWebElements(By by) {
		return driver.findElements(by);
	}


	

	public void closeNonParentWindows() {
		String parentWindow = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String windowHandle : handles) {
			if (!windowHandle.equals(parentWindow)) {
				driver.switchTo().window(windowHandle);
				hardWait(1);
				driver.close();
				driver.switchTo().window(parentWindow); //cntrl to parent window
			}
		}
	}


	public void sendKeysByJavaScript(WebElement element, String text) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].value='" + text + "';", element);
	}

	public void javascriptClick(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
//		executor.executeScript("arguments[0].click();", element);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		executor.executeScript("arguments[0].click();", element);
//		new Actions(driver).moveToElement(element, 1, 1).click().perform();
	}

	public void clearElement(final By locator) {
		WebElement element;
		element = findElement(locator);
		element.clear();

	}

//	Claims specific new methods
	public WebElement highlightElement(By by) {
		WebElement elem = driver.findElement(by);
		// draw a border around the found element
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", elem);
		}
		return elem;
	}

	public void unhighlightElement(By by) {

		WebElement elem = driver.findElement(by);
		// remove a border around the found element
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].style.border='0px'", elem);
	}

	public void unhighlightElement(WebElement elem) {
		// remove a border around the found element
		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].style.border='0px'", elem);
	}

	public WebElement highlightElement(WebElement elem) {
		// draw a border around the found element
		if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid red'", elem);
		}
		return elem;
	}

	public String getdate()
	{
		Date date = Calendar.getInstance().getTime();
		// Display a date in day, month, year format
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String today = formatter.format(date);
		System.out.println("Today : " + today);
		return today;

	}

	public void sendKeysToWindow(){
		Actions builder = new Actions(driver);
		builder.sendKeys(Keys.chord(Keys.TAB)).build()
				.perform();
	}
	public void sendSPACEKeysToWindow(){
		Actions builder = new Actions(driver);
		builder.sendKeys(Keys.chord(Keys.SPACE)).build()
				.perform();
	}
	public void sendMultipleKeysToWindow(){
		Actions builder = new Actions(driver);
		builder.sendKeys(Keys.chord(Keys.ALT,Keys.SHIFT,"t")).build()
				.perform();
	}

	public void scrollToView(By by) {
		WebElement element;
		element = findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public void comparetext(String expected, String Actual)
	{
		if (!expected.equals(Actual)) {

			ExecutionLogger.file_logger.error(expected + "\n" + " is Not Equal to : " + "\n" + Actual+"\n");
//            Assert.fail(expected + "Not Equal to" + Actual);

		}
		else {

			ExecutionLogger.root_logger.info(expected + "\n" + " is Equal to : " + "\n" + Actual+"\n");
		}

	}

	public void selectDropddownValue(By arg,String value)
	{
		driver.findElement(arg).click();
		String common_link = "(//a[@title='{dynamic}'])";
		String link = common_link.replace("{dynamic}",value.trim());
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		driver.findElement(By.xpath(link)).click();
	}


	public static boolean verifyNumeric(String strNum) {
		try {
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	public void clickByJavaScriptWithOffset(By by, int x, int y) {
		WebElement element = null;
		element = findElement(by);
		try {
			if(System.getProperty("os.name").equalsIgnoreCase("Windows 10"))
			{
				Actions builder = new Actions(driver);
				builder.moveToElement(element).moveByOffset(x,y).click().build().perform();
			}
			ExecutionLogger.file_logger.info("Element click  " + element + " successful by JavaScript");
		} catch (Exception e) {
			ExecutionLogger.root_logger.error("Element " + element + "not clickable by JavaScript " + e);
			Assert.fail();
		}
	}
	
	public void focusElement(By by){
		WebElement elem = driver.findElement(by);
	    String javaScript = "var evObj = document.createEvent('MouseEvents');"
	                    + "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);"
	                    + "arguments[0].dispatchEvent(evObj);";
	    
	    if (driver instanceof JavascriptExecutor) {
			((JavascriptExecutor)driver).executeScript(javaScript, elem);
			}  
	}
}

