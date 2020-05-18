#
# EKS Cluster Resources
#  * EC2 Security Group to allow networking traffic with EKS cluster
#

resource "aws_security_group" "webapp-eks-cluster-sg" {
  name        = "webapp-eks-cluster-sg"
  description = "Cluster communication with worker nodes"
  vpc_id      = "${var.vpc_id}"

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "webapp-demo"
  }
}

resource "aws_security_group_rule" "webapp-cluster-ingress-node-https" {
  description              = "Allow pods to communicate with the cluster API Server"
  from_port                = 443
  protocol                 = "tcp"
  security_group_id        = "${aws_security_group.webapp-eks-cluster-sg.id}"
  source_security_group_id = "${aws_security_group.webapp-node-sg.id}"
  to_port                  = 443
  type                     = "ingress"
}

resource "aws_security_group_rule" "webapp-cluster-ingress-workstation-https" {
  cidr_blocks       = ["0.0.0.0/0"]
  description       = "Allow workstation to communicate with the cluster API Server"
  from_port         = 443
  protocol          = "tcp"
  security_group_id = "${aws_security_group.webapp-eks-cluster-sg.id}"
  to_port           = 443
  type              = "ingress"
}