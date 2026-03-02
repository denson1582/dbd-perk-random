# 1. ビルド環境
FROM gradle:7.6-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# 実行権限を付与してビルド
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

# 2. 実行環境
FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]