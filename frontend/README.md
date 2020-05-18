
# React Web Application to list Recipes
## Application Overview
This application provides a web interface to list down all available recipes in the system. The application interacts with a bakcend system using HTTP/REST requests to retrieve information and display on the web application. This application is build and containerized using docker and can be deployed on almost all kind of platforms.


## Technology Stack
- React
- HTML/CSS
- Intellij IDE

## Build Instructions
1. Clone repository
2. Change directory to the cloned repository.<br>
   ``` cd <folder-name> ```
3. Run docker build command to build a new image.<br>
   ```  docker build -t webapp-frontend:<tagname> .```   

## Deploy Instructions
1. Run Docker Image.
   ```
   sudo docker run --network="host" -e API_URL='<backend-server-url>' webapp-frontend
   ```