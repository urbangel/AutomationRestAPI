Feature: escenario reutilizable para crear producto
Scenario: Crear un producto de manera exitosa
* url "http://localhost:8084"
	* def ruta_crear = "/api/v1/product/"
	Given path ruta_crear,"/"
	And request { name : "Iphone 900", description: "Gama Alta de Apple", price: 3000 }
	And header Accept = "application/json"
	When method post
	Then status 201