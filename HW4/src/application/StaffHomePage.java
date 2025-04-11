package application;

import databasePart1.DatabaseHelper;
import QuestionAnswerSystem.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * StaffHomePage
 *
 * Now includes:
 *  - Filter Interactions by type (All, question, answer)
 *  - Sort Interactions by content (asc/desc)
 *  - Flag Interactions
 *  - Add Internal Notes
 *  - Search Interactions by keyword in username or content (User Story #6)
 */
public class StaffHomePage {

    private DatabaseHelper dbHelper;
    private User currentUser;

    // Filter & Sorting
    private ComboBox<String> filterCombo;
    private Button sortButton;
    private boolean ascending = true;

    // Search
    private TextField searchField;
    private Button searchButton;

    private TableView<Interaction> interactionTable;

    public StaffHomePage(DatabaseHelper dbHelper, User staffUser) {
        this.dbHelper = dbHelper;
        this.currentUser = staffUser;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-alignment: center;");

        // Dashboard summary
        Label titleLabel = new Label("Staff Dashboard");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // Basic summary
        Label summaryLabel = new Label(
            "Total Questions: " + dbHelper.countQuestions() + "\n" +
            "Total Answers:   " + dbHelper.countAnswers() + "\n" +
            "Flagged Items:   " + dbHelper.countFlaggedItems()
        );
        summaryLabel.setStyle("-fx-font-size: 14;");

        Button refreshButton = new Button("Refresh Dashboard");
        refreshButton.setOnAction(e -> {
            summaryLabel.setText(
                "Total Questions: " + dbHelper.countQuestions() + "\n" +
                "Total Answers:   " + dbHelper.countAnswers() + "\n" +
                "Flagged Items:   " + dbHelper.countFlaggedItems()
            );
            refreshInteractions();
        });

        // Create the UI row for filter, search, sort
        HBox topControls = createTopControls();

        // Table of interactions
        interactionTable = createInteractionTable();
        refreshInteractions();

        layout.getChildren().addAll(
            titleLabel, summaryLabel, refreshButton,
            topControls, interactionTable
        );

        Scene scene = new Scene(layout, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Staff Dashboard");
        primaryStage.show();
    }

    /**
     * Creates the top row controls for filtering, searching, and sorting.
     */
    private HBox createTopControls() {
        // Filter
        filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll("All", "question", "answer");
        filterCombo.setValue("All");
        filterCombo.setOnAction(e -> refreshInteractions());

        Label filterLabel = new Label("Filter by Type:");

        // Sort
        sortButton = new Button("Sort Asc/Desc");
        sortButton.setOnAction(e -> sortInteractions());

        // Search
        searchField = new TextField();
        searchField.setPromptText("Search username or content");
        searchButton = new Button("Search");
        searchButton.setOnAction(e -> refreshInteractions());

        HBox box = new HBox(10,
            filterLabel, filterCombo,
            new Label("Search:"), searchField, searchButton,
            sortButton
        );
        box.setStyle("-fx-alignment: center;");
        return box;
    }

    /**
     * Builds a TableView with columns: Type, User, Content,
     * plus columns for Flagging and Adding Notes.
     */
    private TableView<Interaction> createInteractionTable() {
        TableView<Interaction> table = new TableView<>();

        TableColumn<Interaction, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType())
        );

        TableColumn<Interaction, String> colUser = new TableColumn<>("User");
        colUser.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUserName())
        );

        TableColumn<Interaction, String> colContent = new TableColumn<>("Content");
        colContent.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getContent())
        );

        // "Flag" column
        TableColumn<Interaction, Void> colFlag = new TableColumn<>("Flag");
        colFlag.setCellFactory(col -> new TableCell<Interaction, Void>() {
            private final Button flagButton = new Button("Flag");

            {
                flagButton.setOnAction(e -> {
                    Interaction item = getTableView().getItems().get(getIndex());
                    handleFlagInteraction(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(flagButton);
                }
            }
        });

        // "Add Note" column
        TableColumn<Interaction, Void> colNotes = new TableColumn<>("Notes");
        colNotes.setCellFactory(col -> new TableCell<Interaction, Void>() {
            private final Button noteButton = new Button("Add Note");

            {
                noteButton.setOnAction(e -> {
                    Interaction item = getTableView().getItems().get(getIndex());
                    handleAddNote(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(noteButton);
                }
            }
        });

        table.getColumns().addAll(colType, colUser, colContent, colFlag, colNotes);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Highlight flagged rows
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Interaction interaction, boolean empty) {
                super.updateItem(interaction, empty);
                if (interaction == null || empty) {
                    setStyle("");
                } else {
                    if (interaction.isFlagged()) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        return table;
    }

    /**
     * Toggles ascending/descending sort by content.
     */
    private void sortInteractions() {
        ObservableList<Interaction> currentItems = interactionTable.getItems();
        if (currentItems == null || currentItems.isEmpty()) return;

        if (ascending) {
            currentItems.sort(Comparator.comparing(Interaction::getContent));
        } else {
            currentItems.sort(Comparator.comparing(Interaction::getContent).reversed());
        }
        ascending = !ascending;
        interactionTable.setItems(currentItems);
    }

    /**
     * Called whenever we need to re-fetch or filter the interactions.
     * Applies 'type' filter + search term filter, then sets the table items.
     */
    private void refreshInteractions() {
        ArrayList<Interaction> all = dbHelper.getAllInteractions();

        String selectedFilter = filterCombo.getValue();     // "All", "question", or "answer"
        String keyword = searchField.getText().trim();      // e.g., "abc"

        ArrayList<Interaction> filtered = new ArrayList<>();
        for (Interaction item : all) {
            // 1) Filter by type
            boolean matchesType = "All".equals(selectedFilter) || item.getType().equals(selectedFilter);

            // 2) Filter by search
            // For a case-insensitive match, you can do .toLowerCase().contains(keyword.toLowerCase())
            boolean matchesKeyword = true;  // default to true if no search is typed
            if (!keyword.isEmpty()) {
                // item must match keyword in username or content
                String usernameLC = item.getUserName().toLowerCase();
                String contentLC  = item.getContent().toLowerCase();
                String keyLC      = keyword.toLowerCase();

                matchesKeyword = usernameLC.contains(keyLC) || contentLC.contains(keyLC);
            }

            if (matchesType && matchesKeyword) {
                filtered.add(item);
            }
        }

        ObservableList<Interaction> data = FXCollections.observableArrayList(filtered);
        interactionTable.setItems(data);
    }

    /**
     * Prompt staff for a flag reason, then set/clear the flagged status.
     */
    private void handleFlagInteraction(Interaction interaction) {
        if (!interaction.isFlagged()) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Flag Interaction");
            dialog.setHeaderText("Flag this " + interaction.getType());
            dialog.setContentText("Enter reason (optional):");
            dialog.showAndWait().ifPresent(reason -> {
                interaction.setFlagged(true);
                interaction.setFlagReason(reason);
                refreshInteractions();
            });
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Unflag?");
            confirm.setHeaderText("Already flagged: " + interaction.getFlagReason());
            confirm.setContentText("Do you want to unflag?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    interaction.setFlagged(false);
                    interaction.setFlagReason(null);
                    refreshInteractions();
                }
            });
        }
    }

    /**
     * Prompt staff for an internal note and attach it to the Interaction object.
     */
    private void handleAddNote(Interaction interaction) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Internal Note");
        dialog.setHeaderText("Add a note for this " + interaction.getType());
        dialog.setContentText("Note content:");
        dialog.showAndWait().ifPresent(note -> {
            interaction.addNote(note);
            System.out.println("Note added: " + note);

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Note Added");
            info.setHeaderText("Current Notes for this " + interaction.getType() + ":");
            StringBuilder sb = new StringBuilder();
            for (String s : interaction.getNotes()) {
                sb.append("- ").append(s).append("\n");
            }
            info.setContentText(sb.toString());
            info.showAndWait();
        });
    }
}
