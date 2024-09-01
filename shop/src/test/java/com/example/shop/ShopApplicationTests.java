package com.example.shop;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.shop.controllers.ShopController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShopApplicationTests {

  @Autowired
  private ShopController controller;

  @Test
  void contextLoads() throws Exception {
    assertThat(controller).isNotNull();
  }
}
