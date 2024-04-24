@modificar
Feature: Modificar un producto usando la api /api/v1/product/
Background: 
* def ruta_modificar = "/api/v1/product/"
* url "http://localhost:8084" 
* def feature_result = callonce read("nuevo.feature")
* print feature_result.response
* def  sku_creado = feature_result.response.sku


Scenario Outline: Modificar un producto con datos inválidos
  Given path ruta_modificar,"/",sku_creado,"/"
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
  