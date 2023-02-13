# Training Plan REST API
A RESTful API built for learning purposes using the Springboot framework and Java 17, designed to provide a convenient and efficient way to create training plans and access their data. This API serves as a bridge between the client and the server, allowing client applications to easily retrieve, create, update, and delete resources without having to interact directly with the server's database.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Java 17+
- Springboot 2.7.5+
- Maven 3.6.3+
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
Run the application:
```
$ mvn spring-boot:run
```
### Testing
- Running integration tests:
```
$ mvn test
```
- Manual testing:
To perform a basic manual GET petition, you can execute the following cURL command:
```
curl --location --request GET '{{host}}/api/health'
```
Or import into Postman the collection contained in the __Training_Plan_App.postman_collection.json__ file.

## Endpoints
The API has the following endpoints:

| Method | Endpoint    | Description                                   |
| ------ |-------------|-----------------------------------------------|
| GET | /api/health | Retrieve the health status of the application |
| GET | /api/topics/{id} | Retrieve a specific learning topic by ID      |
| GET | /api/topics | Retrieve a list of learning topics            |
| POST | /api/topics | Create a new learning topic                   |
| PUT | /api/topics/{id} | Update an existing learning topic             |
| DELETE | /api/topics/{id} | Delete a learning topic                       |

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
