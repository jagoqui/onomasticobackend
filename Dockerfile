#
# Build stage
#
FROM maven:3.6.3-openjdk-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:14-jdk-alpine
COPY --from=build /home/app/target/onomastico.jar /usr/local/lib/onomastico.jar
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=${ENV}","/usr/local/lib/onomastico.jar"]
