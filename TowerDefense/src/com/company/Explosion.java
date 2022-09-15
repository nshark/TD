package com.company;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.pow;

public class Explosion {
    private Tower tower;
    private double radius;
    private boolean repeat;
    private long timeSec = 0;
    private double x;
    private double y;
    private double rc = 0;
    private long lastCalled;
    private boolean maxed = false;
    private static final Color c1 = new Color(200, 10, 10, 200);
    private static final Color c2 = new Color(0, 0, 0, 75);
    public boolean exist = true;
    static public final ArrayList<Explosion> Explosions = new ArrayList<>();

    Explosion(Tower tower, double radius, boolean repeat, double x, double y) {
        this.tower = tower;
        this.radius = radius;
        this.repeat = repeat;
        lastCalled = System.currentTimeMillis();
        this.x = x;
        this.y = y;
        Explosions.add(this);
    }

    public static void explode(Tower tower, double radius, boolean repeat, double x, double y, double m) {
        for (Enemy e : Enemy.enemies) {
            if (pow((e.x - x), 2) + pow((e.y - y), 2) <= pow(radius, 2)) {
                if (repeat) {
                    e.DmgOverTime += tower.stats.get("Damage") * 0.4 * m;
                } else {
                    e.DmgOverTime += tower.stats.get("Damage") * 0.2 * m;
                }
            }
        }
        for (Explosion e : Explosions) {
            if (!e.exist) {
                e.exist = true;
                e.tower = tower;
                e.x = x;
                e.y = y;
                e.repeat = repeat;
                e.radius = radius;
                e.rc = 0;
                e.maxed = false;
                return;
            }
        }
        Explosion e = new Explosion(tower, radius, repeat, x, y);
    }

    public void draw(graphicalInterface gui) {
        long elapsed = System.currentTimeMillis() - lastCalled;
        lastCalled = System.currentTimeMillis();
        if (!maxed) {
            if (repeat) {
                rc += (double) (elapsed) * radius * 0.001;
            } else {
                rc += (double) (elapsed) * radius * 0.003;
            }
        }
        if (maxed) {
            if (rc <= 0) {
                this.exist = false;
                return;
            } else {
                if (repeat) {
                    rc -= (double) (elapsed) * radius * 0.01;
                } else {
                    rc -= (double) (elapsed) * radius * 0.03;
                }
            }
        }
        if (rc >= radius) {
            maxed = true;
        }
        if (repeat) {
            gui.setColor(c1);
            gui.circle(x, y, rc);
            timeSec += elapsed;
        } else {
            gui.setColor(c2);
            gui.circle(x, y, rc);
        }
    }

    public static void update(graphicalInterface gui) {
        ArrayList<Explosion> spc = new ArrayList<>();
        for (Explosion e : Explosions) {
            e.draw(gui);
            if (e.repeat && !e.maxed) {
                if (e.cluster()) {
                    spc.add(e);
                }
            }
        }
        for (Explosion e : spc) {
            double x1 = Enemy.r.nextDouble(e.x - e.rc, e.x + e.rc);
            double y1 = Enemy.r.nextDouble(e.y - e.rc, e.y + e.rc);
            explode(e.tower, e.radius / 2, false, x1, y1, 1);
        }
    }

    public boolean cluster() {
        if (Enemy.r.nextLong(timeSec + 1) > 250) {
            timeSec = 0;
            return true;
        }
        return false;
    }
}
