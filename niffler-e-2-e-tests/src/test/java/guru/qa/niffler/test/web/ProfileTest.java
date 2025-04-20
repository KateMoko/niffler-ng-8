package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class ProfileTest {
  private static final String username = "NiceGuy";
  private static final String password = "qwer";

  @User(
    username = username,
    categories = @Category(archived = true)
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(LoginPage.URL, LoginPage.class)
      .doLogin(username, password)
      .openProfile()
      .clickShowArchivedToggle()
      .verifyCategoryPresentInList(category.name());
  }

  @User(
    username = username,
    categories = @Category(archived = false)
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    Selenide.open(LoginPage.URL, LoginPage.class)
      .doLogin(username, password)
      .openProfile()
      .verifyCategoryPresentInList(category.name());
  }
}