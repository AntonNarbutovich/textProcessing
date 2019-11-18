package ds.lab;

import edu.stanford.nlp.util.Pair;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class Controller {
    @FXML
    private TableView dataTable;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, String> word;
    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> time;

    @FXML
    private TextField path;

    private Stage stage;

    private Main mainApp;

    private Map<String, Integer> map = new HashMap<String, Integer>();
    private ObservableList<Map.Entry<String, Integer>> data = FXCollections.observableArrayList(map.entrySet());

    @FXML
    private TextField filterField;


    public Controller() {
    }

    @FXML
    private void initialize() {
        word.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        time.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getValue()).asObject());
        FilteredList<Map.Entry<String, Integer>> filteredData = new FilteredList<>(data, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(entry -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (entry.getKey().toLowerCase().startsWith(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                }else
                    return false; // Does not match.
            });
            SortedList<Map.Entry<String, Integer>> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(dataTable.comparatorProperty());

            dataTable.setItems(sortedData);
            dataTable.getSelectionModel().selectedItemProperty();
        });

        dataTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    Stage stage = new Stage();
                    Parent root;
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getClassLoader().getResource("modal.fxml"));
                    root = loader.load();
                    ModalController controller = loader.getController();
                    System.out.println(newSelection.toString());
                    controller.setOldWord(newSelection.toString().split("=")[0]);
                    controller.setOldCount(newSelection.toString().split("=")[1]);
                    stage.setScene(new Scene(root));
                    stage.setTitle("Change word");
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(this.stage);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                data.clear();
                data.addAll(Utils.getWordsFromDB().entrySet());
            }
        });

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        dataTable.setItems(data);
        data.addAll(Utils.getWordsFromDB().entrySet());
    }


    @FXML
    public void computeMap() {
        Map<String, Integer> map = WordUtils.parseFile(path.getText());
        Map<String, Pair<Integer, String>> newMap = WordAnalyzer.analyze(map);
        Utils.addWordsToDB(newMap);
        data.clear();
        data.addAll(Utils.getWordsFromDB().entrySet());
    }

    @FXML
    private void openDirectoryPath(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedDirectory = fileChooser.showOpenDialog(stage);
        if (selectedDirectory == null) {
            path.setText("No selected directory");
        } else {
            path.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
