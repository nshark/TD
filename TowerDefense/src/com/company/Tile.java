package com.company;

import java.awt.*;
import java.util.Objects;

public class Tile implements Drawable, Clickable {
    public String type;
    public final int x;
    public final int y;
    public boolean hasTower = false;
    public Tower tower = null;

    Tile(String t, int x1, int y1) {
        type = t;
        x = x1;
        y = y1;
    }
    public void draw(graphicalInterface gui, game game) {
        if (Objects.equals(type, "road")) {
            gui.setColor(Color.darkGray);
            gui.rect(x, y, x + 10, y + 10);
        } else if (Objects.equals(type, "grass")) {
            gui.setColor(Color.green);
            gui.rect(x, y, x + 10, y + 10);
        }
        if (Objects.equals(type, "portal")) {
            gui.setColor(Color.darkGray);
            gui.rect(x, y, x + 10, y + 10);
            gui.setColor(Color.MAGENTA);
            gui.circle(x + 5, y + 5, 4);
        } else if (Objects.equals(type, "crystal")) {
            gui.setColor(Color.darkGray);
            gui.rect(x, y, x + 10, y + 10);
            gui.setColor(Color.cyan);
            gui.circle(x + 5, y + 5, 4);
        }
        if (game.selectedTile == this) {
            gui.setColor(Color.yellow);
        } else {
            gui.setColor(Color.black);
        }
        gui.outlineRect(x, y, x + 10, y + 10);
        if (hasTower) {
            tower.draw(gui, game, game.selectedTile == this);
        }
    }
    public boolean mouseOver(int mx, int my) {
        return mx >= x && my >= y && mx <= x + 10 && my <= y + 10;
    }
}
