FROM maven:3.6.0-jdk-8-alpine as builder
WORKDIR './app'

# add pom.xml only here, and download dependency
ADD pom.xml .

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]

# now we can add all source code and start compiling
ADD . .

RUN ["mvn", "package"]


FROM openjdk:8-jre
COPY --from=builder ./app/target/robotrade-api-0.0.1-SNAPSHOT.jar ./
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","robotrade-api-0.0.1-SNAPSHOT.jar"]
