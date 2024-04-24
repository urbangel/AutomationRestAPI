@createinvalidos
Feature: crear un producto con datos vacios
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
	Scenario Outline: Validar el campo nombre vacio
 * def producto = 
	"""
	{
		"name":"",
		"description":"Telefono de alta gama",
		"price": 1850
	}
	"""
 Given path ruta_crear,"/"
 And request <producto>
 And header Accept = "application/json"
 When method post
 Then status 400
 And match responseType == 'json'
 And match $ == {"sku": "#notnull", "status":false, "message": "El nombre del producto no fue proporcionado"}
	
 
 Examples:
	| producto |
	|  { name : "", description: "Gama Alta de Huawei", price: 1100 }|
	
	Scenario Outline: Validar el campo description vacio
 * def producto = 
	"""
	{
		"name":"Huawei",
		"description":"",
		"price": 1850
	}
	"""
 Given path ruta_crear,"/"
 And request <producto>
 And header Accept = "application/json"
 When method post
 Then status 400
 And match responseType == 'json'
 And match $ == {"sku": "#notnull", "status":false, "message": "La descripción del producto no fue proporcionada"}
	
 
 Examples:
	| producto |
	|  { name : "Huawei", description: "", price: 1100 }|
	
	
	Scenario Outline: Validar el campo precio vacio
 * def producto = 
	"""
	{
		"name":"Huawei",
		"description":"Telefono de alta gama",
	}
	"""
 Given path ruta_crear,"/"
 And request <producto>
 And header Accept = "application/json"
 When method post
 Then status 400
 And match responseType == 'json'
 And match $ == {"sku": "#notnull", "status":false, "message": "El precio del producto no fue proporcionado"}
	
 
 Examples:
	| producto |
	|  { name : "Huawei", description: "Gama Alta de Huawei"  }|
	
	
	
	