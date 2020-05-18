
# Recipe Microservice Application Deployment using EKS
## Infrastructure Overwiew
This repository contains configuration details to deploy the web application over AWS Cloud. The infrastrcuture contains a Kubernetes Cluster architected using the AWS managed EKS service.


## Technology Stack
- AWS EKS Service
- AWS RDS (MySQL) Service
- AWS VPC configs for networking
- AWS IAM 
- AWS ECR
- Terraform
- Kubernetes
- Nginx Ingress
- helm


## Prerequisites
- AWS CLI (>= 1.18)
- kubectl (>= 1.18)
- helm (>= 0.3)
- terraform (>= 0.12)


## Infrastructure Details
The infrastructure is mainly comprised of two parts. The first part corresponds to spinning up the Kubernetes cluster i.e. the underlying worker nodes (EKS) and other required services such as RDS, IAM, etc. The second part coresponds to deploying the containerized web application onto the Kubernetes cluster uing kube control.
The infrastructure configurations are segregated and placed into four directories,

1. AWS<br>
   This directory mainly comprises of all the underlying configuration (terraform) files to spin up the services on the AWS Cloud. Navigate to the aws directory to find details on how to run these configuration files.

2. Initialize-K8s-Cluster<br>
   This directory mainly comprises of configurations required to initialize the Kubernetes cluster with basic utilties. Navigate to the initialize-k8s-cluster directory to find details on how to run these configuration files.

3. Elasticsearch / Backend / Frontend <br> 
   These directories comprise of the individual microservices that needs to be deployed on the Kubernetes cluster and its dependent components. Navigate to each individual directory to find details on how to run these configuration files.