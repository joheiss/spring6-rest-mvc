# app layer - frequent changes
FROM openjdk:21-slim as builder

WORKDIR /app
ADD ./maven/${project.build.finalName}.jar ./

RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

# spring boot layer - infrequent changes
FROM openjdk:21-slim

WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./

# ENV JAVA_OPTS " -Xms512m -Xmx512m -Djava.security.edg=File:/dev/./urandom"

EXPOSE 8071

ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.launch.JarLauncher" ]
