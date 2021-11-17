package ru.externalsort.externalsort;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс, в котором находится точка входа в программу.
 * Наследуется от Application и переопределяет его метод start
 * Stage - класс, отвечающий за представление окна. Кнопки свернуть, растянуть и закрыть, размеры окна, его заголовок.
 * throws IOException- output\input exception - ошибка ввода\вывода - исключение, которое выбрасывается если при вызове fxmlLoader.load() не будет найден файл.
 * @Override - аннотация, помогающая программисту не ошибиться при переопределении метода. Если вы нарушите струтуру переопределяемого метода, то она начнёт ругаться
 **/
public class Main extends Application {
    private static Stage currentStage;
    //инициализируем список
    private static List<Node> nodes = new ArrayList<>();;

    public static List<Node> getNodes() {
        return nodes;
    }

    public static Stage getCurrentStage() {
        return currentStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        //Сохраняем ссылку на сцену
        currentStage = stage;
        //Создаём загрузчик fxml файло, который преопразует fxml в графическое представление (Scene)
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("first_window.fxml"));
        //Преобразуем, устанавливая размеры окна
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        //Ставим заголовок
        stage.setTitle("Алгоритмы сортировки");
        //Устанавливаем внутренности окна
        stage.setScene(scene);
        //Делаем жёсткий размер окна
        stage.setResizable(false);
        //Демострируем окно (до этого его не видно)
        stage.show();
    }

    public static void main(String[] args) {
        //Запускаем процесс отображения окна
        launch();
    }

}