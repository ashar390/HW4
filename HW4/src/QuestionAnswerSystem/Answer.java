package QuestionAnswerSystem;

/**
 * The {@code Answer} class represents an answer submitted by a user
 * in response to a {@link Question}. It contains the answer content,
 * the username of the person who submitted it, and a flag indicating
 * whether the answer has resolved the issue.
 */
public class Answer {

    /** The question to which this answer responds. */
    private Question question;

    /** The username of the person who provided the answer. */
    private String username;

    /** The actual content of the answer. */
    private String input;

    /** Flag indicating whether this answer has resolved the question. */
    private boolean resolved;

    /**
     * Constructs a new {@code Answer} object without specifying a {@code Question}.
     * 
     * @param username the name of the user who submitted the answer
     * @param input    the content of the answer
     */
    public Answer(String username, String input) {
        // We do NOT set this.question here, because there's no question param
        this.username = username;
        this.input = input;
        this.resolved = false;
    }

    /**
     * Constructs a new {@code Answer} object with an associated {@code Question}.
     *
     * @param question the question to which this answer responds
     * @param username the name of the user who submitted the answer
     * @param input    the content of the answer
     */
    public Answer(Question question, String username, String input) {
        this.question = question;
        this.username = username;
        this.input = input;
        this.resolved = false;
    }

    /**
     * Returns the username of the user who submitted this answer.
     *
     * @return the username as a {@code String}
     */
    public String getUserName() {
        return username;
    }

    /**
     * Returns the question associated with this answer.
     *
     * @return the {@code Question} object
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Returns the content of the answer.
     *
     * @return the answer input as a {@code String}
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the resolved status of this answer.
     *
     * @param r {@code true} if the answer resolves the question, {@code false} otherwise
     */
    public void changeResolve(boolean r) {
        resolved = r;
    }

    /**
     * Checks whether this answer has resolved the question.
     *
     * @return {@code true} if resolved, {@code false} otherwise
     */
    public boolean isResolved() {
        return resolved;
    }

    /**
     * Sets or updates the input (content) of the answer.
     *
     * @param input the new answer content
     */
    public void setInput(String input) {
        this.input = input;
    }
}
