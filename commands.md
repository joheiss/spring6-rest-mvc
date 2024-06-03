# create docker image
docker build -f ./Dockerfile -t spring-rest-mvc .

# list docker images
docker images

# run docker container
docker run -p 8071:8071 spring-rest-mvc
