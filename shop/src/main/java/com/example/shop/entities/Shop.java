package com.example.shop.entities;

import com.example.shop.exceptions.BadRequestException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Shop implements Serializable {

  @JsonIgnore
  private final Map<String, Integer> categoryMaxQuantityMapping = Map.of(
    "fruits", 5,
    "vegetables", 5,
    "cosmetics", 10,
    "clothes", 10,
    "dairy", 15
  );

  private final static String shopName = "Application Shop";
  private final ConcurrentHashMap<Long, Product> products;
  private final ConcurrentHashMap<String, Long> nameToIdMap;
  private long lastProductIndex = 0;

  public Shop() {
    this.products = new ConcurrentHashMap<>();
    this.nameToIdMap = new ConcurrentHashMap<>();
  }

  public String getShopName() {
    return shopName;
  }

  public void addProduct(Product product) throws BadRequestException {
    if (!this.categoryMaxQuantityMapping.containsKey(product.getCategory().toLowerCase())) {
      throw new BadRequestException("Category " + product.getCategory() + " unknown");
    }

    if (product.getProductId() == null) {
      product.setProductId(++lastProductIndex);
    }

    this.products.put(product.getProductId(), product);
    this.nameToIdMap.put(product.getName(), product.getProductId());
  }

  public Product getProductByName(String name) {
    Long id = this.nameToIdMap.get(name);
    if (id != null) {
      return this.products.get(id);
    }
    return null;
  }

  public List<Product> getProducts() {
    return this.products.values().stream().toList();
  }

  public void setLastProductIndex(long index) {
    this.lastProductIndex = index;
  }
}
