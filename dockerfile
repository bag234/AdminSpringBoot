FROM openjdk:21-jdk

# Указываем рабочую директорию
WORKDIR /app

COPY target/*.jar app.jar
COPY users.txt users.txt

EXPOSE 8084

# Запускаем Spring Boot приложение
ENTRYPOINT ["java", "-jar", "app.jar"]