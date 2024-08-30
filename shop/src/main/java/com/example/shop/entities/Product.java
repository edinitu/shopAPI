package com.example.shop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import java.io.Serializable;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JacksonJsonParser;

/**
 * DTO class representing a Product.
 */
public class Product implements Serializable {
  @JsonIgnore
  private Long productId;
  private String name;
  private String category;
  private Long price;
  private Boolean isOnSale;
  private Long quantity;

  public Product() {
  }

  public Product(Long productId, String name, String category, Long price, Boolean isOnSale, Long quantity) {
    this.productId = productId;
    this.name = name;
    this.category = category;
    this.price = price;
    this.isOnSale = isOnSale;
    this.quantity = quantity;
  }

  public Product(String name, String category, Long price, Boolean isOnSale, Long quantity) {
    this.name = name;
    this.category = category;
    this.price = price;
    this.isOnSale = isOnSale;
    this.quantity = quantity;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public Boolean getOnSale() {
    return isOnSale;
  }

  public void setIsOnSale(Boolean isOnSale) {
    this.isOnSale = isOnSale;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object product) {
    if (product instanceof Product p) {
      return p.getName().equals(this.name);
    }
    return false;
  }

  @Override
  public String toString() {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      return ow.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
