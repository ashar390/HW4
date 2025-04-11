package QuestionAnswerSystem;
 
 import java.util.ArrayList;
 
 /**
  * The {@code Answers} class manages a list of {@link Answer} objects associated with a single {@link Question}.
  * It provides methods to perform basic CRUD (Create, Read, Update, Delete) operations on the list of answers.
  */
 public class Answers {
 
 	/** The list of answers associated with the question. */
 	private ArrayList<Answer> answers = new ArrayList<Answer>();
 
 	/** The question to which these answers are related. */
 	private Question question;
 
 	/**
 	 * Constructs an {@code Answers} object associated with a specific question.
 	 *
 	 * @param question the question related to this group of answers
 	 */
 	public Answers(Question question) {
 		this.question = question;
 	}
 
 	/**
 	 * Adds a new answer to the list.
 	 *
 	 * @param answer the {@code Answer} to add
 	 */
 	public void addAnswer(Answer answer) {
 		answers.add(answer);
 	}
 
 	/**
 	 * Returns the answer at the specified index in the list.
 	 *
 	 * @param i the index of the answer to retrieve
 	 * @return the {@code Answer} at the specified position
 	 */
 	public Answer getAnswer(int i) {
 		return answers.get(i);
 	}
 
 	/**
 	 * Returns the question associated with this collection of answers.
 	 *
 	 * @return the {@code Question} object
 	 */
 	public Question getQuestion() {
 		return question;
 	}
 
 	/**
 	 * Removes a specific answer from the list.
 	 *
 	 * @param answer the {@code Answer} to remove
 	 */
 	public void removeAnswer(Answer answer) {
 		answers.remove(answer);
 	}
 
 	/**
 	 * Checks whether the list of answers is empty.
 	 *
 	 * @return {@code true} if no answers exist, {@code false} otherwise
 	 */
 	public boolean isEmpty() {
 		return answers.size() == 0;
 	}
 
 	/**
 	 * Returns the number of answers in the list.
 	 *
 	 * @return the total number of {@code Answer} objects
 	 */
 	public int getSize() {
 		return answers.size();
 	}
 }