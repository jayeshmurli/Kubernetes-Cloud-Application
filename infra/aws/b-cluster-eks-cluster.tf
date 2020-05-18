#
# EKS Cluster Resources
#  * EKS Cluster
#
#  It can take a few minutes to provision on AWS

# Note: replace subnet_ids = ["${aws_subnet.demo.*.id}"]
# with:
# subnet_ids = flatten([aws_subnet.demo.0.id, aws_subnet.demo.1.id])
# or:
# subnet_ids = "${aws_subnet.demo.*.id}"

resource "aws_eks_cluster" "webapp-demo" {
  name     = "${var.cluster-name}"
  role_arn = "${aws_iam_role.webapp-cluster-role.arn}"

  vpc_config {
    security_group_ids = ["${aws_security_group.webapp-eks-cluster-sg.id}"]
    subnet_ids         = "${var.public_subnets}"
  }

  depends_on = [
    aws_iam_role_policy_attachment.webapp-cluster-AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.webapp-cluster-AmazonEKSServicePolicy,
  ]
}
