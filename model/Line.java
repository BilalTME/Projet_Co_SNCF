package model;

import controller.lineState;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;

public class Line extends Drawable {
    public Line(int[] xs, int[] ys, Color c, lineState nLineState, float lineThick) {
        super(nLineState, c, lineThick);
        this.npoints = 4;
        System.arraycopy(xs, 0, this.xpoints, 0, this.npoints);
        System.arraycopy(ys, 0, this.ypoints, 0, this.npoints);
    }

    public void setSize(int mouseX, int mouseY) {
        //On défini les coordonnées bas-droite
        this.xpoints[2] = mouseX;
        this.ypoints[2] = mouseY;

        //Haut-droit
        this.xpoints[1] = mouseX;
        this.ypoints[1] = this.ypoints[0];
        
        //Bas-gauche
        this.ypoints[3] = mouseY;
        this.xpoints[3] = this.xpoints[0];
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
		g2.setColor(this.c);
        //g2.setStroke(this.lStroke);
        g2.drawLine(xpoints[0], ypoints[0], xpoints[2], ypoints[2]);
	}
}