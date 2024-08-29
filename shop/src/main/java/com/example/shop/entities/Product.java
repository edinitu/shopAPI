package com.example.shop.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

public class Product implements Serializable {
  private enum knownCategories {
    FRUIT,
    VEGETABLES,
    COSMETICS,
    CLOTHES,
    DAIRY
  }

  @JsonIgnore
  private Long productId;
  private String name;
  private String category;
  private Long price;
  private Boolean isOnSale;
  private Long quantity;

  public Product() {
  }

  public Product(Long productId, String name, String category, Long price, Boolean isOnSale) {
    this.name = name;
    this.category = category;
    this.price = price;
    this.isOnSale = isOnSale;
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
}
