#!/bin/bash

# Script to compile and run the Enigma machine Java program

# Hardcoded source file name
SOURCE_FILE="EnigmaMachine.java"
CLASS_NAME="EnigmaMachine"

# Check if Java is installed
if ! [ -x "$(command -v java)" ]; then
  echo 'Error: Java is not installed.' >&2
  echo 'Installing Java...'
  
  # Install Java (assuming you have root privileges)
  sudo apt-get update
  sudo apt-get install default-jdk -y

  # Installation success check
  if ! [ -x "$(command -v java)" ]; then
    echo 'Error: Java installation failed.' >&2
    exit 1
  fi
fi

# Compilation of the source file
echo "Compiling $SOURCE_FILE..."
javac $SOURCE_FILE

# Compilation success check
if [ $? -eq 0 ]; then
  echo "Compilation successful!"
else
  echo "Compilation failed."
  exit 1
fi

# Run the compiled Java class
echo "Running the program..."
java $CLASS_NAME
