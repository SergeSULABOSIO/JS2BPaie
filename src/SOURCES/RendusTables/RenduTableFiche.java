/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables;



import SOURCES.Interface.InterfaceAgent;
import SOURCES.Interface.InterfaceFiche;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelsTables.ModeleListeFiches;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.ParametreFichesDePaie;
import SOURCES.Utilitaires.Util;
import java.util.Date;

/**
 *
 * @author user
 */
public class RenduTableFiche implements TableCellRenderer {

    private final ImageIcon iconeEdition;
    private final ParametreFichesDePaie parametreFichesDePaie;
    private final ModeleListeFiches modeleListeFiches;

    public RenduTableFiche(ImageIcon iconeEdition, ParametreFichesDePaie parametreFichesDePaie, ModeleListeFiches modeleListeFiches) {
        this.iconeEdition = iconeEdition;
        this.parametreFichesDePaie = parametreFichesDePaie;
        this.modeleListeFiches = modeleListeFiches;
    }

    
    private String getAgent(int idAgent) {
        for(InterfaceAgent Iagent : parametreFichesDePaie.getAgents()){
            if(idAgent == Iagent.getId()){
                String sexe = (Iagent.getSexe() == InterfaceAgent.SEXE_FEMININ)? "(F)" : "(H)";
                return "" + Iagent.getNom()+" " + Iagent.getPostnom()+" " + Iagent.getPrenom() + " " + sexe;
            }
        }
        return "";
    }
    
    private String getMonnaie(int idMonnaie) {
        for(InterfaceMonnaie Imonnaie : parametreFichesDePaie.getMonnaies()){
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
        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base(+)", "Transport(+)", "Logement(+)", "Autres gains(+)", "TOTAL(+)", "Ipr(-)", "Inss(-)", "Syndicat(-)", "Cafétariat(-)", "Av. Salaire(-)", "Ordinateur(-)", "TOTAL(-)", "NET A PAYER"};
        CelluleSimpleTableau cellule = null;
        String monnaie = getMonnaieRow(row);
        switch (column) {
            case 0://N°
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                break;
            case 1://Date
                cellule = new CelluleSimpleTableau(" " + Util.getDateFrancais((Date)value) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 2://Mois
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 3://Agent
                cellule = new CelluleSimpleTableau(" " + getAgent(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 4://Catégorie
                cellule = new CelluleSimpleTableau(" " + getCategorieAgent(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, null);
                break;
            case 5://Monnaie
                cellule = new CelluleSimpleTableau(" " + getMonnaie(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 6://Salaire de base (+)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 7://Transport (+)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 8://Logement(+)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 9://Autres gains(+)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 10://TOTAL(+)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 11://ipr(-)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 12://inss(-)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 13://Syndicat(-)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 14://Cafétariat(-)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 15://Avance sur Salaire(-)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 16://Ordinateur(-)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 17://TOTAL(-)
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
            case 18://NET A PAYER
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " " + monnaie, CelluleSimpleTableau.ALIGNE_DROITE, null);
                break;
        }
        cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return cellule;
    }
    
    
    private String getMonnaieRow(int row) {
        if (this.modeleListeFiches != null && this.parametreFichesDePaie != null) {
            InterfaceFiche Ific = this.modeleListeFiches.getFiche(row);
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
            InterfaceFiche Ific = this.modeleListeFiches.getFiche(row);
            if (Ific != null) {
                return Ific.getBeta();
            }
        }
        return InterfaceFiche.BETA_NOUVEAU;
    }
}
