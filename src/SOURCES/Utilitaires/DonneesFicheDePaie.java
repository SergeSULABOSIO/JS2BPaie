/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;


import SOURCES.Interface.InterfaceFiche;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class DonneesFicheDePaie {
    public Vector<InterfaceFiche> fiches;

    public DonneesFicheDePaie(Vector<InterfaceFiche> fiches) {
        this.fiches = fiches;
    }

    public Vector<InterfaceFiche> getFiches() {
        return fiches;
    }

    public void setFiches(Vector<InterfaceFiche> fiches) {
        this.fiches = fiches;
    }

    @Override
    public String toString() {
        return "DonneesFiche{" + "fiches=" + fiches + '}';
    }
}
