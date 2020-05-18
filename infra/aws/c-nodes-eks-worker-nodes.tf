resource "aws_eks_node_group" "example" {
  cluster_name    = "${aws_eks_cluster.webapp-demo.name}"
  node_group_name = "webapp-node-group"
  node_role_arn   = "${aws_iam_role.webapp-node-role.arn}"
  subnet_ids      = "${var.public_subnets}"

  scaling_config {
    desired_size = 2
    max_size     = 4
    min_size     = 2
  }

  remote_access {
    ec2_ssh_key = "${var.key_name}"
    source_security_group_ids = ["${aws_security_group.webapp-node-sg.id}"]
  }

  # Ensure that IAM Role permissions are created before and deleted after EKS Node Group handling.
  # Otherwise, EKS will not be able to properly delete EC2 Instances and Elastic Network Interfaces.
  depends_on = [
    aws_iam_role_policy_attachment.webapp-node-AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.webapp-node-AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.webapp-node-AmazonEC2ContainerRegistryReadOnly,
  ]
}