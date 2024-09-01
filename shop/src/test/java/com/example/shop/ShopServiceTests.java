package com.example.shop;

import static org.mockito.Mockito.mock;

import com.example.shop.entities.Product;
import com.example.shop.exceptions.BadRequestException;
import com.example.shop.exceptions.ProductAlreadyExistsException;
import com.example.shop.exceptions.ProductDoesNotExistException;
import com.example.shop.exceptions.QuantityOverLimitException;
import com.example.shop.services.FileService;
import com.example.shop.services.ShopService;
import com.example.shop.services.ShopServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShopServiceTests {

  @Autowired
  private ShopService shopService;
  @Mock
  private FileService fileService;

  @BeforeEach
  public void setup() {
    fileService = mock(FileService.class);
    shopService = new ShopServiceImpl(fileService);
  }

  @Test
  void verifyAddProduct()
      throws QuantityOverLimitException, ProductAlreadyExistsException,
      BadRequestException {
    Product product = new Product(
        1L,
        "test_name",
        "fruits",
        100L,
        false,
        4L
    );

    shopService.addProduct(product);

    Assertions.assertEquals(product, shopService.getShop().getProducts().get(0));
  }

  @Test
  void verifyAddProductWrongCategory() {
    Product product = new Product(
        1L,
        "test_name",
        "test_category",
        100L,
        false,
        4L
    );

    Assertions.assertThrows(BadRequestException.class, () -> shopService.addProduct(product));
  }


  @Test
  void verifyAddProductConflict()
      throws QuantityOverLimitException, ProductAlreadyExistsException, BadRequestException {
    Product product = new Product(
        1L,
        "test_name",
        "fruits",
        100L,
        false,
        4L
    );

    shopService.addProduct(product);

    Assertions.assertThrows(ProductAlreadyExistsException.class, () -> shopService.addProduct(product));
  }

  @Test
  void verifyAddProductOverQuantityLimit() {
    Product product = new Product(
        1L,
        "test_name",
        "fruits",
        100L,
        false,
        100L
    );

    Assertions.assertThrows(QuantityOverLimitException.class, () -> shopService.addProduct(product));
  }

  @Test
  void verifyUpdateProduct()
      throws QuantityOverLimitException, ProductAlreadyExistsException,
      BadRequestException, ProductDoesNotExistException {
    Product product = new Product(
        1L,
        "test_name",
        "fruits",
        100L,
        false,
        4L
    );

    shopService.addProduct(product);

    Assertions.assertEquals(product, shopService.getShop().getProducts().get(0));

    Product productUpdate = new Product();
    productUpdate.setProductId(1L);
    productUpdate.setName("test_update");

    shopService.updateProduct(productUpdate);


    Product expectedProduct = new Product(
        1L,
        "test_update",
        "fruits",
        100L,
        false,
        4L
    );
    Assertions.assertEquals(expectedProduct, shopService.getShop().getProducts().get(0));
  }

  @Test
  void verifyUpdateConflict()
      throws QuantityOverLimitException, ProductAlreadyExistsException,
      BadRequestException {
    Product product = new Product(
        1L,
        "test_name",
        "fruits",
        100L,
        false,
        4L
    );

    Product product2 = new Product(
        2L,
        "test_name2",
        "fruits",
        100L,
        false,
        4L
    );

    shopService.addProduct(product);
    shopService.addProduct(product2);

    Product productUpdate = new Product();
    productUpdate.setProductId(2L);
    productUpdate.setName("test_name");

    Assertions.assertThrows(ProductAlreadyExistsException.class, () -> shopService.updateProduct(productUpdate));
  }

  @Test
  void verifyGetProduct()
      throws QuantityOverLimitException, ProductAlreadyExistsException,
      BadRequestException, ProductDoesNotExistException {
    Product product = new Product(
        1L,
        "test_name",
        "fruits",
        100L,
        false,
        4L
    );

    shopService.addProduct(product);

    Assertions.assertEquals(product, shopService.getShop().getProducts().get(0));
    Assertions.assertEquals(product, shopService.getProduct("test_name"));

    Assertions.assertThrows(ProductDoesNotExistException.class, () -> shopService.getProduct("does_not_exist"));
  }

  @Test
  void verifyDeleteProduct()
      throws QuantityOverLimitException, ProductAlreadyExistsException,
      BadRequestException, ProductDoesNotExistException {
    Product product = new Product(
        1L,
        "test_name",
        "fruits",
        100L,
        false,
        4L
    );

    shopService.addProduct(product);

    Assertions.assertEquals(product, shopService.getShop().getProducts().get(0));
    Assertions.assertEquals(product, shopService.deleteProduct(1L));

    Assertions.assertThrows(ProductDoesNotExistException.class, () -> shopService.deleteProduct(5L));
  }
}
