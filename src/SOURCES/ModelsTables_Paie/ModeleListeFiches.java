/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTables_Paie;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.Utilitaires_Paie.ParametreFichesDePaie;
import SOURCES.Utilitaires_Paie.UtilPaie;
import Source.Callbacks.EcouteurSuppressionElement;
import Source.Callbacks.EcouteurValeursChangees;
import Source.GestionEdition;
import Source.Interface.InterfaceFiche;
import Source.Objet.Agent;
import Source.Objet.CouleurBasique;
import Source.Objet.Encaissement;
import Source.Objet.Fiche_paie;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeFiches extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Impôt", "Séc. Sociale", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
    private Vector<Fiche_paie> listeData = new Vector<>();
    private Vector<Fiche_paie> listeDataExclus = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;
    private ParametreFichesDePaie parametreFichesDePaie;
    private CouleurBasique couleurBasique;
    private JProgressBar progress;
    private GestionEdition gestionEdition;

    public ModeleListeFiches(GestionEdition gestionEdition, JProgressBar progress, CouleurBasique couleurBasique, JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, ParametreFichesDePaie parametreFichesDePaie, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.progress = progress;
        this.ecouteurModele = ecouteurModele;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
        this.parametreFichesDePaie = parametreFichesDePaie;
        this.couleurBasique = couleurBasique;
        this.gestionEdition = gestionEdition;
    }

    public void reinitialiserListe() {
        if (progress != null) {
            progress.setVisible(false);
            progress.setIndeterminate(false);
        }
        this.listeData.removeAllElements();
        redessinerTable();
    }

    public void setListeFiches(Vector<Fiche_paie> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public Fiche_paie getFiche(int row) {
        if (row < listeData.size() && row != -1) {
            Fiche_paie art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Fiche_paie getFiche_id(int id) {
        if (id != -1) {
            for (Fiche_paie art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public void setDonneesFichePaie(Fiche_paie fiche) {
        if (progress != null) {
            progress.setVisible(true);
            progress.setIndeterminate(true);
        }
        listeData.add(fiche);
        actualiser();
        if (progress != null) {
            progress.setVisible(false);
            progress.setIndeterminate(false);
        }
    }

    public Vector<Fiche_paie> getListeData() {
        return this.listeData;
    }

    public void actualiser() {
        redessinerTable();
    }

    public void AjouterFichet(Fiche_paie newFiche) {
        this.listeData.add(0, newFiche);
        mEnreg.setCouleur(couleurBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
        btEnreg.setForeground(couleurBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
        redessinerTable();
    }

    public void SupprimerFiche(int row, EcouteurSuppressionElement ecouteurSuppressionElement) {
        if (row < listeData.size() && row != -1) {
            Fiche_paie articl = listeData.elementAt(row);
            if (articl != null) {
                int idASUpp = articl.getId();
                int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    if (row <= listeData.size()) {
                        this.listeData.removeElementAt(row);
                    }
                    redessinerTable();
                    if (ecouteurSuppressionElement != null) {
                        ecouteurSuppressionElement.onSuppressionConfirmee(idASUpp, articl.getSignature());
                    }
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
        Fiche_paie Ifiche = listeData.elementAt(rowIndex);
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
                return UtilPaie.getTotalAPayer(Ifiche);
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
                return UtilPaie.getTotalRetenu(Ifiche);
            case 19: //NET A PAYER
                return UtilPaie.getNetAPayer(Ifiche);
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
        Fiche_paie eleve = null;
        boolean canEdit = false;
        if (listeData.size() > rowIndex) {
            eleve = listeData.elementAt(rowIndex);
            canEdit = gestionEdition.isEditable(eleve.getId(), 0);
        }
        if (canEdit == true) {
            //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
            if (columnIndex == 0 || columnIndex == 4 || columnIndex == 10 || columnIndex == 18 || columnIndex == 19) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void updateCategorieAgent(Fiche_paie Ifiche) {
        if (Ifiche != null) {
            for (Agent Iagent : this.parametreFichesDePaie.getAgents()) {
                if (Ifiche.getIdAgent() == Iagent.getId()) {
                    Ifiche.setCategorieAgent(Iagent.getCategorie());
                    break;
                }
            }
        }
    }

    private boolean isThisPaieAlreadyExists(String mois, int idAgentNew) {
        boolean rep = false;
        for (Fiche_paie iFiche : listeData) {
            if (iFiche.getIdAgent() == idAgentNew && mois.equals(iFiche.getMois())) {
                return true;
            }
        }
        return rep;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
        Fiche_paie Ifiche = listeData.get(rowIndex);
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
                } else {
                    JOptionPane.showMessageDialog(parent, parametreFichesDePaie.getUtilisateur().getNom() + ",\nCette paie a déjà été enregistrée !", "Alert - DOUBLON", JOptionPane.ERROR_MESSAGE);
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
                mEnreg.setCouleur(couleurBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
                btEnreg.setForeground(couleurBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
            }
        }
        listeData.set(rowIndex, Ifiche);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}


