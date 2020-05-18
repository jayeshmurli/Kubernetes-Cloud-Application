
# AWS Cloud Infrastructure Setup
## Infrastructure Overwiew
This repository contains configuration details and templates required to spin up the resources on the AWS Cloud. The automation is implemented using terraform which maintains the status of your stack and does not spin up duplicate resoource if the same resources already exist on the cloud.


## Resources included
- AWS RDS Service
- AWS EKS Cluster
- AWS Security Groups
- AWS IAM Roles and Policies


## Steps to deploy infrastructure
1. Navigate to the aws directory
   ```
   cd infra/aws/
   ```
   <br>
2. Initialize the terraform templates and verify for any syntactical errors
   ```
   terraform init
   terraform plan
   ```
   <br>
3. Once the templates have been verified, apply the changes
   ```
   terraform apply --auto-approve
   ```
   <br>


## Template Details

1. b-cluster-eks-cluster-iam.tf <br>
   This template contains the configuration parameters for the IAM policies and roles required to allow the Kubernetes control plane to manage AWS resources on your behalf

2. b-cluster-eks-cluster-sg.tf <br>
   This template contains the configuration parameters for the security groups to apply to the EKS-managed Elastic Network Interfaces that are created in your worker node subnets

3. b-eks-cluster-eks.tf <br> 
   This template contains the configuration parameters for the EKS cluster to be created

4. c-nodes-eks-worker-nodes-iam.tf <br>
   This template contains the configuration parameters for the IAM polciies and roles to be applied to the worker nodes that will be created under the EKS cluster

5. c-nodes-worker-nodes-sg.tf <br>
   This template contains the configuration parameters for the security groups that need to be applied to the worker nodes that will be created under the EKS cluster

6. c-nodes-worker-nodes.tf <br>
   This template contains the configuration parameters for the auto-scaling group that forms the worker nodes of the EKS Cluster

7. db-setup.tf <br>
   This template contains the configuration parameters for the RDS services that needs to be spin up and its corresponding security groups configuration to allow incoming request on port 3306.

8. variables.tf <br>
   This template contains variables that can be used in other templates for dynamic input.