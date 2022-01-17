FROM adoptopenjdk/openjdk11:ubi
VOLUME /tmp 
ARG JAR_FILE=./build/libs/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]