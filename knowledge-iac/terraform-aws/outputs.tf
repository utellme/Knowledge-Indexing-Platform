output "api_invoke_url" {
  value = aws_apigatewayv2_api.http_api.api_endpoint
}

output "ecr_repository_url" {
  value = aws_ecr_repository.repo.repository_url
}

output "rds_endpoint" {
  value = aws_db_instance.postgres.address
}
