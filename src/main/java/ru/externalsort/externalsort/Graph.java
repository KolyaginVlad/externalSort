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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Long, Double> mapSimple = new HashMap<>();
        Map<Long, Double> mapNatural = new HashMap<>();
        Map<Long, Double> mapAbsorption = new HashMap<>();
        //Заполняем
        for (int i = 0; i < nodes.size(); i++) {
            if (!mapSimple.containsKey(nodes.get(i).getSize())) {
                int count = 0;
                mapSimple.put(nodes.get(i).getSize(), 0D);
                mapAbsorption.put(nodes.get(i).getSize(), 0D);
                mapNatural.put(nodes.get(i).getSize(), 0D);
                for (int j = 0; j < nodes.size(); j++) {
                    if (nodes.get(i).getSize() == nodes.get(j).getSize()) {
                        mapSimple.put(nodes.get(i).getSize(), mapSimple.get(nodes.get(i).getSize()) + nodes.get(j).getSimpleMerge());
                        mapAbsorption.put(nodes.get(i).getSize(), mapAbsorption.get(nodes.get(i).getSize()) + nodes.get(j).getAbsorptionMethod());
                        mapNatural.put(nodes.get(i).getSize(), mapNatural.get(nodes.get(i).getSize()) + nodes.get(j).getNaturalMerge());
                        count++;
                    }
                }
                mapSimple.put(nodes.get(i).getSize(), mapSimple.get(nodes.get(i).getSize())/count);
                mapNatural.put(nodes.get(i).getSize(), mapNatural.get(nodes.get(i).getSize())/count);
                mapAbsorption.put(nodes.get(i).getSize(), mapAbsorption.get(nodes.get(i).getSize())/count);
            }
        }
        for (Long num: mapSimple.keySet()
        ) {
            simple.getData().add(new XYChart.Data(num, mapSimple.get(num)));
            natural.getData().add(new XYChart.Data(num, mapNatural.get(num)));
            absorption.getData().add(new XYChart.Data(num, mapAbsorption.get(num)));
        }
        simple.getData().add(new XYChart.Data(0, 0));
        natural.getData().add(new XYChart.Data(0, 0));
        absorption.getData().add(new XYChart.Data(0, 0));
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