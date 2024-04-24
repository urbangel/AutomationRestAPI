@eliminar
Feature: eliminar el producto usando la api /api/v1/product/
Background: 
* print urlbase
* def ruta_crear = "/api/v1/product/"
* url urlbase
* def feature_result = callonce read("nuevo.feature")
* def  sku_creado = feature_result.response.sku

Scenario: eliminar producto de manera exitosa 
	Given path ruta_crear,"/",sku_creado,"/"
	And header Accept = "application/json"
	When method delete
	Then status 200
	And match responseType == 'json'
	And match $ == {"count": "#number", "status":true, "message": "El producto fue eliminado con éxito"}