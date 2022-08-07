package com.company;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;


public class game implements Runnable, KeyListener, MouseListener, MouseMotionListener {
    private final graphicalInterface gui;
    private final MouseEventHandler mouseEventHandler = new MouseEventHandler(this);
    private final KeyEventHandler keyEventHandler = new KeyEventHandler(this);
    public ArrayList<point> path;
    Tile portal = null;
    Tile crystal = null;
    public static final HashMap<Character, String> types = new HashMap<>(Map.of('1', "grass", '2', "road", '3', "portal", '4', "crystal"));
    public int mx = 0;
    public int my = 0;
    public int counter1 = 0;
    public int counter2 = 0;
    public int counter3 = 0;
    public boolean mp;
    private final ArrayList<point> playButton;
    public boolean towerCreator = false;
    public int coins = 2000;
    public final Button play;
    public Tower cT = null;
    public final ArrayList<Button> towButtons = new ArrayList<>();
    public final HashMap<String, Integer> prices = new HashMap<>(Map.of("grass", 10, "road", -10, "portal", 0, "crystal", 0));
    public boolean wave = false;
    public final ArrayList<Tile> shopTiles = new ArrayList<>();
    public Tile selectedTile = null;
    public final ArrayList<ArrayList<Tile>> tileGrid = new ArrayList<>();
    public static final Font f = new Font("f", Font.PLAIN, 25);
    public final Button quitButton;
    private final Shop Shop;
    game(graphicalInterface gu) {
        gui = gu;
        playButton = new ArrayList<>(List.of(
                new point(gui.width / gui.scaleFactor - 5, 92),
                new point(gui.width / gui.scaleFactor - 1, 95),
                new point(gui.width / gui.scaleFactor - 5, 98)));
        play = new Button((int) (gui.width / gui.scaleFactor - 5), 92, (int) (gui.width / gui.scaleFactor - 1), 98, "");
        quitButton = new Button((int) (gui.width / gui.scaleFactor - 15), 5, (int) (gui.width / gui.scaleFactor - 5), 15, "quit");
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
        Shop = new Shop(gui, this);
    }

    @Override
    public void run() {
        boolean notQuit = true;
        while (notQuit) {
            gui.setColor(Color.BLACK);
            gui.g.fillRect(gui.convertX(100), 0, (gui.width - gui.height), gui.height);
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
                Shop.drawTowerCreator();
            }
            else if (selectedTile != null) {
                Shop.drawTileShopUI();
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
            if (!wave) {
                drawPath();
            }
            notQuit = !quitButton.drawButtonAndReturnTrueIfPressed(gui, mx, my, mp);
            gui.update();
            gui.g.setFont(f);
        }
        gui.frame.dispatchEvent(new WindowEvent(gui.frame, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyEventHandler.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyEventHandler.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {

        keyEventHandler.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        mouseEventHandler.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseEventHandler.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseEventHandler.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        mouseEventHandler.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {

        mouseEventHandler.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        mouseEventHandler.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseEventHandler.mouseMoved(e);
    }

    public void pathFind() {
        if (portal != null && crystal != null) {
            boolean ended = false;
            HashSet<Tile> remove = new HashSet<>();
            HashMap<Tile, Tile> explore = new HashMap<>(Map.of(portal, portal));
            while(!ended) {
                HashMap<Tile, Tile> explores = new HashMap<>();
                if (remove.size() == explore.keySet().size()){
                    ended = true;
                    path = new ArrayList<>();
                }
                for (Tile t : explore.keySet()) {
                    if (!remove.contains(t)) {
                        if (Objects.equals(t.type, "crystal")) {
                            path = new ArrayList<>();
                            boolean e = true;
                            Tile x = t;
                            while(e){
                                path.add(new point(x.x + 5, x.y + 5));
                                if (explore.get(x) == x){
                                    e = false;
                                }
                                else{
                                    x = explore.get(x);
                                }
                            }
                            Collections.reverse(path);
                            ended = true;
                        }
                        else {
                            if (t.x != 90) {
                                if ((Objects.equals(tileGrid.get(t.x / 10 + 1).get(t.y / 10).type, "road") ||
                                        Objects.equals(tileGrid.get(t.x / 10 + 1).get(t.y / 10).type, "crystal"))
                                        && !(explore.containsKey(tileGrid.get(t.x / 10 + 1).get(t.y / 10)) || explores.containsKey(tileGrid.get(t.x / 10 + 1).get(t.y / 10)))) {
                                    explores.put(tileGrid.get(t.x / 10 + 1).get(t.y / 10), t);
                                }
                            }
                            if (t.y != 90) {
                                if ((Objects.equals(tileGrid.get(t.x / 10).get(t.y / 10 + 1).type, "road") ||
                                        Objects.equals(tileGrid.get(t.x / 10).get(t.y / 10 + 1).type, "crystal"))
                                        && !explore.containsKey(tileGrid.get(t.x / 10).get(t.y / 10+1))
                                        && !explores.containsKey(tileGrid.get(t.x / 10).get(t.y / 10+1))) {
                                    explores.put(tileGrid.get(t.x / 10).get(t.y / 10 + 1), t);
                                }
                            }
                            if (t.x != 0) {
                                if ((Objects.equals(tileGrid.get(t.x / 10 - 1).get(t.y / 10).type, "road") ||
                                        Objects.equals(tileGrid.get(t.x / 10 - 1).get(t.y / 10).type, "crystal"))
                                        && !explore.containsKey(tileGrid.get(t.x / 10 - 1).get(t.y / 10))
                                        && !explores.containsKey(tileGrid.get(t.x / 10 - 1).get(t.y / 10))) {
                                    explores.put(tileGrid.get(t.x / 10 - 1).get(t.y / 10), t);
                                }
                            }
                            if (t.y != 0) {
                                if ((Objects.equals(tileGrid.get(t.x / 10).get(t.y / 10 - 1).type, "road") ||
                                        Objects.equals(tileGrid.get(t.x / 10).get(t.y / 10 - 1).type, "crystal"))
                                        && !explore.containsKey(tileGrid.get(t.x / 10).get(t.y / 10 - 1))
                                        && !explores.containsKey(tileGrid.get(t.x / 10).get(t.y / 10 - 1))) {
                                    explores.put(tileGrid.get(t.x / 10).get(t.y / 10 - 1), t);
                                }
                            }
                        }
                        remove.add(t);
                    }
                }
                explore.putAll(explores);
            }
        }
    }

    public ArrayList<point> getPlayButton() {
        return playButton;
    }
    public void setMp(boolean b){
        mp = b;
    }
    public void drawPath(){
        if (crystal != null && portal != null && path != null) {
            gui.setColor(Main.c3);
            point p1 = null;
            for (point p : path) {
                if (p1 != null) {
                    if (p1.x > p.x || p1.y > p.y) {
                        gui.rect(p.x - 1, p.y - 1, p1.x + 1, p1.y + 1);
                    } else {
                        gui.rect(p1.x - 1, p1.y - 1, p.x + 1, p.y + 1);
                    }
                }
                p1 = p;
            }
        }
    }
    public void drawPlayButton(){
        if (crystal != null && portal != null && path != null) {
        if (play.drawButtonAndReturnTrueIfPressed(gui, mx, my, mp) && !wave) {
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
    // getters and setters, folded for brevity
    public graphicalInterface getGui() {
        return gui;
    }

    public MouseEventHandler getMouseEventHandler() {
        return mouseEventHandler;
    }

    public ArrayList<point> getPath() {
        return path;
    }

    public void setPath(ArrayList<point> path) {
        this.path = path;
    }

    public Tile getPortal() {
        return portal;
    }

    public void setPortal(Tile portal) {
        this.portal = portal;
    }

    public Tile getCrystal() {
        return crystal;
    }

    public void setCrystal(Tile crystal) {
        this.crystal = crystal;
    }

    public int getMx() {
        return mx;
    }

    public void setMx(int mx) {
        this.mx = mx;
    }

    public int getCounter1() {
        return counter1;
    }

    public void setCounter1(int counter1) {
        this.counter1 = counter1;
    }

    public int getCounter2() {
        return counter2;
    }

    public void setCounter2(int counter2) {
        this.counter2 = counter2;
    }

    public int getCounter3() {
        return counter3;
    }

    public void setCounter3(int counter3) {
        this.counter3 = counter3;
    }

    public boolean isMp() {
        return mp;
    }

    public boolean isTowerCreator() {
        return towerCreator;
    }

    public void setTowerCreator(boolean towerCreator) {
        this.towerCreator = towerCreator;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getMy() {
        return my;
    }

    public void setMy(int my) {
        this.my = my;
    }

    public Button getPlay() {
        return play;
    }

    public Tower getcT() {
        return cT;
    }

    public void setcT(Tower cT) {
        this.cT = cT;
    }

    public ArrayList<Button> getTowButtons() {
        return towButtons;
    }

    public HashMap<String, Integer> getPrices() {
        return prices;
    }

    public boolean isWave() {
        return wave;
    }

    public void setWave(boolean wave) {
        this.wave = wave;
    }

    public ArrayList<Tile> getShopTiles() {
        return shopTiles;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Tile selectedTile) {
        this.selectedTile = selectedTile;
    }

    public ArrayList<ArrayList<Tile>> getTileGrid() {
        return tileGrid;
    }

    public Button getQuitButton() {
        return quitButton;
    }

}
