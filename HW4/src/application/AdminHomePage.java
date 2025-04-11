package application;
 
 
 import javafx.beans.property.SimpleStringProperty;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import QuestionAnswerSystem.Question;
 import QuestionAnswerSystem.QASystemDatabase;
 import QuestionAnswerSystem.Answer;
 import QuestionAnswerSystem.Review;
import QuestionAnswerSystem.User;
import databasePart1.DatabaseHelper;
 import java.sql.SQLException;
 import java.util.ArrayList;
 
 
 /**
  * AdminPage class represents the user interface for the admin user.
  * This page displays a simple welcome message for the admin.
  */
 
 public class AdminHomePage {
 
     
 
     // Constructor to initialize the DatabaseHelper
   
     private final DatabaseHelper databaseHelper;
     private final QASystemDatabase QAdatabase;
 
     public AdminHomePage(DatabaseHelper databaseHelper, QASystemDatabase QAdatabase) {
         this.databaseHelper = databaseHelper;
         this.QAdatabase = QAdatabase;
     }
 
     /**
      * Displays the admin page in the provided primary stage.
      * @param primaryStage The primary stage where the scene will be displayed.
      */
     public void show(Stage primaryStage) {
         VBox layout = new VBox();
         layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
      // ================== DELETE QUESTIONS ==================
         Label qLabel = new Label("All Questions:");
         TableView<ObservableList<String>> questionTable = new TableView<>();
 
         // Fetch question data
         ArrayList<Question> allQuestions = databaseHelper.getAllQuestions(); // You'll add this method in DatabaseHelper
         ObservableList<ObservableList<String>> questionData = FXCollections.observableArrayList();
         for (Question q : allQuestions) {
             ObservableList<String> row = FXCollections.observableArrayList();
             row.add(q.getUserName());
             row.add(q.getInput());
             questionData.add(row);
         }
 
         // Setup columns
         TableColumn<ObservableList<String>, String> qUser = new TableColumn<>("Username");
         qUser.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(0)));
 
         TableColumn<ObservableList<String>, String> qText = new TableColumn<>("Question");
         qText.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
 
         questionTable.getColumns().addAll(qUser, qText);
         questionTable.setItems(questionData);
 
         // Delete Question Button
         Button deleteQButton = new Button("Delete Question");
         deleteQButton.setOnAction(event -> {
             ObservableList<String> selected = questionTable.getSelectionModel().getSelectedItem();
             if (selected != null) {
                 try {
                     databaseHelper.deleteQuestion(selected.get(0), selected.get(1)); // username, input
                     questionData.remove(selected);
                     System.out.println("✅ Question deleted.");
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         });
 
         // Add to layout
         layout.getChildren().addAll(qLabel, questionTable, deleteQButton);
         
      // ================== DELETE ANSWERS ==================
         Label aLabel = new Label("All Answers:");
         TableView<ObservableList<String>> answerTable = new TableView<>();
 
         // Fetch answer data
         ArrayList<Answer> allAnswers = QAdatabase.getAnswers(); // Requires QAdatabase to be passed in constructor
         ObservableList<ObservableList<String>> answerData = FXCollections.observableArrayList();
         for (Answer a : allAnswers) {
             ObservableList<String> row = FXCollections.observableArrayList();
             row.add(a.getUserName());
             row.add(a.getInput());
             answerData.add(row);
         }
 
         // Setup columns
         TableColumn<ObservableList<String>, String> aUser = new TableColumn<>("Username");
         aUser.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(0)));
 
         TableColumn<ObservableList<String>, String> aText = new TableColumn<>("Answer");
         aText.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
 
         answerTable.getColumns().addAll(aUser, aText);
         answerTable.setItems(answerData);
 
         // Delete Answer Button
         Button deleteAButton = new Button("Delete Answer");
         deleteAButton.setOnAction(event -> {
             ObservableList<String> selected = answerTable.getSelectionModel().getSelectedItem();
             if (selected != null) {
                 try {
                     databaseHelper.deleteAnswer(selected.get(0), selected.get(1)); // username, input
                     answerData.remove(selected);
                     System.out.println("✅ Answer deleted.");
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         });
 
         layout.getChildren().addAll(aLabel, answerTable, deleteAButton);
      // ================== DELETE REVIEWS ==================
         Label rLabel = new Label("All Reviews:");
         TableView<ObservableList<String>> reviewTable = new TableView<>();
 
         // Fetch review data
         ArrayList<Review> allReviews = QAdatabase.getReviews(); // Requires QAdatabase to be passed in constructor
         ObservableList<ObservableList<String>> reviewData = FXCollections.observableArrayList();
         for (Review r : allReviews) {
             ObservableList<String> row = FXCollections.observableArrayList();
             row.add(r.getUsername());
             row.add(r.getInput());
             reviewData.add(row);
         }
 
         // Setup columns
         TableColumn<ObservableList<String>, String> rUser = new TableColumn<>("Username");
         rUser.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(0)));
 
         TableColumn<ObservableList<String>, String> rText = new TableColumn<>("Review");
         rText.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().get(1)));
 
         reviewTable.getColumns().addAll(rUser, rText);
         reviewTable.setItems(reviewData);
 
         // Delete Review Button
         Button deleteRButton = new Button("Delete Review");
         deleteRButton.setOnAction(event -> {
             ObservableList<String> selected = reviewTable.getSelectionModel().getSelectedItem();
             if (selected != null) {
                 try {
                     databaseHelper.deleteReview(selected.get(0), selected.get(1)); // username, input
                     reviewData.remove(selected);
                     System.out.println("✅ Review deleted.");
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         });
 
         layout.getChildren().addAll(rLabel, reviewTable, deleteRButton);
 
 
         // Label to display the welcome message for the admin
         Label adminLabel = new Label("Hello, Admin!");
         adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
         layout.getChildren().add(adminLabel);
 
         // Table to display the list of users and their information
        /* TableView<User> userTable = new TableView<>(); // Added by Abdullah
 
         // Table columns for username, email, and role
         TableColumn<User, String> userNameColumn = new TableColumn<>("Username"); // Added by Abdullah
         userNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserName()));
 
         TableColumn<User, String> emailColumn = new TableColumn<>("Email"); // Added by Abdullah
         emailColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
 
         TableColumn<User, String> roleColumn = new TableColumn<>("Role"); // Added by Abdullah
         roleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRole()));
 
         // Add columns to the table
         userTable.getColumns().addAll(userNameColumn, emailColumn, roleColumn); // Added by Abdullah
 
         // Fetch users from the database and populate the table
         ArrayList<ArrayList<String>> data = databaseHelper.getTable();
         ObservableList<ObservableList<String>> users = FXCollections.observableArrayList();
         for(ArrayList<String> row: data) {
         	users.add(FXCollections.observableArrayList(row));
         }
         tb.setItems();*/
         
         TableView userTable = new TableView();
         
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
         userTable.getColumns().addAll(usernames, email, roles);
         userTable.setItems(users);
 
         // Delete user button
         Button deleteButton = new Button("Delete User"); // Added by Abdullah
         deleteButton.setOnAction(event -> {
             ObservableList<String> selection = (ObservableList<String>) userTable.getSelectionModel().getSelectedItem();
             User selectedUser;
 			try {
 				selectedUser = databaseHelper.getUserByUsername(selection.get(1));
 
 			
             if (selectedUser != null) {
                 boolean confirmation = showConfirmationDialog("Are you sure you want to delete " + selectedUser.getUserName() + "?");
                 if (confirmation) {
                     try {
                         databaseHelper.deleteUser(selectedUser.getUserName());
                         data.remove(userTable.getSelectionModel().getSelectedIndex()); // Remove user from the table
                         System.out.println("User " + selectedUser.getUserName() + " deleted successfully.");
                     } catch (SQLException e) {
                         System.err.println("Error deleting user: " + e.getMessage());
                     }
                 }
             } else {
                 System.out.println("No user selected for deletion.");
             }
 			} catch (SQLException e) {
 				e.printStackTrace();
 			}
         });
 
         // Add table and delete button to layout
         layout.getChildren().addAll(userTable, deleteButton); // Added by Abdullah
 
         // Set the scene to primary stage
         Scene adminScene = new Scene(layout, 800, 400);
         primaryStage.setScene(adminScene);
         primaryStage.setTitle("Admin Page");
     }
 
     /**
      * Shows a confirmation dialog before performing an action.
      * @param message The confirmation message.
      * @return true if the user confirms, false otherwise.
      */
     private boolean showConfirmationDialog(String message) { // Added by Abdullah
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Confirmation");
         alert.setHeaderText(message);
         alert.setContentText("This action cannot be undone.");
 
         ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
         ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
 
         alert.getButtonTypes().setAll(yesButton, noButton);
 
         return alert.showAndWait().orElse(noButton) == yesButton;
     }
 }