package com.example.hexagondrawingfx;

import com.example.hexagondrawingfx.controller.HexagonGameController;
import com.example.hexagondrawingfx.model.HexagonGameModel;
import com.example.hexagondrawingfx.view.HexagonGameView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        HexagonGameModel model = new HexagonGameModel();
        HexagonGameView view = new HexagonGameView();
        HexagonGameController controller = new HexagonGameController(model, view);

        controller.initialize(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
