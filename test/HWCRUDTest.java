package test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import databasePart1.DatabaseHelper;
import QuestionAnswerSystem.Question;
import QuestionAnswerSystem.Answer;
import application.Interaction;

/**
 * HWCRUDTest
 *
 * Verifies the CRUD operations and in-memory behaviors supporting six staff user stories.
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
        }
    }

    @AfterEach
    public void tearDown() {
        dbHelper.closeConnection();
    }

    // ----------------------------------------------------------------------
    // User Story 1: Flag Interactions (in memory)
    // ----------------------------------------------------------------------
    
    @Test
    public void testFlagInteraction() {
        Interaction inter = new Interaction("question","Grace","What is polymorphism?");
        assertFalse(inter.isFlagged(), "Not flagged at creation");

        inter.setFlagged(true);
        inter.setFlagReason("Inappropriate");
        assertTrue(inter.isFlagged(), "Flagged after setFlagged(true)");
        assertEquals("Inappropriate", inter.getFlagReason());
    }

    // ----------------------------------------------------------------------
    // User Story 2: Add Internal Notes (in memory)
    // ----------------------------------------------------------------------
    
    @Test
    public void testAddInternalNotes() {
        Interaction inter = new Interaction("answer","Heidi","The answer is 42.");
        assertTrue(inter.getNotes().isEmpty(), "No notes initially");

        inter.addNote("First note");
        inter.addNote("Second note");
        assertEquals(2, inter.getNotes().size());
        assertTrue(inter.getNotes().contains("First note"));
        assertTrue(inter.getNotes().contains("Second note"));
    }

    // ----------------------------------------------------------------------
    // User Story 3: Mark for Follow-up (in memory)
    // ----------------------------------------------------------------------
    
    @Test
    public void testMarkForFollowUp() {
        Interaction inter = new Interaction("question","Grace","Follow-up?");
        assertFalse(inter.isFollowUpRequired(), "Should not require follow-up at creation");

        inter.setFollowUpRequired(true);
        assertTrue(inter.isFollowUpRequired(), "Should require follow-up once set");

        inter.setFollowUpRequired(false);
        assertFalse(inter.isFollowUpRequired(), "Should no longer require follow-up after clearing");
    }
    
    // ----------------------------------------------------------------------
    // User Story 4: Assign a Priority Level (in memory)
    // ----------------------------------------------------------------------

    @Test
    public void testSetPriorityLevel() {
        Interaction inter = new Interaction("answer","Heidi","Priority test");
        assertNull(inter.getPriority(), "Priority should start null unless defaulted");

        inter.setPriority("Low");
        assertEquals("Low", inter.getPriority());
        inter.setPriority("Medium");
        assertEquals("Medium", inter.getPriority());
        inter.setPriority("High");
        assertEquals("High", inter.getPriority());
    }
    
    // ----------------------------------------------------------------------
    // User Story 5: Add Internal Tags (in memory)
    // ----------------------------------------------------------------------

    @Test
    public void testAddInternalTags() {
        Interaction inter = new Interaction("question","Ivan","Tagging");
        assertTrue(inter.getTags().isEmpty(), "No tags at creation");

        inter.addTag("billing");
        inter.addTag("UX");
        inter.addTag("billing"); // duplicate should be ignored

        assertEquals(2, inter.getTags().size(), "Should only have two unique tags");
        assertTrue(inter.getTags().contains("billing"));
        assertTrue(inter.getTags().contains("UX"));
    }
    
    // ----------------------------------------------------------------------
    // User Story 6: Log an Audit Note on Flag (in memory)
    // ----------------------------------------------------------------------

    @Test
    public void testAutoAuditNoteOnFlag() {
        Interaction inter = new Interaction("answer","Judy","Audit log");
        assertTrue(inter.getNotes().isEmpty(), "No internal notes initially");

        inter.setFlagged(true);
        List<String> notes = inter.getNotes();
        assertFalse(notes.isEmpty(), "There should be at least one audit note after flagging");
        String first = notes.get(0);
        assertTrue(first.contains("flagged by Judy"),
                   "Audit note should mention the user who flagged: " + first);
        assertTrue(first.matches("^\\[\\d{4}-\\d{2}-\\d{2}.*\\].*"),
                   "Audit note should begin with a timestamp in [YYYY-MM-DD …] format");
    }
}
