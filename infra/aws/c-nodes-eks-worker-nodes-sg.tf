#
# EKS Worker Nodes Resources
#  * EC2 Security Group to allow networking traffic
#

resource "aws_security_group" "webapp-node-sg" {
  name        = "webapp-node-sg"
  description = "Security group for all nodes in the cluster"
  vpc_id      = "${var.vpc_id}"

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = map(
     "Name", "webapp-eks-node",
     "kubernetes.io/cluster/${var.cluster-name}", "owned",
    )
}

resource "aws_security_group_rule" "webapp-node-ingress-self" {
  description              = "Allow node to communicate with each other"
  from_port                = 0
  protocol                 = "-1"
  security_group_id        = "${aws_security_group.webapp-node-sg.id}"
  source_security_group_id = "${aws_security_group.webapp-node-sg.id}"
  to_port                  = 65535
  type                     = "ingress"
}

resource "aws_security_group_rule" "webapp-node-ingress-cluster" {
  description              = "Allow worker Kubelets and pods to receive communication from the cluster control plane"
  from_port                = 1025
  protocol                 = "tcp"
  security_group_id        = "${aws_security_group.webapp-node-sg.id}"
  source_security_group_id = "${aws_security_group.webapp-eks-cluster-sg.id}"
  to_port                  = 65535
  type                     = "ingress"
}