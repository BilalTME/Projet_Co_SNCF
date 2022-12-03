package controller;
import java.util.ArrayList;

import model.Drawable;
import model.Ellipse;
import model.Line;
import model.Rectangle;
import model.Triangle;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.awt.BasicStroke;

import view.Display;

public class Application {
    private Display display;

    private ArrayList<Drawable> dArrayList;
    private Rectangle boxSelectRect;

    private drawState dState = drawState.RECTANGLE;
    private lineState lState = lineState.FILL;

    private Color bColor = Color.black;
    private BasicStroke lineStroke = new BasicStroke(1.0f);
    private float lineThick = 1;

    private Drawable selectDrawable;

    //Référence vers l'objet copié
    private Drawable copyDrawable = null;

    //Copie des coordonées de la référence - Initialiser à 4 * 0 pour initialiser la mémoire;
    private int xs[] = {0, 0, 0, 0};
    private int ys[] = {0, 0, 0, 0};

    //Attribut de l'élément copiés
    private Color copyColor = null;
    private lineState copyLineState = null;
    private float copyLineThick = 0.f;


    public Application() {
        //On passe l'applicaiton pour l'utiliser comme controller
        this.display = new Display(this);
        this.dArrayList = new ArrayList<Drawable>();

        //Nouveau tableau pour éviter les références vers un autre tableau
        int[] x = {0, 0, 0, 0};
        int[] y = {0, 0, 0, 0};
        this.boxSelectRect = new Rectangle(x, y, Color.GRAY, lineState.EMPTY, 1.f);
    }   

    public ArrayList<Drawable> getDrawable() {
        return this.dArrayList;
    }

    public void removeElement() {
        try {
            this.dArrayList.remove(this.selectDrawable);
            this.selectDrawable = null;
            resetBoxDimension();
            this.display.repaint();
        } catch (NullPointerException e) {
            System.out.println ("L'élément n'est pas trouvable dans la liste des éléments.");
        }
    }

    public BasicStroke getStroke() {
        if(this.selectDrawable != null) {
            //return this.selectDrawable.getStroke();
        } else {
        }
        return this.lineStroke;
    }

    public float getLineThick() {
        if(this.selectDrawable != null) {
            return this.selectDrawable.getLineThick();
        } else {
            return this.lineThick;
        }
    }

    public drawState getDrawState() {
        return this.dState;
    }

    public lineState getLineState() {
        if(this.selectDrawable != null && !(this.selectDrawable instanceof Line)) {
            return this.selectDrawable.getLineState();
        } else {
            return this.lState;
        }
    }

    public Color getColor() {
        if(this.selectDrawable != null) {
            return this.selectDrawable.getColor();
        } else {
            return this.bColor;
        }
    }

    public Display getDisplay() {
        return this.display;
    }

    public Drawable getCopied() {
        return this.copyDrawable;
    }

    public Drawable getSelectedDrawable() {
        return this.selectDrawable;
    }

    public Rectangle getBoxSelect() {
        return this.boxSelectRect;
    }

    private void resetBoxDimension() {
        for(int i = 0; i < this.boxSelectRect.xpoints.length; i++) {
            this.boxSelectRect.xpoints[i] = 0;
        }
        for(int i = 0; i < this.boxSelectRect.ypoints.length; i++) {
            this.boxSelectRect.ypoints[i] = 0;
        }
    }

    public void resetDrawableArray() {
        this.dArrayList = new ArrayList<Drawable>();
    }

    public void setColor(Color nColor) {
        if(this.selectDrawable != null) {
            this.selectDrawable.setColor(nColor);
        } else {
            this.bColor = nColor;
        }
    }

    public void setDrawState(drawState nDrawState) {
        this.dState = nDrawState;
    }

    public void setCopied() {
        if(this.selectDrawable != null) {
            this.copyDrawable = this.selectDrawable;
            this.copyColor = this.selectDrawable.getColor();
            this.copyLineState = this.selectDrawable.getLineState();
            this.copyLineThick = this.selectDrawable.getLineThick();
            System.arraycopy(this.selectDrawable.xpoints, 0, this.xs, 0, this.selectDrawable.npoints);
            System.arraycopy(this.selectDrawable.ypoints, 0, this.ys, 0, this.selectDrawable.npoints);
        }
    }

    public void setCut() {
        if(this.selectDrawable != null) {
            this.copyDrawable = this.selectDrawable;
            this.copyColor = this.selectDrawable.getColor();
            this.copyLineState = this.selectDrawable.getLineState();
            this.copyLineThick = this.selectDrawable.getLineThick();
            System.arraycopy(this.selectDrawable.xpoints, 0, this.xs, 0, this.selectDrawable.npoints);
            System.arraycopy(this.selectDrawable.ypoints, 0, this.ys, 0, this.selectDrawable.npoints);
            this.dArrayList.remove(this.selectDrawable);
            this.selectDrawable = null;
        }
    }

    public void pasteCopied() {
        if(this.copyDrawable != null) {
            if(this.copyDrawable instanceof Ellipse) {
                this.createEllipse();
            } else if(this.copyDrawable instanceof Line) {
                this.createLine();
            } else if(this.copyDrawable instanceof Rectangle) {
                this.createRectangle();
            } else if(this.copyDrawable instanceof Triangle) {
                this.createTriangle();
            }
        }
    }

    public void setSelectedDrawable(Drawable nSelectedDrawable) {
        this.selectDrawable = nSelectedDrawable;
        if(this.selectDrawable != null) {
            if((this.selectDrawable instanceof Ellipse) || (this.selectDrawable instanceof Line)) {
                this.boxSelectRect.xpoints = this.selectDrawable.xpoints;
                this.boxSelectRect.ypoints = this.selectDrawable.ypoints;
            }
        }
    }

    public void setSizeSelectedDrawable(int mouseX, int mouseY) {
        this.selectDrawable.setSize(mouseX, mouseY);
    }

    public void resizeBoundSelectedDrawable() {
        this.selectDrawable.resizeBounds();
    }

    public void saveDrawables(String filename) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename, false))) {
            for(int i = 0; i < this.dArrayList.size(); i++) {
                oos.writeObject(this.dArrayList.get(i));
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void loadDrawables(String filename) {
        this.resetDrawableArray();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object model;
            while ((model = ois.readObject()) != null) { 
                if(model instanceof Ellipse) {
                    Ellipse ellipse = (Ellipse) model;
                    ellipse.setStroke();
                    this.dArrayList.add(ellipse);
                } else if(model instanceof Line) {
                    Line line = (Line) model;
                    line.setStroke();
                    this.dArrayList.add(line);
                } else if(model instanceof Rectangle) {
                    Rectangle rectangle = (Rectangle) model;
                    rectangle.setStroke();
                    this.dArrayList.add(rectangle);
                } else if(model instanceof Triangle) {
                    Triangle triangle = (Triangle) model;
                    triangle.setStroke();
                    this.dArrayList.add(triangle);
                }
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public void setPositionSelectedDrawable(int mouseX, int mouseY, int offsetX, int offsetY) {
        this.selectDrawable.move(mouseX, mouseY, offsetX, offsetY);
    }

    public void setLineState() {
        if(this.selectDrawable != null) {
            if(this.selectDrawable.getLineState() == lineState.EMPTY) {
                this.selectDrawable.setLineState(lineState.FILL);
            } else {
                this.selectDrawable.setLineState(lineState.EMPTY);
            }
        } else {
            if(this.lState == lineState.EMPTY) {
                this.lState = lineState.FILL;
            } else {
                this.lState = lineState.EMPTY;
            }
        }
    }

    public void setStroke(float width) {
        if(this.selectDrawable != null) {
            this.selectDrawable.setStroke(width);
        } else {
            this.lineThick = width;
            this.lineStroke = new BasicStroke((float) width);
        }
    }

    /* Création des formes */
    public void createEllipse(int mouseX, int mouseY) {
        int[] x = { mouseX, mouseX, mouseX, mouseY };
        int[] y = { mouseY, mouseY, mouseY, mouseY };
        this.dArrayList.add(new Ellipse(x, y, this.bColor, this.lState, this.lineThick));
        this.selectDrawable = this.dArrayList.get(this.dArrayList.size()-1);
    }


    public void createTriangle(int mouseX, int mouseY) {
        int[] x = { mouseX, mouseX, mouseX };
        int[] y = { mouseY, mouseY, mouseY };
        this.dArrayList.add(new Triangle(x, y, this.bColor, this.lState, this.lineThick));
        this.selectDrawable = this.dArrayList.get(this.dArrayList.size()-1);
    }


    public void createRectangle(int mouseX, int mouseY) {
        int[] x = { mouseX, mouseX, mouseX, mouseX };
        int[] y = { mouseY, mouseY, mouseY, mouseY };
        this.dArrayList.add(new Rectangle(x, y, this.bColor, this.lState, this.lineThick));
        this.selectDrawable = this.dArrayList.get(this.dArrayList.size()-1);
    }

    public void createLine(int mouseX, int mouseY) {
        int[] x = { mouseX, mouseX, mouseX, mouseX };
        int[] y = { mouseY, mouseY, mouseY, mouseY };
        this.dArrayList.add(new Line(x, y, this.bColor, this.lState, this.lineThick));
        this.selectDrawable = this.dArrayList.get(this.dArrayList.size()-1);
    }

    private void createEllipse() {
        this.dArrayList.add(new Ellipse(this.xs, this.ys, this.copyColor, this.copyLineState, this.copyLineThick));
        int translateOffset = 50;
        this.dArrayList.get(this.dArrayList.size()-1).translate(translateOffset, translateOffset);
    }

    private void createTriangle() {
        this.dArrayList.add(new Triangle(this.xs, this.ys, this.copyColor, this.copyLineState, this.copyLineThick));
        int translateOffset = 50;
        this.dArrayList.get(this.dArrayList.size()-1).translate(translateOffset, translateOffset);
    }

    private void createRectangle() {
        this.dArrayList.add(new Rectangle(this.xs, this.ys, this.copyColor, this.copyLineState, this.copyLineThick));
        int translateOffset = 50;
        this.dArrayList.get(this.dArrayList.size()-1).translate(translateOffset, translateOffset);
    }

    private void createLine() {
        this.dArrayList.add(new Line(this.xs, this.ys, this.copyColor, this.copyLineState, this.copyLineThick));
        int translateOffset = 50;
        this.dArrayList.get(this.dArrayList.size()-1).translate(translateOffset, translateOffset);
    }

}