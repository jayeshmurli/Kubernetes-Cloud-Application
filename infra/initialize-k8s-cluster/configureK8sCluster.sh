echo "Enter the Cluster name"
read cluster_name

echo "Enter AWS Region"
read aws_regiom

echo "Updating the Kubeconfig for EKS Cluster"
aws eks update-kubeconfig --name $cluster_name --region $aws_regiom

echo "Setting up the k8s Dashboard"
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0-beta8/aio/deploy/recommended.yaml

kubectl apply -f dashboard-adminuser.yaml

kubectl apply -f ClusterRoleBinding.yaml

echo "The Kubernetes Token required to authenticate to the Dashboard"
kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep admin-user | awk '{print $1}')

#kubectl proxy &

#firefox http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/

echo "Configuring Nginx Controller to route traffic to services"
kubectl apply -f helm-rbac.yaml

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/

helm install ingress-nginx ingress-nginx/ingress-nginx

LB_URL=`kubectl get svc ingress-nginx-controller -o jsonpath='{.status.loadBalancer.ingress[*].hostname}'`
echo "NGINX Load Balancer URL : $LB_URL"

