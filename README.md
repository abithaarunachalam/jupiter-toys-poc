
# Webpage Jupiter Toys testing automation
Testing the webpage using Selenium webdriver

### How to initialise the project

* Download the project from here. import as "Existing Maven Projects" in your IDE
* Once successfully built, run the feature file as Java application 

## Pre-requisites and How to Run

* 1. Ensure to perform maven install and build the project. 

* 2. If any error occurs in webdriver.java file due to Java dependency, change the java version to 1.8 in build path

* 3. Build the project using Maven build and right chick on Launchpage.java file and select Run as Java application to run the project.

## Webpage under test


URL under test: https://jupiter.cloud.planittesting.com/#/contact


### Tech Stack used

1. Java 1.8
2. Selenium Webdriver
3. Maven

### Two ways of using the chrome driver
    	
In Runner.java file, The below code has been commented. If we have the latest version of the 
chrome driver downloaded, please copy paste the driver in /usr/local/bin and uncomment the below code

    	 // String chromePath = "//usr//local//bin//";
    	 // System.setProperty("webdriver.chrome.driver", chromePath+"chromedriver");

As of now, the chrome driver is downloaded into the src/main/resources/config/drivers under project folder
