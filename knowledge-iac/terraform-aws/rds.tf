resource "aws_db_subnet_group" "db_subnets" {
  name       = "${local.name}-db-subnets"
  subnet_ids = [for s in aws_subnet.private : s.id]
  tags       = local.tags
}

resource "aws_secretsmanager_secret" "db" {
  name = "${local.name}-db-credentials"
  tags = local.tags
}

resource "aws_secretsmanager_secret_version" "db" {
  secret_id = aws_secretsmanager_secret.db.id
  secret_string = jsonencode({
    username = var.db_username
    password = var.db_password
  })
}

resource "aws_db_instance" "postgres" {
  identifier              = "${local.name}-db"
  engine                  = "postgres"
  engine_version          = "16.3"
  instance_class          = var.db_instance_class
  allocated_storage       = var.db_allocated_storage
  db_name                 = "knowledge"
  username                = var.db_username
  password                = var.db_password
  port                    = var.rds_port
  multi_az                = true
  storage_encrypted       = true
  publicly_accessible     = false
  vpc_security_group_ids  = [aws_security_group.rds_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.db_subnets.name
  skip_final_snapshot     = true
  deletion_protection     = false
  backup_retention_period = 7
  tags                    = local.tags
}
