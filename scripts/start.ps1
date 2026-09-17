docker rm -f blackflag > $null 2>&1

docker run `
  --name blackflag `
  -p 8080:8080 `
  my-spring-app:latest