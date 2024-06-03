FROM openjdk:21-slim

ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.edg=File:/dev/./urandom"

WORKDIR /app

COPY ./target/spring6-rest-mvc-0.0.1-SNAPSHOT.jar ./

EXPOSE 8071

ENTRYPOINT [ "java",  "-jar", "spring6-rest-mvc-0.0.1-SNAPSHOT.jar" ]

