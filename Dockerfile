FROM openjdk:17-alpine AS build-env
ADD . /app
WORKDIR /app
RUN ./gradlew clean bootJar

FROM openjdk:17-alpine
COPY --from=build-env /app/build/libs/candles-0.0.1-SNAPSHOT.jar /app/candles.jar
WORKDIR /app
ENTRYPOINT java -jar /app/candles.jar
