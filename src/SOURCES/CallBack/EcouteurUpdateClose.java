/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack;

import javax.swing.ImageIcon;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurUpdateClose {
    
    public abstract void onActualiser(String texte, ImageIcon icone);
    public abstract void onFermer();
    
    
}
