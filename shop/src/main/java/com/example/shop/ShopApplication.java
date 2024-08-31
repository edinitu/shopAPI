package com.example.shop;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopApplication {
  private static final Logger logger = Logger.getLogger(ShopApplication.class.getSimpleName());

  public static void main(String[] args) {
    logger.log(Level.INFO, "starting App");
    SpringApplication.run(ShopApplication.class, args);
  }
}
