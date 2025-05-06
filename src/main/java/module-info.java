module advanced.prog.advanced {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens advanced.prog.project to javafx.fxml;
    exports advanced.prog.project;
    exports advanced.prog.project.models;
    opens advanced.prog.project.models to javafx.fxml;
}