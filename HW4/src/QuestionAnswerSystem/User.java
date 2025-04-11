package QuestionAnswerSystem;

import application.PasswordEvaluator;
import application.UserNameRecognizer;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as username, password, email, and role.
 * 
 * This updated version addresses:
 * - Proper instance getters/setters instead of static references
 * - Storing an email field
 * - A straightforward constructor
 * - Validation methods referencing PasswordEvaluator and UserNameRecognizer
 */
public class User {

    private String userName;
    private String password;
    private String email;
    private String role;

    /**
     * Constructs a new User with the specified username, password, email, and role.
     *
     * @param userName the user's chosen username
     * @param password the user's password
     * @param email    the user's email address
     * @param role     the user's role (e.g., "admin", "student", "staff")
     */
    public User(String userName, String password, String email, String role) {
        this.userName = userName;
        this.password = password;
        this.email    = email;
        this.role     = role;
    }

    /**
     * Returns the user's username.
     */
 public String getUserName() {
	    return this.userName; 
 }

    /**
     * Sets the user's username.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the user's password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's email address.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets the user's email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's role (e.g., "admin", "student", "staff", etc.).
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Sets the user's role.
     */
    public void setRole(String role) {
        this.role = role;
    }

    // ------------------------------------------------------------------------
    // Static validation helpers referencing other classes (optional, adapt as needed)
    // ------------------------------------------------------------------------

    /**
     * Checks if the given email is valid.
     *
     * In some projects, you might rely on a dedicated EmailValidator class or just a regex.
     * If you have no dedicated class, use a simple regex check or your own logic here.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        // For a quick demonstration, let's do a simple RFC 5322-like pattern:
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Checks if the given password is valid.
     *
     * If your project has a PasswordEvaluator class, call it here.
     * Otherwise, implement your own password requirements.
     */
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        // Example using a PasswordEvaluator class:
        return PasswordEvaluator.isValidPassword(password);
    }

    /**
     * Checks if the given username is valid.
     *
     * If your project has a UserNameRecognizer class, call it here.
     * Otherwise, implement your own criteria (e.g., length, no special chars, etc.).
     */
    public static boolean isValidUserName(String userName) {
        if (userName == null) return false;
        // Example using a UserNameRecognizer class:
        return UserNameRecognizer.checkForValidUserName(userName);
    }
}
