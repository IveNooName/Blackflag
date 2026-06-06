run this command for starting the docker container:
```BASH
docker run -it --rm \
  -v /Users/sonoma/Downloads:/data \
  streamrip_v1_docker --no-db url "https://www.deezer.com/de/album/1346746"
```