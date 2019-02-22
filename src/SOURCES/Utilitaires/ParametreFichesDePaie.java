/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;


import SOURCES.Interface.InterfaceAgent;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceMonnaie;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametreFichesDePaie {
    public InterfaceEntreprise entreprise;
    public InterfaceExercice exercice;
    public Vector<InterfaceAgent> agents;
    public Vector<InterfaceMonnaie> monnaies;
    public String nomUtilisateur;
    public int idUtilisateur;

    public ParametreFichesDePaie(InterfaceEntreprise entreprise, InterfaceExercice exercice, Vector<InterfaceAgent> agents, Vector<InterfaceMonnaie> monnaies, String nomUtilisateur, int idUtilisateur) {
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.agents = agents;
        this.monnaies = monnaies;
        this.nomUtilisateur = nomUtilisateur;
        this.idUtilisateur = idUtilisateur;
    }

    public Vector<InterfaceMonnaie> getMonnaies() {
        return monnaies;
    }

    public void setMonnaies(Vector<InterfaceMonnaie> monnaies) {
        this.monnaies = monnaies;
    }
    
    public InterfaceEntreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(InterfaceEntreprise entreprise) {
        this.entreprise = entreprise;
    }

    public InterfaceExercice getExercice() {
        return exercice;
    }

    public void setExercice(InterfaceExercice exercice) {
        this.exercice = exercice;
    }

    public Vector<InterfaceAgent> getAgents() {
        return agents;
    }

    public void setAgents(Vector<InterfaceAgent> agents) {
        this.agents = agents;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    @Override
    public String toString() {
        return "ParametreFichesDePaie{" + "entreprise=" + entreprise + ", exercice=" + exercice + ", agents=" + agents + ", monnaies=" + monnaies + ", nomUtilisateur=" + nomUtilisateur + ", idUtilisateur=" + idUtilisateur + '}';
    }
}
