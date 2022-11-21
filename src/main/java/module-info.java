module com.example.pong_proj {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.pong_proj to javafx.fxml;
    opens assets.textures;
    opens assets.music;
    exports com.example.pong_proj;
}