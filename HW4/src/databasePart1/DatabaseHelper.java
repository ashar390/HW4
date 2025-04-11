package databasePart1;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import QuestionAnswerSystem.Question;
import QuestionAnswerSystem.User;
import QuestionAnswerSystem.Answer;
import application.Interaction;

/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation,
 * and handling invitation codes.
 */
public class DatabaseHelper {

    // JDBC driver name and database URL 
    static final String JDBC_DRIVER = "org.h2.Driver";   
    static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

    //  Database credentials 
    static final String USER = "sa"; 
    static final String PASS = ""; 

    private Connection connection = null;
    private Statement statement = null; 

    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER); // Load the JDBC driver
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement(); 
            // statement.execute("DROP ALL OBJECTS"); // Uncomment to reset DB if desired
            createTables();  // Create the necessary tables if they don't exist
            System.out.println(getTable().toString());
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    private void createTables() throws SQLException {
        // The user table
        String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "userName VARCHAR(255) UNIQUE, "
                + "password VARCHAR(255), "
                + "email VARCHAR(255), "
                + "role VARCHAR(200))";
        statement.execute(userTable);

        // The invitation codes table
        String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
                + "code VARCHAR(10) PRIMARY KEY, "
                + "isUsed BOOLEAN DEFAULT FALSE, "
                + "roles VARCHAR(255))";
        statement.execute(invitationCodesTable);

        // questionTable
        String createQuestionTable = "CREATE TABLE IF NOT EXISTS questionTable ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "userName VARCHAR(255), "
                + "input VARCHAR(1000))";
        statement.execute(createQuestionTable);

        // answerTable
        String createAnswerTable = "CREATE TABLE IF NOT EXISTS answerTable ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "userName VARCHAR(255), "
                + "input VARCHAR(1000))";
        statement.execute(createAnswerTable);

        // reviewTable
        String createReviewTable = "CREATE TABLE IF NOT EXISTS reviewTable ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "userName VARCHAR(255), "
                + "input VARCHAR(1000))";
        statement.execute(createReviewTable);
    }

    // Check if the database is empty
    public boolean isDatabaseEmpty() throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM cse360users";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("count") == 0;
        }
        return true;
    }

    // Registers a new user in the database.
    public void register(User user) throws SQLException {
        String insertUser = "INSERT INTO cse360users (username, password, email, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole());
            pstmt.executeUpdate();
        }
    }

    public void updateUser(User user) throws SQLException {
        String updateUser = "UPDATE cse360users SET password = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateUser)) {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getUserName());
            pstmt.executeUpdate();
        }
    }

    // Validates a user's login credentials.
    public boolean login(User user) throws SQLException {
        String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Checks if a user already exists in the database based on userName.
    public boolean doesUserExist(String userName) {
        String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Checks if an email already exists in the database.
    public boolean doesEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM cse360users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetch user by email
    public User getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM cse360users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("userName"), rs.getString("password"),
                                rs.getString("email"), rs.getString("role"));
            }
        }
        return null;
    }

    // Fetch user by username
    public User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("userName"), rs.getString("password"),
                                rs.getString("email"), rs.getString("role"));
            }
        }
        return null;
    }

    // Deletes a user from the database
    public void deleteUser(String userName) throws SQLException {
        String query = "DELETE FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + userName + " deleted successfully.");
            } else {
                System.out.println("No user found with username: " + userName);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw e;
        }
    }

    // Returns the list of users in the database
    public ArrayList<ArrayList<String>> getTable() {
        String query = "SELECT * FROM cse360users";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            ArrayList<ArrayList<String>> table = new ArrayList<>();
            int columns = rs.getMetaData().getColumnCount();

            while(rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for(int i = 1; i <= columns; i++) {
                    row.add(rs.getString(i));
                }
                table.add(row);
            }
            return table;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Fetch the given column as a string ArrayList
    public ArrayList<String> getColumn(String columnName) {
        String query = "SELECT " + columnName + " FROM cse360users";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> column = new ArrayList<>();
            while(rs.next()) {
                column.add(rs.getString(columnName));
            }
            return column;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves the role of a user 
    public String getUserRole(String userName) {
        String query = "SELECT role FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Generates an invitation code with assigned roles and inserts it into DB
    public String generateInvitationCode(String roles) {
        String code = UUID.randomUUID().toString().substring(0, 4);
        String query = "INSERT INTO InvitationCodes (code, isUsed, roles) VALUES (?, FALSE, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.setString(2, roles);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return code;
    }

    // Returns the role assigned via invitation code
    public String getInvitedRole(String code) {
        String query = "SELECT roles FROM InvitationCodes WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("roles");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Validates an invitation code
    public boolean validateInvitationCode(String code) {
        String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                markInvitationCodeAsUsed(code);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Marks the invitation code as used
    private void markInvitationCodeAsUsed(String code) {
        String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Closes the database connection
    public void closeConnection() {
        try { 
            if(statement!=null) statement.close();
        } catch(SQLException se2) {
            se2.printStackTrace();
        }
        try {
            if(connection!=null) connection.close();
        } catch(SQLException se){
            se.printStackTrace();
        }
    }

    // Retrieves all questions (from questionTable)
    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questionTable";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(rs.getString("userName"), rs.getString("input")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Retrieves all answers (from answerTable)
    public ArrayList<Answer> getAllAnswers() {
        ArrayList<Answer> answers = new ArrayList<>();
        String query = "SELECT * FROM answerTable";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                answers.add(new Answer(
                        rs.getString("userName"),
                        rs.getString("input")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    // Delete question from questionTable
    public void deleteQuestion(String username, String input) throws SQLException {
        String query = "DELETE FROM questionTable WHERE userName = ? AND input = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, input);
            pstmt.executeUpdate();
        }
    }

    // Delete answer from answerTable
    public void deleteAnswer(String username, String input) throws SQLException {
        String query = "DELETE FROM answerTable WHERE userName = ? AND input = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, input);
            pstmt.executeUpdate();
        }
    }

    // Delete review from reviewTable
    public void deleteReview(String username, String input) throws SQLException {
        String query = "DELETE FROM reviewTable WHERE userName = ? AND input = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, input);
            pstmt.executeUpdate();
        }
    }

    // ------------------------------------------------------------------------
    // Methods for Staff Dashboard
    // ------------------------------------------------------------------------

    /**
     * Counts how many questions are in questionTable.
     */
    public int countQuestions() {
        return getAllQuestions().size();
    }

    /**
     * Counts how many answers are in answerTable.
     */
    public int countAnswers() {
        return getAllAnswers().size();
    }

    /**
     * Counts flagged items. Currently returns 0 since no flagged logic is stored.
     * Update this if you add a 'flagged' column in your question/answer tables.
     */
    public int countFlaggedItems() {
        return 0;
    }

    /**
     * Merges all questions and answers into Interactions (for filtering/sorting).
     * Type is either "question" or "answer".
     */
    public ArrayList<Interaction> getAllInteractions() {
        ArrayList<Interaction> combined = new ArrayList<>();

        for (Question q : getAllQuestions()) {
            combined.add(new Interaction("question", q.getUserName(), q.getInput()));
        }
        for (Answer a : getAllAnswers()) {
            combined.add(new Interaction("answer", a.getUserName(), a.getInput()));
        }
        return combined;
    }

    /**
     * Example method to fetch answers related to a question.
     * NOTE: The current DB schema does not store question info in answerTable,
     * so this is a stub unless you add columns like (questionUser, questionInput).
     */
    public ArrayList<Answer> getAnswersByQuestion(Question question) {
        // Currently, we do not track which question an answer belongs to.
        // If you add columns in the answerTable linking it to the question,
        // filter them here. For now, we return an empty list (or all answers).
        return new ArrayList<>();
    }
    
    public Connection getConnection() {
        return connection;
    }
    /**
     * Adds a question into questionTable.
     */
    public void addQuestion(Question q) throws SQLException {
        String sql = "INSERT INTO questionTable (userName, input) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, q.getUserName());
            pstmt.setString(2, q.getInput());
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates a question's content.
     */
    public void updateQuestion(String userName, String oldContent, String newContent) throws SQLException {
        String sql = "UPDATE questionTable SET input = ? WHERE userName = ? AND input = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newContent);
            pstmt.setString(2, userName);
            pstmt.setString(3, oldContent);
            pstmt.executeUpdate();
        }
    }

    /**
     * Deletes a question from questionTable using its properties.
     */
    public void deleteQuestion(Question q) throws SQLException {
        String query = "DELETE FROM questionTable WHERE userName = ? AND input = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, q.getUserName());
            pstmt.setString(2, q.getInput());
            pstmt.executeUpdate();
        }
    }

    /**
     * Adds an answer into answerTable.
     */
    public void addAnswer(Answer a) throws SQLException {
        // Assuming that the answerTable stores a foreign key qID (referencing questionTable)
        // First, retrieve the question ID corresponding to the answer's question.
        String getQuestion = "SELECT id FROM questionTable WHERE input = ?";
        String insertAnswer = "INSERT INTO answerTable (qID, userName, input) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt1 = connection.prepareStatement(getQuestion)) {
            pstmt1.setString(1, a.getQuestion().getInput());
            ResultSet rs = pstmt1.executeQuery();
            if (rs.next()) {
                int qID = rs.getInt("id");
                try (PreparedStatement pstmt2 = connection.prepareStatement(insertAnswer)) {
                    pstmt2.setInt(1, qID);
                    pstmt2.setString(2, a.getUserName());
                    pstmt2.setString(3, a.getInput());
                    pstmt2.executeUpdate();
                }
            }
        }
    }
}
