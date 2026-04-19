# API Système Bancaire - Deployment Guide

## Architecture
- Spring Boot 3.2.4 (Java 17)
- MySQL 8.0 (Aiven Cloud)
- Docker containerization
- Deployé sur Render

## Configuration

### Variables d'environnement (Render)

Configurez ces variables dans le dashboard Render :

| Variable | Description |
|----------|-------------|
| `MYSQL_URL` | `mysql://avnadmin:VOTRE_MOT_DE_PASSE@mysql-6efc9e1-loic6350-5059.d.aivencloud.com:27121/defaultdb?ssl-mode=REQUIRED` |
| `MYSQL_USER` | `avnadmin` |
| `MYSQL_PASSWORD` | Mot de passe Aiven (cliquez sur CLICK_TO:REVEAL_PASSWORD) |

### Base de données Aiven
- **Host**: mysql-6efc9e1-loic6350-5059.d.aivencloud.com
- **Port**: 27121
- **Database**: defaultdb
- **User**: avnadmin
- **SSL**: REQUIRED

## Déploiement sur Render

### Option 1 : Via render.yaml (recommandé)
1. Connectez votre dépôt GitHub à Render
2. Render détectera automatiquement le fichier `render.yaml`
3. Le service sera créé avec la configuration Docker
4. Ajoutez les variables d'environnement (MYSQL_PASSWORD)

### Option 2 : Déploiement manuel
1. Dans Render Dashboard, créez un nouveau "Web Service"
2. Connectez votre dépôt GitHub
3. Sélectionnez "Docker" comme environnement
4. Définissez les variables d'environnement
5. Déployez

## Développement local avec Docker

```bash
# Build et démarrage avec docker-compose
docker-compose up --build

# L'API sera disponible sur http://localhost:3000
# Swagger UI : http://localhost:3000/swagger-ui.html
# Actuator health : http://localhost:3000/actuator/health
```

## Endpoints API

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | /api/accounts | Liste tous les comptes |
| GET | /api/accounts/{id} | Détails d'un compte |
| POST | /api/accounts | Créer un compte |
| PUT | /api/accounts/{id}/deposit | Effectuer un dépôt |
| PUT | /api/accounts/{id}/withdraw | Effectuer un retrait |

## Structure du projet

```
src/main/java/com/example/demo/
├── DemoApplication.java          # Point d'entrée Spring Boot
├── Entity/
│   └── Account.java              # Entité JPA
├── controller/
│   └── AccountController.java    # REST API
├── service/
│   ├── IAccountService.java      # Interface
│   └── AccountService.java       # Implémentation
├── repository/
│   └── AccountRepo.java          # Repository JPA
├── dto/
│   └── CreateAccountRequest.java # DTO
├── config/
│   └── OpenApiConfig.java        # Configuration Swagger
└── exception/
    ├── GlobalExceptionHandler.java
    ├── UnauthorizeException.java
    ├── ForbiddenException.java
    └── NotfoundException.java
```

## Build manuel

```bash
# Build avec Maven
./mvnw clean package

# Exécution
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Notes
- Le port par défaut est 3000
- La base de données H2 est désactivée en production (MySQL only)
- Les migrations de schéma sont gérées automatiquement par Hibernate (`ddl-auto=update`)
- Pour la production, envisagez d'utiliser Flyway ou Liquibase pour les migrations
