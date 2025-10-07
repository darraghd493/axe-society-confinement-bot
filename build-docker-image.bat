@echo off
chcp 65001 >nul

setlocal enabledelayedexpansion
cd /d %~dp0

echo ============================================
echo 🚀 Building Docker image for Discord bot...
echo ============================================
echo.

echo 🔨 Building Docker image...
docker build -t axe-society-confinement-bot:latest .

if %ERRORLEVEL% neq 0 (
    echo ❌ Docker build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo 📦 Saving Docker image to TAR file...
docker save -o axe-society-confinement-bot.tar axe-society-confinement-bot:latest

if %ERRORLEVEL% neq 0 (
    echo ❌ Failed to save Docker image!
    pause
    exit /b %ERRORLEVEL%
)

echo ✅ Docker image built and saved as: axe-society-confinement-bot.tar
echo Reminder: on the VPS, run:
echo ============================================
echo    docker stop axe-society-confinement-bot
echo    docker rm axe-society-confinement-bot
echo    docker rmi axe-society-confinement-bot:latest
echo ============================================
echo    docker load -i axe-society-confinement-bot.tar
echo    docker run -d --env-file .env --name axe-society-confinement-bot axe-society-confinement-bot:latest
pause
