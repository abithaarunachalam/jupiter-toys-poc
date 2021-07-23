package commonlib;

/**
 * @author abitha
 * @implNote This class is used for log
 *
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class ExecutionLogger {
    public static Logger file_logger = LogManager.getLogger("outfile");
    public static Logger root_logger = LogManager.getLogger("rootlog");
}
