/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack;

import SOURCES.ModelsTable.ModeleListeDecaissement;
import SOURCES.ModelsTable.ModeleListeEncaissement;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurAjout {
    public abstract void setAjoutEncaissement(ModeleListeEncaissement modeleListeEncaissement);
    public abstract void setAjoutDecaissement(ModeleListeDecaissement modeleListeDecaissement);
}
