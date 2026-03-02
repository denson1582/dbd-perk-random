# 1. Javaを実行する環境を用意（軽量なAlpine Linuxを使用）
FROM eclipse-temurin:17-jdk-alpine

# 2. 作業ディレクトリを作成
WORKDIR /app

# 3. ビルドされたJARファイルをコンテナ内にコピー
# ※ ファイル名が違う場合は build/libs/*.jar に合わせて調整されます
COPY build/libs/*.jar app.jar

# 4. アプリを起動するコマンド
ENTRYPOINT ["java", "-Xmx384m", "-jar", "app.jar"]