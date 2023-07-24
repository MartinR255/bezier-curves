package com.example.bezier;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class DragController {
    private final Point point;
    private final Node target;
    private double anchorX;
    private double anchorY;
    private double mouseOffsetFromNodeZeroX;
    private double mouseOffsetFromNodeZeroY;

    private BooleanProperty dragged;

    public DragController(Point point, Node target) {
        this.point = point;
        this.target = target;
        dragged = new SimpleBooleanProperty();
        dragged.set(false);
        createHandlers();
    }

    private void createHandlers() {
        target.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                mouseOffsetFromNodeZeroX = event.getX();
                mouseOffsetFromNodeZeroY = event.getY();
            }
            if (event.isSecondaryButtonDown()) {
                target.setTranslateX(0);
                target.setTranslateY(0);
            }
            dragged.set(true);
        });

        target.setOnMouseDragged(event -> {
            target.setTranslateX(event.getSceneX() - anchorX);
            target.setTranslateY(event.getSceneY() - anchorY);
            point.setX(event.getSceneX());
            point.setY(event.getSceneY());
        });

        target.setOnMouseReleased(event -> {
            //commit changes to LayoutX and LayoutY
            target.setLayoutX(event.getSceneX() - mouseOffsetFromNodeZeroX);
            target.setLayoutY(event.getSceneY() - mouseOffsetFromNodeZeroY);
            point.setX(event.getSceneX());
            point.setY(event.getSceneY());
            //clear changes from TranslateX and TranslateY
            target.setTranslateX(0);
            target.setTranslateY(0);
            dragged.set(false);
        });
    }

    public BooleanProperty objectChanged() {
        return dragged;
    }
}



