if docker info > /dev/null 2>&1; then
  echo "Docker is running! "
else
  echo "Docker läuft nicht oder ist nicht installiert"
fi

docker build -t my-spring-app .
BUILD_EXIT_CODE=$?
if [ $BUILD_EXIT_CODE -ne 0 ]; then
  echo ""
  echo ""
  echo "====================================================================================="
  echo "Build was not successfully with Exit-Code: $BUILD_EXIT_CODE"
  echo "====================================================================================="
  echo ""
  exit 1
fi
echo "Build backend successfully!"


echo ""
echo ""
echo "====================================================================================="
echo "Backflag is build successfully!"
echo "====================================================================================="
echo ""
