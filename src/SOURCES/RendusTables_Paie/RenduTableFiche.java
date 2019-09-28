/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables_Paie;




import SOURCES.ModelsTables_Paie.ModeleListeFiches;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import SOURCES.Utilitaires_Paie.ParametreFichesDePaie;
import SOURCES.Utilitaires_Paie.UtilPaie;
import Source.GestionEdition;
import Source.Interface.InterfaceAgent;
import Source.Interface.InterfaceFiche;
import Source.Objet.Agent;
import Source.Objet.CouleurBasique;
import Source.Objet.Fiche;
import Source.Objet.Monnaie;
import Source.UI.CelluleTableauSimple;
import java.util.Date;

/**
 *
 * @author user
 */
public class RenduTableFiche implements TableCellRenderer {

    private final ImageIcon iconeEdition;
    private final ParametreFichesDePaie parametreFichesDePaie;
    private final ModeleListeFiches modeleListeFiches;
    private CouleurBasique couleurBasique;
    private GestionEdition gestionEdition;

    public RenduTableFiche(GestionEdition gestionEdition, CouleurBasique couleurBasique, ImageIcon iconeEdition, ParametreFichesDePaie parametreFichesDePaie, ModeleListeFiches modeleListeFiches) {
        this.iconeEdition = iconeEdition;
        this.parametreFichesDePaie = parametreFichesDePaie;
        this.modeleListeFiches = modeleListeFiches;
        this.couleurBasique = couleurBasique;
        this.gestionEdition = gestionEdition;
    }

    
    private String getAgent(int idAgent) {
        for(Agent Iagent : parametreFichesDePaie.getAgents()){
            if(idAgent == Iagent.getId()){
                String sexe = (Iagent.getSexe() == InterfaceAgent.SEXE_FEMININ)? "(F)" : "(H)";
                return "" + Iagent.getNom()+" " + Iagent.getPostnom()+" " + Iagent.getPrenom() + " " + sexe;
            }
        }
        return "";
    }
    
    private String getMonnaie(int idMonnaie) {
        for(Monnaie Imonnaie : parametreFichesDePaie.getMonnaies()){
            if(idMonnaie == Imonnaie.getId()){
                return Imonnaie.getCode();
            }
        }
        return "";
    }
    
    private String getCategorieAgent(int categorie){
        switch(categorie){
            case InterfaceAgent.CATEGORIE_ADMINISTRATION_1:
                return "ADMIN 1";
            case InterfaceAgent.CATEGORIE_ADMINISTRATION_2:
                return "ADMIN 2";
            case InterfaceAgent.CATEGORIE_MATERNELLE:
                return "MATERNELLE";
            case InterfaceAgent.CATEGORIE_PARTIEL:
                return "PARTIELLE";
            case InterfaceAgent.CATEGORIE_PRIMAIRE:
                return "PRIMAIRE";
            case InterfaceAgent.CATEGORIE_PRIME:
                return "PRIME";
            case InterfaceAgent.CATEGORIE_SECONDAIRE:
                return "SECONDAIRE";
            case InterfaceAgent.CATEGORIE_SURVEILLANT:
                return "SURVEILLANT";
        }
        return "";
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
        CelluleTableauSimple cellule = null;
        
        ImageIcon icone = null;
        if(gestionEdition != null){
            Fiche agent = this.modeleListeFiches.getFiche(row);
            if(agent != null){
                if(gestionEdition.isEditable(agent.getId(), 0)){
                    icone = iconeEdition;
                }
            }
        }
        
        String monnaie = getMonnaieRow(row);
        switch (column) {
            case 0://N°
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                break;
            case 1://Date
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilPaie.getDateFrancais((Date)value) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 2://Mois
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 3://Agent
                cellule = new CelluleTableauSimple(couleurBasique, " " + getAgent(Integer.parseInt(value+"")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 4://Catégorie
                cellule = new CelluleTableauSimple(couleurBasique, " " + getCategorieAgent(Integer.parseInt(value+"")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, null);
                break;
            case 5://Monnaie
                cellule = new CelluleTableauSimple(couleurBasique, " " + getMonnaie(Integer.parseInt(value+"")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 6://Salaire de base (+)
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 7://Transport (+)
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 8://Logement(+)
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 9://Autres gains(+)
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 10://TOTAL(+)
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
            case 11://ipr(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 12://inss(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 13://Syndicat(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 14://Absence(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 15://Cafétariat(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 16://Avance sur Salaire(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 17://Ordinateur(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 18://TOTAL(-)
                cellule = new CelluleTableauSimple(couleurBasique, "-" + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
            case 19://NET A PAYER
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilPaie.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie + " ", CelluleTableauSimple.ALIGNE_DROITE, null);
                break;
        }
        cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return cellule;
    }
    
    
    private String getMonnaieRow(int row) {
        if (this.modeleListeFiches != null && this.parametreFichesDePaie != null) {
            Fiche Ific = this.modeleListeFiches.getFiche(row);
            if (Ific != null) {
                return getMonnaie(Ific.getIdMonnaie());
            }else{
                return "Um";
            }
        }else{
            return "Um";
        }
    }
    
    
    private int getBeta(int row) {
        if (this.modeleListeFiches != null) {
            Fiche Ific = this.modeleListeFiches.getFiche(row);
            if (Ific != null) {
                return Ific.getBeta();
            }
        }
        return InterfaceFiche.BETA_NOUVEAU;
    }
}

























