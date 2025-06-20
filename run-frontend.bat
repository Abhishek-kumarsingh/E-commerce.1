@echo off
echo Starting E-commerce Frontend...
cd frontend

REM Check if node_modules exists
if not exist "node_modules" (
    echo Installing dependencies...
    npm install
)

echo Starting React development server...
npm start
