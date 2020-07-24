package wrike.tests;

import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import wrike.helpers.AttachmentsHelper;
import wrike.helpers.DriverHelper;
import wrike.helpers.EnvironmentHelper;

import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;
import static com.codeborne.selenide.logevents.SelenideLogger.addListener;


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TestBase extends TestData {

    @BeforeAll
    public static void beforeAll() {
        addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        DriverHelper.configureSelenide();
    }

    @AfterEach
    public void afterEach(){
        String sessionId = DriverHelper.getSessionId();

        AttachmentsHelper.attachScreenshot("Last screenshot");
        AttachmentsHelper.attachPageSource();
//        attachNetwork(); // todo
        AttachmentsHelper.attachAsText("Browser console logs", DriverHelper.getConsoleLogs());
        if (EnvironmentHelper.isVideoOn) AttachmentsHelper.attachVideo(sessionId);

        closeWebDriver();
    }

}



