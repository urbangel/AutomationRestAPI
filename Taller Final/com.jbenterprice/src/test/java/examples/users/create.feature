Feature: Crear un nuevo producto usando la API v1
	Background:
	* url urlbase
	* def ruta_crear = "/api/v1/product/"
	* def nuevo_producto = 
	"""
	{
		"name":"Huawei",
		"description":"Telefono de alta gama",
		"price": 1850
	}
	"""
	

	Scenario: Crear un producto de manera exitosa usando variable de texto
	* def producto = 
	"""
	{
		"name":"Huawei",
		"description":"Telefono de alta gama",
		"price": 1850
	}
	"""
	Given url "http://localhost:8084/api/v1/product/"
	And request producto
	And header Accept = "application/json"
	When method post
	Then status 201
	
	Scenario: Crear un producto de manera exitosa usando json incrustado
	Given url "http://localhost:8084/api/v1/product/"
	And request { name : "SGalaxy", description: "Gama Alta de Samsung", price: 2300 }
	And header Accept = "application/json"
	When method post
	Then status 201
	
	Scenario: Crear un producto de manera exitosa usando variables en Background
	Given path ruta_crear,"/"
	And request nuevo_producto
	And header Accept = "application/json"
	When method post
	Then status 201
	
	Scenario: Crear un producto de manera exitosa validando el response
	Given path ruta_crear,"/"
	And request nuevo_producto
	And header Accept = "application/json"
	When method post
	Then status 201
	And match responseType == 'json'
	And match $ == {"sku": "#notnull", "status":true, "message": "El producto fue creado con éxito!"}
	
	Scenario Outline: Crear un producto de manera exitosa usando Examples
	Given path ruta_crear,"/"
	And request <producto>
	And header Accept = "application/json"
	When method post
	Then status 201
	And match responseType == 'json'
	And match $ == {"sku": "#notnull", "status":true, "message": "El producto fue creado con éxito!"}
	
	Examples:
	| producto |
	|  { name : "Redmi  101", description: "Gama Alta de Huawei", price: 1100 } |
	|  { name : "Redmi  102", description: "Gama Alta de Huawei", price: 1200 } |
	|  { name : "Redmi  103", description: "Gama Alta de Huawei", price: 1300 } |
	
	