package com.jbenterprise.badpath;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jbenterprise.rest_assured.entity.Product;
import com.jbenterprise.rest_assured.entity.ProductRequest;
import com.jbenterprise.rest_assured.entity.ProductResponse;
import com.jbenterprise.rest_assured.utils.Utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class FalloSuite {
	

	@BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8083";
    }
	
	//Eliminar el nombre del campo name
	@Test
	    //@Disabled
	    @DisplayName("Create new product using POST - /api/v1/product/")
	 public void createNewProduct() {    	
	    	ProductRequest productRequest = Utils.generateNewProductRequest();
	    	productRequest.setName("");
	    	given()
	    		 .header("User-Agent", Utils.USER_AGENT)
	    		 .header("Content-Type",ContentType.JSON)
	    		 .body(productRequest)
	    		 
	    			 
	    	
	    	.when()
	    		.post("/api/v1/product/")
	    	.then()
	    		.statusCode(HttpStatus.SC_BAD_REQUEST)
	    		.body("status", equalTo(false))
	    		.body("message", equalTo("El nombre del producto no fue proporcionado"))
	    		.body("sku", CoreMatchers.equalTo(""))
	    		.log()
	    		.all();
	    }
	  //Caso con SKU vacio
	  @Test
	    @DisplayName("Update product using PUT - /api/v1/product/")
	    public void updateProduct() {
	    	ProductRequest productRequest = Utils.generateNewProductRequest();
	    	String sku = given()
	    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON)
	    		.body(productRequest)
	    	.when()
	    		.post("/api/v1/product/")
	    	.then()
	    		.statusCode(HttpStatus.SC_CREATED)
	    		.body("status", equalTo(true))
	    		.body("message", equalTo("El producto fue creado con éxito!"))
	    		.body("sku", CoreMatchers.not(equalTo("")))
	    		.extract()
	    		.jsonPath().getString("sku");
	    	
	    	productRequest.setName("Name Updated");
	    	productRequest.setDescription("Description Updated");
	    	productRequest.setPrice(1900);

	    	given()
	    		.pathParam("codigo", "")
	        	.contentType(ContentType.JSON)
	        	.body(productRequest)
	        .when()
	        	.put("/api/v1/product/{codigo}/")
	        .then()
		    	.statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED)
		    	.body("status", equalTo(405))
		    	.body("message", equalTo(null))
	        	.log()
	        	.all();
	    }
	  
	 // caso con precio=0,nombre y descripcion actualizar vacio 
	  @Test
	    @DisplayName("Update product using PUT - /api/v1/product/")
	    public void updateProductDos() {
	    	ProductRequest productRequest = Utils.generateNewProductRequest();
	    	
	    	String sku = given()
	    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON)
	    		.body(productRequest)
	    	.when()
	    		.post("/api/v1/product/")
	    	.then()
	    		.statusCode(HttpStatus.SC_CREATED)
	    		.body("status", equalTo(true))
	    		.body("message", equalTo("El producto fue creado con éxito!"))
	    		.body("sku", CoreMatchers.not(equalTo("")))
	    		.extract()
	    		.jsonPath().getString("sku");
	    	
	    	productRequest.setName("nameUpdate");
	    	productRequest.setDescription("");
	    	productRequest.setPrice(1500);

            String newMessage="";
	    	if (productRequest.getName().isEmpty()) {
	    	    newMessage="El nuevo nombre no debe estar en blanco";
	    	}
	    	else if (productRequest.getDescription().isEmpty()) {
	    	    newMessage="La nueva descripción no debe estar en blanco";
	    	}
	    	else if (productRequest.getPrice() <= 0) {
	    	    newMessage="El nuevo precio debe ser mayor a cero";
	    	}else{
	    	newMessage="";
	    	}

           	given()
	    		.pathParam("codigo", sku)
	        	.contentType(ContentType.JSON)
	        	.body(productRequest)
	        .when()
	        	.put("/api/v1/product/{codigo}/")
	        .then()
		    	.statusCode(HttpStatus.SC_BAD_REQUEST)
		    	.body("status", equalTo(false))
		    	.body("message", equalTo(newMessage))
	        	.log()
	        	.all();
	    }
	  
	  
	 // Actualizar precio en cero  
	 @Test
	    //@Disabled
	    @DisplayName("Update product price using PATCH - /api/v1/product/")
	    public void updateProductPrice() {
	    	ProductRequest productRequest = Utils.generateNewProductRequest();
	    	
	    	
	    	String sku = given()
	    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON)
	    		.body(productRequest)
	    	.when()
	    		.post("/api/v1/product/")
	    	.then()
	    		.statusCode(HttpStatus.SC_CREATED)
	    		.body("status", equalTo(true))
	    		.body("message", equalTo("El producto fue creado con éxito!"))
	    		.body("sku", CoreMatchers.not(equalTo("")))
	    		.extract()
	    		//Aquí se guarda un sku distinto
	    		.jsonPath().getString("sku");
	    	
	   	productRequest.setName("LORE_IPSUM");
	    	productRequest.setDescription("LORE_IPSUM");
	    	//Aqui se condiciona si el precio es igual a cero
	    	productRequest.setPrice(0);

	    	given()
	    		.contentType(ContentType.JSON)
	    		.body(productRequest)
	    	.when()
	        	.patch(String.format("/api/v1/product/%1$s/",sku))
	        .then()
		    	.statusCode(HttpStatus.SC_BAD_REQUEST)
		    	.body("status", equalTo(false))
		    	.body("message", equalTo("El nuevo precio debe ser mayor a cero"))
		    	.log()
		    	.all();
	    	
	        }
	 
	 //Caso producto no encontrado
	  @Test
	    //@Disabled
	    @DisplayName("Update product price using PATCH - /api/v1/product/")
	    public void updateProductPriceDos() {
	    	ProductRequest productRequest = Utils.generateNewProductRequest();
	    	
	    	
	    	String sku = given()
	    		.headers("User-Agent",Utils.USER_AGENT, "Content-Type", ContentType.JSON)
	    		.body(productRequest)
	    	.when()
	    		.post("/api/v1/product/")
	    	.then()
	    		.statusCode(HttpStatus.SC_CREATED)
	    		.body("status", equalTo(true))
	    		.body("message", equalTo("El producto fue creado con éxito!"))
	    		.body("sku", CoreMatchers.not(equalTo("")))
	    		.extract()
	    		//Aquí se guarda un sku distinto
	    		.jsonPath().getString("123");
	    	
	       	
	    	productRequest.setName("LORE_IPSUM");
	    	productRequest.setDescription("LORE_IPSUM");
	    	productRequest.setPrice(2500);
	    	
	    	
	    	given()
	    		.contentType(ContentType.JSON)
	    		.body(productRequest)
	    	.when()
	        	.patch(String.format("/api/v1/product/%1$s/",sku))
	        .then()
		    	.statusCode(HttpStatus.SC_BAD_REQUEST)
		    	.body("status", equalTo(false))
		    	.body("message", equalTo("El producto no fue encontrado"))
		    	.log()
		    	.all();

	    }

	   //Caso producto no existente  
	    @Test
	    //@Disabled
	    @DisplayName("Delete product using DELETE - /api/v1/product/")
	    public void deleteProduct() {
	    	ProductRequest productRequest = Utils.generateNewProductRequest();
	    	String sku = given()
	    		.contentType(ContentType.JSON)
	    		.body(productRequest)
	    	.when()
	    		.post("/api/v1/product/")
	    	.then()
				.statusCode(HttpStatus.SC_CREATED)
				.body("status", equalTo(true))
				.body("message", equalTo("El producto fue creado con éxito!"))
				.body("sku", CoreMatchers.not(equalTo("")))
	    		.extract()
	    		 //Obtenemos  un sku distinto
	    		
	    		.jsonPath().getString("123");

	    	given()
	        	.contentType(ContentType.JSON)
	        	.body(productRequest)
	        .when()
	        	.delete(String.format("/api/v1/product/%1$s/",sku))
	        .then()
		    	.statusCode(HttpStatus.SC_BAD_REQUEST)
		    	.body("status", equalTo(false))
		    	.body("message", equalTo("El producto no fue encontrado"))
		    	.log()
		    	.all();  	
	    }

	
    @Test
    @DisplayName("GET products verify greater than zero - /api/v1/product/")
    public void getProductsVerifyUsingResponse() {
    	 Response response = given()
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("/api/v2/product/")//Uri
    	.then()
    		.extract()
    		.response();
    	 
         Assertions.assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode());
         Assertions.assertFalse(response.jsonPath().getBoolean("status"));
        

    }
	
	@Test
    @DisplayName("GET products verify greater than zero using extract as - /api/v1/product/")
    public void getProductsVerifyUsinExtract() {
    	 ProductResponse response = given()
    		.contentType(ContentType.JSON)//Headers
    	.when()
    		.get("/api/v1/product/")//Uri
    	.then()
    		.statusCode(HttpStatus.SC_OK)
    		.extract()
    		.as(ProductResponse.class);
    	 
         Assertions.assertTrue(response.getStatus());
         assertThat(response.getProducts().size(), is(greaterThan(0)));
    }
}
