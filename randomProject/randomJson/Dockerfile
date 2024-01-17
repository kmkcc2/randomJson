FROM openjdk:17-oracle
WORKDIR /randomJson

COPY ./.mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

COPY src ./src
CMD ["./mvnw", "spring-boot:run"]
EXPOSE 8080