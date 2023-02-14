FROM maven:3.8.3-openjdk-17

WORKDIR /usr/src/myapp
COPY . .

RUN mvn install

CMD ["java", "-jar", "target/training-plan-app-0.0.1-SNAPSHOT.jar"]
