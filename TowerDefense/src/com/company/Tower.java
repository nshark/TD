package com.company;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.*;

public class Tower{
    static public ArrayList<String> bases = new ArrayList<>(List.of(
            "plain", "raised", "spin"
    ));
    public double x;
    public double buffed = 0;
    public double buffedM = 1;
    public static HashMap<String, Double> bAmmoMods = new HashMap<>(Map.of(
            "standard", 1d, "fire", -2d, "ice", 0.5d, "artillery", 3d)
    );
    public static Color dg = new Color(0,125,0);
    public double y;
    public String projectileType = "standard";
    static public HashMap<String, ArrayList<point>> gunPolys = new HashMap<>(
            Map.of("basic",
                    new ArrayList<>(List.of(
                            new point(-0.5,-2),
                            new point(-0.5,-5),
                            new point(0.5,-5),
                            new point(0.5,-2),
                            new point(-2,-2),
                            new point(-2,2),
                            new point(2,2),
                            new point(2,-2))),
                    "buffer",
                    new ArrayList<>(List.of(
                            new point(-2,-2),
                            new point(-2,2),
                            new point(2,2),
                            new point(2,-2))),
                    "sniper",
                    new ArrayList<>(List.of(
                            new point(-0.25,-2),
                            new point(-0.25,-5),
                            new point(0.25,-5),
                            new point(0.25,-2),
                            new point(-2,-2),
                            new point(-2,2),
                            new point(2,2),
                            new point(2,-2))),
                    "gatling",
                    new ArrayList<>(List.of(
                            new point(-1,-2),
                            new point(-1,-5),
                            new point(-0.5,-5),
                            new point(-0.5,-2),
                            new point(-0.25,-2),
                            new point(-0.25,-5),
                            new point(0.25,-5),
                            new point(0.25,-2),
                            new point(0.5,-2),
                            new point(0.5,-5),
                            new point(1,-5),
                            new point(1,-2),
                            new point(2,-2),
                            new point(2,2),
                            new point(-2,2),
                            new point(-2,-2))),
                    "cannon",
                    new ArrayList<>(List.of(
                            new point(-1,-3),
                            new point(-1,-5),
                            new point(1,-5),
                            new point(1,-3),
                            new point(-3,-3),
                            new point(-3,3),
                            new point(3,3),
                            new point(3,-3)))));
    private long lastTimeCalled = System.currentTimeMillis();
    public String base = "plain";
    public String gunType = "basic";
    private ArrayList<point> toRotate = new ArrayList<>();
    public double h = 0;
    private long timeTillFire = 0;
    public HashMap<String, Double> stats = new HashMap<>(Map.of("Fire Rate", 100d, "Damage", 1d, "TurnSpd", 0.01d, "BulletV", 0.03, "Range", 10d));
    public void computeStats(graphicalInterface gui, Boolean polyChange){
        if (Objects.equals(gunType, "basic")){
            stats.replace("Fire Rate", 500d);
            stats.replace("Damage", 0.5);
            stats.replace("Range", 20d);
            stats.replace("BulletV", 0.03d);
        }
        if (Objects.equals(gunType, "buffer")){
            stats.replace("Fire Rate", 500d);
            stats.replace("Damage", 0.5);
            stats.replace("Range", 20d);
            stats.replace("BulletV", 0.03d);
        }
        if (Objects.equals(gunType, "gatling")){
            stats.replace("Fire Rate", 200d);
            stats.replace("Damage", 0.1);
            stats.replace("Range", 15d);
            stats.replace("BulletV", 0.03d);
        }
        if (Objects.equals(gunType, "cannon")){
            stats.replace("Fire Rate", 2000d);
            stats.replace("Damage", 2d);
            stats.replace("Range", 30d);
            stats.replace("BulletV", 0.06d);
        }
        if (Objects.equals(gunType, "sniper")){
            stats.replace("Fire Rate", 5000d);
            stats.replace("Damage", 4d);
            stats.replace("Range", 40d);
            stats.replace("BulletV", 0.1d);
        }
        if (Objects.equals(projectileType, "artillery")){
            stats.replace("Fire Rate", stats.get("Fire Rate") * 2);
        }
        if (polyChange){
            toRotate = new ArrayList<>();
            h = 0;
            for (int i = 0; i < gunPolys.get(gunType).size(); i++) {
                toRotate.add(new point((gunPolys.get(gunType).get(i).x + x + 5), (gunPolys.get(gunType).get(i).y + y + 5)));
            }
            gui.rotate(x+5,y+5,toRotate, PI/2);
        }
        if (Objects.equals(base, "plain")){
            stats.replace("Fire Rate", stats.get("Fire Rate") * 1.2);
            stats.replace("Damage", stats.get("Damage") * 1.2);
            stats.replace("TurnSpd", 0.01d);
        }
        if (Objects.equals(base, "spin")){
            stats.replace("Fire Rate", stats.get("Fire Rate") * 0.9);
            stats.replace("Damage", stats.get("Damage") * 1);
            stats.replace("Range", stats.get("Range")*0.8);
            stats.replace("TurnSpd", 0.03d);
        }
        if (Objects.equals(base, "raised")){
            stats.replace("Fire Rate", stats.get("Fire Rate") * 3);
            stats.replace("Damage", stats.get("Damage") * 1.5);
            stats.replace("TurnSpd", 0.01d);
            stats.replace("Range", stats.get("Range")*2);
        }
    }
    public void draw(graphicalInterface gui, game game, boolean showRange) {
        long timeElapsed = System.currentTimeMillis() - lastTimeCalled;
        lastTimeCalled = System.currentTimeMillis();
        if (Objects.equals(base, "plain")){
            gui.setColor(Color.white);
            gui.rect(x+1, y+1, x+9,y+9);
        }
        if (Objects.equals(base, "raised")){
            gui.setColor(Color.darkGray);
            gui.rect(x+1, y+1, x+9,y+9);
            gui.setColor(Color.BLACK);
            gui.rect(x+2, y+2, x+8,y+8);
        }
        if (Objects.equals(base, "spin")){
            gui.setColor(Color.darkGray);
            gui.rect(x+1, y+1, x+9,y+9);
            gui.setColor(Color.gray);
            gui.circle(x+5, y+5, 3);
        }
        if (Objects.equals(gunType, "basic")){
            gui.setColor(Color.darkGray);
            gui.poly(toRotate);
        }
        if (Objects.equals(gunType, "gatling")){
            gui.setColor(Color.darkGray);
            gui.poly(toRotate);
        }

        if (Objects.equals(gunType, "sniper")){
            gui.setColor(dg);
            gui.poly(toRotate);
        }
        if (Objects.equals(gunType, "cannon")){
            gui.setColor(Color.ORANGE);
            gui.poly(toRotate);
        }
        if (Objects.equals(gunType, "buffer")){
            gui.setColor(Color.MAGENTA);
            gui.poly(toRotate);
        }
        if (showRange && !(gunType == "buffer")){
            gui.setColor(Main.c1);
            gui.circle(x+5, y+5, stats.get("Range"));
        }
        if (timeTillFire > 0) {
            if(buffed >= abs(buffedM)*timeElapsed*0.1 && buffedM < 0) {
                timeTillFire -= timeElapsed * abs(buffedM);
                buffed -= abs(buffedM) * timeElapsed * 0.1;
            }
            else{
                timeTillFire -= timeElapsed;
            }
        }
        if (game.wave){
            if (gunType != "buffer") {
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
                        gui.rotate(x + 5, y + 5, toRotate, atan2(target.y - y - 5, target.x - x - 5) - h);
                        h = atan2(target.y - y - 5, target.x - x - 5);
                        if (this.timeTillFire <= 0) {
                            timeTillFire = round(stats.get("Fire Rate"));
                            Projectile.shoot(this, projectileType);
                        }
                    } else if (atan2(target.y - y - 5, target.x - x - 5) > h) {
                        h += this.stats.get("TurnSpd") * timeElapsed;
                        gui.rotate(x + 5, y + 5, toRotate, this.stats.get("TurnSpd") * timeElapsed);
                    } else {
                        h -= this.stats.get("TurnSpd") * timeElapsed;
                        gui.rotate(x + 5, y + 5, toRotate, this.stats.get("TurnSpd") * timeElapsed * -1);
                    }
                }
            }
            else if (game.cT != this){
                if (!(round(x/10) == 9)) {
                    if (game.tileGrid.get((int) round(x / 10)+1).get((int) round(y / 10)).hasTower) {
                        game.tileGrid.get((int) round(x / 10)+1).get((int) round(y / 10)).tower.buffed += (stats.get("Damage")*timeElapsed)/stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10)+1).get((int) round(y / 10)).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
                if (!(round(x/10) == 0)) {
                    if (game.tileGrid.get((int) round(x / 10)-1).get((int) round(y / 10)).hasTower) {
                        game.tileGrid.get((int) round(x / 10)-1).get((int) round(y / 10)).tower.buffed += (stats.get("Damage")*timeElapsed)/stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10)-1).get((int) round(y / 10)).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
                if (!(round(y/10) == 9)) {
                    if (game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10)+1).hasTower) {
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10)+1).tower.buffed += (stats.get("Damage")*timeElapsed)/stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10)+1).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
                if (!(round(y/10) == 0)) {
                    if (game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10)-1).hasTower) {
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10)-1).tower.buffed += (stats.get("Damage")*timeElapsed)/stats.get("Fire Rate");
                        game.tileGrid.get((int) round(x / 10)).get((int) round(y / 10)-1).tower.buffedM = bAmmoMods.get(projectileType);
                    }
                }
            }
        }
    }
    Tower(double x1, double y1){
        x = x1;
        y = y1;
    }
    public Tower finalized(double x1, double y1){
        x = x1;
        y = y1;
        return this;
    }
    public int cost(){
        int p = 0;
        if (Objects.equals(base, "plain")){
            p += 50;
        }
        if (Objects.equals(base, "raised")){
            p += 75;
        }
        if (Objects.equals(base, "spin")){
            p += 50;
        }
        if (Objects.equals(gunType, "basic")){
            p += 100;
        }
        if (Objects.equals(gunType, "gatling")){
            p+=150;
        }
        if (Objects.equals(gunType, "cannon")){
            p+=200;
        }
        if (Objects.equals(gunType, "sniper")){
            p+=150;
        }
        if (Objects.equals(gunType, "buffer")){
            p+=250;
        }
        if (Objects.equals(projectileType, "standard")){
            p+=50;
        }
        if (Objects.equals(projectileType, "ice")){
            p+=75;
        }
        if (Objects.equals(projectileType, "fire")){
            p+=75;
        }
        if (Objects.equals(projectileType, "artillery")){
            p+=100;
        }
        return(p);
    }
}
