# create docker image
docker build -f ./Dockerfile -t spring-rest-mvc .

# list docker images
docker images

# run docker container
docker run -p 8071:8071 spring-rest-mvc

# create kubernetes deployment yaml
kubectl create deployment spring-rest-mvc --image joheiss/spring6-rest-mvc --dry-run=client -o=yaml > k8s-deployment.yml

# deploy to kubernetes pod
kubectl apply -f k8s-deployment.yml

# create kubernetes service yaml
kubectl create service clusterip spring-rest-mvc --tcp=8071:8071 --dry-run=client -o=yaml > k8s-service.yml

# deploy kubernetes service
kubectl apply -f k8s-service.yml

# port forwarding to kubernetes
kubectl port-forward service/spring-rest-mvc 8071:8071

# terminate k8s service
kubectl delete service spring-rest-mvc

# terminate k8s deployment (pod)
kubectl delete deployment spring-rest-mvc

# check logs (docker) -- use -f to receive upcoming log events
docker logs <container-id>
docker logs ad3a9f97ed3b

# check logs (k8s cluster) -- use -f to receive upcoming log events
kubectl logs <pod-id>
kubectl logs spring-rest-mvc-f7c596c4-w5fmp
