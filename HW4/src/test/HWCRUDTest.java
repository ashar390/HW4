package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databasePart1.DatabaseHelper;
import QuestionAnswerSystem.Question;
import QuestionAnswerSystem.Answer;
import application.Interaction;
import QuestionAnswerSystem.User;

/**
 * HWCRUDTest
 *
 * Verifies the CRUD operations in the DatabaseHelper that support
 * the following six staff user stories:
 * 
 * 1) View Interaction Dashboard
 * 2) Filter and Sort Interactions
 * 3) View Detailed Interaction History
 * 4) Flag Interactions for Review (in memory)
 * 5) Add Internal Notes (in memory)
 * 6) Search for Interactions
 */
public class HWCRUDTest {

    private DatabaseHelper dbHelper;

    @BeforeEach
    public void setUp() throws SQLException {
        dbHelper = new DatabaseHelper();
        dbHelper.connectToDatabase();
        // Clean the question and answer tables so each test starts fresh.
        try (Statement stmt = dbHelper.getConnection().createStatement()) {
            stmt.execute("DELETE FROM questionTable");
            stmt.execute("DELETE FROM answerTable");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        dbHelper.closeConnection();
    }

    // ----------------------------------------------------------------------
    // User Story 1: View Interaction Dashboard
    // ----------------------------------------------------------------------
    @Test
    public void testDashboardSummary() throws SQLException {
        // Add two questions and one answer
        Question q1 = new Question("Alice", "What is Java?");
        dbHelper.addQuestion(q1);

        Question q2 = new Question("Bob", "How does JDBC work?");
        dbHelper.addQuestion(q2);

        // Add an answer for question1
        Answer a1 = new Answer(q1, "Charlie", "Java is a programming language.");
        dbHelper.addAnswer(a1);

        int questionCount = dbHelper.countQuestions();
        int answerCount = dbHelper.countAnswers();
        int flaggedCount = dbHelper.countFlaggedItems(); // stub returns 0

        assertEquals(2, questionCount, "There should be 2 questions in the DB");
        assertEquals(1, answerCount, "There should be 1 answer in the DB");
        assertEquals(0, flaggedCount, "Flagged items should be 0 initially");
    }

    // ----------------------------------------------------------------------
    // User Story 2: Filter and Sort Interactions
    // ----------------------------------------------------------------------
    @Test
    public void testFilterAndSortInteractions() throws SQLException {
        // Create sample questions and answers
        Question q1 = new Question("Alice", "JavaFX is cool");
        dbHelper.addQuestion(q1);
        Question q2 = new Question("Bob", "Swing is outdated");
        dbHelper.addQuestion(q2);

        Answer a1 = new Answer(q1, "Charlie", "I prefer JavaFX");
        dbHelper.addAnswer(a1);
        Answer a2 = new Answer(q2, "Dave", "Swing is still around");
        dbHelper.addAnswer(a2);

        // Merge questions & answers
        ArrayList<Interaction> allInteractions = dbHelper.getAllInteractions();

        // Filter out only "question" type
        ArrayList<Interaction> questionsOnly = new ArrayList<>();
        for (Interaction i : allInteractions) {
            if ("question".equals(i.getType())) {
                questionsOnly.add(i);
            }
        }
        assertEquals(2, questionsOnly.size(), "Expected 2 question interactions after filtering by 'question' type");

        // Sort them by content in ascending order
        questionsOnly.sort(Comparator.comparing(Interaction::getContent));
        assertTrue(questionsOnly.get(0).getContent().compareTo(questionsOnly.get(1).getContent()) <= 0,
                   "Questions should be sorted in ascending order by content");
    }

    // ----------------------------------------------------------------------
    // User Story 3: View Detailed Interaction History
    // ----------------------------------------------------------------------
    @Test
    public void testDetailedInteractionHistory() throws SQLException {
        // Add a question & answer
        Question q = new Question("Eve", "How to unit test JavaFX?");
        dbHelper.addQuestion(q);

        Answer a = new Answer(q, "Frank", "Use TestFX and JUnit.");
        dbHelper.addAnswer(a);

        // Check merged interactions
        ArrayList<Interaction> interactions = dbHelper.getAllInteractions();
        Interaction foundQ = null;
        for (Interaction i : interactions) {
            if ("question".equals(i.getType()) &&
                i.getUserName().equals("Eve") &&
                i.getContent().equals("How to unit test JavaFX?")) {
                foundQ = i;
                break;
            }
        }
        assertNotNull(foundQ, "Detailed interaction history should include the newly added question");
    }

    // ----------------------------------------------------------------------
    // User Story 4: Flag Interactions (in memory)
    // ----------------------------------------------------------------------
    @Test
    public void testFlagInteraction() {
        // Create an interaction
        Interaction inter = new Interaction("question", "Grace", "What is polymorphism?");
        assertFalse(inter.isFlagged(), "Should not be flagged at creation");

        // Flag it
        inter.setFlagged(true);
        inter.setFlagReason("Inappropriate content");
        assertTrue(inter.isFlagged(), "Should be flagged after setFlagged(true)");
        assertEquals("Inappropriate content", inter.getFlagReason(), "Reason should match");
    }

    // ----------------------------------------------------------------------
    // User Story 5: Add Internal Notes (in memory)
    // ----------------------------------------------------------------------
    @Test
    public void testAddInternalNotes() {
        Interaction inter = new Interaction("answer", "Heidi", "The answer is 42.");
        assertTrue(inter.getNotes().isEmpty(), "No internal notes initially");

        inter.addNote("First internal note");
        inter.addNote("Second note");
        assertEquals(2, inter.getNotes().size(), "Should have 2 internal notes now");
        assertTrue(inter.getNotes().contains("First internal note"), "First note must be present");
        assertTrue(inter.getNotes().contains("Second note"), "Second note must be present");
    }

    // ----------------------------------------------------------------------
    // User Story 6: Search for Interactions
    // ----------------------------------------------------------------------
    @Test
    public void testSearchInteractions() throws SQLException {
        // Add sample question and answers
        Question q1 = new Question("Ivan", "JavaFX supports modern UI");
        dbHelper.addQuestion(q1);
        Question q2 = new Question("Judy", "Swing is older technology");
        dbHelper.addQuestion(q2);

        Answer a1 = new Answer(q1, "Kyle", "I love JavaFX");
        dbHelper.addAnswer(a1);
        Answer a2 = new Answer(q2, "Leo", "Swing is still functional");
        dbHelper.addAnswer(a2);

        ArrayList<Interaction> allInteractions = dbHelper.getAllInteractions();
        String keyword = "javafx";
        ArrayList<Interaction> searchResults = new ArrayList<>();

        for (Interaction i : allInteractions) {
            String userLC = i.getUserName().toLowerCase();
            String contentLC = i.getContent().toLowerCase();
            if (userLC.contains(keyword) || contentLC.contains(keyword)) {
                searchResults.add(i);
            }
        }

        // We expect at least 2 results (Ivan's question and Kyle's answer),
        // since both mention "javafx".
        assertTrue(searchResults.size() >= 2,
                   "Expected at least 2 interactions matching 'javafx' in username or content");
    }
}
