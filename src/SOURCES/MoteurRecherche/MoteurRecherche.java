/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.MoteurRecherche;

import ICONES.Icones;
import SOURCES.CallBack.EcouteurUpdateClose;
import UI.JS2bTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public abstract class MoteurRecherche {

    private JS2bTextField champTexte;
    private Icones icones;
    private EcouteurUpdateClose ecouteurUpdateClose;
    private Thread processus = null;

    public MoteurRecherche(Icones icones, JS2bTextField champTexte, EcouteurUpdateClose ecouteurUpdateClose) {
        this.champTexte = champTexte;
        this.icones = icones;
        this.ecouteurUpdateClose = ecouteurUpdateClose;
        init();
    }

    private void init() {
        if (this.champTexte != null) {
            champTexte.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    //System.out.println("keyTyped : " + e.getKeyCode());
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    //System.out.println("keyPressed : " + e.getKeyCode());
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (champTexte != null) {
                            demarrerRecherche();
                        }
                    }
                }
            });
        }
    }

    public void demarrerRecherche() {
        processus = new Thread() {
            @Override
            public void run() {
                try {
                    patienter();
                    processus.sleep(200);
                    chercher(champTexte.getText().trim());
                    fin();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MoteurRecherche.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };
        processus.start();
    }

    private void patienter() {
        try {
            //Effect visuel
            if (icones != null && champTexte != null) {
                champTexte.setIcon(icones.getSablier_01());
                champTexte.repaint();
                champTexte.setEnabled(false);
            }
            if (ecouteurUpdateClose != null && icones != null) {
                ecouteurUpdateClose.onActualiser("Recherche de \"" + champTexte.getText() + "\"  encours...", icones.getSablier_01());
            }
        } catch (Exception ex) {
            Logger.getLogger(MoteurRecherche.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fin() {
        if (processus != null) {
            try {
                //Effect visuel
                if (icones != null && champTexte != null) {
                    champTexte.setIcon(icones.getChercher_01());
                    champTexte.repaint();
                    champTexte.setEnabled(true);
                }
                if (ecouteurUpdateClose != null && icones != null) {
                    ecouteurUpdateClose.onActualiser("Prêt", icones.getInfos_01());
                }
                processus = null;
            } catch (Exception ex) {
                Logger.getLogger(MoteurRecherche.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public abstract void chercher(String motcle);

}
