package com.company;

import java.awt.*;
import java.util.Objects;

public class Shop {
    graphicalInterface gui;
    game game;
    Shop(graphicalInterface gui, game game){
        this.game = game;
        this.gui = gui;
    }
    public void drawTowerCreator(){
        gui.g.setFont(com.company.game.f);
        gui.setColor(Color.white);
        gui.text("Tower Maker: Coins: " + game.coins, 110, 10);
        game.selectedTile.draw(gui, this.game);
        game.cT.draw(gui, this.game, false);
        gui.setColor(Main.c1);
        gui.circle(game.selectedTile.x + 5, game.selectedTile.y + 5, game.cT.stats.get("Range"));
        gui.setColor(Color.white);
        gui.text(game.cT.base, 130, 35);
        gui.text(game.cT.gunType, 130, 25);
        gui.text(game.cT.projectileType, 130, 45);
        gui.text(String.valueOf(game.cT.cost()), 130, 65);
        for (int i = 0; i < game.cT.stats.size(); i++) {
            gui.text("" + game.cT.stats.entrySet().toArray()[i], gui.width / gui.scaleFactor - 25, 60 + i * 5);
        }
        for (Button b : game.towButtons) {
            if (b.drawButtonAndReturnTrueIfPressed(gui, game.mx, game.my, game.mp)) {
                game.mp = false;
                if (b.y == 30) {
                    if (Objects.equals(b.text, "<")) {
                        game.counter2 -= 1;
                        if (game.counter2 < 0) {
                            game.counter2 = Tower.bases.size() - 1;
                        }
                    } else {
                        game.counter2 += 1;
                        if (game.counter2 >= Tower.bases.size()) {
                            game.counter2 = 0;
                        }
                    }
                    game.cT.base = Tower.bases.get(game.counter2);
                    game.cT.computeStats(gui, false);
                }
                if (b.y == 40) {
                    if (Objects.equals(b.text, "<")) {
                        game.counter3 -= 1;
                        if (game.counter3 < 0) {
                            game.counter3 = Projectile.colorTypes.keySet().toArray().length - 1;
                        }
                    } else {
                        game.counter3 += 1;
                        if (game.counter3 >= Projectile.colorTypes.keySet().toArray().length) {
                            game.counter3 = 0;
                        }
                    }
                    game.cT.projectileType = (String) Projectile.colorTypes.keySet().toArray()[game.counter3];
                    game.cT.computeStats(gui, false);
                } else if (b.y == 20) {
                    if (Objects.equals(b.text, "<")) {
                        game.counter1 -= 1;
                        if (game.counter1 < 0) {
                            game.counter1 = Model.models.size() - 1;
                        }
                    } else {
                        game.counter1 += 1;
                        if (game.counter1 >= Model.models.size()) {
                            game.counter1 = 0;
                        }
                    }
                    game.cT.gunType = (String) Model.models.keySet().toArray()[game.counter1];
                    game.cT.computeStats(gui, true);
                } else if (b.y == 60 && game.coins >= game.cT.cost()) {
                    game.coins -= game.cT.cost();
                    game.selectedTile.hasTower = true;
                    game.selectedTile.tower = game.cT.finalized(game.selectedTile.x, game.selectedTile.y);
                    game.towerCreator = false;
                    game.cT.computeStats(gui, true);
                    game.cT = null;
                }
            }
        }
    }

    public void drawTileShopUI() {
        gui.g.setFont(com.company.game.f);
        gui.setColor(Color.white);
        gui.text("Shop:           Coins: " + game.coins, 110, 10);
        game.selectedTile.draw(gui, game);
        if (!game.selectedTile.hasTower) {
            for (Tile t : game.shopTiles) {
                t.draw(gui, game);
                gui.setColor(Color.white);
                gui.text(String.valueOf(game.prices.get(t.type)), t.x + 2.5, t.y + 15);
            }
            gui.setColor(Color.lightGray);
            gui.rect(110, 70, (gui.width) / gui.scaleFactor - 10, 90);
            gui.setColor(Color.darkGray);
            gui.text("Build Tower", 130, 80);
        }
    }
}
