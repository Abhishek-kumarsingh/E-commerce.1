@echo off
echo ========================================
echo    Maven Manual Installation Guide
echo ========================================
echo.

REM Check if Maven is already installed
where mvn >nul 2>nul
if %ERRORLEVEL% == 0 (
    echo ✅ Maven is already installed!
    mvn --version
    pause
    exit /b 0
)

echo 📋 Manual Maven Installation Steps:
echo.
echo 1. 📥 Download Maven:
echo    - Open your browser
echo    - Go to: https://maven.apache.org/download.cgi
echo    - Download: apache-maven-3.9.9-bin.zip
echo    - Save it to your Downloads folder
echo.
echo 2. 📦 Extract Maven:
echo    - Right-click the downloaded zip file
echo    - Select "Extract All..."
echo    - Extract to: C:\apache-maven\
echo    - You should have: C:\apache-maven\apache-maven-3.9.9\
echo.
echo 3. 🔧 Add to System PATH:
echo    - Press Win + R, type: sysdm.cpl, press Enter
echo    - Click "Environment Variables" button
echo    - Under "System Variables", find and select "Path"
echo    - Click "Edit" then "New"
echo    - Add: C:\apache-maven\apache-maven-3.9.9\bin
echo    - Click OK to save all dialogs
echo.
echo 4. 🔄 Restart Terminal:
echo    - Close this command prompt
echo    - Open a new command prompt
echo    - Test with: mvn --version
echo.
echo 5. 🚀 Start Backend:
echo    - cd backend
echo    - mvn spring-boot:run
echo.
echo ========================================
echo.
echo 🌐 Alternative: Use VS Code (Easier!)
echo.
echo If Maven installation seems complex, use VS Code instead:
echo 1. Open VS Code in your project folder
echo 2. Install "Extension Pack for Java"
echo 3. Open: backend/src/main/java/com/ecommerce/EcommerceApplication.java
echo 4. Click the "Run" button above the main method
echo.
echo This will automatically handle Maven and start your backend!
echo.
echo ========================================
echo.
pause

REM Try to open the Maven download page
echo Opening Maven download page...
start https://maven.apache.org/download.cgi

echo.
echo After downloading and extracting Maven, run this script again to verify installation.
pause
