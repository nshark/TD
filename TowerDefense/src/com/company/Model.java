package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    //TODO Get in from a txt or other load in, instead of writing out.
    // (also, build a secondary app to help with the creation process)
    public static HashMap<String, ArrayList<point>> models = new HashMap<>(Map.of(
            "basic",
            new ArrayList<>(List.of(
                            new point(-0.5, -2),
                            new point(-0.5, -5),
                            new point(0.5, -5),
                            new point(0.5, -2),
                            new point(-2, -2),
                            new point(-2, 2),
                            new point(2, 2),
                            new point(2, -2))),
            "buffer",
            new ArrayList<>(List.of(
                            new point(-2, -2),
                            new point(-2, 2),
                            new point(2, 2),
                            new point(2, -2))),
            "sniper",
            new ArrayList<>(List.of(
                            new point(-0.25, -2),
                            new point(-0.25, -5),
                            new point(0.25, -5),
                            new point(0.25, -2),
                            new point(-2, -2),
                            new point(-2, 2),
                            new point(2, 2),
                            new point(2, -2))),
            "gatling",
            new ArrayList<>(List.of(
                            new point(-1, -2),
                            new point(-1, -5),
                            new point(-0.5, -5),
                            new point(-0.5, -2),
                            new point(-0.25, -2),
                            new point(-0.25, -5),
                            new point(0.25, -5),
                            new point(0.25, -2),
                            new point(0.5, -2),
                            new point(0.5, -5),
                            new point(1, -5),
                            new point(1, -2),
                            new point(2, -2),
                            new point(2, 2),
                            new point(-2, 2),
                            new point(-2, -2))),
            "cannon",
            new ArrayList<>(List.of(
                            new point(-1, -3),
                            new point(-1, -5),
                            new point(1, -5),
                            new point(1, -3),
                            new point(-3, -3),
                            new point(-3, 3),
                            new point(3, 3),
                            new point(3, -3)))));
    Model(){

    }
    public ArrayList<point> points;

    public ArrayList<point> getPoints() {
        return points;
    }

    public void setPoints(String guntype, double x, double y) {
        points = new ArrayList<>();
        for (point p : models.get(guntype)){
            points.add(new point(p.x + x + 5, p.y + y + 5));
        }
    }
}
