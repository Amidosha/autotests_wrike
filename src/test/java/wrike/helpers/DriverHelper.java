package wrike.helpers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;
import org.openqa.selenium.remote.RemoteWebDriver;
import wrike.drivers.CustomWebDriver;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static org.openqa.selenium.logging.LogType.BROWSER;
import static wrike.helpers.EnvironmentHelper.webUrl;


public class DriverHelper {

    public static void configureSelenide() {
        Configuration.browser = CustomWebDriver.class.getName();
//        Configuration.browser = FIREFOX;
//        Configuration.browser = OPERA;
        Configuration.baseUrl = webUrl;
        Configuration.timeout = 10000;
    }


    public static String getSessionId(){
        return ((RemoteWebDriver) getWebDriver()).getSessionId().toString().replace("selenoid","");
    }

    public static String getConsoleLogs() {
        return String.join("\n", Selenide.getWebDriverLogs(BROWSER));
    }

    @Step("Открытие ссылки {url} с авторизацией")
    public static void openUrlWithAuthorization(String url, String login, String password) {
        step("Открытие ссылки " + url, ()-> open(url));

        step("Заполнение формы авторизации", ()-> {
            $(byText("Войти по логину и паролю")).click();
            $("#username").setValue(login);
            $("#password").setValue(password);
            $("#kc-login").click();
        });

        step("Проверка успешной авторизации ", ()->
                $(byText("Выйти")).shouldBe(Condition.visible));
    }

}