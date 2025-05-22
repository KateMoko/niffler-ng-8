package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UsersDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  // DAO Spring JDBC
  private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
  private final AuthAuthorityDao authAuthorityDaoSpring = new AuthAuthorityDaoSpringJdbc();
  private final UdUserDao udUserDaoSpring = new UdUserDaoSpringJdbc();

  // DAO JDBC
  private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
  private final UdUserDao udUserDao = new UdUserDaoJdbc();

  private final TransactionTemplate txTemplate = new TransactionTemplate(
    new JdbcTransactionManager(
      DataSources.dataSource(CFG.authJdbcUrl())
    )
  );

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
    CFG.authJdbcUrl(),
    CFG.userdataJdbcUrl()
  );

  private final TransactionTemplate xaTransactionTemplateChained = new TransactionTemplate(
    new ChainedTransactionManager(
      new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())),
      new JdbcTransactionManager(DataSources.dataSource(CFG.userdataJdbcUrl()))
    )
  );

  //JDBC без транзакций
  public UserJson createUserJdbc(UserJson user) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(user.username());
    authUser.setPassword(pe.encode("12345"));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);

    AuthUserEntity createdAuthUser = authUserDao.create(authUser);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
      e -> {
        AuthorityEntity ae = new AuthorityEntity();
        ae.setUserId(createdAuthUser.getId());
        ae.setAuthority(e);
        return ae;
      }).toArray(AuthorityEntity[]::new);

    authAuthorityDao.create(authorityEntities);
    return UserJson.fromEntity(
      udUserDao.create(UserEntity.fromJson(user)),
      null
    );
  }

  //JDBC с транзакциями
  public UserJson xaCreateUserJdbc(UserJson user) {
    return xaTransactionTemplate.execute(() -> {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
          e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUserId(createdAuthUser.getId());
            ae.setAuthority(e);
            return ae;
          }).toArray(AuthorityEntity[]::new);

        authAuthorityDao.create(authorityEntities);
        return UserJson.fromEntity(
          udUserDao.create(UserEntity.fromJson(user)),
          null
        );
      }
    );
  }

  //Spring JDBC без транзакций
  public UserJson createUserSpringJdbc(UserJson user) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(user.username());
    authUser.setPassword(pe.encode("12345"));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);

    AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
      e -> {
        AuthorityEntity ae = new AuthorityEntity();
        ae.setUserId(createdAuthUser.getId());
        ae.setAuthority(e);
        return ae;
      }).toArray(AuthorityEntity[]::new);

    authAuthorityDaoSpring.create(authorityEntities);
    return UserJson.fromEntity(
      udUserDaoSpring.create(UserEntity.fromJson(user)),
      null
    );
  }

  //Spring JDBC с транзакциями
  public UserJson xaCreateUserSpringJdbc(UserJson user) {
    return xaTransactionTemplate.execute(() -> {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
          e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUserId(createdAuthUser.getId());
            ae.setAuthority(e);
            return ae;
          }).toArray(AuthorityEntity[]::new);

        authAuthorityDaoSpring.create(authorityEntities);
        return UserJson.fromEntity(
          udUserDaoSpring.create(UserEntity.fromJson(user)),
          null
        );
      }
    );
  }

  //ChainedTransactionManager
  public UserJson createUserChainedTxManager(UserJson user) {
    return xaTransactionTemplateChained.execute(status -> {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
          e -> {
            AuthorityEntity ae = new AuthorityEntity();
            ae.setUserId(createdAuthUser.getId());
            ae.setAuthority(e);
            return ae;
          }).toArray(AuthorityEntity[]::new);

        authAuthorityDaoSpring.create(authorityEntities);
        return UserJson.fromEntity(
          udUserDaoSpring.create(UserEntity.fromJson(user)),
          null
        );
      }
    );
  }
}