package com.company;

import java.awt.*;

public class Button {
    public final int x;
    public final int y;
    public final int hx;
    public final int hy;
    public final int width;
    public final int height;
    public final String text;

    Button(int x1, int y1, int x2, int y2, String text1) {
        x = x1;
        y = y1;
        hx = x2;
        hy = y2;
        text = text1;
        width = Math.abs(x - hx);
        height = Math.abs(y - hy);
    }

    public boolean drawButtonAndReturnTrueIfPressed(graphicalInterface gui, int mouseX, int mouseY, boolean mp) {
        gui.setColor(Color.white);
        boolean t = false;
        if (mouseX < hx && mouseX > x && mouseY < hy && mouseY > y) {
            gui.setColor(Color.gray);
            if (mp) {
                t = true;
            }
        }
        gui.g.setFont(game.f);
        gui.text(text, x + (width >> 1), y + (height >> 1));
        return t;
    }
}