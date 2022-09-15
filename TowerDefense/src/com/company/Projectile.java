package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.*;

public class Projectile {
    double dx;
    double dy;
    double x;
    double y;
    Model points = new Model();
    static final HashMap<String, Color> colorTypes = new HashMap<>(Map.of(
            "fire", Color.red,
            "ice", Color.blue,
            "standard", Color.gray,
            "artillery", Color.orange));
    long lastCall = System.currentTimeMillis();
    String type;
    Tower tower;
    boolean exist = true;
    static public final ArrayList<Projectile> projectiles = new ArrayList<>();
    static public void shoot(Tower tower, String type, game game) {
        for (Projectile p : projectiles) {
            if (!p.exist) {
                p.fire(tower, game);
                p.exist = true;
                p.type = type;
                return;
            }
        }
        projectiles.add(new Projectile(tower, type, game));
    }

    Projectile(Tower tower, String type, game game) {
        this.type = type;
        this.fire(tower, game);
    }
    public void fire(Tower tower, game game) {
        dx = tower.stats.get("BulletV") * cos(tower.h);
        dy = tower.stats.get("BulletV") * sin(tower.h);
        x = tower.x + 5;
        y = tower.y + 5;
        x += cos(tower.h)*5;
        y += sin(tower.h)*5;
        double heading = tower.h;
        points.points = new ArrayList<>();
        for (point p : Model.projectile.getPoints()){
            points.points.add(new point(p.x + x, p.y + y));
        }
        game.getGui().rotate(x,y,points.points, heading +Math.PI/2);
        lastCall = System.currentTimeMillis();
        this.tower = tower;
    }
    public void draw(graphicalInterface gui) {
        int time = (int) (System.currentTimeMillis() - lastCall);
        lastCall = System.currentTimeMillis();
        x += dx * time;
        y += dy * time;
        for (point p: points.points){
            p.x += dx*time;
            p.y += dy*time;
        }
        if (x > 100 || x < 0 || y > 100 || y < 0) {
            this.exist = false;
        }
        if (pow(x - tower.x - 5, 2) + pow(y - tower.y - 5, 2) >= pow(tower.stats.get("Range"), 2)) {
            this.exist = false;
        }
        gui.setColor(colorTypes.get(type));
        gui.poly(points.points);
        for (Enemy e : Enemy.enemies) {
            if (e.exist) {
                if (pow(x - e.x, 2) + pow(y - e.y, 2) <= 2d) {
                    this.exist = false;
                    double m = 1;
                    if (this.tower.buffed >= this.tower.buffedM && this.tower.buffedM > 0) {
                        this.tower.buffed -= this.tower.buffedM;
                        m += this.tower.buffedM;
                    }
                    if (Objects.equals(type, "fire")) {
                        e.power -= tower.stats.get("Damage") * 0.5 * m;
                        e.DmgOverTime += tower.stats.get("Damage") * 0.75 * m;
                    }
                    if (Objects.equals(type, "ice")) {
                        e.power -= tower.stats.get("Damage") * 0.75 * m;
                        e.mSpeed = e.mSpeed * (1.75 - m);
                    }
                    if (Objects.equals(type, "standard")) {
                        e.power -= tower.stats.get("Damage") * m;
                    }
                    if (Objects.equals(type, "artillery")) {
                        Explosion.explode(tower, 10, true, x, y, m);
                    }
                }
            }
        }
    }
}
