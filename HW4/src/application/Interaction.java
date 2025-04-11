package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a unified "interaction" in the system, which may be either
 * a question or an answer (or potentially feedback in the future).
 * 
 * Used in StaffHomePage to display and filter items in a single table,
 * and to open a detailed view when needed.
 */
public class Interaction {
    
    private String type;       // "question" or "answer" (could be "feedback" if extended)
    private String userName;   // The user who posted this interaction
    private String content;    // The text of the question or answer
    private boolean flagged;
    private String flagReason;

    // -----------------------------
    // Internal notes (User Story #5)
    // -----------------------------
    private List<String> internalNotes = new ArrayList<>();

    /**
     * Constructs an Interaction object.
     *
     * @param type      the type of this interaction ("question", "answer", etc.)
     * @param userName  the username of the user who created this interaction
     * @param content   the textual content (question or answer)
     */
    public Interaction(String type, String userName, String content) {
        this.type = type;
        this.userName = userName;
        this.content = content;
    }

    /**
     * Returns the type of interaction.
     *
     * @return a string such as "question" or "answer"
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the username of the user who posted this interaction.
     *
     * @return a string representing the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the textual content of this interaction.
     *
     * @return the question or answer text
     */
    public String getContent() {
        return content;
    }

    /**
     * Optionally sets or updates the content of this interaction.
     *
     * @param newContent the new text
     */
    public void setContent(String newContent) {
        this.content = newContent;
    }

    /**
     * Optionally sets a new interaction type (e.g., "feedback").
     *
     * @param newType the new type
     */
    public void setType(String newType) {
        this.type = newType;
    }

    /**
     * Optionally sets a different username, if needed.
     *
     * @param newUserName the new username
     */
    public void setUserName(String newUserName) {
        this.userName = newUserName;
    }

    /**
     * Returns whether this interaction is flagged.
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Sets or clears the flagged status in memory.
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * Returns the reason for flagging (if any).
     */
    public String getFlagReason() {
        return flagReason;
    }

    /**
     * Sets the reason for flagging.
     */
    public void setFlagReason(String flagReason) {
        this.flagReason = flagReason;
    }

    // ---------------------------------------
    // User Story #5: Internal (staff-only) notes
    // ---------------------------------------

    /**
     * Adds a private note to this interaction, visible only to staff in memory.
     *
     * @param note The text of the internal note.
     */
    public void addNote(String note) {
        internalNotes.add(note);
    }

    /**
     * Returns a list of all internal notes added for this interaction.
     *
     * @return a List of note strings
     */
    public List<String> getNotes() {
        return internalNotes;
    }
}
