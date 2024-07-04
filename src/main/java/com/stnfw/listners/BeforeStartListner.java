package com.stnfw.listners;

import com.stnfw.driverutils.DriverInitializer;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class BeforeStartListner implements ISuiteListener {

    private static final Map<String, WebDriver> driverMap = new HashMap<>();

    @Override
    public void onStart(ISuite suite) {
        // Initialize WebDriver before the suite starts
        String browser = suite.getXmlSuite().getParameter("BROWSER");
        String executionMode = suite.getXmlSuite().getParameter("ExecutionMode");

        try {
            WebDriver driver = DriverInitializer.getWebDriverInstance(browser, executionMode);
            driverMap.put(suite.getName(), driver);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        // Quit WebDriver after the suite finishes
        WebDriver driver = driverMap.get(suite.getName());
        if (driver != null) {
            driver.quit();
        }
    }

    public static WebDriver getDriver(String suiteName) {
        return driverMap.get(suiteName);
    }
}
