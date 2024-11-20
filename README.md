
The project is built using the Java Spring Boot framework, ensuring scalability and efficiency. The code has been dockerized, and the Docker image is hosted on Docker Hub.
To run the application, use the following commands:  

docker pull dvaraprasadparimi/prasad:latest  
docker run -p 8080:8080 dvaraprasadparimi/prasad:latest  
##change the port accordingly
Once the container is running, the API will be accessible . The following endpoints are available:  
1. POST /receipts/process: Submit a receipt to process and get a unique ID.  
2. GET /receipts/{id}/points: Retrieve the points associated with a processed receipt using its ID.  

