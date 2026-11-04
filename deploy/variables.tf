variable "aws_region" {
  description = "Region de AWS"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Nombre del proyecto (usado como prefijo en los recursos)"
  type        = string
  default     = "tienda-lego"
}

variable "vpc_cidr" {
  description = "CIDR block de la VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "instance_type" {
  description = "Tipo de instancia EC2"
  type        = string
  default     = "t2.micro"
}

variable "key_name" {
  description = "Nombre del Key Pair de EC2 para SSH"
  type        = string
  default     = "tienda-lego-key"
}

variable "db_username" {
  description = "Usuario de la base de datos RDS"
  type        = string
  default     = "postgres"
}

variable "db_password" {
  description = "Contrasena de la base de datos RDS"
  type        = string
  default     = "TiendaLego2026!"
  sensitive   = true
}

variable "db_instance_class" {
  description = "Clase de instancia RDS"
  type        = string
  default     = "db.t3.micro"
}

variable "min_instances" {
  description = "Numero minimo de instancias en el Auto Scaling Group"
  type        = number
  default     = 2
}

variable "max_instances" {
  description = "Numero maximo de instancias en el Auto Scaling Group"
  type        = number
  default     = 4
}
