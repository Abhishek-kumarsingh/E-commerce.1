#!/usr/bin/env pwsh

Write-Host "Starting E-commerce Frontend..." -ForegroundColor Green

# Add Node.js to PATH
$env:PATH = "C:\Program Files\nodejs;$env:PATH"

# Check if node_modules exists
if (-not (Test-Path "node_modules")) {
    Write-Host "Installing dependencies..." -ForegroundColor Yellow
    npm install
}

Write-Host "Starting Vite development server..." -ForegroundColor Green
npm run dev
