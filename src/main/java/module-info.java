module advanced.prog.advanced {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens advanced.prog.project to javafx.fxml;
    exports advanced.prog.project;
}