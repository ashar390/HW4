package QuestionAnswerSystem;
 
 import java.util.ArrayList;
 
 /**
  * The {@code Questions} class manages a list of {@code Question} objects.
  * It provides basic CRUD operations such as adding, removing, and retrieving questions.
  */
 public class Questions {
 	private ArrayList<Question> questions;
 
 	/**
 	 * Constructs a new {@code Questions} object with an empty list of questions.
 	 */
 	public Questions() {
 		questions = new ArrayList<Question>();
 	}
 
 	/**
 	 * Retrieves a question from the list by index.
 	 *
 	 * @param i the index of the question to retrieve
 	 * @return the {@code Question} at the specified index
 	 */
 	public Question getQ(int i) {
 		return questions.get(i);
 	}
 
 	/**
 	 * Adds a question to the list.
 	 *
 	 * @param question the {@code Question} to add
 	 */
 	public void add(Question question) {
 		questions.add(question);
 	}
 
 	/**
 	 * Removes a question from the list.
 	 *
 	 * @param question the {@code Question} to remove
 	 */
 	public void remove(Question question) {
 		questions.remove(question);
 	}
 
 	/**
 	 * Gets the total number of questions in the list.
 	 *
 	 * @return the size of the questions list
 	 */
 	public int getQLength() {
 		return questions.size();
 	}
 
 	/**
 	 * Checks if the list of questions is empty.
 	 *
 	 * @return {@code true} if the list is empty, otherwise {@code false}
 	 */
 	public boolean isEmpty() {
 		return questions.size() == 0;
 	}
 }