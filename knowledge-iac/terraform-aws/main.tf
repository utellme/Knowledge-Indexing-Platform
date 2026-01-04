
resource "aws_ecs_cluster" "knowledge_ecs_cluster"{
  name = "knowledge-ecs-cluster"
}

resource "aws_ecs_task_definition" "knowledge_ecs_task" {
  family = "knowledge-ecs-task"
  container_definitions = <<DEFINITION
  [
    {
      "name" : "knowledge-ecs-task",
      "image"  : "335242194235.dkr.ecr.us-east-1.amazonaws.com/knowledge-indexing",
      "cpu" : 256,
      "memory" : 512,
      "essential" : true,
      "portMappings": [
        {
          "containerPort" : 8080,
          "hostPort"    : 8080
        }
      ]
    }
  ]
  DEFINITION
  requires_compatibilities = ["FARGATE"]
  network_mode = "awsvpc"
  memory = 512
  cpu = 256
  execution_role_arn = aws_iam_role.ecsTaskExecutionRole.arn
  task_role_arn       = aws_iam_role.ecsTaskRole.arn
}

resource "aws_iam_role" "ecsTaskRole" {
  name = "knowledge-ecs-task-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecsTaskRole_policy" {
  role       = aws_iam_role.ecsTaskRole.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_role" "ecsTaskExecutionRole"{ 
  name = "ecsTaskExecutionRole" 
  assume_role_policy = jsonencode({ 
    Statement = [{ 
      Action = "sts:AssumeRole" 
      Effect = "Allow" 
      Sid = "" 
      Principal = { 
        Service = "ecs-tasks.amazonaws.com" 
        } 
      },] 
    }
  ) 
} 

resource "aws_iam_role_policy_attachment" "ecs_execution_policy" { 
  role = aws_iam_role.ecsTaskExecutionRole.name 
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy" 
}

resource "aws_default_vpc" "default_vpc" {
}

resource "aws_default_subnet" "default_subnet_a" {
  availability_zone = "us-east-1a"
}

resource "aws_default_subnet" "default_subnet_b" {
  availability_zone = "us-east-1b"
}
