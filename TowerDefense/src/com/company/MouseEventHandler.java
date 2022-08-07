package com.company;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Objects;

public class MouseEventHandler implements MouseListener, MouseMotionListener {
    private final com.company.game game;

    public MouseEventHandler(com.company.game game) {
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        game.setMp(true);
        for (ArrayList<Tile> tiles : game.getTileGrid()) {
            for (Tile t : tiles) {
                if (t.mouseOver(game.getMx(), game.getMy())) {
                    if (t == game.getSelectedTile() && t.hasTower) {
                        game.setCoins(game.getCoins() + t.tower.cost());
                        t.hasTower = false;
                        t.tower = null;
                    } else {
                        game.setSelectedTile(t);
                        game.setTowerCreator(false);
                        game.setMp(false);
                    }
                }
            }
        }
        if (game.getSelectedTile() != null) {
            if (!game.getSelectedTile().hasTower && !Objects.equals(game.getSelectedTile().type, "portal") && !Objects.equals(game.getSelectedTile().type, "crystal") && !game.isWave() && !game.isTowerCreator()) {
                for (Tile t : game.getShopTiles()) {
                    if (t.mouseOver(game.getMx(), game.getMy())) {
                        if (game.getPrices().get(t.type) * -1 <= game.getCoins() && !Objects.equals(game.getSelectedTile().type, t.type)) {
                            game.setCoins(game.getCoins() + game.getPrices().get(t.type));
                            game.getSelectedTile().type = t.type;
                            game.setMp(false);
                            if (Objects.equals("portal", t.type)) {
                                if (game.getPortal() != null) {
                                    game.getPortal().type = "grass";
                                }
                                game.setPortal(game.getSelectedTile());
                            }
                            if (Objects.equals("crystal", t.type)) {
                                if (game.getCrystal() != null) {
                                    game.getCrystal().type = "grass";
                                }
                                game.setCrystal(game.getSelectedTile());
                            }
                            game.pathFind();
                        }
                    }
                }
                if (game.getMx() <= (game.getGui().width) / game.getGui().scaleFactor - 10 && game.getMx() >= 110 && game.getMy() >= 70 && game.getMy() <= 90 && Objects.equals(game.getSelectedTile().type, "grass")) {
                    game.setcT(new Tower());
                    game.getcT().computeStats(game.getGui(), true);
                    game.setTowerCreator(true);
                    game.setCounter2(0);
                    game.setCounter1(0);
                    game.setMp(false);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        game.setMp(false);
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
        game.setMx((int) (e.getX() / game.getGui().scaleFactor));
        game.setMy((int) (e.getY() / game.getGui().scaleFactor));
    }
}