#!/bin/bash



if ! [ -x "$(command -v java)" ]; then
  echo 'Error: Java is not installed.' >&2
  echo 'Installing Java...'
  
  # Install Java (assuming you have root privileges)
  sudo apt-get update
  sudo apt-get install default-jdk -y

  
  if ! [ -x "$(command -v java)" ]; then
    echo 'Error: Java installation failed.' >&2
    exit 1
  fi
fi

# Check if the user provided the source file
if [ -z "$1" ]; then
  echo "Usage: ./enigma.sh <SourceFile.java>"
  exit 1
fi

SOURCE_FILE=$1
CLASS_NAME=${SOURCE_FILE%.java}

# Compile the Java source file
echo "Compiling $SOURCE_FILE..."
javac $SOURCE_FILE

# Check if compilation was successful
if [ $? -eq 0 ]; then
  echo "Compilation successful!"
else
  echo "Compilation failed."
  exit 1
fi

# Run the compiled Java program
echo "Running the program..."
java $CLASS_NAME
