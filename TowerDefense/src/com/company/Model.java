package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Model {
    //TODO Get in from a txt or other load in, instead of writing out.
    // (also, build a secondary app to help with the creation process)
    public static final HashMap<String, ArrayList<point>> models = new HashMap<>();
    private static final HashMap<String, String> name_and_filepath_of_models = new HashMap<>(Map.of(
            "basic", "TowerDefense/Models/basic",
            "buffer","TowerDefense/Models/buffer",
            "cannon","TowerDefense/Models/cannon",
            "gatling","TowerDefense/Models/gatling",
            "sniper","TowerDefense/Models/sniper"));
    public static void initModels(){
        for (String key : name_and_filepath_of_models.keySet()) {
            readInModel(name_and_filepath_of_models.get(key), key);
        }
    }
    Model(){

    }
    public ArrayList<point> points;

    public ArrayList<point> getPoints() {
        return points;
    }

    public void setPoints(String gunType, double x, double y) {
        points = new ArrayList<>();
        for (point p : models.get(gunType)){
            points.add(new point(p.x + x + 5, p.y + y + 5));
        }
    }
    private static void readInModel(String filepath, String name){
        File file = new File(filepath);
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            ArrayList<point> model = new ArrayList<>();
            while(scanner.hasNextLine()){
                String[] strings = scanner.nextLine().split(",");
                if (strings.length == 2) {
                    model.add(new point(Double.parseDouble(strings[0]), Double.parseDouble(strings[1])));
                }
            }
            models.put(name, model);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
