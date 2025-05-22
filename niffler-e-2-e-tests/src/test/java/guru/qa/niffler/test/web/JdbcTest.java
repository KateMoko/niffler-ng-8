package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-2",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx",
            null
        )
    );

    System.out.println(spend);
  }

  // Создание пользователя через JDBC без транзакций
  @Test
  void createUserJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserJdbc(
      new UserJson(
        null,
        "user-jdbc-6",
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
      )
    );
    System.out.println(user);
  }

  // Создание пользователя через JDBC с транзакциями
  @Test
  void xaCreateUserJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.xaCreateUserJdbc(
      new UserJson(
        null,
        "user-xa-jdbc-1",
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
      )
    );
    System.out.println(user);
  }

  // Создание пользователя через Spring JDBC без транзакций
  @Test
  void createUserSpringJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserSpringJdbc(
      new UserJson(
        null,
        "user-spring-jdbc-1",
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
      )
    );
    System.out.println(user);
  }

  // Создание пользователя через Spring JDBC с транзакциями
  @Test
  void xaCreateUserSpringJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.xaCreateUserSpringJdbc(
      new UserJson(
        null,
        "user-xa-spring-jdbc-1",
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
      )
    );
    System.out.println(user);
  }

  // Создание пользователя через Spring JDBC с ChainedTransactionManager
  @Test
  void createUserChainedTxManagerTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserChainedTxManager(
      new UserJson(
        null,
        "user-chained-tx-manager-2",
        null,
        null,
        null,
        CurrencyValues.RUB,
        null,
        null,
        null
      )
    );
    System.out.println(user);
  }
}
