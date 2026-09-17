docker info > $null 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "Docker is running! "
} else {
    Write-Host "Docker läuft nicht oder ist nicht installiert"
    exit 1
}

# Image bauen
docker build -t my-spring-app .
$buildExitCode = $LASTEXITCODE

if ($buildExitCode -ne 0) {
    Write-Host ""
    Write-Host ""
    Write-Host "====================================================================================="
    Write-Host "Build was not successfully with Exit-Code: $buildExitCode"
    Write-Host "====================================================================================="
    Write-Host ""
    exit 1
}

Write-Host "Build backend successfully!"

Write-Host ""
Write-Host ""
Write-Host "====================================================================================="
Write-Host "Backflag is build successfully!"
Write-Host "====================================================================================="
Write-Host ""