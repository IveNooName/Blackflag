docker rm -f blackflag 2>/dev/null || true

docker run \
  --name blackflag \
  -p 8080:8080 \
  -v /var/run/docker.sock:/var/run/docker.sock \
  my-spring-app:latest