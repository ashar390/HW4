module HW4 {
    // JavaFX modules
    requires javafx.controls;
    requires java.sql;
    requires javafx.base;

    // JUnit Jupiter (JUnit 5) module
    requires org.junit.jupiter.api;

    // Make the 'application' package accessible via reflection for JavaFX
    opens application to javafx.graphics, javafx.fxml, javafx.base;
}
