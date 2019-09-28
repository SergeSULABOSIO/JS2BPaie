/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Paie;


import Source.Interface.InterfaceFiche;
import Source.Objet.Fiche;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author user
 */
public class UtilPaie {
    
    public static long generateSignature(){
      Random randomno = new Random();
      long value = randomno.nextLong();
      return value;
    }

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
    
    public static double getTotalAPayer(Fiche Ifiche){
        if(Ifiche != null){
            return (Ifiche.getSalaireBase() + Ifiche.getTransport() + Ifiche.getLogement() + Ifiche.getAutresGains());
        }else{
            return 0;
        }
    }
    
    public static double getTotalRetenu(Fiche Ifiche){
        if(Ifiche != null){
            return (Ifiche.getRetenu_IPR() + Ifiche.getRetenu_INSS()+ Ifiche.getRetenu_SYNDICAT() + Ifiche.getRetenu_ABSENCE()+ Ifiche.getRetenu_CAFETARIAT() + Ifiche.getRetenu_AVANCE_SALAIRE() + Ifiche.getRetenu_ORDINATEUR());
        }else{
            return 0;
        }
    }
    
    public static double getNetAPayer(Fiche Ifiche){
        return getTotalAPayer(Ifiche) - getTotalRetenu(Ifiche);
    }

    public static double getNombre_jours(Date dateFin, Date dateDebut) {
        try {
            double nb = (int) ((dateFin.getTime() - dateDebut.getTime()) / (1000 * 60 * 60 * 24));
            nb = UtilPaie.round(nb, 0);
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
            nb = UtilPaie.round(nb, 0);
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
    
    public static Date getDate_ZeroHeure() {
        Date date = new Date();
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }
    
    public static Date getDate_CeMatin(Date date) {
        if (date != null) {
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
        }
        return date;
    }
    
    public static String getTexteCourt(String texteOrigine, int tailleOutput){
        if(texteOrigine.trim().length() > tailleOutput){
            return texteOrigine.substring(0, tailleOutput)+"...";
        }else{
            return texteOrigine;
        }
    }

    public static Date getDate_ZeroHeure(Date date) {
        if (date != null) {
            date.setHours(23);
            date.setMinutes(59);
            date.setSeconds(59);
        }
        return date;
    }

    public static Date getDate_CeMatin() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
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
    
    public static Vector<String> getListeMois(Date dateDebut, Date dateFinale){
        Vector<String> listeMois = new Vector();
        Date dateAmovible = (Date)dateDebut.clone();
        
        listeMois.addElement("" + UtilPaie.getDateFrancais_Mois(dateAmovible));
        while(dateAmovible.before(dateFinale)){
            dateAmovible.setMonth(dateAmovible.getMonth() + 1);
            //System.out.println(" ** " + Util.getDateFrancais(dateAmovible));
            String moisEncours = UtilPaie.getDateFrancais_Mois(dateAmovible);
            if(!listeMois.contains(moisEncours)){
                listeMois.addElement(moisEncours);
            }
        }
        return listeMois;
    }

    public static void main(String[] args) {
        /*
        double origine = 10000.14;
        
        String res = Util.getMontantFrancais(origine);
        System.out.println("Résultat = " + res);
        System.out.println("Résultat = " + Util.getMontantLettres(origine, "Dollars Américains"));
        
        System.out.println("Mois: " + Util.getDateFrancais(new Date()));
        System.out.println("Mois: " + Util.getDateFrancais_Mois(new Date()));
        */
        
        Date dateA = new Date(119, 1, 26);
        Date dateB = new Date(120, 11, 26);
        /*
        System.out.println("Date A: " + Util.getDateFrancais(dateA));
        System.out.println("Date B: " + Util.getDateFrancais(dateB));
        dateA.setMonth(dateA.getMonth()-1);
        System.out.println("Date modifiée: " + Util.getDateFrancais(dateA));
        */
        System.out.println("Date A: " + UtilPaie.getDateFrancais(dateA));
        System.out.println("Date B: " + UtilPaie.getDateFrancais(dateB));
        System.out.println("");
        Vector<String> liste = UtilPaie.getListeMois(dateA, dateB);
        for(String Omois: liste){
            System.out.println(" * " + Omois);
        }
    }
}







