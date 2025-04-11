package application;
 
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 
 import java.sql.SQLException;

import QuestionAnswerSystem.User;
import databasePart1.*;
 
 /**
  * SetupAccountPage class handles the account setup process for new users.
  * Users provide their userName, password, email, and a valid invitation code to register.
  */
 public class SetupAccountPage {
 	
     private final DatabaseHelper databaseHelper;
 
     // DatabaseHelper to handle database operations.
     public SetupAccountPage(DatabaseHelper databaseHelper) {
         this.databaseHelper = databaseHelper;
     }
 
     /**
      * Displays the Setup Account page in the provided stage.
      * @param primaryStage The primary stage where the scene will be displayed.
      */
     public void show(Stage primaryStage) {
         // Input fields for userName, password, email, and invitation code
         TextField userNameField = new TextField();
         userNameField.setPromptText("Enter userName");
         userNameField.setMaxWidth(250);
 
         PasswordField passwordField = new PasswordField();
         passwordField.setPromptText("Enter Password");
         passwordField.setMaxWidth(250);
         
         TextField emailField = new TextField(); // Added by Abdullah: Input field for email
         emailField.setPromptText("Enter Email");
         emailField.setMaxWidth(250);
 
         TextField inviteCodeField = new TextField();
         inviteCodeField.setPromptText("Enter Invitation Code");
         inviteCodeField.setMaxWidth(250);
         
         // Label to display error messages for invalid input or registration issues
         Label errorLabel = new Label();
         errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
 
         Button setupButton = new Button("Setup");
         
         setupButton.setOnAction(a -> {
             // Retrieve user input
             String userName = userNameField.getText();
             String password = passwordField.getText();
             String email = emailField.getText(); // Added by Abdullah: Retrieve email input
             String code = inviteCodeField.getText();
             
             try {
                 // Validate the username
                 if (!User.isValidUserName(userName)) { // Edited by Abdullah: Added username validation
                     errorLabel.setText("Username must be at least 6 characters long.");
                     return;
                 }
                 
                 // Validate the password
 
                 // Edited by Abdullah: Added password validation
                 if(!User.isValidPassword(password)) { 
                     errorLabel.setText("Password must be 10-13 characters long and meet complexity requirements.");
                     return;
                 }
 
                 // Validate the email
                 if (!User.isValidEmail(email)) { // Added by Abdullah: Added email validation
                     errorLabel.setText("Please enter a valid email address.");
                     return;
                 }
                 System.out.println(userName + " " + password + " " + email);
                 // Check if the user already exists
                 if (!databaseHelper.doesUserExist(userName)) {
                     
                     // Validate the invitation code
                     if (databaseHelper.validateInvitationCode(code)) {
                         String roleAssignment = databaseHelper.getInvitedRole(code);
                         // Create a new user and register them in the database
                         // Edited by Abdullah: Included email, Edited by Sofia: Included roleAssignment
                         User user = new User(userName, password, email, roleAssignment); 
                         databaseHelper.register(user);
                         
                         // Navigate to the Welcome Login Page
                         new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
                         
                     } else {
                         errorLabel.setText("Invalid invitation code. Please try again.");
                     }
                 } else {
                     errorLabel.setText("Username is already taken. Please choose another.");
                 }
                 
             } catch (SQLException e) {
                 System.err.println("Database error: " + e.getMessage());
                 e.printStackTrace();
             }
         });
 
         VBox layout = new VBox(10);
         layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
         layout.getChildren().addAll(userNameField, passwordField, emailField, inviteCodeField, setupButton, errorLabel);
 
         primaryStage.setScene(new Scene(layout, 800, 400));
         primaryStage.setTitle("Account Setup");
         primaryStage.show();
     }
 }