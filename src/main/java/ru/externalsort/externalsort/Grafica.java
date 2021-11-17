package ru.externalsort.externalsort;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Класс для управления главным окном
 * Указывается в main.fxml
 *
 * @FXML используется для связки переменных и их отображения. Имена==fx:id
 * initialize() вызовется при отображении экрана. В нём прописываются начальные данные для объектов
 */
public class Grafica {
    @FXML
    private TextField length;

    @FXML
    private Button create;

    @FXML
    private Button showGraph;
    //В угловые ковычки передаю класс, который хранит информацию о строке. Переменные - значения в столбцах
    @FXML
    private TableView<Node> table;
    //В угловых ковычках класс, который хранит информацию и тип значения. Использую классы-обёртки потому что джава не принимает в такие ковычки примитивы(Это всё называется дженерики)
    @FXML
    private TableColumn<Node, Long> size;

    @FXML
    private TableColumn<Node, Long> simpleMerge;

    @FXML
    private TableColumn<Node, Long> naturalMerge;

    @FXML
    private TableColumn<Node, Long> absorptionMethod;

    @FXML
    void initialize() {
        //Создаю классы для соединения значений и столбцов
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        simpleMerge.setCellValueFactory(new PropertyValueFactory<>("simpleMerge"));
        naturalMerge.setCellValueFactory(new PropertyValueFactory<>("naturalMerge"));
        absorptionMethod.setCellValueFactory(new PropertyValueFactory<>("absorptionMethod"));
        //Отображаем список линий
        ObservableList<Node> list = FXCollections.observableList(Main.getNodes());
        table.setItems(list);
        //при нажатии на кнопку "создать файл" сработает код, написанный в handle
        create.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //Берём текст с поля для ввода и приводим к числу
                long size = Long.parseLong(length.getText());
                //Создаём класс функций
                Functions functions = new Functions();
                //Начинаем вычисление, указав длинну
                long[] result = functions.startSort(size);
                //Создаём класс для новой строки и заполняем из массива результатов + длинна
                Node newNode = new Node(size, result[0], result[1], result[2]);
                //Добавляем в список строк
                Main.getNodes().add(newNode);
                //Отображаем новый список
                ObservableList<Node> list = FXCollections.observableList(Main.getNodes());
                table.setItems(list);
            }
        });
        //при нажатии на кнопку отобразить данные в графическом виде меняем сцену
        showGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Stage stage = Main.getCurrentStage();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("second_window.fxml"));
                    //Преобразуем, устанавливая размеры окна
                    Scene scene = new Scene(fxmlLoader.load(), 700, 600);
                    //Устанавливаем внутренности окна
                    stage.setScene(scene);
                    //Демострируем окно (до этого его не видно)
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
