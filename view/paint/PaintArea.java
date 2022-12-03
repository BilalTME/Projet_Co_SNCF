package view.paint;

import controller.*;
import model.Drawable;
import model.Rectangle;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;

public class PaintArea extends JPanel implements MouseInputListener {
    private Application controller;
    private drawState state;

    private int offsetX = 0, offsetY = 0;

    public PaintArea(Application newController) {
        this.setBackground(java.awt.Color.WHITE);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.controller = newController;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.state = this.controller.getDrawState();
        switch(this.state) {
            case ELLIPSE:
                this.controller.setSelectedDrawable(null);
                this.controller.createEllipse(e.getX(), e.getY());
            break;
            case LINE:
                this.controller.setSelectedDrawable(null);
                this.controller.createLine(e.getX(), e.getY());
            break;
            case RECTANGLE:
                this.controller.setSelectedDrawable(null);
                this.controller.createRectangle(e.getX(), e.getY());
            break;
            case TRIANGLE:
                this.controller.setSelectedDrawable(null);
                this.controller.createTriangle(e.getX(), e.getY());
            break;
            case SELECTED:
                this.controller.setSelectedDrawable(null);
                ArrayList<Drawable> drawableArray = this.controller.getDrawable();
                for(int i = drawableArray.size()-1; i >= 0; i--) {
                    if(drawableArray.get(i).contains(new Point(e.getX(), e.getY()))) {
                        this.controller.setSelectedDrawable(drawableArray.get(i));
                        if(drawableArray.get(i).isCornerDrawable(e.getX(), e.getY())) {
                            this.controller.setDrawState(drawState.RESIZE);
                        } else {
                            offsetX = e.getX() - controller.getSelectedDrawable().xpoints[0];
                            offsetY = e.getY() - controller.getSelectedDrawable().ypoints[0];
                        }
                        break;
                    }
                }
            break;
        }
        this.controller.getDisplay().repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.state = this.controller.getDrawState();
        if(this.state != drawState.SELECTED) {
            if(this.state != drawState.RESIZE) {
                this.controller.setSelectedDrawable(null);
            } else {
                this.controller.resizeBoundSelectedDrawable();
                this.controller.setDrawState(drawState.SELECTED);
            } 
        } else {
            offsetX = 0;
            offsetY = 0;
        }
        this.controller.getDisplay().repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        state = controller.getDrawState();
        if(state != drawState.SELECTED) {
            if(controller.getSelectedDrawable() != null) {
                controller.setSizeSelectedDrawable(e.getX(), e.getY());
            }
        } else {
            if(controller.getSelectedDrawable() != null) {
                controller.setPositionSelectedDrawable(e.getX(), e.getY(), offsetX, offsetY);
            }
        }
        this.controller.getDisplay().repaint();
    }


    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; 
        ArrayList<Drawable> drawableArray = this.controller.getDrawable();
        for(int i = 0; i < drawableArray.size(); i++) {
            g2.setStroke(drawableArray.get(i).getStroke());
            drawableArray.get(i).paint(g2);
        }
        if(state == drawState.SELECTED || state == drawState.RESIZE) {
            if(controller.getSelectedDrawable() != null) {
                Rectangle test = controller.getBoxSelect();
                if(test != null) {
                    if(controller.getSelectedDrawable().xpoints == test.xpoints) {
                        g2.setStroke(test.getStroke());
                        test.paint(g2);
                    }
                }
            }
        }
    }
    
    // Non utilisé
    @Override
    public void mouseClicked(MouseEvent arg0) {}

    @Override
    public void mouseEntered(MouseEvent arg0) {}

    @Override
    public void mouseExited(MouseEvent arg0) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}