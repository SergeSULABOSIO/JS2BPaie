/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Paie;



import Source.Objet.Fiche;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class DonneesFicheDePaie {
    public Vector<Fiche> fiches;

    public DonneesFicheDePaie(Vector<Fiche> fiches) {
        this.fiches = fiches;
    }

    public Vector<Fiche> getFiches() {
        return fiches;
    }

    public void setFiches(Vector<Fiche> fiches) {
        this.fiches = fiches;
    }

    @Override
    public String toString() {
        return "DonneesFicheDePaie{" + "fiches=" + fiches + '}';
    }

    
}





