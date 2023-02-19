# Training Plan REST API
A RESTful API built for fun and learning purposes using the Springboot framework and Java 17, designed to provide a convenient and efficient way to create training plans and access their data. This API serves as a bridge between the client and the server, allowing client applications to easily retrieve, create, update, and delete resources without having to interact directly with the server's database.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Java 17+
- Springboot 2.7.5+
- Maven 3.6.3+
- PostgreSQL 14.6+
- Git 2.34.1+

### Installing
Clone the repository to your local machine using:
```
$ git clone https://github.com/eduriol/training-plan-app.git
```
Navigate to the project directory and build the project using Maven:
```
$ mvn install
```

### Database configuration
This application uses a PostgreSQL database to persist training plan data. To configure the database connection, modify the _application.properties_ file located in the _src/main/resources_ directory. By default, the application expects a PostgreSQL server running on localhost with a database named training.

You can customize the connection properties by modifying the following properties in the _application.properties_ file:

- spring.datasource.url: the URL of the database server
- spring.datasource.username: the username used to connect to the database
- spring.datasource.password: the password used to connect to the database

Here's an example configuration for connecting to a PostgreSQL database running on a remote server:
```
spring.datasource.url=jdbc:postgresql://<database_host>:<database_port>/<database_name>
spring.datasource.username=<database_username>
spring.datasource.password=<database_password>
```

### Running the application
Local run:
```
$ mvn spring-boot:run
```
Using Docker:
```
$ sudo docker build --tag="training:latest" .
$ sudo docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://<database_host>:<database_port>/<database_name> -it training:latest
```
As can be seen in _application.properties_, by default the application expects a PostgreSQL server in localhost. That's why it is mandatory to overwrite the datasource property, since the Docker container does not include a PostgreSQL server.

You can also overwrite other connection properties like user (SPRING_DATASOURCE_USERNAME) and password (SPRING_DATASOURCE_PASSWORD) when running the Docker image.

Make sure your PostgreSQL server is up and running and accepts connections from your Docker container.
### Testing
- Running integration tests:
```
$ mvn test
```
- Manual testing:
To perform a basic manual GET petition, you can execute the following cURL command:
```
$ curl --location --request GET '{{host}}/api/health'
```
Or import into Postman the collection contained in the __Training_Plan_App.postman_collection.json__ file.

## Endpoints
The API has the following endpoints:
- Health:

| Method | Endpoint    | Description                                   |
| ------ |-------------|-----------------------------------------------|
| GET | /api/health | Retrieve the health status of the application |

- Plans:

| Method | Endpoint   | Description                                         |
| ------ |------------|-----------------------------------------------------|
| GET | /api/plans/{plan_id} | Retrieve a specific training plan by ID |
| POST | /api/plans | Create a new training plan                          |
| DELETE | /api/plans/{plan_id}   | Delete a training plan                              |

- Topics:

| Method | Endpoint                           | Description                                          |
| ------ |------------------------------------|------------------------------------------------------|
| GET | /api/plans/{plan_id}/topics/{id}   | Retrieve a specific learning topic by ID from a plan |
| GET | /api/plans/{plan_id}/topics        | Retrieve a list of learning topics from a plan       |
| POST | /api/plans/{plan_id}/topics        | Create a new learning topic in a plan                |
| PUT | /api/plans/{plan_id}/topics/{id}   | Update an existing learning topic from a plan        |
| DELETE | /api/plans/{plan_id}/topics/{id}   | Delete a learning topic                              |

## Built With
- [Springboot](https://spring.io/projects/spring-boot) - The web framework used
- [Java 17](https://openjdk.java.net/projects/jdk17/) - The programming language used
- [Maven](https://maven.apache.org/) - Dependency Management

## Contributing
If you'd like to contribute to the project, please send a [Pull Request](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests) or contact eduriol [at] gmail.com.

## Versioning
TBD

## License
This project is licensed under the [MIT License](https://en.wikipedia.org/wiki/MIT_License).
