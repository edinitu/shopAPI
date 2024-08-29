package com.example.shop.controllers;

import com.example.shop.entities.Product;
import com.example.shop.entities.Shop;
import com.example.shop.exceptions.BadRequestException;
import com.example.shop.exceptions.ProductAlreadyExistsException;
import com.example.shop.exceptions.ProductDoesNotExistException;
import com.example.shop.services.ShopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {

  private final ShopService shopService;

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
      throws ProductAlreadyExistsException, BadRequestException {
    shopService.addProduct(product);
    return new ResponseEntity<>(product, HttpStatus.CREATED);
  }

  @ResponseStatus(value= HttpStatus.BAD_REQUEST,
      reason="Does not exist")
  @ExceptionHandler(BadRequestException.class)
  public void badRequest() {
  }

  @ResponseStatus(value= HttpStatus.CONFLICT,
      reason="Data already exists")
  @ExceptionHandler(ProductAlreadyExistsException.class)
  public void conflict() {
  }

  @ResponseStatus(value= HttpStatus.NOT_FOUND,
      reason="Does not exist")
  @ExceptionHandler(ProductDoesNotExistException.class)
  public void notExists() {
  }

  @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR,
      reason="Server error")
  @ExceptionHandler(Exception.class)
  public void internalError() {
  }
}
