#!/bin/bash

# RarityX Build Script
# This script builds the plugin and prepares it for distribution

echo "🔨 Building RarityX Plugin..."

# Clean previous builds
echo "🧹 Cleaning previous builds..."
mvn clean

# Compile and package
echo "📦 Compiling and packaging..."
mvn package

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo "📁 Plugin JAR location: target/RarityX-1.0.0.jar"
    echo ""
    echo "🚀 To install:"
    echo "1. Copy target/RarityX-1.0.0.jar to your server's plugins folder"
    echo "2. Restart your server"
    echo "3. Configure using /rarityx reload after editing config.yml"
    echo ""
    echo "📚 Documentation:"
    echo "- README.md - Complete plugin documentation"
    echo "- QUICKSTART.md - Quick start guide"
    echo "- DEVELOPER.md - Developer documentation"
else
    echo "❌ Build failed! Check the error messages above."
    exit 1
fi