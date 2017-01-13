package io.ariku.console;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import io.ariku.verification.api.LoginRequest;
import io.ariku.verification.api.UserVerification;
import io.ariku.verification.api.UserVerificationService;
import io.ariku.verification.api.VerifySignUpRequest;

import static io.ariku.composer.Composer.COMPOSER;

/**
 * @author Ari Aaltonen
 */
public class LoginPage {

    public static void draw(BasicWindow window) {

        Panel panel = new Panel();

        TextBox emailAddressText = new TextBox();
        emailAddressText.addTo(panel);

        Button okButton = new Button("Login", () -> login(emailAddressText.getText()));
        okButton.addTo(panel);

        Button exitButton = new Button("Exit", () -> UserVerificationMenu.draw(window));
        exitButton.addTo(panel);

        window.setComponent(panel);
    }

    private static void login(String value) {

        UserVerificationService userVerificationService = COMPOSER.userVerificationService;

        if (value.isEmpty())
            return;

        String securityMessage = userVerificationService.login(new LoginRequest(value));
        ConsoleCache.securityMessage = securityMessage;

        if (!securityMessage.isEmpty())
            print("Login OK " + value);
        else
            print("Login FAIL " + value);
    }

    public static void print(String value) {
        System.out.println(value);
    }

}