# ステージ1: ビルド用環境（Gradleを使ってJARを作る）
FROM gradle:7.6-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# テストをスキップしてビルドを実行
RUN gradle build -x test --no-daemon

# ステージ2: 実行用環境（軽量なJava実行環境）
FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
# メモリ制限を付けつつ起動
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]