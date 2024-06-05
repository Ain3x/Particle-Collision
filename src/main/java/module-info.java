module com.example.particle_collision {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.example.particle_collision to javafx.fxml;
    exports com.example.particle_collision;
}