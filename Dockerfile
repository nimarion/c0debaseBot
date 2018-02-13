FROM openjdk:8-jdk-slim

ADD target/c0debase-1.0-SNAPSHOT-shaded.jar c0debaseBot.jar

ENTRYPOINT ["java", "-jar", "-Xmx128m", "c0debaseBot.jar"]