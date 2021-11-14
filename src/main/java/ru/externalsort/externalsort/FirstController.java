package ru.externalsort.externalsort;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
/**
 * Класс для управления главным окном
 * Указывается в main.fxml
 * @FXML используется для связки переменных и их отображения. Имена==fx:id
 * initialize() вызовется при отображении экрана. В нём прописываются начальные данные для объектов
 */
public class FirstController {
    @FXML
    private TextField length;

    @FXML
    private Button create;

    @FXML
    private Button showGraph;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> size;

    @FXML
    private TableColumn<?, ?> simpleMerge;

    @FXML
    private TableColumn<?, ?> naturalMerge;

    @FXML
    private TableColumn<?, ?> absorptionMethod;

    @FXML
    void initialize() {

    }
}
