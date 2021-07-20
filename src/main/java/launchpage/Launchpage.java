/**
 * 
 */
package launchpage;

import pages.Jupitertoyspage;
import commonlib.Runner;
import commonlib.Util;

/**
 * @author abi
 *
 */
public class Launchpage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Jupitertoyspage jupitertoyspage = new Jupitertoyspage();
		Runner runner = new Runner();
		Util util = new Util();
		
		/**
		 * Scenario 1
		 */
		/*
		runner.setup();
        util.clickBrowserMaximizeButton();
		jupitertoyspage.launchjupiter();
		jupitertoyspage.scenario1();
        runner.cleanup();
        
        */
		/**
		 * Scenario 2
		 */
		/*
		runner.setup();
        util.clickBrowserMaximizeButton();
		jupitertoyspage.launchjupiter();
		jupitertoyspage.scenario2();
        runner.cleanup();
        
        */
		
        /**
		 * Scenario 3
		 */
		
		runner.setup();
        util.clickBrowserMaximizeButton();
		jupitertoyspage.launchjupiter();
		jupitertoyspage.scenario3();
       // runner.cleanup();
        
	}

}
