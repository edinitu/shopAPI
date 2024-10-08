package com.example.shop.controllers;

import com.example.shop.entities.Product;
import com.example.shop.entities.Shop;
import com.example.shop.exceptions.BadRequestException;
import com.example.shop.exceptions.ProductAlreadyExistsException;
import com.example.shop.exceptions.ProductDoesNotExistException;
import com.example.shop.exceptions.QuantityOverLimitException;
import com.example.shop.services.ShopService;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {

  private final ShopService shopService;
  private final static Logger logger = Logger.getLogger(ShopController.class.getSimpleName());

  public ShopController(ShopService shopService) {
    this.shopService = shopService;
  }

  @GetMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }

  @RequestMapping(value = "/shop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Shop> getShop() {
    return new ResponseEntity<>(shopService.getShop(), HttpStatus.OK);
  }

  @RequestMapping(value = "/shop/product", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> addProduct(@RequestBody Product product)
      throws ProductAlreadyExistsException, BadRequestException, QuantityOverLimitException {
    if (Arrays.stream(product.getClass().getDeclaredMethods())
        .filter(f -> f.getName().contains("get") && !f.getName().contains("Id"))
        .anyMatch(f -> {
          try {
            return f.invoke(product) == null;
          } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not access object function");
          }
          return false;
        })) {
      throw new BadRequestException("Should not have null fields");
    }

    if (product.getQuantity() < 1) {
      throw new BadRequestException("Quantity should be higher or equal to 1");
    }

    shopService.addProduct(product);
    prettyPrintLog(HttpMethod.POST, "/shop/product", product.toString());
    return new ResponseEntity<>(product, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/shop/product", method = RequestMethod.PATCH,
      produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> updateProduct(@RequestBody Product product)
      throws ProductAlreadyExistsException, BadRequestException, ProductDoesNotExistException, QuantityOverLimitException {

    if (product.getProductId() == null
    || Arrays.stream(product.getClass().getDeclaredMethods())
        .filter(f -> f.getName().contains("get") && !f.getName().contains("Id"))
        .allMatch(f -> {
          try {
            return f.invoke(product) == null;
          } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not access object function");
          }
          return false;
        })) {
      throw new BadRequestException("Cannot update product with all null values");
    }

    if (product.getQuantity() != null && product.getQuantity() < 1) {
      throw new BadRequestException("Quantity should be higher or equal to 1");
    }
    shopService.updateProduct(product);
    prettyPrintLog(HttpMethod.PATCH, "/shop/product", product.toString());
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @RequestMapping(value="/shop/product/{productName}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> getProduct(@PathVariable("productName") String productName)
      throws ProductDoesNotExistException {
    Product product = shopService.getProduct(productName);
    prettyPrintLog(HttpMethod.GET, "/shop/product/" + productName, product.toString());
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @RequestMapping(value="/shop/product/{productId}", method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> deleteProduct(@PathVariable("productId") Long productId)
      throws ProductDoesNotExistException {
    Product product = shopService.deleteProduct(productId);
    prettyPrintLog(HttpMethod.DELETE, "/shop/product/" + productId, product.toString());
    return new ResponseEntity<>(product, HttpStatus.OK);
  }

  @ResponseStatus(value= HttpStatus.BAD_REQUEST,
      reason="Does not exist")
  @ExceptionHandler(BadRequestException.class)
  public void badRequest(BadRequestException e) {
    logger.log(Level.WARNING, "Bad request: " + e.getMessage());
  }

  @ResponseStatus(value= HttpStatus.CONFLICT,
      reason="Data already exists")
  @ExceptionHandler(ProductAlreadyExistsException.class)
  public void conflict(ProductAlreadyExistsException e) {
    logger.log(Level.WARNING, "Product already exists. " + e.getMessage());
  }

  @ResponseStatus(value= HttpStatus.NOT_FOUND,
      reason="Does not exist")
  @ExceptionHandler(ProductDoesNotExistException.class)
  public void notExists(ProductDoesNotExistException e) {
    logger.log(Level.WARNING, "Item not found. " + e.getMessage());
  }

  @ResponseStatus(value= HttpStatus.INSUFFICIENT_STORAGE,
      reason="Does not exist")
  @ExceptionHandler(QuantityOverLimitException.class)
  public void limit(QuantityOverLimitException e) {
    logger.log(Level.WARNING, "Max quantity exceeded. " + e.getMessage());
  }

  @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR,
      reason="Server error")
  @ExceptionHandler(Exception.class)
  public void internalError(Exception ex) {
    logger.log(Level.SEVERE, ex.getMessage());
  }

  private void prettyPrintLog(HttpMethod method, String endpoint, String response) {
    logger.info("\n" + method.name() + " " + endpoint + "\n" + "Response: " + response);
  }
}
