package commonlib;



import static commonlib.Runner.driver;
import static java.lang.Thread.sleep;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {

    private DateFormat dateFormat;
    private Date date;
    private Calendar cal;
    private final String pattern = "dd/MM/yyyy";
    private final String yearpattern = "yyyy/MM/dd";
    private final String patternInSec = "yyyyMMdd_HHmmss";
    private final String patternInMin = "yyyy_MM_dd_HH_mm";
    private String[] dateformats;
    private String requesteddate;
    private String userEmail,quoteEmail;
    private String userCCEmail;
    public static Configuration conf;

    // Process variables
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM ";

    // string char patterns
    private static final Pattern LINE_END_SPACES = Pattern.compile(" +$", Pattern.MULTILINE);

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

    public static void fileLoggerNotAssertEquals(String message, int expected, int actual) {
       if (expected==actual) {
           ExecutionLogger.root_logger.error(message+" Expected: "+expected+" Actual: "+actual);
       }
    }

    public static void fileLoggerAssertTrue(String message, boolean value) {
        if (!value) {
            ExecutionLogger.root_logger.error(message);
        }
    }

    // Remove ending blank spaces (occurs in some documents)
    public static String stripLineEndSpaces(String str) {
        return LINE_END_SPACES.matcher(str).replaceAll("");
    }

    public static String removeLineBreak(String text)  {
        return text.replaceAll("\r", "").replaceAll("\n", " ");
    }

    public static boolean verifyString(String actualText, String expectedText) {
        Boolean found = false;
        if (actualText.contains(expectedText)) {
            //ExecutionLogger.root_logger.info("Found: " + expectedText);
            found = true;
        } else {
            ExecutionLogger.root_logger.error("## NOT FOUND ##: " + expectedText);
        }
        return found;
    }

    public static boolean logDifference(String actualText, String expectedText) {
        Boolean found = false;

//        if (actualText.contains(expectedText)) {
//            //ExecutionLogger.root_logger.info("Found: " + expectedText);
//            found = true;
//        } else {
//            ExecutionLogger.root_logger.error("## NOT FOUND ##: " + expectedText);
//        }
        return found;
    }
    public static boolean verifyString1(String actualText, String expectedText) {
        Boolean found = false;
        if (actualText.contains(expectedText)) {
            //ExecutionLogger.root_logger.info("Found: " + expectedText);
            found = true;
        } else {
            ExecutionLogger.root_logger.error("## NOT FOUND ##: " + expectedText);
        }
        return found;
    }

    public static String splitText(String text, String regexpression, int returnposition){
        String[] words = text.split(regexpression);
        return words[returnposition];
    }


    public String returnToday() {
        dateFormat = new SimpleDateFormat(pattern);
        date = new Date();
        return dateFormat.format(date);
    }

    public String returnTodayYearFirst() {
        dateFormat = new SimpleDateFormat(yearpattern);
        date = new Date();
        return dateFormat.format(date);
    }

    public String returnTodayInSec() {
        dateFormat = new SimpleDateFormat(patternInSec);
        date = new Date();
        return dateFormat.format(date);
    }

    public String returnTodayInMin() {
        dateFormat = new SimpleDateFormat(patternInMin);
        date = new Date();
        return dateFormat.format(date);
    }

    public String returnBackDate(String numberofdays) {
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -Integer.parseInt(numberofdays));
        ExecutionLogger.root_logger.info(" This is current date " + returnToday());
        ExecutionLogger.root_logger.info(" This is "+numberofdays+" days ago "+ dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

    public String returnForwardDate(String numberofdays) {
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, Integer.parseInt(numberofdays));
        ExecutionLogger.root_logger.info("This is current date " + returnToday());
        ExecutionLogger.root_logger.info("This is "+numberofdays+" days forward "+ dateFormat.format(cal.getTime()));
        return dateFormat.format(cal.getTime());
    }

    public String returnDateWithAddition(String reqDate, String numberofdays) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        try {
            date = sdf.parse(reqDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = sdf.getCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, Integer.parseInt(numberofdays));
        String requestedDate = sdf.format(cal.getTime());
        return requestedDate;
    }

    public void getPattern(String status) {
        if (status.equals("passed")) {
            ExecutionLogger.root_logger.info("\033[32m"+"*****************************************************************************");
        } else {
            ExecutionLogger.root_logger.info("\033[31m"+"*****************************************************************************");
        }
    }

    public static void doesDirectoryExist(String sDirectoryToFind) {
        File filDirectory = new File(sDirectoryToFind);
        if (!filDirectory.exists()) {
            boolean blnResult = false;
            try {
                filDirectory.mkdirs();
                blnResult = true;
            } catch (SecurityException se) {
                ExecutionLogger.root_logger.info("Do not have permission to create directory " + sDirectoryToFind);
            }
            if (blnResult) {
                ExecutionLogger.root_logger.info("Created directory: " + sDirectoryToFind);
            } else {
                ExecutionLogger.root_logger.info("Cannot creat directory " + sDirectoryToFind);
            }
        }
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

    public String getComputerName() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

   
    public int generateNumber() {
        Random r = new Random( System.currentTimeMillis() );
        return ((150000000 + (r.nextInt(1)) * 100000 + r.nextInt(100000)));
    }

 

    public String generateEmailIdByRole(String role) {
        Random ran = new Random();
        int BrokerNo = ((100+ (ran.nextInt(3)) * 100 + ran.nextInt(100)));

        if (!role.equals("Broker")) {
            return ("Employer" + generateNumber() + "@mailinator.com");
        } else
            return ("Broker"+BrokerNo+ "@yopmail.com");
    }

    public String generateLastName(String lastname) {
        Random ran = new Random();
        String lastName = ((lastname+ (ran.nextInt(3)) * 10 + ran.nextInt(1000)));
        return (lastName);
    }

    public int generateRandomNumber(){
        Random r = new Random(System.currentTimeMillis());
        return ((150000000 + (r.nextInt(1)) * 100000 + r.nextInt(100000)));
    }

    public void switchToNewWindow() {
        // Store the current window handle
        String winHandleBefore = driver.getWindowHandle();

        // Switch to new window opened
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
       }
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
        val = val.replaceAll(",","");
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(val);
        while(matcher.find()) {
            decVal = Double.valueOf(matcher.group());
        }
        return decVal;
    }

    public String addMonthToSpecificDate(String specificdate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(specificdate));
        } catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.MONTH, 1);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }

    public String addDaysToSpecificDate(String specificdate, String noOfDays){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(specificdate));
        } catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DATE, Integer.parseInt(noOfDays));
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }

    public String addDaysToSpecificDateDDMMYY(String specificdate, String noOfDays){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(specificdate));
        } catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DATE, Integer.parseInt(noOfDays));
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }

    public static String returnFinancialYear(String specificdate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String financialYear;
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(specificdate));
        } catch(ParseException e){
            e.printStackTrace();
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        if (month < 7) {
            financialYear = Integer.toString(year - 1);
        } else {
            financialYear = Integer.toString(year);
        }
        return financialYear;
    }

    public static String returnRateFinancialYear(String specificdate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String financialYear;
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(specificdate));
        } catch(ParseException e){
            e.printStackTrace();
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;

        if (month < 7) {
            if (specificdate.equals("30/06/2018")) {
                financialYear = Integer.toString(year);
            } else {
                financialYear = Integer.toString(year - 1);
            }
        } else {
            financialYear = Integer.toString(year);
        }
        return financialYear;
    }

    public String removeDigitsAfterDecimal(String premiumDetailsVal){
        Double deci=convertStrToDecimal(premiumDetailsVal,2);
        return "$"+new DecimalFormat("#,###").format(deci);
    }

    public String subtractThirtyDaysToSpecificDate(String specificdate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(specificdate));
        }catch(ParseException e){
            e.printStackTrace();
        }

        //Number of Days to add
        c.add(Calendar.DATE, -30);
        //Date after adding the days to the given date
        String newDate = sdf.format(c.getTime());
        return newDate;
    }

    public String getMonthName(String specificDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(specificDate));
        }catch(ParseException e){
            e.printStackTrace();
        }
        return(new SimpleDateFormat("MMMM").format(c.getTime()));

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

    public String getCurrentTime(){
        long currentTime = System.currentTimeMillis();
        String currentMilli= Long.toString(currentTime);
        return currentMilli;
    }
}
