/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;

import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public interface InterfaceExercice {
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    
    public abstract int getId();
    public abstract int getIdEntreprise();
    public abstract int getIdUtilisateur();
    public abstract String getNom();
    public abstract Date getDebut();
    public abstract Date getFin();
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    public abstract void setId(int id);
    public abstract void setIdEntreprise(int idEntreprise);
    public abstract void setIdUtilisateur(int idUtilisateur);
    public abstract void setNom(String nom);
    public abstract void setDebut(Date dateDebut);
    public abstract void setFin(Date dateFin);
    public abstract void setBeta(int newbeta);
}
