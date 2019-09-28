/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Paie;

/**
 *
 * @author user
 */
public class DataPaie {
    public ParametreFichesDePaie parametreFichesDePaie;

    public DataPaie(ParametreFichesDePaie parametreFichesDePaie) {
        this.parametreFichesDePaie = parametreFichesDePaie;
    }

    
    public ParametreFichesDePaie getParametreFichesDePaie() {
        return parametreFichesDePaie;
    }

    public void setParametreFichesDePaie(ParametreFichesDePaie parametreFichesDePaie) {
        this.parametreFichesDePaie = parametreFichesDePaie;
    }

    @Override
    public String toString() {
        return "DataPaie{" + "parametreFichesDePaie=" + parametreFichesDePaie + '}';
    }
}





