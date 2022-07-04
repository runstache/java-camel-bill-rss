package com.lswebworld.rssbillreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Main Application.
 */
@SpringBootApplication
@EntityScan({"com.lswebworld"})
public class RssBillReaderApplication {


  /**
   * Starts the main Spring application.
   *
   * @param args Arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(RssBillReaderApplication.class, args);
  }

}
