# AWS load balancer
resource "aws_lb" "knowledge_alb" {
  name               = "knowledge-alb"
  load_balancer_type = "application"
  subnets            = [aws_default_subnet.default_subnet_a.id,aws_default_subnet.default_subnet_b.id ]
  security_groups    = [aws_security_group.alb_sg.id]
}
