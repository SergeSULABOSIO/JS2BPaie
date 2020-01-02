/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Paie;


import Source.Callbacks.EcouteurEnregistrement;
import Source.Interface.InterfaceFiche;
import Source.Objet.Fiche_paie;
import java.util.Vector;

/**
 *
 * @author user
 */

public class SortiesFichesDePaies {
    private EcouteurEnregistrement ecouteurEnregistrement;
    private Vector<Fiche_paie> listeFichesDePaie;

    public SortiesFichesDePaies(EcouteurEnregistrement ecouteurEnregistrement, Vector<Fiche_paie> listeFichesDePaie) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
        this.listeFichesDePaie = listeFichesDePaie;
    }

    public EcouteurEnregistrement getEcouteurEnregistrement() {
        return ecouteurEnregistrement;
    }

    public void setEcouteurEnregistrement(EcouteurEnregistrement ecouteurEnregistrement) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }

    public Vector<Fiche_paie> getListeFichesDePaie() {
        return listeFichesDePaie;
    }

    public void setListeFichesDePaie(Vector<Fiche_paie> listeFichesDePaie) {
        this.listeFichesDePaie = listeFichesDePaie;
    }

    @Override
    public String toString() {
        return "SortiesFichesDePaies{" + "ecouteurEnregistrement=" + ecouteurEnregistrement + ", listeFichesDePaie=" + listeFichesDePaie + '}';
    }
}






