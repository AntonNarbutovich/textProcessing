package ds.lab;

import edu.stanford.nlp.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static Map<String, String> tagNames = new HashMap<>();

    static {
        tagNames.put("CC", "Coordinating conjunction");
        tagNames.put("CD", "Cardinal number");
        tagNames.put("DT", "Determiner");
        tagNames.put("EX", "Existential there");
        tagNames.put("FW", "Foreign word");
        tagNames.put("IN", "Preposition or subordinating conjunction");
        tagNames.put("JJ", "Adjective");
        tagNames.put("JJR", "Adjective, comparative");
        tagNames.put("JJS", "Adjective, superlative");
        tagNames.put("LS", "List item marker");
        tagNames.put("MD", "Modal");
        tagNames.put("NN", "Noun");
        tagNames.put("NNS", "Noun");
        tagNames.put("NNP", "Noun");
        tagNames.put("NNPS", "Noun");
        tagNames.put("PDT", "Predeterminer");
        tagNames.put("POS", "Possessive ending");
        tagNames.put("PRP", "Pronoun");
        tagNames.put("PRP$", "Pronoun");
        tagNames.put("RB", "Adverb");
        tagNames.put("RBR", "Adverb");
        tagNames.put("RBS", "Particle");
        tagNames.put("RP", "Noun");
        tagNames.put("SYM", "Symbol");
        tagNames.put("TO", "To");
        tagNames.put("UH", "Interjection");
        tagNames.put("VB", "Verb");
        tagNames.put("VBD", "Verb");
        tagNames.put("VBG", "Verb");
        tagNames.put("VBN", "Verb");
        tagNames.put("VBP", "Verb");
        tagNames.put("VBZ", "Verb");
        tagNames.put("WDT", "Wh-determiner");
        tagNames.put("WP", "Wh-pronoun");
        tagNames.put("WP$", "Wh-pronoun");
        tagNames.put("WRB", "Wh-adverb");
    }

    public static void addWordsToDB(Map<String, Pair<Integer, String>> map) {
        String url = "jdbc:mysql://localhost:3306/lab1";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "root";
        Connection connection;
        StringBuilder sql = new StringBuilder("INSERT INTO word_stats (word, amount, defaultForm) VALUES");
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            for(Map.Entry<String, Pair<Integer, String>> entry: map.entrySet()){
                sql.append("('" + entry.getKey() + "', " + entry.getValue().first + ", '" + entry.getValue().second + "'),");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(" ON DUPLICATE KEY UPDATE amount = amount + VALUES(amount)");

            statement.executeUpdate(sql.toString());

            statement.executeUpdate("DELETE FROM word_stats WHERE word=''");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Integer> getWordsFromDB() {
        String url = "jdbc:mysql://localhost:3306/lab1";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "root";
        Connection connection;
        String sql = "SELECT * FROM word_stats";
        Map<String, Integer> map = new HashMap<>();
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()){
                map.put(rs.getString("word"), rs.getInt("amount"));
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public static void addWord(String oldWord, String oldCount, String newWord){
        String url = "jdbc:mysql://localhost:3306/lab1";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "root";
        Connection connection;
        String sql1 = "DELETE FROM word_stats WHERE word='" + oldWord + "'";
        String sql2 = "INSERT INTO word_stats (word, amount) VALUES('" + newWord + "'," + oldCount + ") ON DUPLICATE KEY UPDATE amount = amount+" + oldCount;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql1);
            statement.executeUpdate(sql2);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteWord(String word){
        String url = "jdbc:mysql://localhost:3306/lab1";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "root";
        Connection connection;
        String sql1 = "DELETE FROM word_stats WHERE word='" + word + "'";
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql1);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addTags(Map<String, String> tags){
        String url = "jdbc:mysql://localhost:3306/lab1";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "root";
        Connection connection;
        StringBuilder sql = new StringBuilder("INSERT INTO tags (word, partOfSpeech) VALUES");
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            for(Map.Entry<String, String> entry: tags.entrySet()){
                sql.append("('" + entry.getKey() + "', '" + tagNames.get(entry.getValue()) + "'),");
            }
            sql.deleteCharAt(sql.length()-1);
            sql.append(" ON DUPLICATE KEY UPDATE partOfSpeech = VALUES(partOfSpeech)");
            statement.executeUpdate(sql.toString());

            statement.executeUpdate("DELETE FROM word_stats WHERE word=''");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
