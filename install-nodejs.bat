@echo off
echo Installing Node.js and npm...
echo.

REM Check if Node.js is already installed
node --version >nul 2>&1
if %errorlevel% == 0 (
    echo Node.js is already installed.
    node --version
    echo.
    goto :check_npm
)

REM Download and install Node.js
echo Downloading Node.js installer...
powershell -Command "& {Invoke-WebRequest -Uri 'https://nodejs.org/dist/v20.11.0/node-v20.11.0-x64.msi' -OutFile 'nodejs-installer.msi'}"

if exist nodejs-installer.msi (
    echo Installing Node.js...
    msiexec /i nodejs-installer.msi /quiet /norestart
    echo Node.js installation completed.
    del nodejs-installer.msi
) else (
    echo Failed to download Node.js installer.
    echo Please download and install Node.js manually from https://nodejs.org/
    pause
    exit /b 1
)

:check_npm
REM Check if npm is available
npm --version >nul 2>&1
if %errorlevel% == 0 (
    echo npm is available.
    npm --version
    echo.
    goto :install_dependencies
) else (
    echo npm is not available. Please restart your terminal and try again.
    pause
    exit /b 1
)

:install_dependencies
echo Installing project dependencies...
npm install

if %errorlevel% == 0 (
    echo.
    echo Installation completed successfully!
    echo You can now run: npm run dev
    echo.
) else (
    echo.
    echo Failed to install dependencies.
    echo Please try running: npm install
    echo.
)

pause 