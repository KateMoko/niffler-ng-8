package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpendById(@Path("id") String id);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpends(@Query("username") String username,
                                  @Query("filterCurrency") String filterCurrency,
                                  @Query("from") String from,
                                  @Query("to") String to);

  @DELETE("internal/spends/remove")
  Call<Void> deleteSpends(@Query("username") String username,
                          @Query("ids") List<String> ids);

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

  @GET("internal/categories/all")
  Call<List<CategoryJson>> getCategories(@Query("username") String username,
                                         @Query("excludeArchived") boolean excludeArchived);
}