package com.example.shop.services;

import com.example.shop.entities.Product;
import com.example.shop.entities.Shop;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

  private static final String FILE_PATH = "./data.txt";

  public FileServiceImpl() {}

  @Override
  public void loadDataFromFile(Shop shop) {

  }

  @Override
  public void dumpDataToFile(List<Product> products) {

  }
}
