/**
 * 
 */
package launchpage;


import pages.Scenarios;
import commonlib.Runner;
import commonlib.Util;

/**
 * @author abitha
 * @implNote This class is used to launch Chrome and execute different scenarios
 *
 */
public class Launchpage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scenarios scenario = new Scenarios();
		Runner runner = new Runner();
		Util util = new Util();
		
		/**
		 * Scenario 1 - Comments Page Validation
		 */
		
		runner.setup();
        util.clickBrowserMaximizeButton();
        scenario.scenario1_Comments_Validation();
        runner.cleanup();
        
		/**
		 * Scenario 2 - Adding Multiple Comments
		 */
        runner.setup();
        scenario.scenario2_Multiple_Comments();
        runner.cleanup();
		
        /**
		 * Scenario 3 - Add Items to Cart
		 */
		
		runner.setup();
        util.clickBrowserMaximizeButton();
		scenario.scenario3_Add_Item_To_Cart();
        runner.cleanup();
        
        /**
		 * Scenario 4 - Check Price and Subtotal
		 */
		
		runner.setup();
        util.clickBrowserMaximizeButton();
		scenario.scenario4_Check_Price();
        runner.cleanup();
	}

}
