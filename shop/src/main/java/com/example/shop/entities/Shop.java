package com.example.shop.entities;

import com.example.shop.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonGetter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Shop implements Serializable {

  private enum KnownCategory {
    FRUIT,
    VEGETABLES,
    COSMETICS,
    CLOTHES,
    DAIRY
  }

  private final static String shopName = "Application Shop";
  private final ConcurrentHashMap<Long, Product> products;
  private long lastProductIndex = 0;

  public Shop() {
    this.products = new ConcurrentHashMap<>();
  }

  public String getShopName() {
    return shopName;
  }

  public void addProduct(Product product) throws BadRequestException {
    try {
      KnownCategory category = KnownCategory.valueOf(product.getCategory().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new BadRequestException(e.getMessage());
    }
    product.setProductId(++lastProductIndex);
    this.products.put(product.getProductId(), product);
  }

  public List<Product> getProductsByName(String name) {
    List<Product> productsWithName = new ArrayList<>();
    for (Map.Entry<Long, Product> entry: this.products.entrySet()) {
      if (entry.getValue().getName().equals(name)) {
        productsWithName.add(entry.getValue());
      }
    }
    return productsWithName;
  }

  public List<Product> getProducts() {
    return this.products.values().stream().toList();
  }

  public void setLastProductIndex(long index) {
    this.lastProductIndex = index;
  }
}
