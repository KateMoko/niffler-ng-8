package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.*;

@ExtendWith(BrowserExtension.class)
public class RegistrationTest {

  @Test
  void shouldRegisterNewUser() {
    final String username = randomUsername();
    final String password = randomPassword();

    Selenide.open(RegistrationPage.URL, RegistrationPage.class)
      .doRegister(username, password, password)
      .doLogin(username, password);
  }

  @Test
  void shouldNotRegisterUserWithExistingUsername() {
    final String username = "NiceGuy";
    final String password = randomPassword();

    Selenide.open(RegistrationPage.URL, RegistrationPage.class)
      .setUsername(username)
      .setPassword(password)
      .setPasswordSubmit(password)
      .clickSignUp()
      .verifyErrorDisplayed("Username `" + username + "` already exists");
  }

  @Test
  void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
    Selenide.open(RegistrationPage.URL, RegistrationPage.class)
      .setUsername(randomUsername())
      .setPassword(randomPassword())
      .setPasswordSubmit(randomPassword())
      .clickSignUp()
      .verifyErrorDisplayed("Passwords should be equal");
  }
}