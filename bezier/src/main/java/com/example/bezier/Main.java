package com.example.bezier;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene layoutScene;
    private Pane mainScene;
    private Stage stage;
    private BorderPane layout;
    private double width, height;
    private final double WIDTH_SCREEN_ADJUST_CONSTANT = 0.75, HEIGHT_SCREEN_ADJUST_CONSTANT = 0.7;

    private DraggableObject dragHandler = new DraggableObject();
    Circle circle = new Circle(150.0f, 150.0f, 80.f);


    @Override
    public void start(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        width = screenBounds.getWidth() * WIDTH_SCREEN_ADJUST_CONSTANT;
        height = screenBounds.getHeight() * HEIGHT_SCREEN_ADJUST_CONSTANT;

        this.stage = stage;
        mainScene = new Pane();


        // set up border pane layout
        layout = new BorderPane();
        layout.setCenter(mainScene);

        setWindowEventListeners();
        layoutScene = new Scene(layout, width, height);
        stage.setMinWidth(525);
        stage.setMinHeight(285);
        stage.setTitle("Bezier Curves");
        stage.setScene(layoutScene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

    private void paintScene() {
        mainScene.getChildren().clear();



        mainScene.getChildren().addAll(circle);
    }


    private void setWindowEventListeners() {
        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            width = (double) newValue;
            paintScene();
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            height = (double) newValue;
            paintScene();
        });

        dragHandler.dragObject(circle);
    }

}


