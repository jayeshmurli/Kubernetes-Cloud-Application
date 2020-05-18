
# Backend Web Applicaion Deployment
## Overview
This repository contains all the Kubernetes condfiguration files required to deploy our backend application on the EKS Cluster. The backend application is configured as a Deployment comprising of a Replica Set consisting of scalable pods initialized to a count of 3. The backend application is deployed in an all together new namespace thereby restricting the scope of ConfigMap and Secret within the same namespace.


## K8s Resources
- Deployment
- Ingress Service (Nginx)
- ConfigMap and Secrets
- Autoscalar
- RBAC


## Deploy Instruction
Some input parameters are required to be configured as environment variables which are then used by the web application to process the requests. These input parameters can sometimes contain sensitive information such as Database connection passwords and is not advisable to use them as simple plaintext in Pod configuration. Instead we can make use of Kubernetes resource configMap or even Secrets to store these values and then configure the Pod to read the values from these Secrets. <br>

#### Automation
An automation such as the shell script SetupBackend.sh eases the process to deploy our web application by dynamically taking input from the user at the time of deployment, there by avoiding mentioning these parameters directly in source code.

#### Script Usage:
```
sh SetupBackend.sh
```
#### Script Input Parameters:

| Variable Name | Description | Example |
| --- | --- | --- |
|S3_Bucket| The S3 Bucket Name which will be used to store images of recipe, if any. | csye7374.iyerj.me |
|DB_HOST| The database server endpoint required to communicate with the DB.| some_db_url.amazon.com |
|DB_USER| The database user required to authenticate with the DB. | admin |
|DB_PASSWORD| The database user required to authenticate with the DB. | password |
|Image_name| The docker image repository path and tag| xxxxxxxxx.dkr.ecr.us-east-1.amazonaws.com/webapp-backend:6a3bd4|