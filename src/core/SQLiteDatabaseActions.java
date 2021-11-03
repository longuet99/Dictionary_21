package core;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLiteDatabaseActions {
    public List<String> words = new ArrayList<>();
    private static Connection cnt = null;

    public static Connection connector() throws SQLException {
        String url = "jdbc:sqlite:/Users/mvvj2/Desktop/OOP/Long/Dictionary_2021/src/dict_hh.db";
        if (cnt == null) {
            cnt = DriverManager.getConnection(url);
        } else {
            cnt.close();
            cnt = DriverManager.getConnection(url);
        }
        return cnt;
    }

    public void insertFromSQLiteDatabase() throws SQLException {
        String sql = "SELECT word FROM av";
        Connection conn = connector();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            words.add(resultSet.getString("word"));
        }
        Collections.sort(words);
    }

    public String queryforHtml(String toFind) throws SQLException {
        String sql = "SELECT word, html FROM av";
        Connection connection = connector();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            if (resultSet.getString("word").equals(toFind)) return resultSet.getString("html");
        }
        statement.close();

        return "not found";
    }

    public void insertWord(String word, String def) throws SQLException {
        String htmltext = "<h1>" + word + "</h1><ul><li>" + def + "</li></ul>";
        String pron = "???";
        String sql = "INSERT INTO av(word, description, html, pronounce) VALUES('" + word + "','" + def + "','" + htmltext + "','" + pron + "')";
        Connection connection = connector();
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();

    }

    public void deleteWord(String word) throws SQLException {
        String sql = "DELETE FROM av WHERE word = ?";
        Connection connection = connector();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, word);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public String updateWord(String degradedWord, String upgradeWord, String upgradeDescription, String upgradePronounce) throws SQLException {
        String sql = "UPDATE av SET description = ? , " + "pronounce = ? , " + "word = ? , " + "html = ? " + "WHERE word = ?";
        Connection connection = connector();
        int fullchecker = 0;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (upgradeDescription != "") {
            preparedStatement.setString(1, upgradeDescription);
            fullchecker++;
        }
        if (upgradePronounce != "") {
            preparedStatement.setString(2, upgradePronounce);
            fullchecker++;
        }
        if (upgradeWord != "") {
            preparedStatement.setString(3, upgradeWord);
            fullchecker++;
        }
        if (upgradeDescription != "" && upgradePronounce != "" && upgradeWord != "") {
            String newHTML = "<h1>" + upgradeWord + "</h1><h3><i>" + upgradePronounce + "</i></h3><ul><li>" + upgradeDescription + "</li></ul>";
            preparedStatement.setString(4, newHTML);
        }
        preparedStatement.setString(5, degradedWord);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        if (fullchecker == 3) return "Operation successful!";
        else return "Operation failed! Check what you missed.";
    }

    public void update(String oldWord, String xword, String xhtml, String xdescription, String xpronounce) throws SQLException {
        String sql = "UPDATE av" + " SET id = id";
        if (xword != "") sql += ",word = '" + xword + "\'";
        if (xhtml != "") sql += ",html = '" + xhtml + "\'";
        if (xdescription != "") sql += ",description = '" + xdescription + "\'";
        if (xpronounce != "") sql += ",pronounce = '" + xpronounce + "\'";
        sql += " WHERE word =" + "\'" + oldWord + "\'"; //av (viet-anh, anh-viet)
        System.out.println(sql);
        Connection conn = this.connector();
        conn.setAutoCommit(false);

        Statement statement = conn.createStatement();
        statement.execute(sql);

        conn.commit();
        statement.close();
        conn.close();
    }

    public List<String> realtimeSearch(String preWord) throws SQLException {
        List<String> searchResult = new ArrayList<>();
        Connection connection = connector();
        String sql = "SELECT word FROM av";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String word = resultSet.getString("word");
            if (word.startsWith(preWord) && searchResult.size() < 15) {
                searchResult.add(word);
            }
        }
        return searchResult;
    }
}
