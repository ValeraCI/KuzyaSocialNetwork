FROM openjdk
WORKDIR /app
COPY ./target/*.jar KuzyaSocialNetwork.jar
CMD ["java", "-jar", "KuzyaSocialNetwork.jar"]