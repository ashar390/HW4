package QuestionAnswerSystem;
 
 import java.sql.SQLException;
 import java.util.ArrayList;
 
 import QuestionAnswerSystem.User;
 import javafx.scene.Scene;
 import javafx.scene.control.Label;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import javafx.scene.control.Button;
 import javafx.scene.layout.HBox;
 
 
 /**
  * This class represents the home page for a reviewer in the Question Answer System application.
  * It displays a list of reviews submitted by the currently logged-in reviewer.
  * Each review includes the original answer and the original question it was reviewing.
  */
 public class ReviewerHomePage {
 
     /** Instance of the QASystemDatabase used to retrieve data. */
     private final QASystemDatabase QADatabase;
 
     /** The currently logged-in user (assumed to have a reviewer role). */
     private QuestionAnswerSystem.User user;
 
     /**
      * Constructs a ReviewerHomePage with the provided database and user.
      *
      * @param QADatabase The database instance used to fetch reviews.
      * @param user The user currently logged in (assumed to be a reviewer).
      */
     public ReviewerHomePage(QASystemDatabase QADatabase, QuestionAnswerSystem.User user) {
         this.user = user;
         this.QADatabase = QADatabase;
     }
 
     /**
      * Displays the reviewer's home page in the provided primaryStage.
      * It shows a list of reviews made by the user along with the corresponding
      * original answer and question for each review.
      *
      * @param primaryStage The JavaFX stage where the home page UI will be displayed.
      */
     public void show(Stage primaryStage) {
         VBox layout = new VBox(10);
         layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
 
         // Reviewer identity
         Label existingQuestionsLabel = new Label("Reviews from " + user.getUserName());
         layout.getChildren().add(existingQuestionsLabel);
 
         // ================== NEW PART: Write Reviews on Any Answer ==================
         Label answersLabel = new Label("Answers to Review:");
         answersLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
         layout.getChildren().add(answersLabel);
 
         ArrayList<Answer> allAnswers = QADatabase.getAnswers();
         for (Answer a : allAnswers) {
             Label answerText = new Label("Answer by " + a.getUserName() + ": " + a.getInput());
             answerText.setStyle("-fx-font-size: 12px;");
 
             Label questionText = new Label("Question: " + a.getQuestion().getInput());
             questionText.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");
 
             javafx.scene.control.TextField reviewInput = new javafx.scene.control.TextField();
             reviewInput.setPromptText("Write your review here...");
 
             Button submitReviewBtn = new Button("Submit Review");
             submitReviewBtn.setOnAction(e -> {
                 String reviewText = reviewInput.getText().trim();
                 if (!reviewText.isEmpty()) {
                     try {
                         QADatabase.addReview(new Review(a, user.getUserName(), reviewText));
                         reviewInput.clear();
                         show(primaryStage); // Refresh
                     } catch (Exception ex) {
                         ex.printStackTrace();
                     }
                 }
             });
 
             VBox answerBlock = new VBox(5, answerText, questionText, reviewInput, submitReviewBtn);
             answerBlock.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1;");
             layout.getChildren().add(answerBlock);
         }
         // =========================================================================
 
         // ================== OLD REVIEWS SECTION ==================
         ArrayList<Review> reviews = QADatabase.getReviewsByUser(user.getUserName());
         System.out.println("Total reviews fetched: " + reviews.size());
         if (!reviews.isEmpty()) {
             for (Review r : reviews) {
                 Label review = new Label(r.getInput());
                 review.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
 
                 Label answer = new Label("Original answer: " + r.getAnswer().getInput());
                 answer.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
 
                 Label question = new Label("Original question: " + r.getAnswer().getQuestion().getInput());
                 question.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
 
                 Button deleteButton = new Button("Delete");
                 deleteButton.setOnAction(e -> {
                     try {
                         QADatabase.deleteReview(user.getUserName(), r.getInput());
                         show(primaryStage); // Refresh the page
                     } catch (SQLException ex) {
                         ex.printStackTrace();
                     }
                 });
 
                 VBox reviewDetails = new VBox(3, review, answer, question);
                 HBox reviewBlock = new HBox(10, reviewDetails, deleteButton);
                 layout.getChildren().add(reviewBlock);
             }
         }
         // ==========================================================
 
         primaryStage.setScene(new Scene(layout, 800, 600));
         primaryStage.setTitle("Questions and Answers");
         primaryStage.show();
     }
 }