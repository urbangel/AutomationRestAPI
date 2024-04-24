Feature: Actualizar un producto inválido usando la api v1
	Background:
	* url "http://localhost:8084"
	* def ruta_crear = "/api/v1/product/"
	Given path ruta_crear,"/"
	And request { name : "Iphone 900", description: "Gama Alta de Apple", price: 3000 }
	And header Accept = "application/json"
	When method post
	Then status 201
	* def sku_creado = response.sku
	
	Scenario Outline: Actualizar el campo nombre 
	Given path ruta_crear,"/",sku_creado,"/"
  And request <producto>
  And header Accept = "application/json"
  When method put
  Then status <status>
  And match responseType == "json"
  And match $ == {"sku":"#notnull", "status":false , "message":"El nuevo nombre no debe estar en blanco" }
		
  Examples:
  | producto | status | 
  | { name : "", description: "Gama Alta de Apple 4", price: 2300 } | 400 | 
  
	
	Scenario Outline: Actualizar el campo descripción
  Given path ruta_crear,"/",sku_creado,"/"
  And request <producto>
  And header Accept = "application/json"
  When method put
  Then status <status>
  And match responseType == "json"
  And match $ == {"sku":"#notnull", "status":false , "message":"La nueva descripción no debe estar en blanco" }
		
  Examples:
  | producto | status | 
  | { name : "Iphone Actualizado 3", description: "", price: 2300 } | 400 | 
  
	
Scenario Outline: Actualizar el campo precio
  Given path ruta_crear,"/",sku_creado,"/"
  And request <producto>
  And header Accept = "application/json"
  When method put
  Then status <status>
  And match responseType == "json"
  And match $ == {"sku":"#notnull", "status":false , "message":"El nuevo precio debe ser mayor a cero" }
		
  Examples:
  | producto | status | 
  | { name : "Iphone Actualizado 4", description: "Gama Alta de Apple 4", price: "" } | 400|
  
  Scenario Outline: Actualizar los tres campos 
  Given path ruta_crear,"/",sku_creado,"/"
  And request <producto>
  And header Accept = "application/json"
  When method put
  Then status <status>
  And match responseType == "json"
  And match $ == {"sku":"#notnull", "status":false , "message":<message> }
		
    
  Examples:
  | producto | status | message|
  | { name : "", description: "Iphone Actualizado 4", price: "2300" } | 400|"El nuevo nombre no debe estar en blanco"|
  
  | { name : "Iphone Actualizado 3", description: "", price: "2300" } | 400|"La nueva descripción no debe estar en blanco"|
  
  | { name : "Iphone Actualizado 4", description: "Gama Alta de Apple 4", price: "" } | 400|"El nuevo precio debe ser mayor a cero"|
  