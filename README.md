Application Java Spring Boot conteneurisée avec Docker, déployée dans Kubernetes via Minikube, avec un pipeline CI/CD GitHub Actions automatisé.
Ce projet couvre 3 TPs :

TP 1 : CI/CD avec GitHub Actions + Docker
TP 2 : Déploiement Kubernetes avec Minikube
TP 3 : Migration vers Spring Boot avec Actuator et Prometheus
 

Technologies utilisées

Java 17 + Spring Boot 3.2.5
Maven
Docker (multi-stage build)
GitHub Actions (CI/CD)
GitHub Container Registry (ghcr.io)
Kubernetes / Minikube / kubectl
Spring Actuator + Micrometer + Prometheus


Structure du projet :  MVC
boutique/
├── .github/workflows/
│   └── ci.yml                        # Pipeline CI/CD
├── src/
│   ├── main/java/fr/boutique/
│   │   ├── BoutiqueApplication.java  # Main Spring Boot
│   │   ├── model/
│   │   │   ├── Article.java
│   │   │   ├── LignePanier.java
│   │   │   └── Panier.java
│   │   ├── service/
│   │   │   └── CatalogueService.java
│   │   └── controller/
│   │       └── CatalogueController.java
│   └── main/resources/
│       └── application.properties
├── k8s/
│   ├── deployment.yml
│   ├── service.yml
│   ├── configmap.yml
│   └── secret.yml                   
└── Dockerfile

TP 1 — CI/CD GitHub Actions + Docker
Le pipeline se déclenche automatiquement à chaque push sur main.
Il fait deux choses :

test : lance mvn test pour vérifier que le code compile et que les tests passent
build-push : build l'image Docker et la pousse sur ghcr.io

L'image est publiée sur : ghcr.io/assisko/devops-tp1-boutique:latest
Pour builder l'image manuellement (multi-plateforme pour Mac M1/M2) :
bashdocker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t ghcr.io/assisko/devops-tp1-boutique:latest \
  --push .

TP 2 — Kubernetes avec Minikube
Prérequis

Docker Desktop
Minikube
kubectl

Démarrer le cluster
bashminikube start --driver=docker
kubectl get nodes
Déployer toute l'application en une commande
bashkubectl apply -f k8s/
kubectl get all
minikube service boutique-svc --url
Self-healing
Kubernetes recrée automatiquement les pods qui plantent :
bash# Supprimer un pod et observer la recréation automatique
kubectl delete pod <nom-du-pod>
kubectl get pods -w
Scaling
bash# Monter à 5 réplicas
kubectl scale deployment boutique --replicas=5

# Redescendre à 2
kubectl scale deployment boutique --replicas=2

# Bonne pratique : modifier replicas dans deployment.yml et réappliquer
kubectl apply -f k8s/deployment.yml
Rolling update sans coupure de service
bash# Déployer une nouvelle version
kubectl set image deployment/boutique boutique=ghcr.io/assisko/devops-tp1-boutique:v2.0.0
kubectl rollout status deployment/boutique
Rollback
bash# Voir l'historique des déploiements
kubectl rollout history deployment/boutique

# Revenir à la version précédente
kubectl rollout undo deployment/boutique

TP 3 — Migration Spring Boot
L'application de base était un projet JUnit5 sans serveur HTTP — impossible à déployer dans Kubernetes. On l'a migrée vers Spring Boot pour avoir :

Un serveur HTTP sur le port 8080
Des endpoints REST
Des health checks pour Kubernetes
Des métriques pour Prometheus

Endpoints disponibles
EndpointDescriptionGET /api/articlesListe des 8 articles du catalogueGET /api/articles/{ref}Détail d'un article (ex: REF-001)GET /api/healthStatut simplifiéGET /actuator/healthHealth check complet (utilisé par K8s)GET /actuator/prometheusMétriques JVM et HTTP
Health probes Kubernetes
Le deployment.yml configure deux probes sur /actuator/health :

livenessProbe : Kubernetes redémarre le pod si l'app ne répond plus
readinessProbe : Kubernetes n'envoie du trafic que si l'app est prête

Lancer en local
bashmvn package -DskipTests
java -jar target/boutique.jar
curl http://localhost:8080/api/articles
curl http://localhost:8080/actuator/health

Notes

Ne jamais commiter k8s/secret.yml avec de vraies valeurs → il est dans .gitignore
Les anciens tests JUnit5 sont dans src/test/java/legacy/
L'image doit être buildée en linux/amd64,linux/arm64 pour fonctionner à la fois sur Mac M1/M2 et dans Minikube
