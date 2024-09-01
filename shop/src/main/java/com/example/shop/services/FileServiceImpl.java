package com.example.shop.services;

import com.example.shop.entities.Product;
import com.example.shop.entities.Shop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

  private static final Logger logger = Logger.getLogger(FileServiceImpl.class.getSimpleName());
  private static String FILE_PATH;
  private static final String DELIMITER = ",";

  public FileServiceImpl() {
    FILE_PATH = "./data.txt";
  }

  @Override
  public void loadDataFromFile(Shop shop) {
    try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
      logger.info("Try loading products from file");
      String line;
      long index = 0;
      while ((line = br.readLine()) != null) {
        String[] fields = line.split(DELIMITER);
        try {
          Product product = new Product(
              Long.valueOf(fields[0]),
              fields[1],
              fields[2],
              Long.valueOf(fields[3]),
              Boolean.valueOf(fields[4]),
              Long.valueOf(fields[5])
          );
          shop.addProduct(product);
          if (product.getProductId() > index) {
            index = product.getProductId();
          }
        } catch (Exception e) {
          logger.log(Level.SEVERE, "Something is wrong with product: " + line + ". Could not add it to shop");
        }
      }
      shop.setLastProductIndex(index);
      logger.info("Loaded " + index + " products into shop");
    } catch (FileNotFoundException e) {
      logger.log(Level.WARNING, "Could not load data from file, does not exist");
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage());
    }
  }

  @Override
  public void dumpDataToFile(List<Product> products) {
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
      for (Product product: products) {
        String sb = product.getProductId()
            + DELIMITER
            + product.getName()
            + DELIMITER
            + product.getCategory()
            + DELIMITER
            + product.getPrice()
            + DELIMITER
            + product.getIsOnSale()
            + DELIMITER
            + product.getQuantity()
            + "\n";
        writer.write(sb);
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage());
    }
  }

  // Used just for unit testing
  @Override
  public void setFilePath(String filePath) {
    FILE_PATH = filePath;
  }
}
