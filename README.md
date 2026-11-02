# Tienda LEGO — Despliegue AWS

Aplicacion web de tienda de sets LEGO desplegada en AWS con infraestructura como codigo.

## Arquitectura

- **Backend:** Spring Boot 3.4 + PostgreSQL + JPA/Hibernate + Flyway
- **Frontend:** HTML/CSS/JS (servido por nginx)
- **Infra AWS:** VPC, ALB, Auto Scaling Group (multi-AZ), RDS PostgreSQL con Read Replica
- **CI/CD:** GitHub Actions (build, test, deploy)
- **IaC:** Terraform

## Desarrollo local

```bash
# Levantar PostgreSQL
docker compose up -d

# Ejecutar backend
cd backend
./gradlew bootRun

# La app queda en http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# Health:  http://localhost:8080/actuator/health
```

## Despliegue en AWS

```bash
cd deploy
cp terraform.tfvars.example terraform.tfvars
# Editar terraform.tfvars con tus valores

terraform init
terraform plan
terraform apply
```

## Prueba de carga

```bash
k6 run -e BASE_URL=http://<ALB_DNS> k6/load-test.js
```

## Estructura

```
backend/          → Spring Boot (Java 21)
frontend/         → HTML/CSS/JS
deploy/           → Terraform (VPC, ALB, ASG, RDS)
k6/               → Pruebas de carga
.github/workflows → CI/CD (GitHub Actions)
```
