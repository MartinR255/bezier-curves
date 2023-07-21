package com.example.bezier;

import javafx.scene.Node;

public class DraggableObject {
    private double mouseX;
    private double mouseY;

    public void dragObject(Node node) {

        node.setOnMousePressed(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
        });

        node.setOnMouseDragged(mouseEvent -> {
            node.setLayoutX(mouseEvent.getSceneX() - mouseX);
            node.setLayoutY(mouseEvent.getSceneY() - mouseY);
        });
    }
}
