# Blackflag



## Anpassungen:
### In `./python/`:
1. ARL einfügen auf Zeile 65 (anleitung und beschreibung vorhanden)
2. File von `config.toml.backup` zu `config.toml` umbenennen
3. Docker Container bauen

### In der Root directory (`./`)
1. Pfade in `AlbumController` auf Zeile 51 und 52 umschreiben auf die richtigen Pfade
2. Docker container bauen


## Container bauen:
``` BASH
cd python
docker build -t streamrip_v1_docker .
cd ..
docker build -t my-spring-app .
docker run -p 8080:8080 my-spring-app:latest
```
