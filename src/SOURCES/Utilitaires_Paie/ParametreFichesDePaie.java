/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Paie;




import Source.Objet.Agent;
import Source.Objet.Entreprise;
import Source.Objet.Annee;
import Source.Objet.Monnaie;
import Source.Objet.Utilisateur;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametreFichesDePaie {
    public Entreprise entreprise;
    public Utilisateur utilisateur;
    public Annee exercice;
    public Vector<Agent> agents;
    public Vector<Monnaie> monnaies;

    public ParametreFichesDePaie(Utilisateur utilisateur, Entreprise entreprise, Annee exercice, Vector<Agent> agents, Vector<Monnaie> monnaies) {
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.agents = agents;
        this.monnaies = monnaies;
        this.utilisateur = utilisateur;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public Annee getExercice() {
        return exercice;
    }

    public void setExercice(Annee exercice) {
        this.exercice = exercice;
    }

    public Vector<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Vector<Agent> agents) {
        this.agents = agents;
    }

    public Vector<Monnaie> getMonnaies() {
        return monnaies;
    }

    public void setMonnaies(Vector<Monnaie> monnaies) {
        this.monnaies = monnaies;
    }

    @Override
    public String toString() {
        return "ParametreFichesDePaie{" + "entreprise=" + entreprise + ", utilisateur=" + utilisateur + ", exercice=" + exercice + ", agents=" + agents + ", monnaies=" + monnaies + '}';
    }

    
}









