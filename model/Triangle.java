package model;

import controller.lineState;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;


public class Triangle extends Drawable {
    public Triangle(int[] x, int[] y, Color c, lineState nLineState, float lineThick) {
        super(nLineState, c, lineThick);
        this.npoints = 3;
        System.arraycopy(x, 0, this.xpoints, 0, this.npoints);
        System.arraycopy(y, 0, this.ypoints, 0, this.npoints);
    }

    public void setSize(int mouseX, int mouseY) {
        this.xpoints[1] = mouseX;
        Point mPoint; 

        if (this.xpoints[0] > this.xpoints[1]) {
            mPoint = new Point((this.xpoints[1] + (Math.abs(this.xpoints[0] - this.xpoints[1])/2)), mouseY);
        }
        else {
            mPoint = new Point((this.xpoints[1] - (Math.abs(this.xpoints[0] - this.xpoints[1])/2)), mouseY);
        }
        this.xpoints[2] = mPoint.x;
        this.ypoints[2] = mPoint.y;
    }

    public boolean isCornerDrawable(int mouseX, int mouseY) {
        if(this.xpoints[0] >= mouseX - 10 && this.xpoints[0] <= mouseX + 10 && this.ypoints[0] >= mouseY - 10 && this.ypoints[0] <= mouseY + 10) {
            return true;
        } else if (this.xpoints[1] >= mouseX - 10 && this.xpoints[1] <= mouseX + 10 && this.ypoints[1] >= mouseY - 10 && this.ypoints[1] <= mouseY + 10) {
            return true;
        } else if (this.xpoints[2] >= mouseX - 10 && this.xpoints[2] <= mouseX + 10 && this.ypoints[2] >= mouseY - 10 && this.ypoints[2] <= mouseY + 10) {
            return true;
        }
        return false;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
		g2.setColor(this.c);
        //g2.setStroke(this.lStroke);
        switch(this.lState) {
            case EMPTY:
                g2.drawPolygon(xpoints, ypoints, npoints);
            break;
            case FILL:
                g2.fillPolygon(xpoints, ypoints, npoints);
            break;
        }
	}
}
