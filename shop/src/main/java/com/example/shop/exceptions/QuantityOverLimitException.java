package com.example.shop.exceptions;

public class QuantityOverLimitException extends Exception {
  public QuantityOverLimitException(String message) {
    super(message);
  }
}
