@echo off
echo Starting E-commerce Backend...
cd backend

REM Try to use Maven if available
where mvn >nul 2>nul
if %ERRORLEVEL% == 0 (
    echo Using Maven...
    mvn spring-boot:run -Dspring-boot.run.profiles=dev
) else (
    echo Maven not found. Trying alternative methods...

    REM Try to find Maven in common locations
    if exist "C:\Program Files\Apache\maven\bin\mvn.cmd" (
        echo Found Maven in Program Files...
        "C:\Program Files\Apache\maven\bin\mvn.cmd" spring-boot:run -Dspring-boot.run.profiles=dev
    ) else if exist "C:\apache-maven\bin\mvn.cmd" (
        echo Found Maven in C:\apache-maven...
        "C:\apache-maven\bin\mvn.cmd" spring-boot:run -Dspring-boot.run.profiles=dev
    ) else (
        echo Maven not found. Please:
        echo 1. Install Maven: choco install maven
        echo 2. Or use VS Code: Open EcommerceApplication.java and click "Run Java"
        echo 3. Or restart your terminal after Maven installation
        echo 4. Or manually download Maven from https://maven.apache.org/download.cgi
        pause
    )
)
