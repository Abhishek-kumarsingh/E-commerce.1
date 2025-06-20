@echo off
echo Starting E-commerce Backend...
cd backend

REM Try to use Maven if available
where mvn >nul 2>nul
if %ERRORLEVEL% == 0 (
    echo Using Maven...
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
) else (
    echo Maven not found. Please install Maven or use VS Code to run the application.
    echo You can:
    echo 1. Install Maven: choco install maven
    echo 2. Or use VS Code: Open EcommerceApplication.java and click "Run Java"
    echo 3. Or restart your terminal after Maven installation
    pause
)
