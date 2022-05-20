package com.company;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class Enemy {
    static public ArrayList<Enemy> enemies = new ArrayList<>();
    static public Font f1 = new Font("p", Font.PLAIN, 10);
    static public Random r = new Random();
    public boolean exist = false;
    public double DmgOtime = 0;
    static int pForWave = 0;
    static int pUsed = 0;
    public double x = 0;
    public double mSpeed = 1;
    public double y = 0;
    public int placePath = 0;
    public long lastUpdate = System.currentTimeMillis();
    public double power = 1;
    public static long lSpawn = System.currentTimeMillis();

    static void spawn(double Tpower, game game) {
        for (Enemy e : enemies) {
            if (!e.exist) {
                e.power = Tpower;
                e.x = game.path.get(0).x;
                e.y = game.path.get(0).y;
                e.exist = true;
                e.DmgOtime = 0;
                e.placePath = 0;
                e.mSpeed = 1;
                pUsed += Tpower;
                lSpawn = System.currentTimeMillis();
                return;
            }
        }
        Enemy e = new Enemy();
        e.exist = true;
        e.x = game.path.get(0).x;
        e.y = game.path.get(0).y;
        e.power = Tpower;
        enemies.add(e);
        e.mSpeed = 1;
        lSpawn = System.currentTimeMillis();
        pUsed += Tpower;
    }

    static void drawEnemies(game game, graphicalInterface gui) {
        if (pUsed < pForWave) {
            if (500 <= r.nextInt((int) abs(System.currentTimeMillis() - lSpawn) + 1)) {
                if (pForWave - pUsed == 1) {
                    spawn(1, game);
                } else {
                    spawn(r.nextInt(1, (pForWave - pUsed)), game);
                }
            }
        }
        boolean waveEnded = true;
        for (Enemy es : enemies) {
            if (es.exist) {
                es.draw(game, gui);
                waveEnded = false;
            }
        }
        if (waveEnded && (pUsed == pForWave)) {
            game.wave = false;
            game.coins += pForWave * 10;
        }
    }

    public void draw(game game, graphicalInterface gui) {
        long timePassed = System.currentTimeMillis() - lastUpdate;
        lastUpdate = System.currentTimeMillis();
        power -= timePassed * 0.005 * DmgOtime;
        DmgOtime -= timePassed * 0.005 * DmgOtime;
        if (power <= 0) {
            this.exist = false;
        }
        if (DmgOtime < 0) {
            DmgOtime = 0;
        }
        if (game.path.get(placePath + 1).x - 0.01 <= this.x && game.path.get(placePath + 1).y - 0.01 <= this.y &&
                game.path.get(placePath + 1).x + 0.01 >= this.x && game.path.get(placePath + 1).y + 0.01 >= this.y) {
            if (placePath + 2 == game.path.size()) {
                exist = false;
            } else {
                placePath++;
            }
        } else {
            double movement = timePassed * 0.005 * mSpeed;
            if (abs(game.path.get(placePath + 1).x - x) <= movement) {
                movement -= abs(game.path.get(placePath + 1).x - x);
                x = game.path.get(placePath + 1).x;
            } else if (game.path.get(placePath + 1).x > x) {
                x += movement;
                movement = 0;
            } else if (game.path.get(placePath + 1).x < x) {
                x -= movement;
                movement = 0;
            }
            if (abs(game.path.get(placePath + 1).y - y) <= movement) {
                y = game.path.get(placePath + 1).y;
            } else if (game.path.get(placePath + 1).y > y) {
                y += movement;
            } else if (game.path.get(placePath + 1).y < y) {
                y -= movement;
            }
            gui.setColor(Color.red);
            gui.circle(x, y, 2);
            gui.setColor(Color.black);
            gui.g.setFont(f1);
            gui.text(String.valueOf(((double) (round(power * 10))) / 10), x, y);
            gui.g.setFont(com.company.game.f);
        }
    }
}
