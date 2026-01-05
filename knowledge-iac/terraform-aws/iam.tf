data "aws_iam_policy_document" "ecs_task_assume" {
  statement {
    effect = "Allow"
    actions = ["sts:AssumeRole"]
    principals { 
        type = "Service" 
        identifiers = ["ecs-tasks.amazonaws.com"] 
    }
  }
}

resource "aws_iam_role" "ecs_task_execution" {
  name               = "${local.name}-ecs-exec"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume.json
  tags               = local.tags
}

resource "aws_iam_role_policy_attachment" "ecs_exec_attach" {
  role       = aws_iam_role.ecs_task_execution.name
  policy_arn  = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# ecs task tp emab;e AWS System Management Service core iam policy
resource "aws_iam_role" "ecs_task" {
  name               = "${local.name}-ecs-task"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume.json
  tags               = local.tags
}

resource "aws_iam_role_policy_attachment" "ecs_task_attach" {
  role       = aws_iam_role.ecs_task.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

# Allow ECS task to read DB secret
data "aws_iam_policy_document" "task_secrets" {
  statement {
    actions   = ["secretsmanager:GetSecretValue"]
    resources = [aws_secretsmanager_secret.db.arn]
  }
}

# task_secrets iam policy
resource "aws_iam_policy" "task_secrets" {
  name   = "${local.name}-task-secrets"
  policy = data.aws_iam_policy_document.task_secrets.json
}

resource "aws_iam_role_policy_attachment" "task_secrets_attach" {
  role      = aws_iam_role.ecs_task.name
  policy_arn = aws_iam_policy.task_secrets.arn
}
