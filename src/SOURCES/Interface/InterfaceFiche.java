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
public interface InterfaceFiche {
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    
    public abstract int getId();
    public abstract int getIdEntreprise();
    public abstract int getIdUtilisateur();
    public abstract int getIdExercice();
    public abstract int getIdAgent();
    public abstract int getCategorieAgent();
    public abstract double getSalaireBase();
    public abstract double getTransport();
    public abstract double getLogement();
    public abstract double getAutresGains();
    public abstract double getRetenu_IPR();
    public abstract double getRetenu_INSS();
    public abstract double getRetenu_SYNDICAT();
    public abstract double getRetenu_ABSENCE();
    public abstract double getRetenu_CAFETARIAT();
    public abstract double getRetenu_AVANCE_SALAIRE();
    public abstract double getRetenu_ORDINATEUR();
    public abstract Date getDateEnregistrement();
    public abstract String getMois();
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    public abstract void setId(int id);
    public abstract void setIdEntreprise(int idEntreprise);
    public abstract void setIdUtilisateur(int idUtilisateur);
    public abstract void setIdExercice(int idExercice);
    public abstract void setIdAgent(int idAgent);
    public abstract void setCategorieAgent(int categorieAgent);
    public abstract void setSalaireBase(double salaireBase);
    public abstract void setTransport(double transport);
    public abstract void setLogement(double logement);
    public abstract void setAutresGains(double autreGains);
    public abstract void setRetenu_IPR(double ipr);
    public abstract void setRetenu_INSS(double inss);
    public abstract void setRetenu_SYNDICAT(double syndicat);
    public abstract void setRetenu_ABSENCE(double absence);
    public abstract void setRetenu_CAFETARIAT(double cafetariat);
    public abstract void setRetenu_AVANCE_SALAIRE(double avanceSalaire);
    public abstract void setRetenu_ORDINATEUR(double ordinateur);
    public abstract void setDateEnregistrement(Date dateEnregistremen);
    public abstract void setMois(String mois);

    public abstract void setBeta(int newbeta);
}
