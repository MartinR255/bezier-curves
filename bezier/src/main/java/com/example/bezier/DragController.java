package com.example.bezier;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

public class DragController {
    private final Point point;
    private final Node node;
    private double anchorPointX;
    private double anchorPointY;
    private double cursorOffsetX;
    private double cursorOffsetY;
    private double offsetY;

    private BooleanProperty dragged;

    public DragController(Point point, Node node, double offsetY) {
        this.point = point;
        this.node = node;
        this.offsetY = offsetY;
        dragged = new SimpleBooleanProperty();
        dragged.set(false);
        createHandlers();
    }

    private void createHandlers() {
        node.setOnMousePressed(event -> {
            anchorPointX = event.getSceneX();
            anchorPointY = event.getSceneY();
            cursorOffsetX = event.getX();
            cursorOffsetY = event.getY();
            dragged.set(true);
        });

        node.setOnMouseDragged(event -> {
            node.setTranslateX(event.getSceneX() - anchorPointX);
            node.setTranslateY(event.getSceneY() - anchorPointY);
            point.setX(event.getSceneX());
            point.setY(event.getSceneY());
        });

        node.setOnMouseReleased(event -> {
            node.setLayoutX(event.getSceneX() - cursorOffsetX);
            node.setLayoutY(event.getSceneY() - cursorOffsetY - offsetY);
            point.setX(event.getSceneX());
            point.setY(event.getSceneY() - offsetY);
            node.setTranslateX(0);
            node.setTranslateY(0);
            dragged.set(false);
        });
    }

    public BooleanProperty objectChanged() {
        return dragged;
    }
}



