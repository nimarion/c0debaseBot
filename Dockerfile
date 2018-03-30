FROM openjdk:10-jdk-slim

LABEL maintainer = "biosphere.dev@gmx.de"

COPY target/c0debase-1.0-SNAPSHOT-shaded.jar c0debaseBot.jar

ENTRYPOINT ["java", "-jar", "-Xmx128m", "c0debaseBot.jar"]