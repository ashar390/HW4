package application;
 
 import java.util.ArrayList;
 import databasePart1.*;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.control.CheckBox;
 import javafx.scene.control.Label;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 
 /**
  * InvitePage class represents the page where an admin can generate an invitation code.
  * The invitation code is displayed upon clicking a button.
  */
 
 public class InvitationPage {
 
 	/**
      * Displays the Invite Page in the provided primary stage.
      * 
      * @param databaseHelper An instance of DatabaseHelper to handle database operations.
      * @param primaryStage   The primary stage where the scene will be displayed.
      */
 	String user_roles = "";
     public void show(DatabaseHelper databaseHelper,Stage primaryStage) {
     	VBox layout = new VBox();
 	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
 	    
 	    // Label to display the title of the page
 	    Label userLabel = new Label("Invite ");
 	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
 	    
 	    //Check boxes to select which roles the new user will have-- student, instructor, staff, reviewer
 	    //An arraylist is used so that if we want to add a new role it will be easier to implement
 	    layout.getChildren().add(new Label("Select which roles you would like the invited user to have:"));
 	    
 	    ArrayList<String> roles = new ArrayList<String>();
 	    roles.add("student");
 	    roles.add("instructor");
 	    roles.add("staff");
 	    roles.add("reviewer");
 	    
 	    for(int i = 0; i < roles.size(); i++) {
 	    	CheckBox cb = new CheckBox(roles.get(i));
 	    	layout.getChildren().add(cb);
 	    	String r = roles.get(i);
 	    	cb.setOnAction(a -> {
 	    		user_roles += r + ", ";
 	    		System.out.println(user_roles);
 	    		
 	    	});
 	    }
 	    
 	    
 	    // Button to generate the invitation code
 	    Button showCodeButton = new Button("Generate Invitation Code");
 	    
 	    // Label to display the generated invitation code
 	    Label inviteCodeLabel = new Label(""); ;
         inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
         
         showCodeButton.setOnAction(a -> {
         	// Generate the invitation code using the databaseHelper and set it to the label
             String invitationCode = databaseHelper.generateInvitationCode(user_roles);
             inviteCodeLabel.setText(invitationCode);
             System.out.println(invitationCode);
         });
 	    
 
         layout.getChildren().addAll(userLabel, showCodeButton, inviteCodeLabel);
 	    Scene inviteScene = new Scene(layout, 800, 400);
 
 	    // Set the scene to primary stage
 	    primaryStage.setScene(inviteScene);
 	    primaryStage.setTitle("Invite Page");
     	
     }
 }