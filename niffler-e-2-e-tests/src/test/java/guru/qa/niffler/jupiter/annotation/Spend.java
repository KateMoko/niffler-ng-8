package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.CurrencyValues;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Spend {
  String category();

  String description();

  double amount();

  CurrencyValues currency();
}
