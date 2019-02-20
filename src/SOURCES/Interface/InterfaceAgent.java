/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;

/**
 *
 * @author user
 */
public interface InterfaceAgent {
    //Constantes - SEXE
    public static final int SEXE_MASCULIN = 0;
    public static final int SEXE_FEMININ = 1;
    //Constanzte - NIVEAU ETUDE
    public static final int NIVEAU_ETUDE_DIPLOME_ETAT = 0;
    public static final int NIVEAU_ETUDE_GRADUE = 1;
    public static final int NIVEAU_ETUDE_LICENCIE = 2;
    public static final int NIVEAU_ETUDE_MASTER = 3;
    public static final int NIVEAU_ETUDE_DOCTEUR = 4;
    public static final int NIVEAU_ETUDE_AUTRE = 5;
    //Constantes - CATEGORIE
    public static final int CATEGORIE_ADMINISTRATION_1 = 0;
    public static final int CATEGORIE_ADMINISTRATION_2 = 1;
    public static final int CATEGORIE_MATERNELLE = 2;
    public static final int CATEGORIE_PARTIEL = 3;
    public static final int CATEGORIE_PRIMAIRE = 4;
    public static final int CATEGORIE_PRIME = 5;
    public static final int CATEGORIE_SECONDAIRE = 6;    
    public static final int CATEGORIE_SURVEILLANT = 7;
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    
    
    //les getters
    public abstract int getId();
    public abstract int getIdEntreprise();
    public abstract int getIdUtilisateur();
    public abstract int getIdExercice();
    
    public abstract String getNom();
    public abstract String getPostnom();
    public abstract String getPrenom();
    public abstract int getSexe();
    public abstract int getNiveauEtude();
    public abstract long getSignature();
    public abstract int getCategorie();
    public abstract int getBeta();  // 0 = Existant, 1 =  Modifié, 2 = Nouveau
    
    //Les setters
    public abstract void setId(int id);
    public abstract void setIdEntreprise(int idEntreprise);
    public abstract void setIdUtilisateur(int idUtilisateur);
    public abstract void setIdExercice(int idExercice);
    
    public abstract void setNom(String nom);
    public abstract void setPostnom(String postnom);
    public abstract void setPrenom(String prenom);
    public abstract void setSexe(int sexe);
    public abstract void setNiveauEtude(int niveauEtude);
    public abstract void setSignature(long signature);
    public abstract void setCategorie(int categorie);
    public abstract void setBeta(int newbeta);
    
}
