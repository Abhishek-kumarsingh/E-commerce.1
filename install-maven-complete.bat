@echo off
setlocal enabledelayedexpansion

echo ========================================
echo    Maven Installation Script
echo ========================================
echo.

REM Check if Maven is already installed
echo Checking if Maven is already installed...
where mvn >nul 2>nul
if %ERRORLEVEL% == 0 (
    echo ✅ Maven is already installed!
    mvn --version
    echo.
    echo You can now run: cd backend && mvn spring-boot:run
    pause
    exit /b 0
)

echo ❌ Maven not found. Installing Maven...
echo.

REM Check if Java is installed
echo Checking Java installation...
java -version >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo ❌ Java is not installed or not in PATH!
    echo Please install Java 17 or higher first.
    pause
    exit /b 1
)

echo ✅ Java is installed.
java -version
echo.

REM Create Maven directory
set MAVEN_HOME=C:\apache-maven
set MAVEN_VERSION=3.9.9
set MAVEN_DIR=%MAVEN_HOME%\apache-maven-%MAVEN_VERSION%

echo Creating Maven directory: %MAVEN_HOME%
if not exist "%MAVEN_HOME%" (
    mkdir "%MAVEN_HOME%"
)

REM Download Maven using PowerShell
echo.
echo 📥 Downloading Maven %MAVEN_VERSION%...
set MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip
set MAVEN_ZIP=%MAVEN_HOME%\apache-maven-%MAVEN_VERSION%-bin.zip

echo Downloading from: %MAVEN_URL%
echo To: %MAVEN_ZIP%

powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%'}"

if not exist "%MAVEN_ZIP%" (
    echo ❌ Failed to download Maven!
    echo Please download manually from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo ✅ Maven downloaded successfully!

REM Extract Maven
echo.
echo 📦 Extracting Maven...
powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_HOME%' -Force"

if not exist "%MAVEN_DIR%" (
    echo ❌ Failed to extract Maven!
    pause
    exit /b 1
)

echo ✅ Maven extracted successfully!

REM Clean up zip file
del "%MAVEN_ZIP%"

REM Add Maven to PATH for current session
echo.
echo 🔧 Setting up Maven for current session...
set PATH=%MAVEN_DIR%\bin;%PATH%

REM Test Maven installation
echo.
echo 🧪 Testing Maven installation...
"%MAVEN_DIR%\bin\mvn.cmd" --version
if %ERRORLEVEL% neq 0 (
    echo ❌ Maven test failed!
    pause
    exit /b 1
)

echo.
echo ✅ Maven installed successfully!
echo.
echo 📋 IMPORTANT: To make Maven permanent, add to your system PATH:
echo    %MAVEN_DIR%\bin
echo.
echo 🔧 To add to PATH automatically (requires admin):
choice /C YN /M "Do you want to add Maven to system PATH automatically"
if errorlevel 2 goto :manual_path
if errorlevel 1 goto :auto_path

:auto_path
echo Adding Maven to system PATH...
powershell -Command "Start-Process cmd -ArgumentList '/c setx PATH \"%PATH%;%MAVEN_DIR%\bin\" /M' -Verb RunAs"
echo ✅ Maven added to system PATH (restart terminal to use globally)
goto :test_backend

:manual_path
echo.
echo 📋 Manual PATH setup:
echo 1. Open System Properties (Win + R, type: sysdm.cpl)
echo 2. Click "Environment Variables"
echo 3. Under "System Variables", find and select "Path"
echo 4. Click "Edit" then "New"
echo 5. Add: %MAVEN_DIR%\bin
echo 6. Click OK to save
echo 7. Restart your terminal
echo.

:test_backend
echo.
echo 🚀 Now you can start the backend:
echo    cd backend
echo    mvn spring-boot:run
echo.
choice /C YN /M "Do you want to start the backend now"
if errorlevel 2 goto :end
if errorlevel 1 goto :start_backend

:start_backend
echo.
echo Starting Spring Boot backend...
cd backend
"%MAVEN_DIR%\bin\mvn.cmd" spring-boot:run -Dspring-boot.run.profiles=dev
goto :end

:end
echo.
echo ========================================
echo    Maven Installation Complete!
echo ========================================
echo.
echo Maven Location: %MAVEN_DIR%
echo Maven Binary: %MAVEN_DIR%\bin\mvn.cmd
echo.
echo Next steps:
echo 1. Restart your terminal (if you added to PATH)
echo 2. cd backend
echo 3. mvn spring-boot:run
echo.
pause
