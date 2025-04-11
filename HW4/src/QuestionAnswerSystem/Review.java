package QuestionAnswerSystem;
 
 public class Review {
 	private Answer answer;
 	private String username;
 	private String input;
 	private String reviewerName;
 	
 	public Review(Answer answer, String username, String input) {
 		this.answer = answer;
 		this.username = username;
 		this.input = input;
 	}
 	
 	public Answer getAnswer() {
 		return answer;
 	}
 	
 	public String getUsername() {
 		return username;
 	}
 	
 	public String getInput() {
 		return input;
 	}
 
    public String getUserName() {
        return reviewerName;
    }
	
	}
 	
 