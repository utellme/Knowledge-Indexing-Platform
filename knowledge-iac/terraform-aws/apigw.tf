# API Gateway HTTP API
resource "aws_apigatewayv2_api" "http_api" {
  name          = "${local.name}-api"
  protocol_type = "HTTP"
  tags          = local.tags
}

# VPC Link from API GW to internal ALB
resource "aws_apigatewayv2_vpc_link" "vpclink" {
  name               = "${local.name}-vpclink"
  security_group_ids = [aws_security_group.alb_sg.id]
  subnet_ids         = [for s in aws_subnet.private : s.id]
  tags               = local.tags
}

# Integration to ALB listener (HTTP)
resource "aws_apigatewayv2_integration" "alb_integration" {
  api_id                 = aws_apigatewayv2_api.http_api.id
  integration_type       = "HTTP_PROXY"
  integration_method     = "ANY"
  integration_uri        = aws_lb_listener.http.arn
  connection_type        = "VPC_LINK"
  connection_id          = aws_apigatewayv2_vpc_link.vpclink.id
  payload_format_version = "1.0"
}

resource "aws_apigatewayv2_route" "proxy" {
  api_id    = aws_apigatewayv2_api.http_api.id
  route_key = "ANY /{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.alb_integration.id}"
}

resource "aws_apigatewayv2_stage" "stage" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default"
  auto_deploy = true
  tags        = local.tags
}
