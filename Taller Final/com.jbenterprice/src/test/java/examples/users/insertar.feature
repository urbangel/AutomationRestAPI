Feature: crear un nuevo producto usando juego de datos
Background: 
* def ruta_crear = "/api/v1/product/"
* url "http://localhost:8084" 

Scenario Outline: crear producto de manera exitosa usando juego de datos
	Given path ruta_crear,"/"
	And request {name:<name>,description:<description>,price:<price>}
	And header Accept = "application/json"
	When method post
	Then status 201
	And match responseType == 'json'
	And match $ == {"sku": "#notnull", "status":true, "message": "El producto fue creado con éxito!"}
	
		Examples:
| read("file:D:\\trabajo\\productos.csv") |
