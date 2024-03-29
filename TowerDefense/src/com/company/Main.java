package com.company;

import java.awt.*;

public class Main {
    public static final Color c1 = new Color(128, 128, 128, 100);
    public static final Color c3 = new Color(255, 255, 255, 200);

    public static void main(String[] args) {
        //load in models
        Model.initModels();
        //setup graphics
        graphicalInterface gui = new graphicalInterface();
        //create a new instance of the game
        game g = new game(gui);
        new Thread(g).start();
    }
}
