/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTables;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Interface.InterfaceAgent;
import SOURCES.Interface.InterfaceFiche;
import SOURCES.Utilitaires.ParametreFichesDePaie;
import SOURCES.Utilitaires.Util;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeFiches extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
    private Vector<InterfaceFiche> listeData = new Vector<>();
    private Vector<InterfaceFiche> listeDataExclus = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;
    private ParametreFichesDePaie parametreFichesDePaie;

    public ModeleListeFiches(JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, ParametreFichesDePaie parametreFichesDePaie, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
        this.parametreFichesDePaie = parametreFichesDePaie;
    }

    
    public void chercher(String mois, Date dateA, Date dateB, String motcle, int idCategorie) {
        this.listeData.addAll(this.listeDataExclus);
        this.listeDataExclus.removeAllElements();
        for (InterfaceFiche Ific : this.listeData) {
            if (Ific != null) {
                search_verifier_mois(Ific, mois, dateA, dateB, motcle, idCategorie);
            }
        }
        //En fin, on va nettoyer la liste - en enlevant tout objet qui a été black listé
        search_nettoyer();
    }
    
    private void search_verifier_mois(InterfaceFiche Ifiche, String mois, Date dateA, Date dateB, String motcle, int idCategorie) {
        //System.out.println("Ifiche = " + Ifiche.getCategorieAgent());
        //System.out.println("idCategorie = " + idCategorie);
        if (Ifiche != null) {
            if (mois.trim().startsWith("TOU") == true) {
                //On ne fait rien
            } else if (!Ifiche.getMois().equals(mois)) {
                search_blacklister(Ifiche);
            }
            search_verifier_periode(Ifiche, dateA, dateB, motcle, idCategorie);
        }
    }
    
    private void search_verifier_periode(InterfaceFiche Ifiche, Date dateA, Date dateB, String motcle, int idCategorie) {
        if (Ifiche != null) {
            boolean apresA = (Ifiche.getDateEnregistrement().after(dateA) || Ifiche.getDateEnregistrement().equals(dateA));
            boolean avantB = (Ifiche.getDateEnregistrement().before(dateB) || Ifiche.getDateEnregistrement().equals(dateB));
            if (apresA == true && avantB == true) {
                //On ne fait rien
            } else {
                search_blacklister(Ifiche);
            }
            search_verifier_categorie(Ifiche, motcle, idCategorie);
        }
    }
    
    private void search_verifier_categorie(InterfaceFiche Ifiche, String motcle, int idCategorie) {
        //System.out.println("Ifiche = " + Ifiche.getCategorieAgent());
        //System.out.println("idCategorie = " + idCategorie);
        if (Ifiche != null) {
            if (idCategorie == -1) {
                //On ne fait rien
            } else if (Ifiche.getCategorieAgent() != idCategorie) {
                search_blacklister(Ifiche);
            }
            search_verifier_motcle(Ifiche, motcle);
        }
    }
    
    private void search_verifier_motcle(InterfaceFiche Ifiche, String motcle) {
        if (Ifiche != null) {
            if (motcle.trim().length() == 0) {
                //On ne fait rien
            } else if (Util.contientMotsCles(Ifiche.getMois(), motcle) == false && Util.contientMotsCles(getAgent(Ifiche.getIdAgent()), motcle) == false) {
                search_blacklister(Ifiche);
            }
        }
    }
    
    private String getAgent(int idAgent){
        for(InterfaceAgent Icha : this.parametreFichesDePaie.getAgents()){
            if(Icha.getId() == idAgent){
                return Icha.getNom()+" " + Icha.getPostnom()+" " + Icha.getPrenom();
            }
        }
        return "";
    }

    private void search_blacklister(InterfaceFiche Ifiche) {
        if (Ifiche != null && this.listeDataExclus != null) {
            if (!listeDataExclus.contains(Ifiche)) {
                this.listeDataExclus.add(Ifiche);
            }
        }
    }
    
    private void search_nettoyer() {
        if (this.listeDataExclus != null && this.listeData != null) {
            this.listeDataExclus.forEach((IeleveASupp) -> {
                this.listeData.removeElement(IeleveASupp);
            });
            redessinerTable();
        }
    }
    
    
    
    public void setListeFiches(Vector<InterfaceFiche> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public InterfaceFiche getFiche(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceFiche art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public InterfaceFiche getFiche_id(int id) {
        if (id != -1) {
            for (InterfaceFiche art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<InterfaceFiche> getListeData() {
        return this.listeData;
    }

    public void actualiser() {
        redessinerTable();
    }

    public void AjouterFichet(InterfaceFiche newFiche) {
        this.listeData.add(0, newFiche);
        mEnreg.setCouleur(Color.blue);
        btEnreg.setCouleur(Color.blue);
        redessinerTable();
    }

    public void SupprimerFiche(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceFiche articl = listeData.elementAt(row);
            if (articl != null) {
                int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    if (row <= listeData.size()) {
                        this.listeData.removeElementAt(row);
                    }
                    redessinerTable();
                }
            }
        }
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public void redessinerTable() {
        fireTableDataChanged();
        ecouteurModele.onValeurChangee();
    }

    @Override
    public int getRowCount() {
        return listeData.size();
    }

    @Override
    public int getColumnCount() {
        return titreColonnes.length;
    }

    @Override
    public String getColumnName(int column) {
        return titreColonnes[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
        InterfaceFiche Ifiche = listeData.elementAt(rowIndex);
        switch (columnIndex) {
            case 0: //N°
                return (rowIndex + 1) + "";
            case 1: //Date
                return Ifiche.getDateEnregistrement();
            case 2: //Mois
                return Ifiche.getMois();
            case 3: //Agent
                return Ifiche.getIdAgent();
            case 4: //Catégorie
                return Ifiche.getCategorieAgent();
            case 5: //Monnaie
                return Ifiche.getIdMonnaie();
            case 6: //Salaire de base(+)
                return Ifiche.getSalaireBase();
            case 7: //Transport(+)
                return Ifiche.getTransport();
            case 8: //Logement(+)
                return Ifiche.getLogement();
            case 9: //Autres gains(+)
                return Ifiche.getAutresGains();
            case 10: //TOTAL (+)
                return Util.getTotalAPayer(Ifiche);
            case 11: //IPR(-)
                return Ifiche.getRetenu_IPR();
            case 12: //INSS(-)
                return Ifiche.getRetenu_INSS();
            case 13: //SYNDICAT(-)
                return Ifiche.getRetenu_SYNDICAT();
            case 14: //ABSENCE(-)
                return Ifiche.getRetenu_ABSENCE();
            case 15: //CAFETARIAT(-)
                return Ifiche.getRetenu_CAFETARIAT();
            case 16: //AVANCE SUR SALAIRE(-)
                return Ifiche.getRetenu_AVANCE_SALAIRE();
            case 17: //ORDINATEUR(-)
                return Ifiche.getRetenu_ORDINATEUR();
            case 18: //TOTAL(-)
                return Util.getTotalRetenu(Ifiche);
            case 19: //NET A PAYER
                return Util.getNetAPayer(Ifiche);
            default:
                return "Null";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
        switch (columnIndex) {
            case 0: //N°
                return Integer.class;
            case 1: //Date
                return Date.class;
            case 2: //Mois
                return String.class;
            case 3: //Agent
                return Integer.class;
            case 4: //Catégorie
                return Integer.class;
            case 5: //Monnaie
                return Integer.class;
            case 6: //Salaire de base(+)
                return Double.class;
            case 7: //Transport(+)
                return Double.class;
            case 8: //Logement(+)
                return Double.class;
            case 9: //Autres gains(+)
                return Double.class;
            case 10: //TOTAL(+)
                return Double.class;
            case 11: //IPR(-)
                return Double.class;
            case 12: //INSS(-)
                return Double.class;
            case 13: //SYNDICAT(-)
                return Double.class;
            case 14: //ABSENCE(-)
                return Double.class;
            case 15: //CAFETARIAT(-)
                return Double.class;
            case 16: //AVANCE SUR SALAIRE(-)
                return Double.class;
            case 17: //ORDINATEUR(-)
                return Double.class;
            case 18: //TOTAL(-)
                return Double.class;
            case 19: //NET A PAYER
                return Double.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
        if (columnIndex == 0 || columnIndex == 4 || columnIndex == 10 || columnIndex == 18 || columnIndex == 19) {
            return false;
        } else {
            return true;
        }
    }

    private void updateCategorieAgent(InterfaceFiche Ifiche) {
        if (Ifiche != null) {
            for (InterfaceAgent Iagent : this.parametreFichesDePaie.getAgents()) {
                if (Ifiche.getIdAgent() == Iagent.getId()) {
                    Ifiche.setCategorieAgent(Iagent.getCategorie());
                    break;
                }
            }
        }
    }

    private boolean isThisPaieAlreadyExists(String mois, int idAgentNew) {
        boolean rep = false;
        for (InterfaceFiche iFiche : listeData) {
            if (iFiche.getIdAgent() == idAgentNew && mois.equals(iFiche.getMois())) {
                return true;
            }
        }
        return rep;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
        InterfaceFiche Ifiche = listeData.get(rowIndex);
        String avant = Ifiche.toString();
        switch (columnIndex) {
            case 1://Date
                Ifiche.setDateEnregistrement((Date) aValue);
                break;
            case 2://Mois
                Ifiche.setMois(aValue + "");
                break;
            case 3://Agent
                int newIdAgent = Integer.parseInt(aValue + "");
                if (!isThisPaieAlreadyExists(Ifiche.getMois(), newIdAgent)) {
                    Ifiche.setIdAgent(newIdAgent);
                    updateCategorieAgent(Ifiche);
                }else{
                    JOptionPane.showMessageDialog(parent, parametreFichesDePaie.getNomUtilisateur()+",\nCette paie a déjà été enregistrée !", "Alert - DOUBLON", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 5://Monnaie
                Ifiche.setIdMonnaie(Integer.parseInt(aValue + ""));
                break;
            case 6://Salaire de base
                Ifiche.setSalaireBase(Double.parseDouble(aValue + ""));
                break;
            case 7://Transport
                Ifiche.setTransport(Double.parseDouble(aValue + ""));
                break;
            case 8://Logement
                Ifiche.setLogement(Double.parseDouble(aValue + ""));
                break;
            case 9://Autres gains
                Ifiche.setAutresGains(Double.parseDouble(aValue + ""));
                break;
            case 11://IPR
                Ifiche.setRetenu_IPR(Double.parseDouble(aValue + ""));
                break;
            case 12://INSS
                Ifiche.setRetenu_INSS(Double.parseDouble(aValue + ""));
                break;
            case 13://Syndicat
                Ifiche.setRetenu_SYNDICAT(Double.parseDouble(aValue + ""));
                break;
            case 14://Absence
                Ifiche.setRetenu_ABSENCE(Double.parseDouble(aValue + ""));
                break;
            case 15://CAfétariat
                Ifiche.setRetenu_CAFETARIAT(Double.parseDouble(aValue + ""));
                break;
            case 16://Avance sur Salaire
                Ifiche.setRetenu_AVANCE_SALAIRE(Double.parseDouble(aValue + ""));
                break;
            case 17://Ordinateur
                Ifiche.setRetenu_ORDINATEUR(Double.parseDouble(aValue + ""));
                break;
            default:
                break;
        }
        String apres = Ifiche.toString();
        if (!avant.equals(apres)) {
            if (Ifiche.getBeta() == InterfaceFiche.BETA_EXISTANT) {
                Ifiche.setBeta(InterfaceFiche.BETA_MODIFIE);
                mEnreg.setCouleur(Color.blue);
                btEnreg.setCouleur(Color.blue);
            }
        }
        listeData.set(rowIndex, Ifiche);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
