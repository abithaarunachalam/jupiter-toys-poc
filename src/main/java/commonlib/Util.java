package commonlib;



import static commonlib.Runner.driver;
import static java.lang.Thread.sleep;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author abitha
 * @implNote This class is a container of different functions/utilities
 *
 */

public class Util {

    public static Configuration conf;

    // Process variables
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM ";

    public Util() {
    }

    public static void AssertEqualsString(String message, String expected, String actual) {
        if (!expected.equals(actual)) {
            ExecutionLogger.root_logger.error(message+" Expected: "+expected+" Actual: "+actual);
        }
    }
    
    public static void AssertEqualsInt(String message, int expected, int actual) {
        if (expected != actual) {
            ExecutionLogger.root_logger.error(message+" Expected: "+expected+" Actual: "+actual);
        }
    }

    public static void NotAssertEquals(String message, int expected, int actual) {
       if (expected==actual) {
           ExecutionLogger.root_logger.error(message+" Expected: "+expected+" Actual: "+actual);
       }
    }

    public static void AssertTrue(String message, boolean value) {
        if (!value) {
            ExecutionLogger.root_logger.error(message);
        }
    }

    public static String removeLineBreak(String text)  {
        return text.replaceAll("\r", "").replaceAll("\n", " ");
    }

   public static String splitText(String text, String regexpression, int returnposition){
        String[] words = text.split(regexpression);
        return words[returnposition];
    }

    // Returns if the JVM is 32 or 64 bit version
    public static String jvmBitVersion() {
        return System.getProperty("sun.arch.data.model");
    }

  
    public static boolean isProcessRunning(String serviceName) throws Exception {
        Process p = Runtime.getRuntime().exec(TASKLIST);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public static void killProcess(String serviceName) throws Exception {
        Runtime.getRuntime().exec(KILL + serviceName);
    }

    public static void threadWait(Integer waitTime) {
        try {
            sleep(waitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int generateNumber() {
        Random r = new Random( System.currentTimeMillis() );
        return ((150000000 + (r.nextInt(1)) * 100000 + r.nextInt(100000)));
    }

    public String generateEmailId() {
            return ("user" + generateNumber() + "@planit.com.au");
  }
    
    public String generateMessage() {
        return ("This is the random Message with number" + generateNumber());
}

    public String generatetName(String name) {
        Random ran = new Random();
        String newName = ((name+ (ran.nextInt(3)) * 10 + ran.nextInt(1000)));
        return (newName);
    }

    public int generateRandomNumber(){
        Random r = new Random(System.currentTimeMillis());
        return ((150000000 + (r.nextInt(1)) * 100000 + r.nextInt(100000)));
    }

    public void clickBrowserBackButton() {
        threadWait(1);
        driver.navigate().back();
    }
    public void clickBrowserForwardButton() {
        threadWait(1);
        driver.navigate().forward();
    }


    public void clickBrowserMaximizeButton() {
        threadWait(1);
        driver.manage().window().maximize();
    }

    
    public static Double convertStrToDecimal(String val, int decipoint){
        Double decVal = 0.0;
        val = val.replaceAll("$","");
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(val);
        while(matcher.find()) {
            decVal = Double.valueOf(matcher.group());
        }
        return decVal;
    }

    public String addcurrency(Double price){
    	String p = Double. toString(price);
        return "$"+p;
    }
}
