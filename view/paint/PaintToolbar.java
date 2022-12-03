package view.paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JToolBar;

import controller.*;

public class PaintToolbar extends JToolBar {
    private Application controller;

    private JButton bBackgroundColorChooser;
    private JButton bEllipse;
    private JButton bRectangle;
    private JButton bTriangle;
    private JButton bLine;
    private JButton bChangeLineState;
    private JButton bSelected;
    private JButton bAddThick;
    private JButton bMinusThick;

    public PaintToolbar(Application newController) {
        this.controller = newController;

        //Bouton pour les cercles
        this.bEllipse = new JButton(new ImageIcon("assets/circle_fill.png"));
        this.bEllipse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDrawState(drawState.ELLIPSE);
            }
        });

        //Bouton pour les rectangle
        this.bRectangle = new JButton(new ImageIcon("assets/rectangle_fill.png"));
        this.bRectangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDrawState(drawState.RECTANGLE);
            }
        });

        this.bLine = new JButton(new ImageIcon("assets/line.png"));
        this.bLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDrawState(drawState.LINE);
            }
        });

        //Bouton pour les triangles
        this.bTriangle = new JButton(new ImageIcon("assets/triangle_fill.png"));
        this.bTriangle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDrawState(drawState.TRIANGLE);
            }
        });

        //Bouton pour les triangles
        this.bChangeLineState = new JButton(new ImageIcon("assets/rectangle_stroke.png"));
        this.bChangeLineState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controller.getSelectedDrawable() == null) {
                    if(controller.getLineState().equals(lineState.EMPTY)) {
                        bEllipse.setIcon(new ImageIcon("assets/circle_fill.png"));
                        bRectangle.setIcon(new ImageIcon("assets/rectangle_fill.png"));
                        bTriangle.setIcon(new ImageIcon("assets/triangle_fill.png"));
                        bChangeLineState.setIcon(new ImageIcon("assets/rectangle_stroke.png"));
                    } else {
                        bEllipse.setIcon(new ImageIcon("assets/circle_stroke.png"));
                        bRectangle.setIcon(new ImageIcon("assets/rectangle_stroke.png"));
                        bTriangle.setIcon(new ImageIcon("assets/triangle_stroke.png"));
                        bChangeLineState.setIcon(new ImageIcon("assets/rectangle_fill.png"));
                    }
                }
                controller.setLineState();
                controller.getDisplay().repaint();
            }
        });

        this.bBackgroundColorChooser = new JButton(new ImageIcon("assets/color-picker.png"));
        this.bBackgroundColorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                java.awt.Color c = JColorChooser.showDialog(controller.getDisplay(), "ColorPicker", controller.getColor());
                controller.setColor(c);
                controller.getDisplay().repaint();
            }
        });

        this.bSelected = new JButton(new ImageIcon("assets/cursor.png"));
        this.bSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setDrawState(drawState.SELECTED);
            }
        });

        this.bAddThick = new JButton(new ImageIcon("assets/plus.png"));
        this.bAddThick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controller.getLineThick() < 100.f) {
                    controller.setStroke(controller.getLineThick()+1.f);
                    controller.getDisplay().repaint();
                }
            }
        });
        this.bMinusThick = new JButton(new ImageIcon("assets/minus.png"));
        this.bMinusThick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(controller.getLineThick() > 1.f) {
                    controller.setStroke(controller.getLineThick()-1.f);
                    controller.getDisplay().repaint();
                }
            }
        });

        //Rendre la barre non déplacable
        this.setFloatable(false);

        //Ajout des boutons à la toolbar
        this.add(this.bChangeLineState);
        this.add(this.bBackgroundColorChooser);
        this.add(this.bEllipse);
        this.add(this.bLine);
        this.add(this.bRectangle);
        this.add(this.bTriangle);
        this.add(this.bAddThick);
        this.add(this.bMinusThick);
        this.add(this.bSelected);

        //Mise en forme de haut en bas
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}