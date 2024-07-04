package com.stnfw.driverutils;

import com.stnfw.constants.Browser;
import com.stnfw.constants.DriverExecutionMode;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ChromeBrowser {
    WebDriver driver;
    DriverExecutionMode driverExecutionMode;
    public static final int IdleTimeout = 10;

    public WebDriver getChromeBrowserDriver(DriverExecutionMode driverExecutionMode) throws MalformedURLException {
        this.driverExecutionMode=driverExecutionMode;
        switch (driverExecutionMode){
            case NORMAL :
                return new ChromeDriver(normalMode());
            case GRID:
                return new RemoteWebDriver(new URL(""),gridMode("TestName","BuildName"));
            case HEADLESS:
                return new ChromeDriver(headlessMode());
            default:  throw new IllegalArgumentException("Unsupported execution mode: " + driverExecutionMode);
        }
    }

    private ChromeOptions normalMode(){
        ChromeOptions chromeOptions = new ChromeOptions();
        String userProfile =System.getenv("APPDATA"+"\\Google\\Chrome\\User Data");
//        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--disable-application-cache");
        chromeOptions.addArguments("--disable-restore-session-state");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.setExperimentalOption("useAutomationExtension",false);
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        Map<String,Object> prefs= new HashMap<>();
        File downloadPath=new File("downloads");
        prefs.put("profile.default_content_setting_values.notifications",5);
        prefs.put("download.prompt_for_download",false);
        prefs.put("download.default_directory",downloadPath.getAbsolutePath());
        chromeOptions.setExperimentalOption("prefs",prefs);
        return chromeOptions;

    }

    private ChromeOptions headlessMode(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.setExperimentalOption("useAutomationExtension",false);
        chromeOptions.addArguments("--proxy-server='direct://'");
        chromeOptions.addArguments("--proxy-bypass-list=*");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("--headless");
        return chromeOptions;
    }

    private DesiredCapabilities gridMode(String buildName, String testName){
        DesiredCapabilities cap= new DesiredCapabilities(Browser.CHROME.toString().toLowerCase(),"", Platform.ANY);
        ChromeOptions chromeOptions=new ChromeOptions();
        chromeOptions.addArguments("--incognito");
        cap.setCapability(ChromeOptions.CAPABILITY,chromeOptions);
        cap.setCapability("name",testName);
        cap.setCapability("build",buildName);
        cap.setCapability("idleTimeout",IdleTimeout);
//        cap.setAcceptInsecureCerts(true);
        return null;
    }



}
