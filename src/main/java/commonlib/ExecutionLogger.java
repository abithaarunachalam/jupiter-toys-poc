package commonlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class ExecutionLogger {

    public static Logger console_logger = LogManager.getLogger("console");
    public static Logger file_logger = LogManager.getLogger("outfile");
    public static Logger data_logger = LogManager.getLogger("outdata");
    // Log to both file and data logs
    public static Logger filedata_logger = LogManager.getLogger("filedata");

    // To log in both console and file
    public static Logger root_logger = LogManager.getLogger("rootlog");
    // Log everywhere
   // public static Logger all_logger = Logger.getLogger("alllog");

}
