package com.example.shop;

import com.example.shop.services.ShopService;
import jakarta.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class ShopApplication {
  private static final Logger logger = Logger.getLogger(ShopApplication.class.getSimpleName());

  public static void main(String[] args) {
    logger.log(Level.INFO, "starting App");
    SpringApplication.run(ShopApplication.class, args);
  }
}
