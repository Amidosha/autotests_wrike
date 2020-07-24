package wrike.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class LoginPage {

    public static SelenideElement
            loginWithPasswordLink = $(byText("Войти по логину и паролю")),
            loginInput = $("#username"),
            passwordInput = $("#password"),
            submitButton = $("#kc-login");

    public static void loginAs(String login, String password) {
        loginWithPasswordLink.click();
        loginInput.setValue(login);
        passwordInput.setValue(password);
        submitButton.click();
    }
}
