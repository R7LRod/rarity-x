#!/bin/bash

# Set Java 17 as the active version
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Verify Java version
echo "Using Java version:"
java -version

# Build the project
cd /workspaces/rarity-x
mvn clean package

# Check if build was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build successful!"
    echo "📁 Plugin JAR location: target/RarityX-1.0.0.jar"
    ls -lh target/RarityX-1.0.0.jar
else
    echo ""
    echo "❌ Build failed!"
    exit 1
fi
