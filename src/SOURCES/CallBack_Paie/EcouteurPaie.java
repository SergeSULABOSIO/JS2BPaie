/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack_Paie;

import SOURCES.Utilitaires_Paie.SortiesFichesDePaies;


/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurPaie {
    public abstract void onEnregistre(SortiesFichesDePaies sortiesTresorerie);
    public abstract void onDetruitTout(int idExercice);
    public abstract void onDetruitElement(int idElement);
    
}




