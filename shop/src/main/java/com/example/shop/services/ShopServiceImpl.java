package com.example.shop.services;

import com.example.shop.entities.Product;
import com.example.shop.entities.Shop;
import com.example.shop.exceptions.BadRequestException;
import com.example.shop.exceptions.ProductAlreadyExistsException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {
  private final FileService fileService;
  private final Shop shop;
  private final Logger logger = Logger.getLogger(ShopServiceImpl.class.getSimpleName());

  public ShopServiceImpl(FileService fileService) {
    this.fileService = fileService;
    this.shop = new Shop();
  }

  @Override
  @PostConstruct
  public void init() {
    logger.log(Level.INFO, "init shop service");
    fileService.loadDataFromFile(this.shop);
    Thread t = new Thread(new FileWriteManager(shop, fileService));
    t.start();
  }

  @Override
  public Shop getShop() {
    return this.shop;
  }

  @Override
  public void addProduct(Product product) throws ProductAlreadyExistsException, BadRequestException {
    List<Product> alreadyExistingProducts = this.shop.getProductsByName(product.getName());
    if (alreadyExistingProducts.stream().anyMatch(p -> p.equals(product))) {
      throw new ProductAlreadyExistsException(String.format("Product %s already exists", product));
    }

    this.shop.addProduct(product);
  }

  @Override
  public void updateProduct(Product product) {

  }

  @Override
  public void getProduct(String productName) {

  }

  @Override
  public void deleteProduct(String productName) {

  }

  public class FileWriteManager implements Runnable {
    private final FileService fileService;
    private final Shop shop;

    public FileWriteManager(Shop shop, FileService fileService) {
      this.fileService = fileService;
      this.shop = shop;
    }

    @Override
    public void run() {
      logger.log(Level.INFO, "starting file writer thread");
      while (true) {
        try {
          int updateTime = 60; // 1 min
          Thread.sleep(updateTime * 1000);
          logger.log(Level.INFO, "try dumping data to file");
          fileService.dumpDataToFile(shop.getProducts());
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
