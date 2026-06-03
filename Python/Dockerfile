FROM ubuntu:latest
LABEL authors="sonoma"

# Schlankes Python-Basisimage wählen
FROM python:3.12-slim

# FFmpeg installieren (wichtig für Audiokonvertierung und Metadaten)
RUN apt-get update && apt-get install -y \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

# Streamrip via pip installieren
RUN pip3 install --no-cache-dir streamrip

# 1. Erstelle den Konfigurations-Ordner im Container
RUN mkdir -p /root/.config/streamrip/

# 2. Kopiere deine lokale config.toml genau in diesen Ordner
COPY config.toml /root/.config/streamrip/config.toml

# Arbeitsverzeichnis für die Downloads setzen
WORKDIR /data

# Standardbefehl beim Starten des Containers
ENTRYPOINT ["rip"]
CMD ["--help"]