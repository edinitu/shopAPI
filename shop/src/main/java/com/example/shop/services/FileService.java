package com.example.shop.services;

import com.example.shop.entities.Product;
import com.example.shop.entities.Shop;
import java.util.List;

public interface FileService {
  void loadDataFromFile(Shop shop);

  void dumpDataToFile(List<Product> products);
}
