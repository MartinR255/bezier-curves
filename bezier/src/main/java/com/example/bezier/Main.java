package com.example.bezier;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private Scene layoutScene;
    private Pane mainScene;
    private Stage stage;
    private BorderPane layout;
    private double width, height;
    private final double WIDTH_SCREEN_ADJUST_CONSTANT = 0.75, HEIGHT_SCREEN_ADJUST_CONSTANT = 0.7;


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


        layoutScene = new Scene(layout, width, height);

        initializePoints();
        initializePointsCircles();
        setWindowEventListeners();

        stage.setMinWidth(525);
        stage.setMinHeight(285);
        stage.setTitle("Bezier Curves");
        stage.setScene(layoutScene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }


    private final int DEFAULT_POINT_QUANTITY = 3;
    Points points;
    private void initializePoints() {
        points = new Points();
        double part_size = width / 4;
        for (int i = 0; i < DEFAULT_POINT_QUANTITY; i++) {
            points.addPoint(new Point(part_size * (i + 1), 350.0f));
        }
    }


    private double interpolate(double pA, double pB, double t) {
        return pA + ((pB - pA) * t);
    }


    private List<Double> deCasteljau(List<List<Double>> points, double t) {
        List<Double> a, b;
        List<List<Double>> midpoints = new ArrayList<>();

        while(points.size() > 1){
		    final int num = points.size() - 1;
            for(int i = 0; i < num; ++i){
                a = points.get(i);
                b = points.get(i+1);
                midpoints.add(
                    List.of(
                            interpolate(a.get(0), b.get(0), t),
                            interpolate(a.get(1), b.get(1), t)
                    )
                );
            }
            points = midpoints;
            midpoints = new ArrayList<>();
        }

        return points.get(0);
    }



    private final double delta = 0.05;
    private void drawCurve() {
        System.out.println("idem idem");
        for (double t = 0; t <= 1.005; t += delta) {
            List<Double> p = deCasteljau(points.getPointsCoordinates(), t);

            Circle point = new Circle(p.get(0), p.get(1), 3);
            Line line = new Line();
            mainScene.getChildren().addAll(point, line);
        }
    }


    List<Circle> pointCircleObjects;
    private void initializePointsCircles() {
        pointCircleObjects = new ArrayList<>();
        for (Point point : points.getPoints()) {
            pointCircleObjects.add(new Circle(point.getX(), point.getY(), 10));
        }
    }


    private void paintScene() {
        mainScene.getChildren().clear();
        drawCurve();
        mainScene.getChildren().addAll(pointCircleObjects);
    }



    private void createDragController(int pointNumber) {
        DragController dragController = new DragController(points.getPoint(pointNumber), pointCircleObjects.get(pointNumber));
        dragControllers.add(dragController);
        dragController.objectChanged().addListener((observable, oldValue, newValue) -> paintScene());
    }

    List<DragController> dragControllers;
    private void setWindowEventListeners() {
        dragControllers = new ArrayList<>();
        for (int i = 0; i < points.getPointsSize(); i++) {
            createDragController(i);
        }

        stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            width = (double) newValue;
            paintScene();
        });

        stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            height = (double) newValue;
            paintScene();
        });
    }
}


