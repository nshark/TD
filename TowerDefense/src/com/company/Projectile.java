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

    static public void shoot(Tower tower, String type) {
        for (Projectile p : projectiles) {
            if (!p.exist) {
                p.fire(tower);
                p.exist = true;
                p.type = type;
                return;
            }
        }
        projectiles.add(new Projectile(tower, type));
    }

    Projectile(Tower tower, String type) {
        this.type = type;
        this.fire(tower);
    }

    public void fire(Tower tower) {
        dx = tower.stats.get("BulletV") * cos(tower.h);
        dy = tower.stats.get("BulletV") * sin(tower.h);
        x = tower.x + 5;
        y = tower.y + 5;
        lastCall = System.currentTimeMillis();
        this.tower = tower;
    }

    public void draw(graphicalInterface gui, game game) {
        int time = (int) (System.currentTimeMillis() - lastCall);
        lastCall = System.currentTimeMillis();
        x += dx * time;
        y += dy * time;
        if (x > 100 || x < 0 || y > 100 || y < 0) {
            this.exist = false;
        }
        if (pow(x - tower.x - 5, 2) + pow(y - tower.y - 5, 2) >= pow(tower.stats.get("Range"), 2)) {
            this.exist = false;
        }
        gui.setColor(colorTypes.get(type));
        gui.circle(x, y, 1);
        for (Enemy e : Enemy.enemies) {
            if (e.exist) {
                if (pow(x - e.x, 2) + pow(y - e.y, 2) <= 9d) {
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
                        e.mSpeed = e.mSpeed * 0.75 * m;
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
