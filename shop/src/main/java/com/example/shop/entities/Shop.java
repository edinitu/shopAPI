package com.example.shop.entities;

import com.example.shop.exceptions.BadRequestException;
import com.example.shop.exceptions.ProductAlreadyExistsException;
import com.example.shop.exceptions.ProductDoesNotExistException;
import com.example.shop.exceptions.QuantityOverLimitException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Shop implements Serializable {

  @JsonIgnore
  private final Map<String, Long> categoryMaxQuantityMapping = Map.of(
    "fruits", 5L,
    "vegetables", 5L,
    "cosmetics", 10L,
    "clothes", 10L,
    "dairy", 15L
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

  private void checkQuantity(String category, Long quantity) throws QuantityOverLimitException {
    if (category == null || quantity == null) {
      return;
    }
    long maxQuantity = this.categoryMaxQuantityMapping.get(category);
    if (quantity > maxQuantity) {
      throw new QuantityOverLimitException("Quantity too high: " + quantity + ". Max accepted " + maxQuantity);
    }
  }

  public void addProduct(Product product) throws BadRequestException, QuantityOverLimitException {
    if (!this.categoryMaxQuantityMapping.containsKey(product.getCategory().toLowerCase())) {
      throw new BadRequestException("Category " + product.getCategory() + " unknown");
    }

    checkQuantity(product.getCategory(), product.getQuantity());

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

  public void updateProduct(Product product)
      throws ProductDoesNotExistException, ProductAlreadyExistsException, QuantityOverLimitException {
    Product productInShop = this.products.get(product.getProductId());
    if (productInShop == null) {
      throw new ProductDoesNotExistException("Could not update product. Id does not exist");
    }

    if (product.getName() != null
        && !Objects.equals(productInShop.getName(), product.getName())
        && this.getProductByName(product.getName()) != null) {
      throw new ProductAlreadyExistsException("Cannot change name to an existing product");
    }

    if (product.getName() == null) {
      product.setName(productInShop.getName());
    }

    if (product.getCategory() == null) {
      product.setCategory(productInShop.getCategory());
    }

    if (product.getOnSale() == null) {
      product.setIsOnSale(productInShop.getOnSale());
    }

    if (product.getPrice() == null) {
      product.setPrice(productInShop.getPrice());
    }

    if (product.getQuantity() == null) {
      product.setQuantity(productInShop.getQuantity());
    }

    checkQuantity(product.getCategory(), product.getQuantity());

    this.products.put(product.getProductId(), product);
  }

  public List<Product> getProducts() {
    return this.products.values().stream().toList();
  }

  public Product deleteProduct(Long productId) throws ProductDoesNotExistException {
    Product productToDelete = this.products.get(productId);
    if (productToDelete == null) {
      throw new ProductDoesNotExistException("Product with id " + productId + " does not exist");
    }

    this.products.remove(productId);
    return productToDelete;
  }

  public void setLastProductIndex(long index) {
    this.lastProductIndex = index;
  }
}
