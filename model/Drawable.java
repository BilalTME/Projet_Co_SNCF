package model;

import controller.lineState;

import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;


public class Drawable extends Polygon {
    protected lineState lState;
    protected Color c;
    transient protected BasicStroke lStroke;
    protected float lThick;

    public Drawable(lineState nLineState, Color nColor, float lineThick) {
        this.lState = nLineState;
        this.c = nColor;
        this.lThick = lineThick;
        this.lStroke = new BasicStroke(lineThick);
    }

    public void setColor(Color c) {
        this.c = c;
    }

    public void setLineState(lineState nLineState) {
        this.lState = nLineState;
    }

    public void setStroke(float value) {
        this.lThick = value;
        this.lStroke = new BasicStroke((float) value);
    }

    public void setStroke(){
        this.lStroke = new BasicStroke(this.lThick);
    }

    public Color getColor() {
        return this.c;
    }

    public BasicStroke getStroke() {
        return this.lStroke;
    }


    public float getLineThick() {
        return this.lThick;
    }

    public lineState getLineState() {
        return this.lState;
    }

    public void move(int mouseX, int mouseY, int offsetX, int offsetY) {
        translate(mouseX - offsetX - this.xpoints[0], mouseY - offsetY - this.ypoints[0]);
    }

    public void resizeBounds() {
        this.bounds.x = Math.min(this.xpoints[0], this.xpoints[1]);
        this.bounds.y = Math.min(this.ypoints[0], this.ypoints[2]);
        this.bounds.width = Math.max(this.xpoints[0], this.xpoints[1]) - this.bounds.x;
        this.bounds.height = Math.max(this.ypoints[0], this.ypoints[2]) - this.bounds.y;
    }

    public boolean isCornerDrawable(int mouseX, int mouseY) {
        if (this.xpoints[3] >= mouseX - 10 && this.xpoints[3] <= mouseX + 10 && this.ypoints[3] >= mouseY - 10 && this.ypoints[3] <= mouseY + 10) {
            return true;
        } else if (this.xpoints[0] >= mouseX - 10 && this.xpoints[0] <= mouseX + 10 && this.ypoints[0] >= mouseY - 10 && this.ypoints[0] <= mouseY + 10) {
            return true;
        } else if (this.xpoints[1] >= mouseX - 10 && this.xpoints[1] <= mouseX + 10 && this.ypoints[1] >= mouseY - 10 && this.ypoints[1] <= mouseY + 10) {
            return true;
        } else if (this.xpoints[2] >= mouseX - 10 && this.xpoints[2] <= mouseX + 10 && this.ypoints[2] >= mouseY - 10 && this.ypoints[2] <= mouseY + 10) {
            return true;
        }
        return false;
    }

    public void setSize(int mouseX, int mouseY) {}


    public void paint(Graphics g) {}
}
