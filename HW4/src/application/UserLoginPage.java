package application;
 
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 
 import java.sql.SQLException;

import QuestionAnswerSystem.User;
import databasePart1.*;
 
 /**
  * The UserLoginPage class provides a login interface for users to access their accounts.
  * It validates the user's credentials and navigates to the appropriate page upon successful login.
  */
 public class UserLoginPage {
 
     private final DatabaseHelper databaseHelper;
     
     public UserLoginPage(DatabaseHelper databaseHelper) {
         this.databaseHelper = databaseHelper;
     }
 
     public void show(Stage primaryStage) {
         // Input field for the user's username/email and password
         TextField userField = new TextField();
         userField.setPromptText("Enter Username or Email");
         userField.setMaxWidth(250);
 
         PasswordField passwordField = new PasswordField();
         passwordField.setPromptText("Enter Password");
         passwordField.setMaxWidth(250);
         
         // Label to display error messages
         Label errorLabel = new Label();
         errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
 
         Button loginButton = new Button("Login");
 
         loginButton.setOnAction(a -> {
             // Retrieve user inputs
             String userInput = userField.getText();
             String password = passwordField.getText();
 
             try {
                 User user = null;
 
                 // Check if input is email or username
                 if (userInput.contains("@")) {
                     user = databaseHelper.getUserByEmail(userInput); // Fetch user by email
                 } else {
                     user = databaseHelper.getUserByUsername(userInput); // Fetch user by username
                     System.out.println(user.getUserName() + "\n");
                 }
   
                 // Validate user credentials
                 
                 //Edited by Sofia: adding one-time password feature
                 if(!databaseHelper.validateInvitationCode(password)) {
 	                if (user != null && user.getPassword().equals(password)){
 	                    WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
 	
 	                    // Navigate based on user role
 	                    welcomeLoginPage.show(primaryStage, user);
 	                    System.out.println(user.getUserName() + "\n");
 	                }
                 }else {
                     // Display an error if the login fails
                 	System.out.println(user.getPassword());
                     errorLabel.setText("Invalid username, email or password.");
                 }
 
             } catch (SQLException e) {
                 System.err.println("Database error: " + e.getMessage());
                 e.printStackTrace();
             }
         });
         VBox layout = new VBox(10);
         layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
         layout.getChildren().addAll(userField, passwordField, loginButton, errorLabel);
 
         primaryStage.setScene(new Scene(layout, 800, 400));
         primaryStage.setTitle("User Login");
         primaryStage.show();
     }
 }