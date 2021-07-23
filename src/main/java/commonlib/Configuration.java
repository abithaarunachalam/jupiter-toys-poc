package commonlib;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author abitha
 * @implNote This class is used to create different functions for configuration
 *
 */

public class Configuration {
    private Properties config;
    private String basepath = "//src//main//resources//config//";
    private String profilePath = basepath + "profiles//";
    private String envPath = basepath + "envs//";
    private String sysPath = basepath + "system//";
    private String driver = basepath + "drivers//";

    public Configuration() {
        initConfig();
    }

    public void loadSystemConfig() {
        try {
            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + sysPath + "framework.properties");

            Properties config = new Properties();
            config.load(fs);

            this.config = config;
        } catch (IOException e) {
            ExecutionLogger.root_logger.error("Cannot find " + e);
        }
    }
    
    public void loaddriverConfig() {
        try {
            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + driver + "chromedriver");

            Properties config = new Properties();
            config.load(fs);

            this.config = config;
        } catch (IOException e) {
            ExecutionLogger.root_logger.error("Cannot find " + e);
        }
    }

    public void loadUserConfig(String pcname) {
        try {
            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + profilePath + pcname + ".properties");

            Properties config = new Properties();
            config.load(fs);

            this.config = config;
        } catch (IOException e) {
            ExecutionLogger.root_logger.error("Cannot find User config file: " + pcname);
        }
    }


    public void loadConfig(String pcname, String configname) {
        try {
            FileInputStream fs1 = new FileInputStream(System.getProperty("user.dir") + profilePath + "default.properties");
            FileInputStream fs2 = new FileInputStream(System.getProperty("user.dir") + envPath +  "Environment.properties");

            Properties config = new Properties();
            config.load(fs1);
            config.load(fs2);

            this.config = config;
        } catch (IOException e) {
            ExecutionLogger.root_logger.error("Cannot find config file");
        }
    }

    public void loadRunConfig(String pcname) {
        try {
            FileInputStream fs1 = new FileInputStream(System.getProperty("user.dir") + sysPath + "framework.properties");
          //  FileInputStream fs2 = new FileInputStream(System.getProperty("user.dir") + sysPath + "alm.properties");
            FileInputStream fs3 = new FileInputStream(System.getProperty("user.dir") + profilePath + pcname + ".properties");

            Properties config = new Properties();
            config.load(fs1);   // Framework
           // config.load(fs2);   // ALM
            config.load(fs3);   // PC
            FileInputStream fs4 = new FileInputStream(System.getProperty("user.dir") + envPath + "Environment.properties");
            config.load(fs4);   // Environment

            this.config = config;
        } catch (IOException e) {
            ExecutionLogger.root_logger.error("Cannot find properties files");
        }
    }

    public String getProperty(String propertyName) {
        return config.getProperty(propertyName);
    }

    private void initConfig() {
        loadRunConfig("default");
    }
}
