package wrike.drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import wrike.helpers.EnvironmentHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


public class CustomWebDriver implements WebDriverProvider {
    @Override
    public WebDriver createDriver(DesiredCapabilities capabilities) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);

        capabilities.setBrowserName(EnvironmentHelper.browser);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", EnvironmentHelper.isVideoOn);
        capabilities.setCapability("videoFrameRate", 24);

        // todo implement for other drivers - opera, firefox, safari
        capabilities.setCapability(ChromeOptions.CAPABILITY, getChromeOptions());
        WebDriverManager.chromedriver().setup();

        if(EnvironmentHelper.isRemoteDriver) {
            return getRemoteWebDriver(capabilities);
        } else {
            return getLocalChromeDriver(capabilities);
        }
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();

        if (EnvironmentHelper.isWebMobile) {
            Map<String, Object> mobileDevice = new HashMap<>();
            mobileDevice.put("deviceName", EnvironmentHelper.webMobileDevice);
            chromeOptions.setExperimentalOption("mobileEmulation", mobileDevice);
        }
        chromeOptions.addArguments("--window-size=" + EnvironmentHelper.screenResolution);
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-infobars");
        if(EnvironmentHelper.isHeadless) chromeOptions.addArguments("headless");

        return chromeOptions;
    }

//    private OperaOptions getOperaOptions() { // todo
//        ...
//    }

//    private FirefoxOptions getFirefoxOptions() { // todo
//        ...
//    }

//    private SafariOptions getSafariOptions() { // todo
//        ...
//    }

//    private InternetExplorerOptions getInternetExplorerOptions() { // not todo
//        ...
//    }

    @SuppressWarnings("deprecation")
    private WebDriver getLocalChromeDriver(DesiredCapabilities capabilities) {
        return new ChromeDriver(capabilities);
    }

    private WebDriver getRemoteWebDriver(DesiredCapabilities capabilities) {
        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(getRemoteWebdriverUrl(), capabilities);
        remoteWebDriver.setFileDetector(new LocalFileDetector());

        return remoteWebDriver;
    }

    private URL getRemoteWebdriverUrl() {
        try {
            return new URL(EnvironmentHelper.remoteDriverUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}