# Shop API

Basic CRUD API for a shop. Written in java, using Spring Boot and Maven. Has the following features:

### 1. Endpoints

```
POST /shop/product

Example Request Body:
{
    "name" : "test_product",
    "category": "fruits",
    "price": 10,
    "isOnSale": false,
    "quantity": 2
}

Example Response:
201 Created
Response-Body:
{
    "productId": 1,
    "name" : "test_product",
    "category": "fruits",
    "price": 10,
    "isOnSale": false,
    "quantity": 2
}
```

```
PATCH /shop/product

Example Request Body:
{
    "productId": 1,
    "name" : "test_product_update",
}

Example Response:
200 OK
Response-Body:
{
    "productId": 1,
    "name" : "test_product_update",
    "category": "fruits",
    "price": 10,
    "isOnSale": false,
    "quantity": 2
}
```

```
GET /shop

Example Response:
200 OK
Response-Body:
{
    "products": [
        {
            "productId": 1,
            "name": "test_product_update",
            "category": "fruits",
            "price": 10,
            "isOnSale": false,
            "quantity": 2
        }
    ],
    "shopName": "Application Shop"
}
```

```
GET /shop/product/test_product_update

Example Response:
200 OK
Response-Body:
{
    "productId": 1,
    "name" : "test_product_update",
    "category": "fruits",
    "price": 10,
    "isOnSale": false,
    "quantity": 2
}
```

```
DELETE /shop/product/1

Example Response:
200 OK
Response-Body:
{
    "productId": 1,
    "name" : "test_product_update",
    "category": "fruits",
    "price": 10,
    "isOnSale": false,
    "quantity": 2
}
```

### 2. File persistence
Apart from the in memory storage, the API persists the data periodically to a file. If restarted, it loads the data found in the file.

### 3. Restrictions

 - can only add products with a quantity lower than or equal to predefined values
 - a product is unique by its name - cannot add 2 products with the same name

## Requierements
java 17, Maven.

- to build the project: run ```mvn clean install``` in /shop directory
- to run the API: ```java -jar <jar_found_in_target>```
