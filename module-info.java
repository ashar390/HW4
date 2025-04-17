module HW4 {
    // JavaFX
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    // JDBC
    requires java.sql;

    // JUnit 5 API for your test package
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.junit.platform.engine;
    requires org.junit.platform.launcher;


    // Your own packages—export any you want to document or use reflectively
    exports application;
    exports databasePart1;
    exports QuestionAnswerSystem;

    // Tests live in "test" and need to be opened to JUnit
    opens test to org.junit.jupiter.api;
    
    // JavaFX reflection for FXML
    opens application to javafx.graphics, javafx.fxml;
    }
