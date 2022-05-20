package com.company;

import java.awt.*;

public class Button {
    public int x;
    public int y;
    public int hx;
    public int hy;
    public int width;
    public int height;
    public String text;

    Button(int x1, int y1, int x2, int y2, String text1) {
        x = x1;
        y = y1;
        hx = x2;
        hy = y2;
        text = text1;
        width = Math.abs(x - hx);
        height = Math.abs(y - hy);
    }

    public boolean draw(graphicalInterface gui, int mouseX, int mouseY, boolean mp) {
        boolean t = false;
        if (mouseX < hx && mouseX > x && mouseY < hy && mouseY > y) {
            if (mp) {
                t = true;
            }
        }
        gui.setColor(Color.white);
        gui.g.setFont(game.f);
        gui.text(text, x + width / 2, y + height / 2);
        return t;
    }
}