package io.goorm.backend.config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

public class DatabaseConfig {

  public static DataSource getDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:/Users/jung0522/Desktop/h2/bin/data;AUTO_SERVER=TRUE;IFEXISTS=TRUE");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    return dataSource;
  }
}
