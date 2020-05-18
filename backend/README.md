
# Java Spring Boot Recipe Application
## Application Overview
This application provides RESTful services to create, update and delete recipe information. The application requires an external MySQL database server to store the data. This application is build and containerized using docker and can be deployed on almost all kind of platforms.


## Technology Stack
- Java SpringBoot for REST API
- Intellij IDE
- MariaDB for Database
- Elasticsearch Server
- Postman to test REST endpoints

## Build Instructions
1. Clone repository
2. Change directory to the cloned repository.<br>
   ``` cd <folder-name> ```
3. Run docker build command to build a new image.<br>
   ```  docker build -t webapp-backend:<tagname> .```   

## Deploy Instructions
1. Create a new database named 'receipe' in MariaDB.
   ```
   create database receipe
   ```
2. Add a mysql user which will be used by the application to communicate with the database 
   ```
   grant all privileges on recipe.* to 'admin' identified by 'password'
   ```
3. Run Docker Image.
   ```
   sudo docker run --network="host" -e DB_HOST='<db-host>' -e DB_USERNAME='<de-user>' -e DB_PASSWORD='<db-pass>' -e ELATICSEARCH_HOST='<elasticsearch-url>' webapp-backend "--spring.profiles.active=dev"
   ```

## Running Tests
1. Run the webapp-backend project imported in Intellij as **JUnit Test**

## API Guidelines
1. **Register New user**
   <br>This api registers a new user in the system <br>
   ```
   POST /v1/user {user object}
   ```

2. **Get Logged in User**
   <br>This api returns the user object of the logged in user <br>
   ```
   GET /v1/user/self 
   ```

3. **Update Logged in User**
   <br>This api updates the user object of the logged in user <br>
   ```
   PUT /v1/user/self  {user object}
   ```

4. **Add new recipe** <br>
   This api creates a new recipe in the system. <br>
   ```
   POST /v1/recipe {recipe Object}
   ```

5. **Retreive recipes from the system**
   <br>This api retreives list of all recipes available on the system.
   ```
   GET /v1/recipes
   ```
   This api retreived a particular recipe from the system.
   ```
   GET /v1/recipe/{RecipeId}
   ```

6. **Update Existing Recipe** <br>
   This api updates an existing recipe in the system authorized for the logged in user. <br>
   ```
   PUT /v1/recipe/{RecipeId}
   ```

7. **Delete Existing Recipe** <br>
   This api deletes an existing recipe from the system authorized for the logged in user. <br>
   ```
   DELETE /v1/recipe/{RecipeId}
   ```      