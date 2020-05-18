#!/bin/sh

kubectl apply -f namespacecreation.yaml

echo "Enter image name"
read image_name
sed -e "s|front_image_to_replace|$image_name|g" rs-01_template.yaml > rs-01.yaml

echo "Enter Backend Load Balancer URL"
read dns_name
LOADBALANCER="$dns_name/api"
echo $LOADBALANCER
cat "configMap.yaml" | sed "s|{{LOADBALANCER}}|$LOADBALANCER|g" | kubectl apply -f -

kubectl apply -f frontend_service.yaml

kubectl apply -f rs-01.yaml

kubectl apply -f serviceaccount.yaml

kubectl apply -f role.yaml

kubectl apply -f rolebinding.yaml

kubectl apply -f ingress_frontend.yaml

kubectl apply -f frontend-autoscaling.yaml
