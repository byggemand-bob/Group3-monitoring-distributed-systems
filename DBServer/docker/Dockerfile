# use base image with OpenJDK JRE
FROM openjdk:11.0.11-oraclelinux7

COPY DBServer-0.0.1-SNAPSHOT.jar /app.jar
COPY configuration.properties /src/main/resources/configuration.properties

RUN mkdir /src/main/resources/sqlite
RUN mkdir /src/main/resources/sqlite/db
#RUN touch /src/main/resources/sqlite/queue.db

CMD [ "/usr/bin/java", "-jar", "/app.jar" ]