package com.lswebworld.rssbillreader.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application Properties from Environment variables.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppSettings {

  private String billPrefix;
  private int pollInterval;

}
