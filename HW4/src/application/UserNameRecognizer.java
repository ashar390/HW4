package application;
 
 
 public class UserNameRecognizer {
 	/**
 	 * <p> Title: FSM-translated UserNameRecognizer. </p>
 	 * 
 	 * <p> Description: A demonstration of the mechanical translation of Finite State Machine 
 	 * diagram into an executable Java program using the UserName Recognizer. The code 
 	 * detailed design is based on a while loop with a select list</p>
 	 * 
 	 * <p> Copyright: Lynn Robert Carter © 2024 </p>
 	 * 
 	 * @author Lynn Robert Carter
 	 * 
 	 * @version 1.00		2024-09-13	Initial baseline derived from the Even Recognizer
 	 * @version 1.01		2024-09-17	Correction to address UNChar coding error, improper error
 	 * 									message, and improve internal documentation
 	 * 
 	 */
 
 	/**********************************************************************************************
 	 * 
 	 * Result attributes to be used for GUI applications where a detailed error message and a 
 	 * pointer to the character of the error will enhance the user experience.
 	 * 
 	 */
 
 	public static String userNameRecognizerErrorMessage = "";	// The error message text
 	public static String userNameRecognizerInput = "";			// The input being processed
 	public static int userNameRecognizerIndexofError = -1;		// The index of error location
 	private static int state = 0;						// The current state value
 	private static int nextState = 0;					// The next state value
 	private static boolean finalState = false;			// Is this state a final state?
 	private static String inputLine = "";				// The input line
 	private static char currentChar;					// The current character in the line
 	private static int currentCharNdx;					// The index of the current character
 	private static boolean running;						// The flag that specifies if the FSM is 
 														// running
 	private static int userNameSize = 0;			// A numeric value may not exceed 16 characters
 
 	// Private method to display debugging data
 	private static void displayDebuggingInfo() {
 		// Display the current state of the FSM as part of an execution trace
 		if (currentCharNdx >= inputLine.length())
 			// display the line with the current state numbers aligned
 			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
 					((finalState) ? "       F   " : "           ") + "None");
 		else
 			System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state + 
 				((finalState) ? "       F   " : "           ") + "  " + currentChar + " " + 
 				((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") + 
 				nextState + "     " + userNameSize);
 	}
 	
 	// Private method to move to the next character within the limits of the input line
 	private static void moveToNextCharacter() {
 		currentCharNdx++;
 		if (currentCharNdx < inputLine.length())
 			currentChar = inputLine.charAt(currentCharNdx);
 		else {
 			currentChar = ' ';
 			running = false;
 		}
 	}
 
 	/**********
 	 * This method is a mechanical transformation of a Finite State Machine diagram into a Java
 	 * method.
 	 * 
 	 * @param input		The input string for the Finite State Machine
 	 * @return			An output string that is empty if every things is okay or it is a String
 	 * 						with a helpful description of the error
 	 */
 	public static boolean checkForValidUserName(String input) {
        // If the input is empty or null, automatically fail
        if (input == null || input.length() == 0) {
            return false;
        }

        // Local FSM variables (mirroring the snippet)
        int state = 0;
        int nextState = -1;
        boolean running = true;
        boolean finalState = false;
        int userNameSize = 0;

        // We track the current character index and character
        int currentCharNdx = 0;
        char currentChar = input.charAt(currentCharNdx);

        // The FSM runs until we either run out of characters or fail a transition
        while (running) {
            switch (state) {
                case 0:
                    // State 0: Must start with A-Z or a-z
                    if ((currentChar >= 'A' && currentChar <= 'Z') || 
                        (currentChar >= 'a' && currentChar <= 'z')) {
                        nextState = 1;
                        userNameSize++;
                    } else {
                        // Invalid starting character => fail
                        running = false;
                    }
                    break;

                case 1:
                    // State 1: A-Z, a-z, 0-9 => remain in state 1
                    //          or '.'/'_'/'-' => go to state 2
                    if ((currentChar >= 'A' && currentChar <= 'Z') ||
                        (currentChar >= 'a' && currentChar <= 'z') ||
                        (currentChar >= '0' && currentChar <= '9')) {
                        nextState = 1;
                        userNameSize++;
                    } else if (currentChar == '.' || currentChar == '_' || currentChar == '-') {
                        nextState = 2;
                        userNameSize++;
                    } else {
                        running = false;
                    }

                    // If the size exceeds 16, fail
                    if (userNameSize > 16) {
                        running = false;
                    }
                    break;

                case 2:
                    // State 2: The character after '.'/'_'/'-' must be A-Z, a-z, or 0-9 => return to state 1
                    if ((currentChar >= 'A' && currentChar <= 'Z') ||
                        (currentChar >= 'a' && currentChar <= 'z') ||
                        (currentChar >= '0' && currentChar <= '9')) {
                        nextState = 1;
                        userNameSize++;
                    } else {
                        running = false;
                    }

                    if (userNameSize > 16) {
                        running = false;
                    }
                    break;
            }

            // If we can still run, move to the next state/char
            if (running) {
                state = nextState;
                nextState = -1;

                // If we land in state == 1, we consider that a "final" state
                if (state == 1) {
                    finalState = true;
                }

                // Move to the next character index
                currentCharNdx++;
                if (currentCharNdx < input.length()) {
                    currentChar = input.charAt(currentCharNdx);
                } else {
                    // We've used up all characters in 'input'
                    // We'll break out of the loop after checking final state
                    break;
                }
            }
        }

        // Once we break out, the loop has ended
        // For the username to be valid:
        //   1. We must have ended in or be able to remain in a final state (state == 1)
        //   2. We didn't break early due to an invalid transition or oversize
        return finalState && running;
    }
}