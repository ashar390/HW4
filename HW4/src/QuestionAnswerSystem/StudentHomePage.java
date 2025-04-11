package QuestionAnswerSystem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import databasePart1.DatabaseHelper;

/**
 * This page displays the student's home page, showing existing questions,
 * allowing students to post new questions and answer existing ones,
 * and providing a filter for resolved answers.
 */
public class StudentHomePage {

    private boolean filter = false;        // If true, only resolved answers are displayed
    private boolean canFilter = false;     // If true, answers exist and can be filtered
    private boolean reviewer = false;      // If the current user has the 'reviewer' role
    private final QASystemDatabase QADatabase; 
    private final User user;              // The currently logged-in user
    private final DatabaseHelper dbHelper;

    /**
     * Constructor for the StudentHomePage.
     * 
     * @param QADatabase The database instance to be used.
     * @param user       The currently logged-in user.
     * @param dbHelper   A helper for additional DB operations.
     */
    public StudentHomePage(QASystemDatabase QADatabase, User user, DatabaseHelper dbHelper) {
        this.QADatabase = QADatabase;
        this.user = user;
        this.dbHelper = dbHelper;
    }

    public void show(Stage primaryStage) {

        // Check if the user has the 'reviewer' role.
        // Instead of User.getRole(), use user.getRole()
        String[] role = user.getRole().strip().split(",");
        System.out.println(Arrays.toString(role) + " length: " + role.length);

        for (String s : role) {
            String trimmed = s.strip();
            System.out.println(trimmed);
            if (trimmed.equals("reviewer")) {
                reviewer = true;
                System.out.println("Reviewer: " + reviewer);
            }
        }
        System.out.println("Reviewer: " + reviewer);

        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label for the list of posted questions.
        Label existingQuestionsLabel = new Label("Questions:");
        layout.getChildren().add(existingQuestionsLabel);

        // Populate the page with questions and answers.
        updatePage(layout);

        // Display a small "Reviewers" label.
        Label reviewerListLabel = new Label("Reviewers:");
        reviewerListLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        layout.getChildren().add(reviewerListLabel);

        // Show reviewer names from QADatabase
        ArrayList<String> reviewers = QADatabase.getAllReviewers();
        for (String r : reviewers) {
            Label reviewerLabel = new Label(r);
            reviewerLabel.setStyle("-fx-font-size: 10px;");
            layout.getChildren().add(reviewerLabel);
        }

        // Display reviewers from DatabaseHelper (table approach)
        Label reviewersLabel = new Label("Reviewers:");
        layout.getChildren().add(reviewersLabel);

        ArrayList<ArrayList<String>> userData = dbHelper.getTable();
        for (ArrayList<String> row : userData) {
            // row structure might be [0: user_id, 1: username, 2: password, 3: email, 4: role]
            String roles = row.get(4).toLowerCase();
            if (roles.contains("reviewer")) {
                Label reviewerName = new Label(row.get(1)); // row[1] is the username
                layout.getChildren().add(reviewerName);
            }
        }

        // TextField for user input
        TextField postQuestion = new TextField("Write your Question here");

        // A "Submit" button to post a new question
        Button submit = new Button("Submit");
        submit.setOnAction(a -> {
            String input = postQuestion.getText();
            if (!input.isEmpty()) {
                try {
                    // Use instance method user.getUserName()
                    Question newQ = new Question(user.getUserName(), input);
                    QADatabase.addQuestion(newQ);
                    System.out.println(user.getUserName() + " posted a new question");

                    // Refresh the page or UI
                    updatePage(layout);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error: Empty question. Please try again");
            }
        });

        // Add the text field and button to the layout
        layout.getChildren().addAll(postQuestion, submit);

        // CheckBox to filter resolved answers
        CheckBox filterCheckbox = new CheckBox("Show only resolved answers");
        filterCheckbox.setOnAction(e -> {
            // Send an error if there is nothing to filter
            if (canFilter) {
                filter = filterCheckbox.isSelected();
                System.out.println("filter checkbox changed");
                updatePage(layout);
            } else {
                System.out.println("Error: nothing to filter");
            }
        });
        layout.getChildren().add(filterCheckbox);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Questions and Answers");
        primaryStage.show();
    }

    /**
     * Rebuilds the portion of the UI that displays questions and their answers.
     */
    public void updatePage(VBox layout) {
        QADatabase.printAnswers(QADatabase.getAnswers());
        VBox questionDisplay = new VBox();
        layout.getChildren().removeIf(node -> node instanceof VBox);

        ArrayList<Question> questions = QADatabase.getQuestions();

        for (Question q : questions) {
            Label ql = new Label("Question: " + q.getInput());
            ql.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            Label ul = new Label("By User: " + q.getUserName());
            ul.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            Button deleteQuestion = new Button("Delete this Question");
            deleteQuestion.setOnAction(e -> {
                try {
                    QADatabase.deleteQuestion(q);
                    System.out.println("Question: " + q.getInput() + " successfully deleted");
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
                updatePage(layout);
            });

            questionDisplay.getChildren().addAll(ql, ul, deleteQuestion);

            // Retrieve the list of answers. If filter is true, only show resolved ones.
            ArrayList<Answer> answers;
            if (filter) {
                ArrayList<Answer> allResolved = QADatabase.getResolvedAnswers();
                answers = new ArrayList<>();
                for (Answer a : allResolved) {
                    if (a.getQuestion().getInput().equals(q.getInput())
                     && a.getQuestion().getUserName().equals(q.getUserName())) {
                        answers.add(a);
                    }
                }
            } else {
                answers = QADatabase.getAnswersbyQuestion(q);
            }

            if (!answers.isEmpty()) {
                canFilter = true;
                Label answerLabel = new Label("Answers");
                answerLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
                questionDisplay.getChildren().add(answerLabel);

                for (Answer ans : answers) {
                    try {
                        // Skip displaying if not resolved and filter is on
                        if (filter && !QADatabase.isResolved(ans)) {
                            continue;
                        }
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    Label answerText = new Label(ans.getInput());
                    answerText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

                    // Display who posted the answer
                    Label answerFrom = new Label("By User: " + ans.getUserName());
                    answerFrom.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

                    // CheckBox to mark an answer as resolving the issue
                    CheckBox resolvedCheckBox = new CheckBox("This resolved my issue?");
                    resolvedCheckBox.setStyle("-fx-font-size: 10px;");
                    resolvedCheckBox.setOnAction(a -> {
                        ans.changeResolve(resolvedCheckBox.isSelected());
                        try {
                            QADatabase.changeResolveAnswer(ans);
                            System.out.println(QADatabase.isResolved(ans));
                            QADatabase.printAnswers(QADatabase.getResolvedAnswers());
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });

                    // Add a button to delete this answer
                    Button deleteAnswer = new Button("Delete this answer");
                    deleteAnswer.setStyle("-fx-font-size: 10px;");
                    deleteAnswer.setOnAction(e -> {
                        try {
                            QADatabase.deleteAnswer(ans);
                            updatePage(layout);
                            System.out.println("Answer deleted: " + ans.getInput());
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });

                    questionDisplay.getChildren().addAll(answerText, answerFrom, resolvedCheckBox, deleteAnswer);

                    // If the current user is a reviewer, display review-related UI
                    if (reviewer) {
                        ArrayList<Review> reviews = QADatabase.getReviewsByAnswer(ans);
                        if (!reviews.isEmpty()) {
                            for (Review r : reviews) {
                                Label reviewText = new Label(r.getInput());
                                reviewText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-alignment: right;");
                                Label reviewFrom = new Label("By Reviewer: " + r.getUserName());
                                reviewFrom.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-alignment: right;");
                                questionDisplay.getChildren().addAll(reviewText, reviewFrom);
                            }
                        }

                        TextField postReview = new TextField("Write your Review here");
                        postReview.setStyle("-fx-font-size: 10px;");
                        Button submitReview = new Button("Submit Review");
                        submitReview.setStyle("-fx-font-size: 10px;");
                        submitReview.setOnAction(c -> {
                            String input = postReview.getText();
                            if (!input.isEmpty()) {
                                // Use the user's actual name
                                Review review = new Review(ans, user.getUserName(), input);
                                try {
                                    QADatabase.addReview(review);
                                    QADatabase.printReviews(QADatabase.getReviews());
                                    updatePage(layout); // Refresh UI
                                    canFilter = true;
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                System.out.println("Error: Empty review. Please try again");
                            }
                        });
                        questionDisplay.getChildren().addAll(postReview, submitReview);
                    }
                }
            }

            // Now we add a "submit answer" UI for each question
            TextField postAnswer = new TextField("Write your answer here");
            postAnswer.setStyle("-fx-font-size: 10px;");
            Button submitAnswer = new Button("Submit Answer");
            submitAnswer.setStyle("-fx-font-size: 10px;");
            // This is the correct place to handle adding an answer
            submitAnswer.setOnAction(c -> {
                String input = postAnswer.getText();
                if (!input.isEmpty()) {
                    // Use the instance user’s name
                    Answer answer = new Answer(q, user.getUserName(), input);
                    try {
                        QADatabase.addAnswer(answer);
                        updatePage(layout); // Refresh UI
                        canFilter = true;
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    System.out.println("Error: Empty answer. Please try again");
                }
            });
            questionDisplay.getChildren().addAll(postAnswer, submitAnswer);
        }

        layout.getChildren().add(questionDisplay);
    }
}
