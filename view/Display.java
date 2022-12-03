package view;

import view.paint.*;
import controller.Application;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import java.awt.event.*;
import java.util.ArrayList;

public class Display extends JFrame implements ActionListener {

    private MenuBar mBar;
    private PaintArea paintArea;
    private PaintToolbar pToolbar;
    private Application controller;

    public Display(Application newController) {
        //Liaison du controller à la vue
        this.controller = newController;

        //Instanciation des éléménts nécessaires à l'interface
        this.mBar = new MenuBar();
        this.pToolbar = new PaintToolbar(newController);
        this.paintArea = new PaintArea(newController);

        //Ici, on précise qu'on charge une interface "Paint"
        paintDisplay();

        //On affiche la fenêtre
        this.setVisible(true);
    }

    public void paintDisplay() {
        //On précise à la MenuBar de charger les éléments pour une interface à la Paint
        this.mBar.createPaintMenuBar();

        //Ajout des actionListener
        for(int i = 0; i < this.mBar.getItems().size(); i++) {
            this.mBar.getItems().get(i).addActionListener(this);
        }

        //Définir la taille, le layout et s'il est visible ou non
        this.setTitle("NicoPaint");
        this.setSize(1600, 900);
        this.setLayout(new BorderLayout());

        //Fermer le programme a la fermeture de la fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Ajouter le menu  à la fenêtre
        this.add(this.mBar, BorderLayout.NORTH);

        //Ajout de la zone de dessin à notre affichage
        this.add(this.paintArea,BorderLayout.CENTER);   
        this.add(this.pToolbar, BorderLayout.WEST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Copier")) {
            if(controller.getSelectedDrawable() != null) {
                controller.setCopied();
            }
        }
        else if(e.getActionCommand().equals("Coller")) {
            if(controller.getCopied() != null) {
                controller.pasteCopied();
                this.repaint();
            }
        }
        else if(e.getActionCommand().equals("Quitter")) {
            if(this.controller.getDrawable().size() >= 1) {
                int res = JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder votre travail avant de quitter le programme ?");
                if(res != JOptionPane.CANCEL_OPTION) {
                    if(res == JOptionPane.YES_OPTION) {
                        if(this.saveFile()) {
                            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                        }
                    } else {
                        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                    }
                }
                this.repaint();
            }
        }
        else if(e.getActionCommand().equals("Nouveau")) {
            if(this.controller.getDrawable().size() >= 1) {
                int res = JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder votre travail avant de commencer un nouveau dessin ?");
                if(res != JOptionPane.CANCEL_OPTION) {
                    if(res == JOptionPane.YES_OPTION) {
                        if(this.saveFile()) {
                            this.controller.resetDrawableArray();
                        }
                    } else {
                        this.controller.resetDrawableArray();
                    }
                }
                this.repaint();
            }
        }
        else if(e.getActionCommand().equals("Couper")) {
            if(controller.getSelectedDrawable() != null) {
                controller.setCut();
                this.repaint();
            }
        }
        else if(e.getActionCommand().equals("Supprimer")) {
            if(controller.getSelectedDrawable() != null) {
                controller.removeElement();
            }
        }
        else if(e.getActionCommand().equals("Sauvegarder")) {
            this.saveFile();
        }
        else if (e.getActionCommand().equals("Ouvrir")) {
            if(this.controller.getDrawable().size() >= 1) {
                int res = JOptionPane.showConfirmDialog(null, "Voulez-vous sauvegarder votre travail avant d'en ouvrir un autre'?");
                if(res != JOptionPane.CANCEL_OPTION) {
                    if(res == JOptionPane.YES_OPTION) {
                        if(this.saveFile()) {
                            JFileChooser choose = new JFileChooser();
                            int res2 = choose.showOpenDialog(null);
                            if(res2 == JFileChooser.APPROVE_OPTION) {
                                controller.loadDrawables(choose.getSelectedFile().toString());
                            }
                        }
                    } else {
                        JFileChooser choose = new JFileChooser();
                        int res2 = choose.showOpenDialog(null);
                        if(res2 == JFileChooser.APPROVE_OPTION) {
                            controller.loadDrawables(choose.getSelectedFile().toString());
                        }
                    }
                }
            } else {
                JFileChooser choose = new JFileChooser();
                int res2 = choose.showOpenDialog(null);
                if(res2 == JFileChooser.APPROVE_OPTION) {
                    controller.loadDrawables(choose.getSelectedFile().toString());
                }
            }
            this.repaint();
        }
    }

    private boolean saveFile() {
        if(this.controller.getDrawable().size() >= 1) {
            JFileChooser choose = new JFileChooser();

            int res = choose.showSaveDialog(null);
            if(res == JFileChooser.APPROVE_OPTION) {
                controller.saveDrawables(choose.getSelectedFile().toString());
                return true;
            }
            return false;
        }
        return false;
    }

    //Classe interne - MenuBar - pour faciliter l'ajout de la barre princpale -> Voir pour l'ajout de sous-menu
    public class MenuBar extends JMenuBar {
        private ArrayList<JMenu> menus;
        private ArrayList<JMenuItem> menuItems;

        public MenuBar() {
            this.menus = new ArrayList<JMenu>();
            this.menuItems = new ArrayList<JMenuItem>();
        }

        public ArrayList<JMenuItem> getItems() {
            return this.menuItems;
        }

        public void addNewMenu(JMenu menu, JMenuItem...items) {
            this.menus.add(menu);

            for(JMenuItem ele : items) {
                this.menuItems.add(ele);
                this.menus.get(this.menus.size()-1).add(this.menuItems.get(this.menuItems.size()-1));
            }

            this.add(this.menus.get(this.menus.size()-1));
        }

        public void createPaintMenuBar() {
            this.addNewMenu(new JMenu("Fichier"), new JMenuItem("Nouveau"), new JMenuItem("Ouvrir"), new JMenuItem("Sauvegarder"), new JMenuItem("Quitter"));
            this.addNewMenu(new JMenu("Edition"), new JMenuItem("Copier"), new JMenuItem("Couper"), new JMenuItem("Coller"), new JMenuItem("Supprimer"));

            int[] tabRaccourciClavier = {
                //Fichier
                KeyEvent.VK_N,
                KeyEvent.VK_O,
                KeyEvent.VK_S,
                KeyEvent.VK_ESCAPE,
                //Edition
                KeyEvent.VK_C,
                KeyEvent.VK_X,
                KeyEvent.VK_V,
                KeyEvent.VK_DELETE
            };
            
            for(int i = 0; i < tabRaccourciClavier.length; i++) {
                this.menuItems.get(i).setAccelerator(KeyStroke.getKeyStroke(tabRaccourciClavier[i], ActionEvent.CTRL_MASK));
            }
        }
    }
}