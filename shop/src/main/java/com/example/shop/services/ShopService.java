package com.example.shop.services;

import com.example.shop.entities.Product;
import com.example.shop.entities.Shop;
import com.example.shop.exceptions.BadRequestException;
import com.example.shop.exceptions.ProductAlreadyExistsException;
import com.example.shop.exceptions.ProductDoesNotExistException;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.context.annotation.Bean;

public interface ShopService {

  Shop getShop();

  void init();

  void addProduct(Product product) throws ProductAlreadyExistsException, BadRequestException;

  void updateProduct(Product product)
      throws ProductDoesNotExistException, ProductAlreadyExistsException;

  Product getProduct(String productName) throws ProductDoesNotExistException;

  Product deleteProduct(Long productId) throws ProductDoesNotExistException;
}
