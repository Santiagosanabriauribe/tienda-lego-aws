output "vpc_id" {
  description = "ID de la VPC"
  value       = aws_vpc.main.id
}

output "alb_dns" {
  description = "DNS del Application Load Balancer"
  value       = aws_lb.app.dns_name
}

output "alb_url" {
  description = "URL de la aplicacion"
  value       = "http://${aws_lb.app.dns_name}"
}

output "swagger_url" {
  description = "URL de Swagger UI"
  value       = "http://${aws_lb.app.dns_name}/swagger-ui.html"
}

output "health_url" {
  description = "URL del endpoint de salud"
  value       = "http://${aws_lb.app.dns_name}/actuator/health"
}

output "db_primary_endpoint" {
  description = "Endpoint de la base de datos primaria"
  value       = aws_db_instance.primary.endpoint
}

output "db_replica_endpoint" {
  description = "Endpoint de la replica de lectura"
  value       = aws_db_instance.replica.endpoint
}

output "asg_name" {
  description = "Nombre del Auto Scaling Group"
  value       = aws_autoscaling_group.app.name
}
