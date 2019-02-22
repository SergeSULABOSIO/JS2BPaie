/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.CallBack.EcouteurEnregistrement;
import SOURCES.Interface.InterfaceFiche;
import java.util.Vector;

/**
 *
 * @author user
 */

public class SortiesFichesDePaies {
    private EcouteurEnregistrement ecouteurEnregistrement;
    private Vector<InterfaceFiche> listeFichesDePaie;

    public SortiesFichesDePaies(EcouteurEnregistrement ecouteurEnregistrement, Vector<InterfaceFiche> listeFichesDePaie) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
        this.listeFichesDePaie = listeFichesDePaie;
    }

    public EcouteurEnregistrement getEcouteurEnregistrement() {
        return ecouteurEnregistrement;
    }

    public void setEcouteurEnregistrement(EcouteurEnregistrement ecouteurEnregistrement) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }

    public Vector<InterfaceFiche> getListeFichesDePaie() {
        return listeFichesDePaie;
    }

    public void setListeFichesDePaie(Vector<InterfaceFiche> listeFichesDePaie) {
        this.listeFichesDePaie = listeFichesDePaie;
    }

    @Override
    public String toString() {
        return "SortiesTresorerie{" + "ecouteurEnregistrement=" + ecouteurEnregistrement + ", listeFichesDePaie=" + listeFichesDePaie + '}';
    }
}
