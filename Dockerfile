FROM openjdk:11-jre-slim
LABEL maintainer="avijitmondal38@gmail.com"

ADD target/grpc-server-*-SNAPSHOT.jar /usr/local/lib/app.jar

EXPOSE 50051
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
