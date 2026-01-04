resource "aws_lb_target_group" "knowledge_tg" {
    name = "knowledge-tg"
    port = 3000
    protocol = "HTTP"
    vpc_id = aws_default_vpc.default_vpc.id
    target_type = "ip"


    health_check {
        path = "/actuator/health"
        matcher = "200"
        interval = 30
        timeout = 5
        healthy_threshold = 2
        unhealthy_threshold = 3
    }
}


resource "aws_lb_listener" "http" {
    load_balancer_arn = aws_lb.knowledge_alb.arn
    port = 80
    protocol = "HTTP"


    default_action {
        type = "forward"
        target_group_arn = aws_lb_target_group.knowledge_tg.arn
    }
}