package com.example.ptn.config;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration to set datasource.
 */
@Configuration
public class AppConfiguration {


  /**
   * Create dataSource.
   *
   * @return DataSource
   */
  @Bean
  @Primary
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    DataSourceBuilder<?> dsb = DataSourceBuilder.create();
    return dsb.build();
  }
}
