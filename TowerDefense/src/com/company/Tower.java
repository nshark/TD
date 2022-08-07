package com.company;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.*;

import static java.lang.Math.*;

public class Tower {
    static public final ArrayList<String> bases = new ArrayList<>(List.of(
            "plain", "raised", "spin"
    ));
    public double x;
    public double buffed = 0;
    public double buffedM = 1;
    public static final HashMap<String, Double> bAmmoMods = new HashMap<>(Map.of(
            "standard", 1d, "fire", -2d, "ice", 0.5d, "artillery", 3d)
    );
    public static final Color dg = new Color(0, 125, 0);
    public static final HashMap<String, Color> gunColors = new HashMap<>(Map.of("basic", Color.darkGray, "cannon", Color.orange, "sniper", dg, "buffer", Color.pink, "gatling", Color.darkGray));
    public double y;
    public final Model model = new Model();
    public String projectileType = "standard";
    private long lastTimeCalled = System.currentTimeMillis();
    public String base = "plain";
    public String gunType = "basic";
    public double h = 0;
    public ResearchTree tree;
    private long timeTillFire = 0;
    public final HashMap<String, Double> stats = new HashMap<>(Map.of("Fire Rate", 100d, "Damage", 1d, "TurnSpd", 0.01d, "BulletV", 0.03, "Range", 10d));

    public void computeStats(graphicalInterface gui, Boolean polyChange) {
        readInStats(gunType, stats);
        readInStats(base, stats);
        if (Objects.equals(projectileType, "artillery")) {
            stats.replace("Fire Rate", stats.get("Fire Rate") * 2);
        }
        if (polyChange) {
            model.setPoints(gunType, x, y);
            gui.rotate(x + 5, y + 5, model.points, PI / 2);
        }
    }
    public static void readInStats(String type, HashMap<String, Double> stats){
        try{
            File myObj = new File("TowerDefense/src/com/company/stats");
            Scanner myReader = new Scanner(myObj);
            boolean found = false;
            while(myReader.hasNextLine()){
                String line = myReader.nextLine();
                if (line != null) {
                    if (found) {
                        if (line.toCharArray()[0] == '|'){
                            return;
                        }
                        else if (line.toCharArray()[0] == ':'){
                            String[] args = line.split(":")[1].split("-");
                            if(stats.containsKey(args[0])) {
                                stats.replace(args[0], Double.parseDouble(args[1]));
                            }
                        }
                        else if (line.toCharArray()[0] == ';'){
                            String[] args = line.split(";")[1].split("-");
                            if(stats.containsKey(args[0])) {
                                stats.replace(args[0], Double.parseDouble(args[1]) * stats.get(args[0]));
                            }
                        }
                    } else if (line.equals("|" + type)) {
                        found = true;
                    }
                }
            }
            myReader.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public void draw(graphicalInterface gui, game game, boolean showRange) {
        long timeElapsed = System.currentTimeMillis() - lastTimeCalled;
        lastTimeCalled = System.currentTimeMillis();
        if (Objects.equals(base, "plain")) {
            gui.setColor(Color.white);
            gui.rect(x + 1, y + 1, x + 9, y + 9);
        }
        if (Objects.equals(base, "raised")) {
            gui.setColor(Color.darkGray);
            gui.rect(x + 1, y + 1, x + 9, y + 9);
            gui.setColor(Color.BLACK);
            gui.rect(x + 2, y + 2, x + 8, y + 8);
        }
        if (Objects.equals(base, "spin")) {
            gui.setColor(Color.darkGray);
            gui.rect(x + 1, y + 1, x + 9, y + 9);
            gui.setColor(Color.gray);
            gui.circle(x + 5, y + 5, 3);
        }
        gui.setColor(gunColors.get(gunType));
        gui.poly(model.getPoints());
        if (showRange && !(Objects.equals(gunType, "buffer"))) {
            gui.setColor(Main.c1);
            gui.circle(x + 5, y + 5, stats.get("Range"));
            tree.draw(game, gui);
        }
        if (timeTillFire > 0) {
            if (buffed >= abs(buffedM) * timeElapsed * 0.1 && buffedM < 0) {
                timeTillFire -= timeElapsed * abs(buffedM);
                buffed -= abs(buffedM) * timeElapsed * 0.1;
            } else {
                timeTillFire -= timeElapsed;
            }
        }
        if (game.wave) {
            if (!Objects.equals(gunType, "buffer")) {
                point target = null;
                Enemy e1 = null;
                for (Enemy e : Enemy.enemies) {
                    if (pow(x + 5 - e.x, 2) + pow(y + 5 - e.y, 2) <= pow(this.stats.get("Range"), 2) && e.exist) {
                        e1 = e;
                    }
                }
                if (e1 != null) {
                    double TimeToHit = pow((pow(x + 5 - e1.x, 2) + pow(y + 5 - e1.y, 2)) / stats.get("BulletV"), 0.5);
                    double x2 = ((game.path.get(e1.placePath + 1).x - e1.x) * (TimeToHit * e1.mSpeed * 0.005)) + e1.x;
                    double y2 = ((game.path.get(e1.placePath + 1).y - e1.y) * (TimeToHit * e1.mSpeed * 0.005)) + e1.y;
                    target = new point(x2, y2);
                }
                if (target != null) {
                    if (abs(atan2(target.y - y - 5, target.x - x - 5) - h) <= (this.stats.get("TurnSpd") * timeElapsed)) {
                        gui.rotate(x + 5, y + 5, model.points, atan2(target.y - y - 5, target.x - x - 5) - h);
                        h = atan2(target.y - y - 5, target.x - x - 5);
                        if (this.timeTillFire <= 0) {
                            timeTillFire = round(stats.get("Fire Rate"));
                            Projectile.shoot(this, projectileType);
                        }
                    } else if (atan2(target.y - y - 5, target.x - x - 5) > h) {
                        h += this.stats.get("TurnSpd") * timeElapsed;
                        gui.rotate(x + 5, y + 5, model.points, this.stats.get("TurnSpd") * timeElapsed);
                    } else {
                        h -= this.stats.get("TurnSpd") * timeElapsed;
                        gui.rotate(x + 5, y + 5, model.points, this.stats.get("TurnSpd") * timeElapsed * -1);
                    }
                }
            } else if (game.cT != this) {
                //TODO generally create method to return a iterable list of adjacent tiles
                if (!(round(x / 10) == 9)) {
                    if (game.tileGrid.get((int) round(x / 10) + 1).get((int) round(y / 10)).hasTower) {
                        game.tileGrid.get((int) round(x / 10) + 1).get((int) round(y / 10)).tower.buffed += (stats.get("Damage") * timeElapsed) / stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10) + 1).get((int) round(y / 10)).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
                if (!(round(x / 10) == 0)) {
                    if (game.tileGrid.get((int) round(x / 10) - 1).get((int) round(y / 10)).hasTower) {
                        game.tileGrid.get((int) round(x / 10) - 1).get((int) round(y / 10)).tower.buffed += (stats.get("Damage") * timeElapsed) / stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10) - 1).get((int) round(y / 10)).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
                if (!(round(y / 10) == 9)) {
                    if (game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10) + 1).hasTower) {
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10) + 1).tower.buffed += (stats.get("Damage") * timeElapsed) / stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10) + 1).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
                if (!(round(y / 10) == 0)) {
                    if (game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10) - 1).hasTower) {
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10) - 1).tower.buffed += (stats.get("Damage") * timeElapsed) / stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10) - 1).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
            }
        }
    }

    Tower() {
        x = 120;
        y = 60;
    }

    public Tower finalized(double x1, double y1) {
        x = x1;
        y = y1;
        tree = new ResearchTree(gunType, this);
        return this;
    }

    public int cost() {
        int p = 0;
        if (Objects.equals(base, "plain")) {
            p += 50;
        }
        if (Objects.equals(base, "raised")) {
            p += 75;
        }
        if (Objects.equals(base, "spin")) {
            p += 50;
        }
        if (Objects.equals(gunType, "basic")) {
            p += 100;
        }
        if (Objects.equals(gunType, "gatling")) {
            p += 150;
        }
        if (Objects.equals(gunType, "cannon")) {
            p += 200;
        }
        if (Objects.equals(gunType, "sniper")) {
            p += 150;
        }
        if (Objects.equals(gunType, "buffer")) {
            p += 250;
        }
        if (Objects.equals(projectileType, "standard")) {
            p += 50;
        }
        if (Objects.equals(projectileType, "ice")) {
            p += 75;
        }
        if (Objects.equals(projectileType, "fire")) {
            p += 75;
        }
        if (Objects.equals(projectileType, "artillery")) {
            p += 100;
        }
        return (p);
    }
}
