@echo off
echo Installing Maven manually...
echo.

REM Create directory
if not exist "C:\apache-maven" (
    echo Creating Maven directory...
    mkdir "C:\apache-maven"
)

echo.
echo 📋 Manual Maven Installation Steps:
echo.
echo 1. Download Maven from: https://maven.apache.org/download.cgi
echo    - Download "Binary zip archive" (apache-maven-3.9.9-bin.zip)
echo.
echo 2. Extract the zip file to: C:\apache-maven\
echo    - You should have: C:\apache-maven\apache-maven-3.9.9\
echo.
echo 3. Add to PATH:
echo    - Open System Properties ^> Environment Variables
echo    - Add to PATH: C:\apache-maven\apache-maven-3.9.9\bin
echo.
echo 4. Restart your terminal and run:
echo    - mvn --version
echo.
echo 5. Then run the backend:
echo    - cd backend
echo    - mvn spring-boot:run
echo.
echo ⚡ OR use VS Code (Recommended):
echo    - Open backend/src/main/java/com/ecommerce/EcommerceApplication.java
echo    - Click the "Run" button above main method
echo.
pause
