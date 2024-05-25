module com.example.hexagondrawingfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.hexagondrawingfx to javafx.fxml;
    exports com.example.hexagondrawingfx;
}