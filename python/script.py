import os
import subprocess

# ==========================================
DOWNLOAD_DIR = os.path.expanduser("~/Downloads/")
# ==========================================

ALBUM_URL   = input("Deezer Album URL: ")
CONFIG_PATH = os.path.expanduser("~/.config/streamrip/config.toml")

def download_album():
    os.makedirs(DOWNLOAD_DIR, exist_ok=True)

    # Das ist der saubere Befehl für streamrip Version 2.x
    cmd = ["rip","--no-db" ,"url", ALBUM_URL]

    print(f"[*] Starte Download: {ALBUM_URL}")
    print(f"[+] Zielverzeichnis : {DOWNLOAD_DIR}\n")

    subprocess.run(cmd, text=True)
    print(f"\n[+] Fertig! Musik liegt unter: {DOWNLOAD_DIR}")


if __name__ == "__main__":
    download_album()