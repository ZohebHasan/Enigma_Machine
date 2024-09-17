
---

# Enigma Machine

## Overview

This is a Java implementation of the Enigma Machine, a cipher device used during World War II for encrypting and decrypting messages. This program simulates the behavior of the historical machine, allowing users to configure rotors, reflector, plugboard settings, and encrypt messages just like the original Enigma.

## How to Run the Program

1. **Clone or download the repository to your local machine**.
2. **Navigate to the project directory** where the `enigma.sh` script and Java files are located.

### Prerequisites

- Java Development Kit (JDK) installed. You can install it using the following commands:
   ```bash
   sudo apt update
   sudo apt install default-jdk
   ```
   Verify the installation with:
   ```bash
   javac -version
   java -version
   ```

- **Ensure WSL is installed** and you're running in a Linux-like environment on Windows.

### Steps to Run

1. **Make the script executable** (only needed once):
   ```bash
   chmod +x enigma.sh
   ```

2. **Run the script with the Java source file**:
   ```bash
   ./enigma.sh EnigmaMachine.java
   ```

### Example Output

After running the script, the program will prompt you for the necessary inputs:

```bash
xoh3b@xoh3b:/mnt/c/Enigma_Machine$ ./enigma.sh EnigmaMachine.java
Compiling EnigmaMachine.java...
Compilation successful!
Running the program...
Enter the reflector code (A/B/C): b
Enter the rotor list (e.g., 123): 123
Enter the rotor starting positions (e.g., AAA): aaa
Enter the plugboard setup (e.g., AB,CD,EF): ab,cd,ef
Enter the message to encrypt: security
Encrypted message: JKJRFLHF
```

### Input Options

1. **Reflector Code**: Select the reflector type (`A`, `B`, or `C`).
2. **Rotor List**: Specify the rotors used (e.g., `123`, `531`).
3. **Rotor Starting Positions**: Define the initial rotor positions (e.g., `AAA`, `ZBD`).
4. **Plugboard Setup**: Provide plugboard pairs (e.g., `AB,CD,EF`).
5. **Message to Encrypt**: Enter the plain text message to be encrypted.

### Encrypted Output
The program will return the encrypted message after processing it through the Enigma machine simulation.

---

## Structure

- **EnigmaMachine.java**: The main Java source code that implements the Enigma Machine.
- **enigma.sh**: A shell script used to compile and run the Java program.

---

### License

This project is open-source and available for anyone to use and modify. Feel free to contribute and improve the functionality.

---

