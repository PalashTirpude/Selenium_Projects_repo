package com.stnfw.driverutils;

import com.stnfw.constants.DriverExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FirefoxBrowser {
    WebDriver driver;
    DriverExecutionMode driverExecutionMode;
    public static final int IdleTimeout = 10;

    public WebDriver getFireFoxBrowserDriver(DriverExecutionMode driverExecutionMode) throws MalformedURLException {
        this.driverExecutionMode = driverExecutionMode;
        switch (driverExecutionMode) {
            case NORMAL:
                return new FirefoxDriver(normalMode());
            case GRID:
                return new RemoteWebDriver(new URL("http://your-grid-url"), gridMode("TestName", "BuildName"));
            case HEADLESS:
                return new FirefoxDriver(headlessMode());
            default:
                throw new IllegalArgumentException("Unsupported execution mode: " + driverExecutionMode);
        }
    }

    private FirefoxOptions normalMode() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("-start-maximized");
        firefoxOptions.addArguments("--disable-application-cache");
        firefoxOptions.addArguments("--disable-restore-session-state");
        firefoxOptions.addArguments("--no-sandbox");
        firefoxOptions.setCapability("useAutomationExtension", false);
        firefoxOptions.addArguments("--disable-infobars");
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--disable-gpu");
        firefoxOptions.addArguments("--disable-dev-shm-usage");

        Map<String, Object> prefs = new HashMap<>();
        File downloadPath = new File("downloads");
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadPath.getAbsolutePath());
        firefoxOptions.addPreference("prefs", prefs);
        return firefoxOptions;
    }

    private FirefoxOptions headlessMode() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--window-size=1920,1080");
        firefoxOptions.addArguments("--disable-extensions");
        firefoxOptions.addArguments("--disable-gpu");
        firefoxOptions.setCapability("useAutomationExtension", false);
        firefoxOptions.addArguments("--proxy-server='direct://'");
        firefoxOptions.addArguments("--proxy-bypass-list=*");
        firefoxOptions.addArguments("-start-maximized");
        firefoxOptions.addArguments("--headless");
        return firefoxOptions;
    }

    private DesiredCapabilities gridMode(String buildName, String testName) {
        DesiredCapabilities cap = new DesiredCapabilities();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--private");
        cap.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
        cap.setCapability("name", testName);
        cap.setCapability("build", buildName);
        cap.setCapability("idleTimeout", IdleTimeout);
        return cap;
    }
}
