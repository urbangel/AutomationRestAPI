@modificar
Feature: Modificar un producto usando la api /api/v1/product/
Background: 
* def ruta_modificar = "/api/v1/product/"
* url "http://localhost:8084" 
* def feature_result = callonce read("nuevo.feature")
* print feature_result.response
* def  sku_creado = feature_result.response.sku

Scenario Outline: Modificar un producto de forma exitosa.
Given path ruta_modificar,"/",sku_creado,"/"
And request <producto>
And header Accept = "application/json"
When method put
Then status 200
And match responseType == "json"
And match $ == {"sku":"#notnull", "status": true,"message":"El producto fue actualizado con éxito"}
	Examples:
	| producto |
	|  { name : "Redmi  101", description: "Gama Alta de Huawei", price: 1100 } |
	|  { name : "Redmi  102", description: "Gama Alta de Huawei", price: 1200 } |
	|  { name : "Redmi  103", description: "Gama Alta de Huawei", price: 1300 } |