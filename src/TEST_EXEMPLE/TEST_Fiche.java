/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Interface.InterfaceFiche;
import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public class TEST_Fiche implements InterfaceFiche{
    
    public int id;
    public int idEntreprise;
    public int idUtilisateur;
    public int idExercice;
    public int idMonnaie;
    public int idAgent;
    public int categorieAgent;
    public double salaireBase;
    public double transport;
    public double logement;
    public double autresGains;
    public double retenu_IPR;
    public double retenu_INSS;
    public double retenu_SYNDICAT;
    public double retenu_ABSENCE;
    public double retenu_CAFETARIAT;
    public double retenu_AVANCE_SALAIRE;
    public double retenu_ORDINATEUR;
    public Date dateEnregistrement;
    public String mois;
    public int beta;

    public TEST_Fiche(int id, int idEntreprise, int idUtilisateur, int idExercice, int idMonnaie, int idAgent, int categorieAgent, double salaireBase, double transport, double logement, double autresGains, double retenu_IPR, double retenu_INSS, double retenu_SYNDICAT, double retenu_ABSENCE, double retenu_CAFETARIAT, double retenu_AVANCE_SALAIRE, double retenu_ORDINATEUR, Date dateEnregistrement, String mois, int beta) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.idUtilisateur = idUtilisateur;
        this.idExercice = idExercice;
        this.idMonnaie = idMonnaie;
        this.idAgent = idAgent;
        this.categorieAgent = categorieAgent;
        this.salaireBase = salaireBase;
        this.transport = transport;
        this.logement = logement;
        this.autresGains = autresGains;
        this.retenu_IPR = retenu_IPR;
        this.retenu_INSS = retenu_INSS;
        this.retenu_SYNDICAT = retenu_SYNDICAT;
        this.retenu_ABSENCE = retenu_ABSENCE;
        this.retenu_CAFETARIAT = retenu_CAFETARIAT;
        this.retenu_AVANCE_SALAIRE = retenu_AVANCE_SALAIRE;
        this.retenu_ORDINATEUR = retenu_ORDINATEUR;
        this.dateEnregistrement = dateEnregistrement;
        this.mois = mois;
        this.beta = beta;
    }

    public int getIdMonnaie() {
        return idMonnaie;
    }

    public void setIdMonnaie(int idMonnaie) {
        this.idMonnaie = idMonnaie;
    }

    

    public int getIdAgent() {
        return idAgent;
    }

    public void setIdAgent(int idAgent) {
        this.idAgent = idAgent;
    }


    public double getRetenu_SYNDICAT() {
        return retenu_SYNDICAT;
    }

    public void setRetenu_SYNDICAT(double retenu_SYNDICAT) {
        this.retenu_SYNDICAT = retenu_SYNDICAT;
    }

    

    public TEST_Fiche() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(int idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    public int getCategorieAgent() {
        return categorieAgent;
    }

    public void setCategorieAgent(int categorieAgent) {
        this.categorieAgent = categorieAgent;
    }

    public double getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(double salaireBase) {
        this.salaireBase = salaireBase;
    }

    public double getTransport() {
        return transport;
    }

    public void setTransport(double transport) {
        this.transport = transport;
    }

    public double getLogement() {
        return logement;
    }

    public void setLogement(double logement) {
        this.logement = logement;
    }

    public double getAutresGains() {
        return autresGains;
    }

    public void setAutresGains(double autresGains) {
        this.autresGains = autresGains;
    }

    public double getRetenu_IPR() {
        return retenu_IPR;
    }

    public void setRetenu_IPR(double retenu_IPR) {
        this.retenu_IPR = retenu_IPR;
    }

    public double getRetenu_INSS() {
        return retenu_INSS;
    }

    public void setRetenu_INSS(double retenu_INSS) {
        this.retenu_INSS = retenu_INSS;
    }

    public double getRetenu_ABSENCE() {
        return retenu_ABSENCE;
    }

    public void setRetenu_ABSENCE(double retenu_ABSENCE) {
        this.retenu_ABSENCE = retenu_ABSENCE;
    }

    public double getRetenu_CAFETARIAT() {
        return retenu_CAFETARIAT;
    }

    public void setRetenu_CAFETARIAT(double retenu_CAFETARIAT) {
        this.retenu_CAFETARIAT = retenu_CAFETARIAT;
    }

    public double getRetenu_AVANCE_SALAIRE() {
        return retenu_AVANCE_SALAIRE;
    }

    public void setRetenu_AVANCE_SALAIRE(double retenu_AVANCE_SALAIRE) {
        this.retenu_AVANCE_SALAIRE = retenu_AVANCE_SALAIRE;
    }

    public double getRetenu_ORDINATEUR() {
        return retenu_ORDINATEUR;
    }

    public void setRetenu_ORDINATEUR(double retenu_ORDINATEUR) {
        this.retenu_ORDINATEUR = retenu_ORDINATEUR;
    }

    public Date getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    @Override
    public String toString() {
        return "XX_Fiche{" + "id=" + id + ", idEntreprise=" + idEntreprise + ", idUtilisateur=" + idUtilisateur + ", idExercice=" + idExercice + ", idMonnaie=" + idMonnaie + ", idAgent=" + idAgent + ", categorieAgent=" + categorieAgent + ", salaireBase=" + salaireBase + ", transport=" + transport + ", logement=" + logement + ", autresGains=" + autresGains + ", retenu_IPR=" + retenu_IPR + ", retenu_INSS=" + retenu_INSS + ", retenu_SYNDICAT=" + retenu_SYNDICAT + ", retenu_ABSENCE=" + retenu_ABSENCE + ", retenu_CAFETARIAT=" + retenu_CAFETARIAT + ", retenu_AVANCE_SALAIRE=" + retenu_AVANCE_SALAIRE + ", retenu_ORDINATEUR=" + retenu_ORDINATEUR + ", dateEnregistrement=" + dateEnregistrement + ", mois=" + mois + ", beta=" + beta + '}';
    }
}
