
# ECS Knowledge Service
resource "aws_ecs_service" "knowledge_service" {
  name            = "knowledge-service"
  cluster         = aws_ecs_cluster.knowledge_ecs_cluster.id
  task_definition = aws_ecs_task_definition.knowledge_ecs_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_default_subnet.default_subnet_a.id, aws_default_subnet.default_subnet_b.id]
    security_groups = [aws_security_group.ecs_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.knowledge_tg.arn
    container_name   = "knowledge-ecs-task"
    container_port   = 8080
  }

  depends_on = [aws_lb_listener.http]
}
