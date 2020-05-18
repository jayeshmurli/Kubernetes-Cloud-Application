#!/bin/bash

kubectl apply -f namespacecreation.yaml

echo "Enter S3 Bucket name"
read S3BUCKET
sed -e "s/S3BUCKET/$S3BUCKET/g" configMap.yaml_template.yaml > configMap.yaml

echo "Enter DB Hostname"
read db_host
echo "Enter DB Username"
read db_user
echo "Enter DB Password"
read db_pass
sed -e "s|db_hostname_to_replace|$db_host|g ; s|db_user_to_replace|$db_user|g ; s|db_pass_to_replace|$db_pass|g" secret_template.yaml > secret.yaml

echo "Enter image name"
read image_name
sed -e "s|image_name_to_replace|$image_name|g" rs-01_template.yaml > rs-01.yaml

kubectl apply -f secret.yaml

kubectl apply -f configMap.yaml

kubectl apply -f api_service.yaml

kubectl apply -f rs-01.yaml

kubectl apply -f serviceaccount.yaml

kubectl apply -f role.yaml

kubectl apply -f rolebinding.yaml

kubectl apply -f ingress_backend.yaml

kubectl apply -f backend-autoscaling.yaml