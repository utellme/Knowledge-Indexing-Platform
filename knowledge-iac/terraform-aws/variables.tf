variable "aws_region" { default = "us-east-1" }
variable "environment" { default = "devprod" }
# variable "container_image" { description = "ECR image URI" }
variable "db_user" { default = "appuser" }
# variable "db_password" { sensitive = true }
variable "vpc_cidr" { default = "10.0.0.0/16" }
variable "cluster_name" { default = "knowledge-ecs-cluster" }