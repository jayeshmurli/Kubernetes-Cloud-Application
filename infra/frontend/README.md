
# Frontend Web Applicaion Deployment
## Overview
This repository contains all the Kubernetes condfiguration files required to deploy our frontend react web application on the EKS Cluster. The web application is configured as a Deployment comprising of a Replica Set consisting of scalable pods initialized to a count of 3. The web application is deployed in an all together new namespace thereby restricting the scope of ConfigMap and Secret within the same namespace.


## K8s Resources
- Deployment
- Ingress Service (Nginx)
- ConfigMap and Secrets
- Autoscalar
- RBAC


## Deploy Instruction
Some input parameters are required to be configured as environment variables which are then used by the web application to process the requests. These input parameters can sometimes contain sensitive information such as backend server authentication details and is not advisable to use them as simple plaintext in Pod configuration. Instead we can make use of Kubernetes resource configMap or even Secrets to store these values and then configure the Pod to read the values from these Secrets. <br>

#### Automation
An automation such as the shell script SetupFrontend.sh eases the process to deploy our web application by dynamically taking input from the user at the time of deployment, there by avoiding mentioning these parameters directly in source code.

#### Script Usage:
```
sh SetupFrontend.sh
```
#### Script Input Parameters:

| Variable Name | Description | Example |
| --- | --- | --- |
|Image_name| The docker image repository path and tag| xxxxxxxxx.dkr.ecr.us-east-1.amazonaws.com/webapp-frontend:6a3bd4|
|Loadbalancer_url| The backend service load balancer url to connect for retrieving records| api-loadbalancer-1234567890.us-east-1.elb.amazonaws.com |