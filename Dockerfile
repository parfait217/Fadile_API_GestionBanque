# Étape 1 : Build de l'application avec Maven
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw

# Télécharger les dépendances (cette étape est mise en cache)
RUN ./mvnw dependency:go-offline

# Copier le code source
COPY src ./src

# Build de l'application
RUN ./mvnw clean package -DskipTests

# Étape 2 : Image d'exécution légère
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Installer curl pour les checks de santé (optionnel)
RUN apk add --no-cache curl

# Copier le JAR buildé depuis l'étape précédente
COPY --from=builder /app/target/demo-0.0.1-SNAPSHOT.jar app.jar

# Port d'écoute
EXPOSE 3000

# Variables d'environnement par défaut (seront surchargées sur Render)
ENV MYSQL_URL=jdbc:mysql://localhost:3306/defaultdb?ssl-mode=REQUIRED
ENV MYSQL_USER=avnadmin
ENV MYSQL_PASSWORD=

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:3000/actuator/health || exit 1

# Lancement de l'application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
