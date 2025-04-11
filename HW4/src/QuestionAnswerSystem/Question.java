package QuestionAnswerSystem;
 
 /**
  * The {@code Question} class represents a question submitted by a user.
  * It includes the question's text, the username of the person who submitted it,
  * and a collection of answers associated with it.
  */
 public class Question {
 	private String username;
 	private String input;
 	private Answers answers = new Answers(this);
 
 	/**
 	 * Constructs a new Question object.
 	 *
 	 * @param username the name of the user who asked the question
 	 * @param input the content of the question
 	 */
 	public Question(String username, String input) {
 		this.username = username;
 		this.input = input;
 	}
 
 	/**
 	 * Gets the username of the person who submitted the question.
 	 *
 	 * @return the username
 	 */
 	public String getUserName() {
 		return username;
 	}
 
 	/**
 	 * Gets the text/content of the question.
 	 *
 	 * @return the question input
 	 */
 	public String getInput() {
 		return input;
 	}
 
 	/**
 	 * Retrieves the list of answers associated with this question.
 	 *
 	 * @return the Answers object containing answers
 	 */
 	public Answers getAnswers() {
 		return answers;
 	}
 
 	/**
 	 * Sets or updates the text of the question.
 	 *
 	 * @param input the new input for the question
 	 */
 	public void setInput(String input) {
 		this.input = input;
 	}
 }