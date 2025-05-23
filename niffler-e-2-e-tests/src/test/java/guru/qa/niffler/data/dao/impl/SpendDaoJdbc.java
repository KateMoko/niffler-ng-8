package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendDaoJdbc implements SpendDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public SpendEntity create(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
        "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
            "VALUES ( ?, ?, ?, ?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, spend.getUsername());
      ps.setDate(2, spend.getSpendDate());
      ps.setString(3, spend.getCurrency().name());
      ps.setDouble(4, spend.getAmount());
      ps.setString(5, spend.getDescription());
      ps.setObject(6, spend.getCategory().getId());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      spend.setId(generatedKey);
      return spend;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
      "SELECT * FROM spend WHERE id = ?"
    )) {
      ps.setObject(1, id);

      ps.execute();

      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription(rs.getString("description"));
          CategoryEntity category = new CategoryEntity();
          category.setId(rs.getObject("category_id", UUID.class));
          se.setCategory(category);
          return Optional.of(se);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
      "SELECT * FROM spend WHERE username = ?"
    )) {
      ps.setObject(1, username);

      ps.execute();

      List<SpendEntity> spendEntities = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription(rs.getString("description"));
          CategoryEntity category = new CategoryEntity();
          category.setId(rs.getObject("category_id", UUID.class));
          se.setCategory(category);
          spendEntities.add(se);
        }
      }
      return spendEntities;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSpend(SpendEntity spend) {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
      "DELETE from spend WHERE id = ?"
    )) {
      ps.setObject(1, spend.getId());

      int affectedRows = ps.executeUpdate();

      if (affectedRows == 0) {
        throw new SQLException("No spend found with id: " + spend.getId());
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAll() {
    try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
      "SELECT * FROM spend"
    )) {
      ps.execute();
      List<SpendEntity> spendEntities = new ArrayList<>();

      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          SpendEntity se = new SpendEntity();
          se.setId(rs.getObject("id", UUID.class));
          se.setUsername(rs.getString("username"));
          se.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
          se.setSpendDate(rs.getDate("spend_date"));
          se.setAmount(rs.getDouble("amount"));
          se.setDescription(rs.getString("description"));
          CategoryEntity category = new CategoryEntity();
          category.setId(rs.getObject("category_id", UUID.class));
          se.setCategory(category);
          spendEntities.add(se);
        }
      }
      return spendEntities;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}