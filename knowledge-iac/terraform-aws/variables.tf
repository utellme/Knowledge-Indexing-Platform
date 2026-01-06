
# variable "container_image" { description = "ECR image URI" }
variable "cluster_name" { default = "knowledge-ecs-cluster" }

variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "project" {
  type    = string
  default = "knowledge"
}

variable "env" {
  type    = string
  default = "devprod"
}

variable "vpc_cidr" {
  type    = string
  default = "10.20.0.0/16"
}

variable "public_subnet_cidrs" {
  type    = list(string)
  default = ["10.20.0.0/24", "10.20.1.0/24"]
}

variable "private_subnet_cidrs" {
  type    = list(string)
  default = ["10.20.10.0/24", "10.20.11.0/24"]
}

variable "container_port" {
  type    = number
  default = 8080
}

variable "rds_port" {
  type    = number
  default = 5432
}

variable "desired_count" {
  type    = number
  default = 2
}

variable "cpu" {
  type    = number
  default = 512
}

variable "memory" {
  type    = number
  default = 1024
}

variable "image_tag" {
  type    = string
  default = "1.0.0"
}

variable "db_username" {
  type      = string
  default   = "knowledge"
  sensitive = true
}

variable "db_password" {
  type      = string
  sensitive = true
}

variable "db_instance_class" {
  type    = string
  default = "db.t4g.micro"
}

variable "db_allocated_storage" {
  type    = number
  default = 20
}
variable "cloud_watch_retention_days" {
  type    = number
  default = 2
}
variable "enable_blue_green" {
  type    = bool
  default = true
}
