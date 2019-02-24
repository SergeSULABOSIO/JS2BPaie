/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceFiche;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author user
 */
public class Util {

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static boolean contientMotsCles(String base, String motscles) {
        boolean rep = false;
        String[] tabMotsCles = motscles.split(" ");
        for (int i = 0; i < tabMotsCles.length; i++) {
            if (base.toLowerCase().contains(tabMotsCles[i].toLowerCase().trim())) {
                return true;
            }
        }
        return rep;
    }

    public static String getLettres(double montant, String NomMonnaie) {
        String texte = "";
        try {
            texte = Nombre.CALCULATE.getLettres(montant, NomMonnaie);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texte;
    }
    
    public static double getTotalAPayer(InterfaceFiche Ifiche){
        if(Ifiche != null){
            return (Ifiche.getSalaireBase() + Ifiche.getTransport() + Ifiche.getLogement() + Ifiche.getAutresGains());
        }else{
            return 0;
        }
    }
    
    public static double getTotalRetenu(InterfaceFiche Ifiche){
        if(Ifiche != null){
            return (Ifiche.getRetenu_IPR() + Ifiche.getRetenu_INSS()+ Ifiche.getRetenu_SYNDICAT() + Ifiche.getRetenu_ABSENCE()+ Ifiche.getRetenu_CAFETARIAT() + Ifiche.getRetenu_AVANCE_SALAIRE() + Ifiche.getRetenu_ORDINATEUR());
        }else{
            return 0;
        }
    }
    
    public static double getNetAPayer(InterfaceFiche Ifiche){
        return getTotalAPayer(Ifiche) - getTotalRetenu(Ifiche);
    }

    public static double getNombre_jours(Date dateFin, Date dateDebut) {
        try {
            double nb = (int) ((dateFin.getTime() - dateDebut.getTime()) / (1000 * 60 * 60 * 24));
            nb = Util.round(nb, 0);
            return nb;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static Date getDate_AjouterAnnee(Date dateActuelle, int nbAnnee) {
        try {
            int plus = dateActuelle.getYear() + nbAnnee;
            dateActuelle.setYear(plus);
            return dateActuelle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double getNombre_jours_from_today(Date dateFin) {
        try {
            double nb = (double) ((dateFin.getTime() - (new Date()).getTime()) / (1000 * 60 * 60 * 24));
            nb = Util.round(nb, 0);
            return nb;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateFrancais(Date date) {
        String dateS = "";
        try {
            String pattern = "dd MMM yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            dateS = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateS;
    }
    
    public static String getDateFrancais_Mois(Date date) {
        String dateS = "";
        try {
            String pattern = "MMMM YYYY";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            dateS = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateS;
    }
    

    public static String getMontantFrancais(double montant) {
        String val = "";
        int ValEntiere = (int) montant;
        char[] valInput = (ValEntiere + "").toCharArray();
        int index = 0;
        for (int i = valInput.length - 1; i >= 0; i--) {
            //System.out.println(" \t *  " + valInput[i]);
            if (index % 3 == 0 && index != 0) {
                val = valInput[i] + "." + val;
            } else {
                val = valInput[i] + val;
            }
            index++;
        }
        int ValApresVirgule = (int)(round(((montant - ValEntiere)*100), 0));
        //System.out.println("Valeur d'origine = " + montant);
        //System.out.println("Partie entière = " + ValEntiere);
        //System.out.println("Partie décimale = " + ValApresVirgule);
        return val+"," + ValApresVirgule;
    }
    
    public static String getMontantLettres(double montant, String NomMonnaie){
        String texte = "";
        try{
            texte = Nombre.CALCULATE.getLettres(montant, NomMonnaie);
        }catch(Exception e){
            System.out.println("Un problème est survenu lors de la conversion des chiffres en nombre.");
            texte = "";
        }
        return texte;
    }

    public static void main(String[] args) {
        double origine = 10000.14;
        
        String res = Util.getMontantFrancais(origine);
        System.out.println("Résultat = " + res);
        System.out.println("Résultat = " + Util.getMontantLettres(origine, "Dollars Américains"));
        
        System.out.println("Mois: " + Util.getDateFrancais(new Date()));
        System.out.println("Mois: " + Util.getDateFrancais_Mois(new Date()));
    }
}
