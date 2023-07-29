package com.example.bezier;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Main extends Application {
    private Scene layoutScene;
    private Pane mainScene;
    private HBox topPanel;
    private Stage stage;
    private BorderPane layout;
    private double width, height;
    private final double WIDTH_SCREEN_ADJUST_CONSTANT = 0.75, HEIGHT_SCREEN_ADJUST_CONSTANT = 0.7;

    private enum Status {
        ADD,
        DRAG,
        REMOVE
    }
    private Status currentState = Status.DRAG;


    @Override
    public void start(Stage stage) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        width = screenBounds.getWidth() * WIDTH_SCREEN_ADJUST_CONSTANT;
        height = screenBounds.getHeight() * HEIGHT_SCREEN_ADJUST_CONSTANT;

        this.stage = stage;
        // set up border pane layout
        layout = new BorderPane();

        mainScene = new Pane();
        layout.setCenter(mainScene);

        topPanel = new HBox();
        layout.setTop(topPanel);

        initializeTopPanel();
        initializePoints();
        initializePointsCircles();
        setWindowEventListeners();



        layoutScene = new Scene(layout, width, height);

        paintScene();
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
        double part_size = 200;
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
        for (double t = 0; t <= 1.005; t += delta) {
            List<Double> p = deCasteljau(points.getPointsCoordinates(), t);

            Circle point = new Circle(p.get(0), p.get(1), 3);
            Line line = new Line();
            mainScene.getChildren().addAll(point, line);
        }
    }


    List<Circle> pointCircleObjects;
    private double pointRadius = 10;
    private void initializePointsCircles() {
        pointCircleObjects = new ArrayList<>();
        for (Point point : points.getPoints()) {
            pointCircleObjects.add(new Circle(point.getX(), point.getY(), pointRadius));
        }
    }


    private void createDragController(int pointNumber) {
        DragController dragController = new DragController(points.getPoint(pointNumber), pointCircleObjects.get(pointNumber), topPanel.getPrefHeight() + pointRadius);
        dragControllers.add(dragController);
        dragController.objectChanged().addListener((observable, oldValue, newValue) -> paintScene());
    }


    private void deletePoint(int index) {
        pointCircleObjects.remove(index);
        points.removePoint(index);
    }


    List<DragController> dragControllers;
    private void setWindowEventListeners() {
        dragControllers = new ArrayList<>();
        for (int i = 0; i < points.getPointsSize(); i++) {
            createDragController(i);
        }

        mainScene.setOnMouseClicked(event -> {
            if (currentState == Status.ADD) {
                Point point = new Point(event.getX(), event.getY());
                points.addPoint(point);
                pointCircleObjects.add(new Circle(point.getX(), point.getY(), 10));
                createDragController(pointCircleObjects.size()-1);
                paintScene();
            } else if (currentState == Status.REMOVE) {
                for (int i=0; i < points.getPointsSize(); i++) {
                    Point point = points.getPoint(i);
                    double xDiff = point.getX() - event.getX();
                    double yDiff = point.getY() - event.getY();
                    if ((xDiff * xDiff) + (yDiff * yDiff) <= pointRadius * pointRadius) {
                        deletePoint(i);
                        paintScene();
                        break;
                    }
                }
            }
        });
    }

    public void paintScene() {
        mainScene.getChildren().clear();
        drawCurve();
        mainScene.getChildren().addAll(pointCircleObjects);
    }

    public void initializeTopPanel() {
        topPanel.getStylesheets().add(
                Objects.requireNonNull(this.getClass().getResource("/css/top_panel_style.css")).toExternalForm());
        topPanel.getStyleClass().add("hbox");
        topPanel.setAlignment(Pos.CENTER_LEFT);
        topPanel.setPrefHeight(30);
        topPanel.setSpacing(20);
        setupButtons();
    }


    private void setupButtons() {
        Button addNewNode = new Button("+");
        Button deleteNode = new Button("-");

        addNewNode.setOnAction(event -> {
            if (currentState == Status.DRAG) {
                currentState = Status.ADD;
            } else if (currentState == Status.REMOVE) {
                currentState = Status.ADD;
            } else {
                currentState = Status.DRAG;
            }
        });

        deleteNode.setOnAction(event -> {
            if (currentState == Status.DRAG) {
                currentState = Status.REMOVE;
            } else if (currentState == Status.ADD) {
                currentState = Status.REMOVE;
            } else {
                currentState = Status.DRAG;
            }
        });

        topPanel.getChildren().addAll(addNewNode, deleteNode);
    }

}


