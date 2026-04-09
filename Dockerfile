# Estágio 1: Compilação e Segurança (NOMEADO COMO extractor)
FROM eclipse-temurin:24-jdk-alpine AS extractor
WORKDIR /app

RUN apk add --no-cache openssl mysql-client mariadb-connector-c

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN tr -cd '\11\12\15\40-\176' < src/main/resources/application.properties > src/main/resources/application.properties.tmp && \
    mv src/main/resources/application.properties.tmp src/main/resources/application.properties

RUN openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 365 -nodes \
    -subj "/CN=minha-api" && \
    openssl pkcs12 -export -out src/main/resources/keystore.p12 -inkey key.pem -in cert.pem \
    -name minha-api -passout pass:123456

RUN mkdir -p src/main/resources/certs && \
    openssl genrsa -out src/main/resources/certs/private.pem 2048 && \
    openssl rsa -in src/main/resources/certs/private.pem -pubout -out src/main/resources/certs/public.pem

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
RUN apk add --no-cache mariadb-client mariadb-connector-c

ENV MYSQL_UNIX_PORT=/dev/null

COPY --from=extractor /app/target/*.jar app.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]