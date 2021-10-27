package Application;

import com.jfoenix.controls.JFXTextArea;
import core.SQLiteDatabaseActions;
import core.TranslatorAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import speech.TextToSpeech;

import java.io.IOException;
import java.sql.SQLException;


public class Controller {
    @FXML
    private TextField word, addword, defword, delword;
    @FXML
    private TextField oldword, newword, def, pronounce;
    @FXML
    private JFXTextArea jfxtect;
    @FXML
    private Text myWord;
    @FXML
    private WebView webview = new WebView();
    @FXML
    private Button btnlookup, btntrans, buttonedit;

    ObservableList<String> seasonList = FXCollections.observableArrayList();
    @FXML
    private ListView listview ;

    TextToSpeech tts = new TextToSpeech();
    SQLiteDatabaseActions actions = new SQLiteDatabaseActions();


    public void Submit(ActionEvent event) throws IOException, SQLException {
        String answer = actions.queryforHtml(word.getText());
        final WebEngine engine = webview.getEngine();
        engine.loadContent(answer);
    }

    public void translate(ActionEvent event) throws IOException {
        TranslatorAPI translatorAPI = new TranslatorAPI();
        String answer = translatorAPI.processor(jfxtect.getText());
        myWord.setText(answer);
    }

    public void addWord(ActionEvent event) throws IOException, SQLException {
        actions.insertWord(addword.getText(), defword.getText());
    }

    public void deleteWord(ActionEvent event) throws IOException, SQLException {
        actions.deleteWord(delword.getText());
    }

    public void updateWord(ActionEvent event) throws IOException, SQLException {
        actions.updateWord(oldword.getText(), newword.getText(), def.getText(), pronounce.getText());
    }

    public void speechWord(ActionEvent event) throws IOException {

        String sptext = word.getText();
        tts.speak(sptext, 2.0f, false, true);
    }

    public ListView getListview() {
        return listview;
    }
    @FXML
    public void updateListView(KeyEvent event) throws  IOException, SQLException{

        String www = word.getText();
        ObservableList<String> observableList = FXCollections.observableArrayList(actions.realtimeSearch(www));
        listview.setItems(observableList);


    }
    @FXML public void mouseclick (javafx.scene.input.MouseEvent event) throws IOException,SQLException {
        String anan = event.getSource().toString();
        ListView<String> alist =  (ListView<String>)event.getSource();
        alist.getItems();
        String ans = event.getTarget().toString();
        String res = "'";
        String finale = "";
        char x = res.charAt(0);
        int chk = 2;
        for( char i : ans.toCharArray()){
            if (i == x || i == '"') {
                chk --;
                continue;
            }
            if (chk == 0) break;
            if (chk == 1) finale += i;

        }
        final WebEngine engine = webview.getEngine();
        System.out.println(event.getTarget());
        word.setText(finale);
        engine.loadContent(actions.queryforHtml(finale));
    }


    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;

        if (event.getSource() == btnlookup) {
            stage = (Stage) btnlookup.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        } else if (event.getSource() == btntrans) {
            stage = (Stage) btntrans.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("translate.fxml"));
        } else {
            stage = (Stage) buttonedit.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("editdata.fxml"));
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}