import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Quizsystem {

    private static final String USERS_FILE = "src/main/resources/user.json";
    private static final String QUIZ_FILE = "src/main/resources/quiz.json";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        JSONArray users = loadUsers();
        JSONArray quiz = loadQuiz();


        System.out.print("System:> Enter your username\nUser:> ");
        String username = scanner.nextLine();

        System.out.print("System:> Enter password\nUser:> ");
        String password = scanner.nextLine();

        JSONObject loggedInUser = authenticateUser(users, username, password);
        if (loggedInUser != null) {
            String role = loggedInUser.getString("role");
            if (role.equals("admin")) {
                handleAdmin(quiz);
            } else if (role.equals("student")) {
                handleStudent(quiz);
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static JSONArray loadUsers() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
        return new JSONArray(content);
    }

    private static JSONArray loadQuiz() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(QUIZ_FILE)));
        return new JSONArray(content);
    }

    private static JSONObject authenticateUser(JSONArray users, String username, String password) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject user = users.getJSONObject(i);
            if (user.getString("username").equals(username) && user.getString("password").equals(password)) {
                return user;
            }
        }
        return null;
    }

    private static void handleAdmin(JSONArray quiz) throws IOException {
        System.out.println("System:> Welcome admin! Please create new questions in the question bank.");
        while (true) {
            System.out.print("System:> Input your question\nAdmin:> ");
            String question = scanner.nextLine();
            System.out.print("System:> Input option 1:\nAdmin:> ");
            String option1 = scanner.nextLine();
            System.out.print("System:> Input option 2:\nAdmin:> ");
            String option2 = scanner.nextLine();
            System.out.print("System:> Input option 3:\nAdmin:> ");
            String option3 = scanner.nextLine();
            System.out.print("System:> Input option 4:\nAdmin:> ");
            String option4 = scanner.nextLine();
            System.out.print("System:> What is the answer key?\nAdmin:> ");
            int answerKey = Integer.parseInt(scanner.nextLine());

            JSONObject newQuestion = new JSONObject();
            newQuestion.put("question", question);
            newQuestion.put("option 1", option1);
            newQuestion.put("option 2", option2);
            newQuestion.put("option 3", option3);
            newQuestion.put("option 4", option4);
            newQuestion.put("answerkey", answerKey);

            quiz.put(newQuestion);

            System.out.println("System:> Saved successfully! Do you want to add more questions? (press s for start and q for quit)");
            String input = scanner.nextLine();
            if (input.equals("q")) {
                break;
            }
        }

        saveQuiz(quiz);
    }

    private static void saveQuiz(JSONArray quiz) throws IOException {
        FileWriter fileWriter = new FileWriter(QUIZ_FILE);
        fileWriter.write(quiz.toString(4));
        fileWriter.close();
    }

    private static void handleStudent(JSONArray quiz) {
        System.out.println("System:> Welcome salman to the quiz! We will throw you 10 questions. Each MCQ mark is 1 and no negative marking. Are you ready? Press 's' to start.");
        String start = scanner.nextLine();
        if (start.equals("s")) {
            int score = 0;
            Random rand = new Random();

            for (int i = 0; i < 10; i++) {
                JSONObject question = quiz.getJSONObject(rand.nextInt(quiz.length()));

                System.out.println("[Question " + (i + 1) + "] " + question.getString("question"));
                System.out.println("1. " + question.getString("option 1"));
                System.out.println("2. " + question.getString("option 2"));
                System.out.println("3. " + question.getString("option 3"));
                System.out.println("4. " + question.getString("option 4"));

                System.out.print("Student:> ");
                int answer = Integer.parseInt(scanner.nextLine());

                if (answer == question.getInt("answerkey")) {
                    score++;
                }
            }

            if (score >= 8) {
                System.out.println("Excellent! You have got " + score + " out of 10");
            } else if (score >= 5) {
                System.out.println("Good. You have got " + score + " out of 10");
            } else if (score >= 2) {
                System.out.println("Very poor! You have got " + score + " out of 10");
            } else {
                System.out.println("Very sorry you are failed. You have got " + score + " out of 10");
            }
        }
    }
}
