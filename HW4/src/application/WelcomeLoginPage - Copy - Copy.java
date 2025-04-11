package application;
 
 import javafx.scene.Scene;
 import javafx.beans.property.SimpleStringProperty;
 import javafx.collections.ObservableList;
 import javafx.scene.control.*;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import javafx.application.Platform;
 import javafx.beans.binding.StringExpression;
 import javafx.beans.value.ObservableValue;
 import javafx.collections.FXCollections;
 
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.UUID;
 import databasePart1.*;
 import QuestionAnswerSystem.*;
 
 /**
  * The WelcomeLoginPage class displays a welcome screen for authenticated users.
  * It allows users to navigate to their respective pages based on their role or quit the application.
  */
 public class WelcomeLoginPage {
 	
 
 	private final DatabaseHelper databaseHelper;
     private static final QASystemDatabase QAdatabase = new QASystemDatabase();
 
     public WelcomeLoginPage(DatabaseHelper databaseHelper) {
         this.databaseHelper = databaseHelper;
         try {
             QAdatabase.connectToDatabase();
         } catch (SQLException e) {
         	System.out.println(e.getMessage());
         }
         
         
     }
     public void show( Stage primaryStage, User user) {
     	
     	VBox layout = new VBox(5);
 	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
 	    
 	    Label welcomeLabel = new Label("Welcome!!");
 	    welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
 	    layout.getChildren().add(welcomeLabel);
 	    
     	//Edited by Sofia: make a navigation page for users
     	String[] role =user.getRole().strip().split(",");
     	System.out.println(role);
     		
     		System.out.println(Arrays.toString(role));
     		//Added one button per user role
     		for(int i = 0; i < role.length; i++) {
     		    Button roleButton = new Button("Click here to continue to " + role[i] + " page");
 
     		    if(role[i].strip().equals("student")) {
     		        roleButton.setOnAction(b -> {
     		            new StudentHomePage(QAdatabase, user, databaseHelper).show(primaryStage);
     		        });
     		    }
     		    else if(role[i].strip().equals("admin")) {
     		        roleButton.setOnAction(b -> {
     		            new AdminHomePage(databaseHelper, QAdatabase).show(primaryStage);
     		        });
     		    }
     		    else if(role[i].strip().equals("reviewer")) {
     		        roleButton.setOnAction(b -> {
     		            new ReviewerHomePage(QAdatabase, user).show(primaryStage); // ✅ This is the missing part
     		        });
     		    }
     		    else {
     		        roleButton.setOnAction(b -> {
     		            new UserHomePage().show(primaryStage);
     		        });
     		    }
 
     		    layout.getChildren().add(roleButton);
     		}
 
 	    // Button to quit the application
 	    Button quitButton = new Button("Quit");
 	    quitButton.setOnAction(a -> {
 	    	databaseHelper.closeConnection();
 	    	Platform.exit(); // Exit the JavaFX application
 	    });
 	    
 
 
 	    if ("admin".equals(user.getRole())) {
 		    // "Invite" button for admin to generate invitation codes
             Button inviteButton = new Button("Invite");
             inviteButton.setOnAction(a -> {
                 new InvitationPage().show(databaseHelper, primaryStage);
             });
 
             //Added by Sofia: "One-time Password" button users who have forgotten their password
             Label otPassword = new Label("Generate a one-time password");
             TextField usernameField = new TextField();
             usernameField.setPromptText("Enter user here");
             usernameField.setMaxWidth(250);
             
             Button generate = new Button("Generate Password");
             layout.getChildren().addAll(otPassword, usernameField, generate);
             
             generate.setOnAction(a -> {
             	String userInput = usernameField.getText();
             	try {
             		User u = databaseHelper.getUserByUsername(userInput);
             		String newPW = databaseHelper.generateInvitationCode(u.getRole()); // Generate a random 4-character code (like the invite code)
             		u.setPassword(newPW);
             		databaseHelper.updateUser(u);
             		System.out.println(u.getPassword());
             		Label newPWLabel = new Label("The new password for " + userInput + "is " + newPW);
             		System.out.println(databaseHelper.getTable().toString());
             		layout.getChildren().add(newPWLabel);
             	}catch(SQLException e){
             		e.printStackTrace();
             	}
             });
             
             //Added by Sofia: Displaying the list of users in TableView
             
             TableView tb = new TableView();
             tb.setEditable(true);
             
             //Add applicable fields and set them to columns
             ArrayList<ArrayList<String>> data = databaseHelper.getTable();
             ObservableList<ObservableList<String>> users = FXCollections.observableArrayList();
             for(ArrayList<String> row: data) {
             	users.add(FXCollections.observableArrayList(row));
             }
             
             TableColumn<ObservableList<String>, String> usernames = new TableColumn<>("Username");
             usernames.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
 
             
             TableColumn<ObservableList<String>, String> email = new TableColumn("Email Address");     
             email.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(3)));
             
             TableColumn<ObservableList<String>, String> roles = new TableColumn<>("Role(s)");
             roles.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(4)));
 
             //Set the table's items to the corresponding columns
             tb.getColumns().addAll(usernames, email, roles);
             tb.setItems(users);
             System.out.println(databaseHelper.getTable().toString());
             
             layout.getChildren().addAll(inviteButton, tb);
         }
 	    layout.getChildren().add(quitButton);
 	    Scene welcomeScene = new Scene(layout, 800, 400);
 
 	    // Set the scene to primary stage
 	    primaryStage.setScene(welcomeScene);
 	    primaryStage.setTitle("Welcome Page");
     }
     
     
 }