package com.company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class KeyEventHandler implements KeyListener {
    private final com.company.game game;

    public KeyEventHandler(com.company.game game) {
        this.game = game;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (game.getSelectedTile() != null) {
            if (com.company.game.types.containsKey(e.getKeyChar())) {
                if (!Objects.equals(game.getSelectedTile().type, "portal") && !Objects.equals(game.getSelectedTile().type, "crystal") && !game.getSelectedTile().hasTower) {
                    if (game.getPrices().get(com.company.game.types.get(e.getKeyChar())) * -1 <= game.getCoins() && !Objects.equals(game.getSelectedTile().type, com.company.game.types.get(e.getKeyChar()))) {
                        game.setCoins(game.getCoins() + game.getPrices().get(com.company.game.types.get(e.getKeyChar())));
                        game.getSelectedTile().type = com.company.game.types.get(e.getKeyChar());
                        if (Objects.equals("portal", com.company.game.types.get(e.getKeyChar()))) {
                            if (game.getPortal() != null) {
                                game.getPortal().type = "grass";
                            }
                            game.setPortal(game.getSelectedTile());
                        }
                        if (Objects.equals("crystal", com.company.game.types.get(e.getKeyChar()))) {
                            if (game.getCrystal() != null) {
                                game.getCrystal().type = "grass";
                            }
                            game.setCrystal(game.getSelectedTile());
                        }
                        game.pathFind();
                    }
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (game.getSelectedTile() != null) {
            if ((e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP) && game.getSelectedTile().y != 0) {
                game.setSelectedTile(game.getTileGrid().get(game.getSelectedTile().x / 10).get(game.getSelectedTile().y / 10 - 1));
                if(game.towerCreator){
                    game.towerCreator = false;
                }
            } else if ((e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) && game.getSelectedTile().x != 0) {
                game.setSelectedTile(game.getTileGrid().get(game.getSelectedTile().x / 10 - 1).get(game.getSelectedTile().y / 10));
                if(game.towerCreator){
                    game.towerCreator = false;
                }
            } else if ((e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) && game.getSelectedTile().x != 90) {
                game.setSelectedTile(game.getTileGrid().get(game.getSelectedTile().x / 10 + 1).get(game.getSelectedTile().y / 10));
                if(game.towerCreator){
                    game.towerCreator = false;
                }
            } else if ((e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN) && game.getSelectedTile().y != 90) {
                game.setSelectedTile(game.getTileGrid().get(game.getSelectedTile().x / 10).get(game.getSelectedTile().y / 10 + 1));
                if(game.towerCreator){
                    game.towerCreator = false;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}