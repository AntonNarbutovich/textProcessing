package ds.lab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModalController {

    private Stage stage;

    @FXML
    private TextField word;

    @FXML
    private Label label;

    @FXML
    private Label oldWordLabel;

    private String oldWord;

    private String oldCount;

    public ModalController() {
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void addWord(){
        Utils.addWord(oldWord, oldCount, word.getText());
    }

    @FXML
    private void deleteWord(){
        Utils.deleteWord(oldWord);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOldWord(String word){
        oldWord = word;
        oldWordLabel.setText(oldWordLabel.getText() + oldWord);
    }

    public void setOldCount(String count){
        oldCount = count;
    }
}
