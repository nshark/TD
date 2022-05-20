package com.company;

import java.awt.*;

public class Main {
    public static Color c1 = new Color(128, 128, 128, 100);
    public static Color c2 = new Color(128, 128, 128);
    public static Color c3 = new Color(255,255,255,200);
    public static void main(String[] args) {
	    //setup graphics
        graphicalInterface gui = new graphicalInterface();
        //create a new instance of the game
        game g = new game(gui);

    }
}
