package ru.externalsort.externalsort;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Graph {
    //В скобках указываются типы осей
    @FXML
    private LineChart<NumberAxis, NumberAxis> lineChart;

    @FXML
    private Button toTable;

    @FXML
    void initialize() {
        //Создаём классы, хранящие точки на графике и задаём им имена
        XYChart.Series simple = new XYChart.Series();
        simple.setName("Простое слияние");
        XYChart.Series natural = new XYChart.Series();
        natural.setName("Естественное слияние");
        XYChart.Series absorption = new XYChart.Series();
        absorption.setName("Метод поглощения");
        List<Node> nodes = Main.getNodes();
        //Заполняем
        for (int i = 0; i < nodes.size(); i++) {
            long size = nodes.get(i).getSize();
            simple.getData().add(new XYChart.Data(size, nodes.get(i).getSimpleMerge()));
            natural.getData().add(new XYChart.Data(size, nodes.get(i).getNaturalMerge()));
            absorption.getData().add(new XYChart.Data(size, nodes.get(i).getAbsorptionMethod()));
        }
        //Отображем
        lineChart.getData().add(simple);
        lineChart.getData().add(natural);
        lineChart.getData().add(absorption);

        toTable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Stage stage = Main.getCurrentStage();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("first_window.fxml"));
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