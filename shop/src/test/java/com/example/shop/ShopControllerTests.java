package com.example.shop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.shop.controllers.ShopController;
import com.example.shop.entities.Product;
import com.example.shop.exceptions.ProductAlreadyExistsException;
import com.example.shop.exceptions.ProductDoesNotExistException;
import com.example.shop.services.ShopService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(ShopController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ShopControllerTests {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ShopService service;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .build();
  }

  @Test
  void verifyPostProductReturnsOk() throws Exception {
    Product product = new Product(
        1L,
        "test_name",
        "test_category",
        100L,
        false,
        10L
    );

    MvcResult result = this.mockMvc.perform(post("/shop/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(product).getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isCreated())
        .andReturn();

    assertThat(result).isNotNull();
    String userJson = result.getResponse().getContentAsString();
    assertThat(userJson).isNotEmpty();
    assertThat(userJson).isEqualToIgnoringCase(toJson(product));
  }

  @Test
  void verifyPostProductWithNullThrows() throws Exception {
    Product product = new Product(
        1L,
        "test_name",
        "test_category",
        100L,
        null,
        null
    );

   this.mockMvc.perform(post("/shop/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(product).getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void verifyPostProductReturnsConflict() throws Exception {
    Product product = new Product(
        1L,
        "test_name",
        "test_category",
        100L,
        false,
        10L
    );

    doThrow(ProductAlreadyExistsException.class).when(service).addProduct(product);

    this.mockMvc.perform(post("/shop/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(product).getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isConflict());
  }

  @Test
  void verifyUpdateProductReturnsOk() throws Exception {
    Product product = new Product();
    product.setProductId(1L);
    product.setName("test_update");

    this.mockMvc.perform(patch("/shop/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(product).getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isOk());
  }

  @Test
  void verifyUpdateProductBadRequest() throws Exception {
    Product product = new Product();
    product.setProductId(1L);

    this.mockMvc.perform(patch("/shop/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(product).getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isBadRequest());

    Product product1 = new Product();
    product1.setName("test_update");

    this.mockMvc.perform(patch("/shop/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(product1).getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void verifyPatchProductReturnsConflict() throws Exception {
    Product product = new Product(
        1L,
        "test_name",
        "test_category",
        100L,
        false,
        10L
    );

    doThrow(ProductAlreadyExistsException.class).when(service).updateProduct(product);

    this.mockMvc.perform(patch("/shop/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(product).getBytes(StandardCharsets.UTF_8)))
        .andExpect(status().isConflict());
  }

  @Test
  void verifyGetProductReturnsOk() throws Exception {
    Product product = new Product(
        1L,
        "test_name",
        "test_category",
        100L,
        false,
        10L
    );

    when(service.getProduct("test_name")).thenReturn(product);

    MvcResult result = this.mockMvc.perform(get("/shop/product/test_name"))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(result).isNotNull();
    String userJson = result.getResponse().getContentAsString();
    assertThat(userJson).isNotEmpty();
    assertThat(userJson).isEqualToIgnoringCase(toJson(product));
  }

  @Test
  void verifyGetProductReturnsNotFound() throws Exception {
    when(service.getProduct("test_name")).thenThrow(ProductDoesNotExistException.class);

    this.mockMvc.perform(get("/shop/product/test_name"))
        .andExpect(status().isNotFound());
  }

  @Test
  void verifyDeleteProductReturnsOk() throws Exception {
    Product product = new Product(
        1L,
        "test_name",
        "test_category",
        100L,
        false,
        10L
    );

    when(service.deleteProduct(1L)).thenReturn(product);

    MvcResult result = this.mockMvc.perform(delete("/shop/product/1"))
        .andExpect(status().isOk())
        .andReturn();

    assertThat(result).isNotNull();
    String userJson = result.getResponse().getContentAsString();
    assertThat(userJson).isNotEmpty();
    assertThat(userJson).isEqualToIgnoringCase(toJson(product));
  }

  @Test
  void verifyDeleteProductReturnsNotFound() throws Exception {
    when(service.deleteProduct(1L)).thenThrow(ProductDoesNotExistException.class);

    this.mockMvc.perform(delete("/shop/product/1"))
        .andExpect(status().isNotFound());
  }

  private String toJson(Object o) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(o);
  }
}
