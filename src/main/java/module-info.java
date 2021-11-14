module ru.externalsort.externalsort {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens ru.externalsort.externalsort to javafx.fxml;
    exports ru.externalsort.externalsort;
}