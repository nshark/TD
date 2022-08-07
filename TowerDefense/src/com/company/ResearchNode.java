package com.company;

import java.awt.*;

import static java.lang.Math.pow;

public class ResearchNode {
    public final String name;
    public final ResearchNode precede;
    public final ResearchTree parent;
    public final int cost;
    public final String stat;
    public final double effect;
    public boolean upgraded = false;
    public final double x;
    public final double y;
    ResearchNode(String name, ResearchNode precede, int cost, String stat, double effect, ResearchTree parent, double x, double y){
        this.name = name;
        this.precede = precede;
        this.cost = cost;
        this.stat = stat;
        this.effect = effect;
        this.parent = parent;
        this.x = x;
        this.y = y;
    }
    public void draw(graphicalInterface gui, game game){
        if (upgraded){
            gui.setColor(Color.green);
            if (precede != null){
                gui.line(x,y,precede.x, precede.y-2.5);
            }
        }
        else if(precede != null){
            if (precede.upgraded){
                gui.setColor(Color.yellow);
                gui.line(x,y,precede.x, precede.y-2.5);
            }
            else{
                gui.setColor(Color.RED);
                gui.line(x,y,precede.x, precede.y-2.5);
            }
        }
        else {
            gui.setColor(Color.yellow);
        }
        if (pow(x - game.mx, 2) + pow(y - game.my, 2) <= 36 && !upgraded){
            gui.outlineCircle(x, y, 8.5);
            if (precede != null) {
                if (game.mp && precede.upgraded && game.coins >= cost) {
                    game.mp = false;
                    game.coins -= cost;
                    this.upgraded = true;
                    parent.tower.stats.replace(stat, parent.tower.stats.get(stat) * effect);
                }
            }
            else{
                if (game.mp && game.coins >= cost){
                    game.mp = false;
                    game.coins -= cost;
                    this.upgraded = true;
                    parent.tower.stats.replace(stat, parent.tower.stats.get(stat) * effect);
                }
            }
        }
        gui.circle(x,y,7.5);
        gui.setColor(Color.black);
        gui.text(name, x-5, y-1.25);
        gui.text("Cost: "+cost, x-6,y+1.5);
    }
}
