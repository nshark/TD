package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.*;

public class graphicalInterface {
    public final JFrame frame;
    public final Canvas canvas;
    public final int width;
    public final int height;
    public final float scaleFactor;
    public Graphics2D g;

    graphicalInterface() {
        //new frame, canvas and panel
        frame = new JFrame("Tower Defense");
        canvas = new Canvas();
        //setup each
        frame.add(canvas);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //make it full screen
        frame.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        frame.setVisible(true);
        frame.setResizable(false);
        canvas.setBounds(frame.getBounds());
        canvas.setVisible(true);
        canvas.createBufferStrategy(2);
        canvas.requestFocus();
        canvas.setIgnoreRepaint(true);
        width = canvas.getWidth();
        height = canvas.getHeight();
        scaleFactor = ((float) height) / 100;
        g = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
        //get a new graphics2d, g
    }

    public void graphicTick() {
        g = (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
        g.clearRect(0, 0, width, height);
    }

    public void update() {
        //advance the canvas a frame
        canvas.getBufferStrategy().show();
        canvas.update(g);
        g.dispose();
        this.graphicTick();
    }

    public void setColor(Color c) {
        g.setColor(c);
    }

    public void rect(double x, double y, double x2, double y2) {
        g.fillRect(convertX(x), convertY(y), (int) round((x2 - x) * scaleFactor), (int) round(scaleFactor * (y2 - y)));
    }

    public void outlineRect(double x, double y, double x2, double y2) {
        g.drawRect(convertX(x), convertY(y), (int) round((x2 - x) * scaleFactor), (int) round(scaleFactor * (y2 - y)));
    }

    public int convertX(double x) {
        return (int) round((x * scaleFactor));
    }

    public int convertY(double y) {
        return (int) round((y * scaleFactor));
    }

    public void circle(double x, double y, double r) {
        g.fillOval(convertX(x - r), convertY(y - r), (int) round(2 * r * scaleFactor), (int) round(2 * r * scaleFactor));
    }
    public void outlineCircle(double x, double y, double r) {
        g.drawOval(convertX(x - r), convertY(y - r), (int) round(2 * r * scaleFactor), (int) round(2 * r * scaleFactor));
    }
    public void text(String s, double x, double y) {
        g.drawString(s, convertX(x), convertY(y));
    }

    public void rotate(double originX, double originY, ArrayList<point> offsets, double h) {
        for (point offset : offsets) {
            double x1 = offset.x - originX;
            double y1 = offset.y - originY;
            offset.x = x1 * cos(h) - y1 * sin(h) + originX;
            offset.y = x1 * sin(h) + y1 * cos(h) + originY;
        }
    }

    public void poly(ArrayList<point> points) {
        ArrayList<Integer> Xs = new ArrayList<>();
        ArrayList<Integer> Ys = new ArrayList<>();
        for (point p : points) {
            Xs.add(convertX(p.x));
            Ys.add(convertY(p.y));
        }
        g.fillPolygon(Xs.stream().mapToInt(i -> i).toArray(), Ys.stream().mapToInt(i -> i).toArray(), points.size());
    }
    public void line(double x1, double y1, double x2, double y2){
        g.drawLine(convertX(x1), convertY(y1), convertX(x2), convertY(y2));
    }
}
