@echo off
echo Starting E-commerce Backend...
echo.

REM Set environment variables
set DATABASE_PASSWORD=9934
set MONGODB_URI=mongodb://localhost:27017/ecommerce_products
set SPRING_PROFILES_ACTIVE=dev

echo Setting up environment variables...
echo DATABASE_PASSWORD=%DATABASE_PASSWORD%
echo MONGODB_URI=%MONGODB_URI%
echo SPRING_PROFILES_ACTIVE=%SPRING_PROFILES_ACTIVE%
echo.

cd backend

REM Try different Maven locations
echo Trying to find Maven...

REM Check if mvn is in PATH
where mvn >nul 2>nul
if %ERRORLEVEL% == 0 (
    echo Found Maven in PATH
    mvn spring-boot:run
    goto :end
)

REM Check chocolatey installation
if exist "C:\ProgramData\chocolatey\bin\mvn.cmd" (
    echo Found Maven in Chocolatey bin
    "C:\ProgramData\chocolatey\bin\mvn.cmd" spring-boot:run
    goto :end
)

REM Check common Maven locations
if exist "C:\Program Files\Apache\maven\bin\mvn.cmd" (
    echo Found Maven in Program Files
    "C:\Program Files\Apache\maven\bin\mvn.cmd" spring-boot:run
    goto :end
)

if exist "C:\apache-maven\bin\mvn.cmd" (
    echo Found Maven in C:\apache-maven
    "C:\apache-maven\bin\mvn.cmd" spring-boot:run
    goto :end
)

REM If no Maven found, provide instructions
echo.
echo ❌ Maven not found in common locations.
echo.
echo 📋 Please try one of these options:
echo.
echo 1. 🔄 Restart your terminal/command prompt (Maven might need PATH refresh)
echo.
echo 2. 💻 Use VS Code:
echo    - Open backend/src/main/java/com/ecommerce/EcommerceApplication.java
echo    - Click the "Run" button above the main method
echo.
echo 3. 📦 Install Maven manually:
echo    - Download from: https://maven.apache.org/download.cgi
echo    - Extract to C:\apache-maven
echo    - Add C:\apache-maven\bin to your PATH
echo.
echo 4. 🍫 Try Chocolatey again:
echo    - Run: choco install maven -y
echo    - Restart terminal
echo.
pause

:end
echo.
echo Backend startup script completed.
pause
