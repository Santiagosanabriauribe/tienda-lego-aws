#!/bin/bash
set -ex

# 1. Instalar Java 21, nginx y git
dnf update -y
dnf install -y java-21-amazon-corretto-devel nginx git

# Configurar JAVA_HOME para Gradle (usar javac para asegurar que apunta al JDK)
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
echo "export JAVA_HOME=$JAVA_HOME" >> /etc/environment
echo "JAVA_HOME detectado: $JAVA_HOME"
javac -version

# 2. Crear directorios
mkdir -p /var/www/tienda-lego/frontend
mkdir -p /opt/tienda-lego

# 3. Configurar nginx como proxy reverso
cat > /etc/nginx/conf.d/tienda-lego.conf << 'NGINX'
server {
    listen 8080;
    server_name _;

    root /var/www/tienda-lego/frontend;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /actuator/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
    }

    location /swagger-ui.html {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
    }

    location /swagger-ui/ {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
    }

    location /api-docs {
        proxy_pass http://127.0.0.1:8081;
        proxy_set_header Host $host;
    }
}
NGINX

rm -f /etc/nginx/conf.d/default.conf
systemctl enable nginx
systemctl start nginx

# 4. Crear servicio systemd
cat > /etc/systemd/system/tienda-lego.service << EOF
[Unit]
Description=Tienda LEGO Backend
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/tienda-lego
ExecStart=/usr/bin/java -jar /opt/tienda-lego/app.jar --spring.profiles.active=prod --server.port=8081
Environment=DATABASE_URL=${db_url}
Environment=DATABASE_USER=${db_username}
Environment=DATABASE_PASSWORD=${db_password}
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl enable tienda-lego

# 5. Clonar, compilar y desplegar
cd /tmp
git clone ${repo_url} repo
cd repo

# Backend
cd backend
chmod +x gradlew
./gradlew bootJar --no-daemon -Dorg.gradle.java.home=$JAVA_HOME
cp build/libs/*.jar /opt/tienda-lego/app.jar
chown ec2-user:ec2-user /opt/tienda-lego/app.jar
cd ..

# Frontend
cp -r frontend/* /var/www/tienda-lego/frontend/
chmod -R 755 /var/www/tienda-lego/

# Limpiar
rm -rf /tmp/repo

# 6. Iniciar
systemctl start tienda-lego
