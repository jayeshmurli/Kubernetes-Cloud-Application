#
# Variables Configuration
#

variable "cluster-name" {
  default = "webapp-demo"
  type    =  string
}

variable "vpc_id" {
  default = "vpc-3a40f240"
  type    =  string
}

variable "key_name" {
  default = "ubuntu-vm"
  type    =  string
}

variable "public_subnets" {
  description = "List of all public subnets available"
  type        = list
  default     = ["subnet-1088ee77", "subnet-e8e98ab4", "subnet-7f0b6751"]
}

variable "db_sg" {
  default = "sg-0ad8aeb38c9bb8cb6"
  type    =  string
}
