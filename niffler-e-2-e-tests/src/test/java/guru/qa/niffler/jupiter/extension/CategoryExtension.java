package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static guru.qa.niffler.utils.RandomDataUtils.randomCategoryName;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
  private final SpendApiClient spendApiClient = new SpendApiClient();
  private final SpendDbClient spendDbClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
      .ifPresent(user -> {
        if (user.categories().length > 0) {
          CategoryJson categoryJson = new CategoryJson(
            null,
            randomCategoryName(),
            user.username(),
            user.categories()[0].archived()
          );
          CategoryJson createdCategory = spendDbClient.createCategory(categoryJson);

          context.getStore(NAMESPACE).put(context.getUniqueId(), createdCategory);
        }
      });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class))
      .filter(category -> !category.archived())
      .ifPresent(category -> spendApiClient.updateCategory(
        new CategoryJson(category.id(), category.name(), category.username(), true)
      ));
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}