
# Initializing Kubernetes Cluster
## Overview
This repository contains information on configuring the kubectl cli tool with your EKS Cluster details. It also contains the kubernetes configuration files to initialize the cluster with common utilities such as the metrics server, kubernetes dashboard, etc. 


## Tools Used
- AWS CLI
- Kubectl
- helm


## Deploy Instructions

#### 1. Configuring Kubectl
   Once the EKS Cluster has spun up over the AWS Cloud, you need to configure the kubectl client to authenticate and communicate with the Kubernetes Cluster API Server. In case of EKS, this can be done using the AWS CLI command as given below
   ```
   aws eks update-kubeconfig --name <cluster-name> --region <aws-region>
   ```
   <br>

#### 2. Deploying the Kubernetes Dashboard
   Dashboard is a web-based Kubernetes user interface. You can use Dashboard to deploy containerized applications to a Kubernetes cluster, troubleshoot your containerized application, and manage the cluster resources
   ```
   kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml

   kubectl apply -f dashboard-adminuser.yaml

   kubectl apply -f ClusterRoleBinding.yaml
   ```
   <br>
   
#### 3. Authentication to the Dashboard
   To protect your cluster data, Dashboard deploys with a minimal RBAC configuration by default. Currently, Dashboard only supports logging in with a Bearer Token. Use below command to get the token for dashboard login
   ```
   kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep admin-user | awk '{print $1}')
   ```
   <br>

#### 4. Accessing the Dashboard
   You can access Dashboard using the kubectl command-line tool by running the following command:
   ```
   kubectl proxy
   ```
   Kubectl will make Dashboard available at http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/


## Automation
The entire process of initializing the Kubernetes Cluster involves many steps and automating these steps can ease the process. One way to achieve automation is by defining a simple shell script which will execute the series of commands required to initialize the cluster thereby reducing manual effort. Also this script may include any other additional steps required as per business needs. For example, our script configureK8sCluster.sh script performs the following steps
   - Configure the kubectl
   - Deploy the K8s dashboard
   - Install Nginx controller for the web application