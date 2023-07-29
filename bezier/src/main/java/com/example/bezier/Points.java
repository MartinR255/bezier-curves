package com.example.bezier;

import java.util.ArrayList;
import java.util.List;

public class Points {
    List<Point> points;

    public Points() {
        points = new ArrayList<>();
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public Point getPoint(int pointIndex) {
        return points.get(pointIndex);
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getPointsSize() {
        return points.size();
    }

    public List<List<Double>> getPointsCoordinates() {
        return points.stream().map(p -> p.getPoint()).toList();
    }

    public void removePoint(int index) {
        points.remove(index);
    }
}
