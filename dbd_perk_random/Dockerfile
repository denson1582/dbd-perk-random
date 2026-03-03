# バージョンを21に上げ、より汎用的な構成にします
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .

# エラーの詳細を出力させる設定
RUN mvn clean package -DskipTests --errors --batch-mode

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]