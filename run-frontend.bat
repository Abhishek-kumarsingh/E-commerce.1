@echo off
echo Starting E-commerce Frontend...

REM Add Node.js to PATH if not already there
set "NODE_PATH=C:\Program Files\nodejs"
set "PATH=%NODE_PATH%;%PATH%"

REM Check if node_modules exists
if not exist "node_modules" (
    echo Installing dependencies...
    npm install
)

echo Starting Vite development server...
npm run dev
