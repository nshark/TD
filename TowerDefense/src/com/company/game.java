package com.company;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class game implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    private final graphicalInterface gui;
    public ArrayList<point> path;
    Tile portal = null;
    Tile crystal = null;
    public int mx = 0;
    public int lk = 0;
    public int kl = 0;
    public int klj = 0;
    public boolean mp;
    private final ArrayList<point> playButton;
    public boolean towerCreator = false;
    public int coins = 2000;
    public int my = 0;
    public Button play;
    public Tower cT = null;
    public ArrayList<Button> towButtons = new ArrayList<>();
    public HashMap<String, Integer> prices = new HashMap<>(Map.of("grass", 10, "road", -10, "portal", 0, "crystal", 0));
    public boolean wave = false;
    public ArrayList<Tile> shopTiles = new ArrayList<>();
    public Tile selectedTile = null;
    public final ArrayList<ArrayList<Tile>> tileGrid = new ArrayList<>();
    public static Font f = new Font("f", Font.PLAIN, 25);

    game(graphicalInterface gu) {
        gui = gu;
        playButton = new ArrayList<>(List.of(
                new point(gui.width / gui.scaleFactor - 5, 92),
                new point(gui.width / gui.scaleFactor - 1, 95),
                new point(gui.width / gui.scaleFactor - 5, 98)));
        play = new Button((int) (gui.width / gui.scaleFactor - 5), 92, (int) (gui.width / gui.scaleFactor - 1), 98, "");
        gui.canvas.addKeyListener(this);
        gui.canvas.addMouseListener(this);
        gui.canvas.addMouseMotionListener(this);
        towButtons.add(new Button(120, 30, 130, 40, "<"));
        towButtons.add(new Button((int) (gui.width / gui.scaleFactor) - 20, 30, (int) ((gui.width / gui.scaleFactor) - 10), 40, ">"));
        towButtons.add(new Button(120, 40, 130, 50, "<"));
        towButtons.add(new Button((int) (gui.width / gui.scaleFactor) - 20, 40, (int) ((gui.width / gui.scaleFactor) - 10), 50, ">"));
        towButtons.add(new Button(120, 20, 130, 30, "<"));
        towButtons.add(new Button((int) (gui.width / gui.scaleFactor) - 20, 20, (int) ((gui.width / gui.scaleFactor) - 10), 30, ">"));
        towButtons.add(new Button(120, 60, 130, 70, ""));
        for (int i = 0; i < 10; i++) {
            ArrayList<Tile> tiles = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                tiles.add(new Tile("grass", i * 10, j * 10));
            }
            tileGrid.add(tiles);
        }
        shopTiles.add(new Tile("grass", 120, 20));
        shopTiles.add(new Tile("road", 135, 20));
        shopTiles.add(new Tile("portal", 120, 40));
        shopTiles.add(new Tile("crystal", 135, 40));
        new Thread(this).start();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            gui.setColor(Color.BLACK);
            gui.g.fillRect(gui.height, 0, (gui.width - gui.height), gui.height);
            for (ArrayList<Tile> tiles : tileGrid) {
                for (Tile t : tiles) {
                    if (t != selectedTile) {
                        t.draw(gui, this);
                    }
                }
            }
            for (Projectile p : Projectile.projectiles) {
                if (p.exist) {
                    p.draw(gui, this);
                }
            }
            Explosion.update(this, gui);
            if (wave) {
                Enemy.drawEnemies(this, gui);
            }
            if (towerCreator) {
                gui.g.setFont(game.f);
                gui.setColor(Color.white);
                gui.text("Tower Creator:       Coins: " + coins, 110, 10);
                selectedTile.draw(gui, this);
                cT.draw(gui, this, false);
                gui.setColor(Main.c1);
                gui.circle(selectedTile.x + 5, selectedTile.y + 5, cT.stats.get("Range"));
                gui.setColor(Color.white);
                gui.text(cT.base, 140, 35);
                gui.text(cT.gunType, 140, 25);
                gui.text(cT.projectileType, 135, 45);
                gui.text(String.valueOf(cT.cost()), 130, 65);
                for (int i = 0; i < cT.stats.size(); i++) {
                    gui.text("" + cT.stats.entrySet().toArray()[i], gui.width / gui.scaleFactor - 25, 60 + i * 5);
                }
                for (Button b : towButtons) {
                    if (b.draw(gui, mx, my, mp)) {
                        mp = false;
                        if (b.y == 30) {
                            if (Objects.equals(b.text, "<")) {
                                kl -= 1;
                                if (kl < 0) {
                                    kl = Tower.bases.size() - 1;
                                }
                                cT.base = Tower.bases.get(kl);
                                cT.computeStats(gui, false);
                            } else {
                                kl += 1;
                                if (kl >= Tower.bases.size()) {
                                    kl = 0;
                                }
                                cT.base = Tower.bases.get(kl);
                                cT.computeStats(gui, false);
                            }
                        }
                        if (b.y == 40) {
                            if (Objects.equals(b.text, "<")) {
                                klj -= 1;
                                if (klj < 0) {
                                    klj = Projectile.colorTypes.keySet().toArray().length - 1;
                                }
                                cT.projectileType = (String) Projectile.colorTypes.keySet().toArray()[klj];
                                cT.computeStats(gui, false);
                            } else {
                                klj += 1;
                                if (klj >= Projectile.colorTypes.keySet().toArray().length) {
                                    klj = 0;
                                }
                                cT.projectileType = (String) Projectile.colorTypes.keySet().toArray()[klj];
                                cT.computeStats(gui, false);
                            }
                        } else if (b.y == 20) {
                            if (Objects.equals(b.text, "<")) {
                                lk -= 1;
                                if (lk < 0) {
                                    lk = Tower.gunPolys.size() - 1;
                                }
                                cT.gunType = (String) Tower.gunPolys.keySet().toArray()[lk];
                                cT.computeStats(gui, true);
                            } else {
                                lk += 1;
                                if (lk >= Tower.gunPolys.size()) {
                                    lk = 0;
                                }
                                cT.gunType = (String) Tower.gunPolys.keySet().toArray()[lk];
                                cT.computeStats(gui, true);
                            }
                        } else if (b.y == 60 && coins >= cT.cost()) {
                            coins -= cT.cost();
                            selectedTile.hasTower = true;
                            selectedTile.tower = cT.finalized(selectedTile.x, selectedTile.y);
                            towerCreator = false;
                            cT.computeStats(gui, true);
                            cT = null;
                        }
                    }
                }
            } else if (selectedTile != null) {
                gui.g.setFont(game.f);
                gui.setColor(Color.white);
                gui.text("Shop:           Coins: " + coins, 110, 10);
                selectedTile.draw(gui, this);
                if (!selectedTile.hasTower) {
                    for (Tile t : shopTiles) {
                        t.draw(gui, this);
                        gui.setColor(Color.white);
                        gui.text(String.valueOf(prices.get(t.type)), t.x + 2.5, t.y + 15);
                    }
                    gui.setColor(Color.lightGray);
                    gui.rect(110, 70, (gui.width) / gui.scaleFactor - 10, 90);
                    gui.setColor(Color.darkGray);
                    gui.text("Build Tower", 130, 80);
                }
            }
            if (!wave) {
                if (crystal != null && portal != null && path != null) {
                    gui.setColor(Main.c3);
                    point p1 = null;
                    for (point p : path) {
                        if (p1 == null) {
                            p1 = p;
                        } else {
                            if (p1.x > p.x || p1.y > p.y) {
                                gui.rect(p.x - 1, p.y - 1, p1.x + 1, p1.y + 1);
                            } else {
                                gui.rect(p1.x - 1, p1.y - 1, p.x + 1, p.y + 1);
                            }
                            p1 = p;
                        }
                    }
                    if (play.draw(gui, mx, my, mp) && !wave) {
                        wave = true;
                        Enemy.pForWave += 2;
                        Enemy.pUsed = 0;
                    }
                    gui.setColor(Color.green);
                } else {
                    gui.setColor(Color.red);
                }
                gui.poly(playButton);
            }
            gui.update();
            gui.g.setFont(f);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mp = true;
        for (ArrayList<Tile> tiles : tileGrid) {
            for (Tile t : tiles) {
                if (t.mouseOver(mx, my)) {
                    if (t == selectedTile && t.hasTower) {
                        coins += t.tower.cost();
                        t.hasTower = false;
                        t.tower = null;
                    } else {
                        selectedTile = t;
                        towerCreator = false;
                        mp = false;
                    }
                }
            }
        }
        if (selectedTile != null) {
            if (!selectedTile.hasTower && !Objects.equals(selectedTile.type, "portal") && !Objects.equals(selectedTile.type, "crystal") && !wave && !towerCreator) {
                for (Tile t : shopTiles) {
                    if (t.mouseOver(mx, my)) {
                        if (prices.get(t.type) * -1 <= coins && !Objects.equals(selectedTile.type, t.type)) {
                            coins += prices.get(t.type);
                            selectedTile.type = t.type;
                            mp = false;
                            if (Objects.equals("portal", t.type)) {
                                if (portal != null) {
                                    portal.type = "grass";
                                }
                                portal = selectedTile;
                            }
                            if (Objects.equals("crystal", t.type)) {
                                if (crystal != null) {
                                    crystal.type = "grass";
                                }
                                crystal = selectedTile;
                            }
                            pathFind();
                        }
                    }
                }
                if (mx <= (gui.width) / gui.scaleFactor - 10 && mx >= 110 && my >= 70 && my <= 90 && Objects.equals(selectedTile.type, "grass")) {
                    cT = new Tower(120, 60);
                    cT.computeStats(gui, true);
                    towerCreator = true;
                    kl = 0;
                    lk = 0;
                    mp = false;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mp = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mx = (int) (e.getX() / gui.scaleFactor);
        my = (int) (e.getY() / gui.scaleFactor);
    }

    public void pathFind() {
        if (portal != null && crystal != null) {
            boolean[][] e = new boolean[10][10];
            path = pather(new ArrayList<>(), portal.x / 10, portal.y / 10, e);
        }
    }

    // This should be called as little as possible, and at some point should be replaced with a A* search. As of
    // now, it is good enough.
    private ArrayList<point> pather(ArrayList<point> points, int x, int y, boolean[][] explored) {
        points.add(new point(x * 10 + 5, y * 10 + 5));
        explored[x][y] = true;
        if (tileGrid.get(x).get(y) == crystal) {
            return (points);
        } else {
            ArrayList<ArrayList<point>> p = new ArrayList<>();
            if (x != 0) {
                if (!Objects.equals(tileGrid.get(x - 1).get(y).type, "grass") && !explored[x - 1][y]) {
                    p.add(pather((ArrayList<point>) points.clone(), x - 1, y, deepCopy(explored)));
                }
            }
            if (x != 9) {
                if (!Objects.equals(tileGrid.get(x + 1).get(y).type, "grass") && !explored[x + 1][y]) {
                    p.add(pather((ArrayList<point>) points.clone(), x + 1, y, deepCopy(explored)));
                }
            }
            if (y != 0) {
                if (!Objects.equals(tileGrid.get(x).get(y - 1).type, "grass") && !explored[x][y - 1]) {
                    p.add(pather((ArrayList<point>) points.clone(), x, y - 1, deepCopy(explored)));
                }
            }
            if (y != 9) {
                if (!Objects.equals(tileGrid.get(x).get(y + 1).type, "grass") && !explored[x][y + 1]) {
                    p.add(pather((ArrayList<point>) points.clone(), x, y + 1, deepCopy(explored)));

                }
            }
            int len = 1000;
            ArrayList<point> p1 = null;
            for (ArrayList<point> p2 : p) {
                if (p2 != null) {
                    if (p2.size() < len) {
                        p1 = p2;
                        len = p2.size();
                    }
                }
            }
            return p1;
        }
    }

    public static boolean[][] deepCopy(boolean[][] original) {
        if (original == null) {
            return null;
        }

        final boolean[][] result = new boolean[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }
}
