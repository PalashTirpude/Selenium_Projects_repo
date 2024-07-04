package com.stnfw.driverutils;

import com.stnfw.constants.Browser;
import com.stnfw.constants.DriverExecutionMode;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public class DriverInitializer {

    private static WebDriver driver;

    public static WebDriver getWebDriverInstance(String browserName, String executionMode) throws MalformedURLException {
        if (driver == null) {
            driver = launchBrowser(browserName, executionMode);
        }
        return driver;
    }

    public static WebDriver launchBrowser(String browserName, String executionMode) throws MalformedURLException {
        WebDriver driver = null;
        Browser browser = Browser.valueOf(browserName.toUpperCase());
        DriverExecutionMode mode = DriverExecutionMode.valueOf(executionMode.toUpperCase());

        switch (browser) {
            case CHROME:
                driver = new ChromeBrowser().getChromeBrowserDriver(mode);
                break;
            case MSEDGE:
                driver = new EdgeBrowser().getMsEdgeBrowserDriver(mode);
                break;
            case FIREFOX:
                driver = new FirefoxBrowser().getFireFoxBrowserDriver(mode);
                break;
            default:
                throw new IllegalArgumentException("Invalid browser specified");
        }
        return driver;
    }

}
