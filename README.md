# Recipes
A Spring boot multi-user web service that allows storing, retrieving, updating, and deleting recipes.

Technology stack:
1) Spring Boot, Security, MVC
2) Hibernate, H2 Database
3) Apache Tomcat
3) Rest API
4) JSON
5) Project Lombok

Multi-user application that allows you to store recipes in a database. 
Data exchange takes place via REST API. User registration and loading / updating of the recipe occurs by sending a JSON file.

The following endpoints exist:

POST /api/register (All users permission)
receives a JSON object with two fields: email (string), and password (string).
If a user with a specified email does not exist, the program saves (registers) the user in a database and responds with 200 (Ok). 
If a user is already in the database, respond with the 400 (Bad Request) status code.
Both fields are required and must be valid: email should contain @ and . symbols, password should contain at least 8 characters and shouldn't be blank.
If the fields do not meet these restrictions, the service should respond with 400 (Bad Request).

DELETE /api/deleteuser/{id} (Only Admin role permission)
Removes the user and all his recipes from the database, respond with the 204 (No Content) status code.
If a user with a specified id does not exist, respond with the 404 (Not Found) status code.
If the request was made by a non-Admin role, respond with the 403 (Forbidden) status code.

GET /api/recipe/{id} (All users)
Respond JSON witn the recipe from database.
If the recipe is not found in the database, respond with the 404 (Not Found) status code.
It is possible to search for recipes by category or incomplete name (partial match). In this case, JSON is returned with a list of recipes.

POST /api/recipe/new (Only authenticated user)
Saves the new recipe to the database, respond with new JSON with recipe id in database.

PUT /api/recipe/{id} (Only authenticated user, owner of recipe)
Updates the recipe in the database, respond with the 204 (No Content) status code.
If the recipe is not found in the database, respond with the 404 (Not Found) status code.
If the user is not the author of the recipe, respond with the 403 (Forbidden) status code.

DELETE /api/recipe/{id} (Only authenticated user, owner of recipe)
Delete the recipe from the database, respond with the 204 (No Content) status code.
If the recipe is not found in the database, respond with the 404 (Not Found) status code.
If the user is not the author of the recipe, respond with the 403 (Forbidden) status code.

DELETE /api/recipe/delete (Only Admin role permission)
Removes all recipes from the database
