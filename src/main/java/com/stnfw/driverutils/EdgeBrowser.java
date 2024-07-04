package com.stnfw.driverutils;

import com.stnfw.constants.DriverExecutionMode;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EdgeBrowser {
    WebDriver driver;
    DriverExecutionMode driverExecutionMode;
    public static final int IdleTimeout = 10;

    public WebDriver getMsEdgeBrowserDriver(DriverExecutionMode driverExecutionMode) throws MalformedURLException {
        this.driverExecutionMode=driverExecutionMode;
        switch (driverExecutionMode) {
            case NORMAL:
                return new EdgeDriver(normalMode());
            case GRID:
                return new RemoteWebDriver(new URL("http://your-grid-url"), gridMode("TestName", "BuildName"));
            case HEADLESS:
                return new EdgeDriver(headlessMode());
            default:
                throw new IllegalArgumentException("Unsupported execution mode: " + driverExecutionMode);
        }
    }

    private EdgeOptions normalMode() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("start-maximized");
        edgeOptions.addArguments("--disable-application-cache");
        edgeOptions.addArguments("--disable-restore-session-state");
        edgeOptions.addArguments("--no-sandbox");
        edgeOptions.setExperimentalOption("useAutomationExtension", false);
        edgeOptions.addArguments("--disable-infobars");
        edgeOptions.addArguments("--disable-extensions");
        edgeOptions.addArguments("--disable-gpu");
        edgeOptions.addArguments("--disable-dev-shm-usage");

        Map<String, Object> prefs = new HashMap<>();
        File downloadPath = new File("downloads");
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.default_directory", downloadPath.getAbsolutePath());
        edgeOptions.setExperimentalOption("prefs", prefs);
        return edgeOptions;
    }

    private EdgeOptions headlessMode() {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--window-size=1920,1080");
        edgeOptions.addArguments("--disable-extensions");
        edgeOptions.addArguments("--disable-gpu");
        edgeOptions.setExperimentalOption("useAutomationExtension", false);
        edgeOptions.addArguments("--proxy-server='direct://'");
        edgeOptions.addArguments("--proxy-bypass-list=*");
        edgeOptions.addArguments("start-maximized");
        edgeOptions.addArguments("--headless");
        return edgeOptions;
    }

    private DesiredCapabilities gridMode(String buildName, String testName) {
        DesiredCapabilities cap = new DesiredCapabilities();
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--inprivate");
        cap.setCapability(EdgeOptions.CAPABILITY, edgeOptions);
        cap.setCapability("name", testName);
        cap.setCapability("build", buildName);
        cap.setCapability("idleTimeout", IdleTimeout);
        return cap;
    }
}
