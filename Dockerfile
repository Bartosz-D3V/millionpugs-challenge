FROM gradle:7.3.3-jdk11-alpine AS GRADLE_BUILD
COPY build.gradle.kts /tmp/
COPY settings.gradle.kts /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN gradle build -x test ktlintCheck

FROM openjdk:11
EXPOSE 8080
COPY --from=GRADLE_BUILD /tmp/build/libs/api-0.0.1-SNAPSHOT.jar /data/api-0.0.1-SNAPSHOT.jar
CMD java -jar /data/api-0.0.1-SNAPSHOT.jar
