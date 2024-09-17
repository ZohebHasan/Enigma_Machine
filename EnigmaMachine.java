
import java.util.*;

public class EnigmaMachine {

    private static final Map<Character, Character> reflectorA = new HashMap<>();
    private static final Map<Character, Character> reflectorB = new HashMap<>();
    private static final Map<Character, Character> reflectorC = new HashMap<>();

    private final Map<Character, Character> reflector;

    private final PlugBoard plugBoard;

    private final char[] staticBoard;

    private final Rotor[] rotors = new Rotor[3];

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static {
        // Reflector A
        reflectorA.put('A', 'E');
        reflectorA.put('B', 'J');
        reflectorA.put('C', 'M');
        reflectorA.put('D', 'Z');
        reflectorA.put('E', 'A');
        reflectorA.put('F', 'L');
        reflectorA.put('G', 'Y');
        reflectorA.put('H', 'X');
        reflectorA.put('I', 'V');
        reflectorA.put('J', 'B');
        reflectorA.put('K', 'W');
        reflectorA.put('L', 'F');
        reflectorA.put('M', 'C');
        reflectorA.put('N', 'R');
        reflectorA.put('O', 'Q');
        reflectorA.put('P', 'U');
        reflectorA.put('Q', 'O');
        reflectorA.put('R', 'N');
        reflectorA.put('S', 'T');
        reflectorA.put('T', 'S');
        reflectorA.put('U', 'P');
        reflectorA.put('V', 'I');
        reflectorA.put('W', 'K');
        reflectorA.put('X', 'H');
        reflectorA.put('Y', 'G');
        reflectorA.put('Z', 'D');

        // Reflector B
        reflectorB.put('A', 'Y');
        reflectorB.put('B', 'R');
        reflectorB.put('C', 'U');
        reflectorB.put('D', 'H');
        reflectorB.put('E', 'Q');
        reflectorB.put('F', 'S');
        reflectorB.put('G', 'L');
        reflectorB.put('H', 'D');
        reflectorB.put('I', 'P');
        reflectorB.put('J', 'X');
        reflectorB.put('K', 'N');
        reflectorB.put('L', 'G');
        reflectorB.put('M', 'O');
        reflectorB.put('N', 'K');
        reflectorB.put('O', 'M');
        reflectorB.put('P', 'I');
        reflectorB.put('Q', 'E');
        reflectorB.put('R', 'B');
        reflectorB.put('S', 'F');
        reflectorB.put('T', 'Z');
        reflectorB.put('U', 'C');
        reflectorB.put('V', 'W');
        reflectorB.put('W', 'V');
        reflectorB.put('X', 'J');
        reflectorB.put('Y', 'A');
        reflectorB.put('Z', 'T');

        // Reflector C
        reflectorC.put('A', 'F');
        reflectorC.put('B', 'V');
        reflectorC.put('C', 'P');
        reflectorC.put('D', 'J');
        reflectorC.put('E', 'I');
        reflectorC.put('F', 'A');
        reflectorC.put('G', 'O');
        reflectorC.put('H', 'Y');
        reflectorC.put('I', 'E');
        reflectorC.put('J', 'D');
        reflectorC.put('K', 'R');
        reflectorC.put('L', 'Z');
        reflectorC.put('M', 'X');
        reflectorC.put('N', 'W');
        reflectorC.put('O', 'G');
        reflectorC.put('P', 'C');
        reflectorC.put('Q', 'T');
        reflectorC.put('R', 'K');
        reflectorC.put('S', 'U');
        reflectorC.put('T', 'Q');
        reflectorC.put('U', 'S');
        reflectorC.put('V', 'B');
        reflectorC.put('W', 'N');
        reflectorC.put('X', 'M');
        reflectorC.put('Y', 'H');
        reflectorC.put('Z', 'L');
    }

    public static class PlugBoard {

        private final Map<Character, Character> plugins = new HashMap<>();

        public PlugBoard(String plugSetup) {
            if (plugSetup != null && !plugSetup.isEmpty()) {
                String[] pairs = plugSetup.split(",");
                for (String pair : pairs) {
                    if (pair.length() == 2) {
                        char first = pair.charAt(0);
                        char second = pair.charAt(1);

                        if (!plugins.containsKey(first) && !plugins.containsKey(second)) {
                            plugins.put(first, second);
                            plugins.put(second, first);
                        } else {
                            throw new IllegalArgumentException("Invalid plugboard configuration: " + pair);
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid plugboard pair: " + pair);
                    }
                }
            }
        }

        public char substitute(char letter) {
            return plugins.getOrDefault(letter, letter);
        }
    }

    public static class Key {

        char letter;
        char wiredTo;

        public Key(char letter, char wiredTo) {
            this.letter = letter;
            this.wiredTo = wiredTo;
        }
    }

    public static class KeyPair {

        Key rightKey;
        Key leftKey;

        public KeyPair(Key rightKey, Key leftKey) {
            this.rightKey = rightKey;
            this.leftKey = leftKey;
        }
    }

    public class Rotor {

        List<KeyPair> keyPairs;
        protected char notch;

        int rotationCount;

        public Rotor(String wiringMap, char notch) {
            this.notch = notch;
            keyPairs = new ArrayList<>();

            for (int i = 0; i < ALPHABET.length(); i++) {
                char leftChar = ALPHABET.charAt(i);
                char rightChar = ALPHABET.charAt(i);
                char rightCharMappedTo = wiringMap.charAt(i);
                char leftCharMappedTo = '\0';
                for (int j = 0; j < wiringMap.length(); j++) {
                    if (leftChar == wiringMap.charAt(j)) {
                        leftCharMappedTo = ALPHABET.charAt(j);
                        break;
                    }
                }

                Key leftKey = new Key(leftChar, leftCharMappedTo);
                Key rightKey = new Key(rightChar, rightCharMappedTo);
                KeyPair pair = new KeyPair(rightKey, leftKey);
                keyPairs.add(pair);
            }
        }

        public void setPosition(char initialPosition) {
            int index = -1;
            for (int i = 0; i < keyPairs.size(); i++) {
                if (keyPairs.get(i).rightKey.letter == initialPosition) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                List<KeyPair> newOrder = new ArrayList<>();
                newOrder.addAll(keyPairs.subList(index, keyPairs.size()));
                newOrder.addAll(keyPairs.subList(0, index));
                keyPairs = newOrder;

            } else {
                System.out.println("Initial position not found in keyPairs.");
            }
        }

        public void rotate() {
            KeyPair firstKey = keyPairs.remove(0);
            keyPairs.add(firstKey);
            rotationCount++;
        }

        public boolean atNotch() {
            return keyPairs.get(0).rightKey.letter == notch;

        }

        public KeyPair encryptForwardRotor3(char input) {
            int staticIndex = 0;
            for (int i = 0; i < staticBoard.length; i++) {
                if (input == staticBoard[i]) {
                    staticIndex = i;
                    break;
                }
            }

            char newRotatedKey = this.keyPairs.get(staticIndex).rightKey.wiredTo;
            for (KeyPair keyPair : keyPairs) {
                if (keyPair.leftKey.letter == newRotatedKey) {
                    return keyPair;
                }
            }
            return null;
        }

        public KeyPair encryptForwardRotors(char input, int index) {
            KeyPair currentPair = this.keyPairs.get(index);
            char encryptedChar = currentPair.rightKey.wiredTo;
            for (KeyPair keyPair : keyPairs) {
                if (keyPair.leftKey.letter == encryptedChar) {
                    return keyPair;
                }
            }
            return null;
        }

        public KeyPair encryptBackward(KeyPair output) {
            char encryptedLetter = output.leftKey.wiredTo;
            for (KeyPair keypair : this.keyPairs) {
                if (encryptedLetter == keypair.rightKey.letter) {
                    return keypair;
                }
            }
            return null;
        }
    }

    public class Rotor1 extends Rotor {

        public Rotor1() {
            super("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');

        }
    }

    public class Rotor2 extends Rotor {

        public Rotor2() {
            super("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E');
        }
    }

    public class Rotor3 extends Rotor {

        public Rotor3() {
            super("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V');
        }
    }

    public class Rotor4 extends Rotor {

        public Rotor4() {
            super("ESOVPZJAYQUIRHXLNFTGKDCMWB", 'J');
        }
    }

    public class Rotor5 extends Rotor {

        public Rotor5() {
            super("VZBRGITYUPSDNHLXAWMJQOFECK", 'Z');

        }
    }

    public EnigmaMachine(String reflectorCode, String rotorList, String rotorStartingPoint, String plugBoardSetup) {

        staticBoard = new char[26];
        for (int i = 0; i < 26; i++) {
            staticBoard[i] = ALPHABET.charAt(i);
        }

        switch (reflectorCode) {
            case "A":
                this.reflector = reflectorA;
                break;
            case "B":
                this.reflector = reflectorB;
                break;
            case "C":
                this.reflector = reflectorC;
                break;
            default:
                throw new IllegalArgumentException("Invalid reflector code");
        }

        char[] rotorChars = rotorList.toCharArray();
        for (int i = 0; i < 3; i++) {
            switch (rotorChars[i]) {
                case '1':
                    rotors[i] = new Rotor1();
                    break;
                case '2':
                    rotors[i] = new Rotor2();
                    break;
                case '3':
                    rotors[i] = new Rotor3();
                    break;
                case '4':
                    rotors[i] = new Rotor4();
                    break;
                case '5':
                    rotors[i] = new Rotor5();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid rotor number: " + rotorChars[i]);
            }
        }

        char[] rotorStartingPositions = rotorStartingPoint.toCharArray();
        for (int i = 0; i < 3; i++) {
            rotors[i].setPosition(rotorStartingPositions[i]);
        }

        this.plugBoard = new PlugBoard(plugBoardSetup);
    }

    public String processMessage(String message) {
        StringBuilder result = new StringBuilder();
        KeyPair temp = null;

        char[] messageInChars = message.toCharArray();

        for (char letter : messageInChars) {
            // Rotate the rotors as necessary
            if (rotors[2].atNotch() && rotors[1].atNotch()) {
                rotors[2].rotate();
                rotors[1].rotate();
                rotors[0].rotate();
            } else if (rotors[2].atNotch()) {
                rotors[2].rotate();
                rotors[1].rotate();
            } else {
                rotors[2].rotate();
            }

            // Plugboard substitution
            letter = plugBoard.substitute(letter);

            // Forward encryption through the rotors
            for (int i = 2; i >= 0; i--) {
                if (i == 2) {
                    temp = rotors[i].encryptForwardRotor3(letter);
                    letter = temp.leftKey.letter;
                } else {
                    int nextRotorIndex = rotors[i + 1].keyPairs.indexOf(temp);
                    temp = rotors[i].encryptForwardRotors(letter, nextRotorIndex);
                    letter = temp.leftKey.letter;

                    // Special case for the first rotor mapping back to the static board
                    if (i == 0) {
                        int staticIndex = rotors[i].keyPairs.indexOf(temp);
                        letter = staticBoard[staticIndex];
                    }
                }
            }

            // Reflector substitution
            letter = reflector.get(letter);

            // Backward encryption through the rotors
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    int alphabetIndex = ALPHABET.indexOf(letter);
                    KeyPair current = rotors[i].keyPairs.get(alphabetIndex);
                    temp = rotors[i].encryptBackward(current);
                    letter = temp.rightKey.letter;
                } else {
                    int previousRotorIndex = rotors[i - 1].keyPairs.indexOf(temp);
                    KeyPair current = rotors[i].keyPairs.get(previousRotorIndex);
                    temp = rotors[i].encryptBackward(current);
                    letter = temp.rightKey.letter;

                    // Special case for the last rotor mapping back to the static board
                    if (i == 2) {
                        int staticIndex = rotors[i].keyPairs.indexOf(temp);
                        letter = staticBoard[staticIndex];
                    }
                }
            }

            // Final plugboard substitution
            letter = plugBoard.substitute(letter);
            result.append(letter);
        }

        return result.toString();
    }

    public static void main(String[] args) {

        Scanner stdin = new Scanner(System.in);

        // Prompt for reflector code
        System.out.print("Enter the reflector code (A/B/C): ");
        String reflectorCode = stdin.nextLine().toUpperCase();

        // Take rotor list input
        System.out.print("Enter the rotor list (e.g., 123): ");
        String rotorList = stdin.nextLine().toUpperCase();

        // Take rotor starting positions input
        System.out.print("Enter the rotor starting positions (e.g., AAA): ");
        String rotorStartingPoint = stdin.nextLine().toUpperCase();

        // Take plugboard setup input
        System.out.print("Enter the plugboard setup (e.g., AB,CD,EF): ");
        String plugBoardSetup = stdin.nextLine().toUpperCase().replace(" ", "");

        // Create Enigma machine with user inputs
        EnigmaMachine enigma = new EnigmaMachine(reflectorCode, rotorList, rotorStartingPoint, plugBoardSetup);

        // Take the message to encrypt
        System.out.print("Enter the message to encrypt: ");
        String message = stdin.nextLine().toUpperCase();

        // Process the message
        String encryptedMessage = enigma.processMessage(message);
        System.out.println("Encrypted message: " + encryptedMessage);

        stdin.close();
    }

}
