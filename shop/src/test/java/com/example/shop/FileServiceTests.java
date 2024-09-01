package com.example.shop;

import com.example.shop.entities.Shop;
import com.example.shop.services.FileService;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileServiceTests {

  @Autowired
  private FileService fileService;

  @Test
  void verifyFileIO() {
    // input reading
    fileService.setFilePath("./src/test/resources/test_data_input.txt");
    Shop shop = new Shop();
    fileService.loadDataFromFile(shop);
    Assertions.assertEquals(2, shop.getProducts().size());
    Assertions.assertNotNull(shop.getProductByName("test_data1"));
    Assertions.assertNotNull(shop.getProductByName("test_data2"));

    // writing to file
    fileService.setFilePath("./src/test/resources/test_data_output.txt");
    fileService.dumpDataToFile(shop.getProducts());
    fileService.loadDataFromFile(shop);
    Assertions.assertEquals(2, shop.getProducts().size());
    Assertions.assertNotNull(shop.getProductByName("test_data1"));
    Assertions.assertNotNull(shop.getProductByName("test_data2"));

    // cleanup
    fileService.dumpDataToFile(new ArrayList<>());
  }
}
