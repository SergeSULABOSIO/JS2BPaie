/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI_Paie;

import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.CallBack_Paie.EcouteurActualisationPaie;
import SOURCES.CallBack_Paie.EcouteurAjoutPaie;
import SOURCES.CallBack_Paie.EcouteurPaie;
import SOURCES.EditeurTable_Paie.EditeurAgents;
import SOURCES.EditeurTable_Paie.EditeurDate;
import SOURCES.EditeurTable_Paie.EditeurMois;
import SOURCES.EditeurTable_Paie.EditeurMonnaie;
import SOURCES.GenerateurPDF_Paie.DocumentPDFPaie;
import SOURCES.ModelsTables_Paie.ModeleListeFiches;
import SOURCES.RendusTables_Paie.RenduTableFiche;
import SOURCES.Utilitaires_Paie.DataPaie;
import SOURCES.Utilitaires_Paie.ParametreFichesDePaie;
import SOURCES.Utilitaires_Paie.SortiesFichesDePaies;
import SOURCES.Utilitaires_Paie.UtilPaie;
import Source.Callbacks.EcouteurEnregistrement;
import Source.Callbacks.EcouteurSuppressionElement;
import Source.Callbacks.EcouteurUpdateClose;
import Source.Callbacks.EcouteurValeursChangees;
import Source.GestionClickDroit;
import Source.GestionEdition;
import Source.Interface.InterfaceAgent;
import Source.Interface.InterfaceFiche;
import Source.Interface.InterfaceMonnaie;
import Source.Interface.InterfaceUtilisateur;
import Source.Objet.Agent;
import Source.Objet.CouleurBasique;
import Source.Objet.Entreprise;
import Source.Objet.Fiche;
import Source.Objet.Monnaie;
import Source.Objet.Utilisateur;
import Source.UI.NavigateurPages;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author HP Pavilion
 */
public class PanelPaie extends javax.swing.JPanel {

    /**
     * Creates new form Panel
     */
    //public int indexTabSelected = 0;
    private Icones icones = null;
    private final JTabbedPane parent;
    private PanelPaie moi = null;
    private EcouteurUpdateClose ecouteurClose = null;
    private EcouteurAjoutPaie ecouteurAjout = null;
    public Bouton btEnregistrer, btAjouter, btSupprimer, btVider, btImprimer, btPDF, btFermer, btActualiser, btPDFSynth, btEdition;
    public RubriqueSimple mEnregistrer, mAjouter, mSupprimer, mVider, mImprimer, mPDF, mFermer, mActualiser, mPDFSynth, mEdition;
    private MenuContextuel menuContextuel = null;
    private BarreOutils bOutils = null;
    private EcouteurPaie ecouteurPaie = null;

    private ModeleListeFiches modeleListeFiches;
    public int indexTabSelected = -1;
    public DataPaie dataPaie;
    public double totBrut, totRetenu, totNet = 0;
    public double totBrutSel, totRetenuSel, totNetSel = 0;

    public String monnaieOutput = "";
    public static final int TYPE_EXPORT_TOUT = 0;
    public static final int TYPE_EXPORT_SELECTION = 1;
    public int typeExport = TYPE_EXPORT_TOUT;
    private Agent SelectedAgent = null;
    private Fiche SelectedFichePaie = null;
    private CouleurBasique couleurBasique;
    private EcouteurActualisationPaie ecouteurActualisationPaie = null;
    private JProgressBar progress;
    private GestionEdition gestionEdition = new GestionEdition();

    public PanelPaie(CouleurBasique couleurBasique, JProgressBar progress, JTabbedPane parent, DataPaie dataPaie, EcouteurPaie ecouteurPaie) {
        this.initComponents();
        this.progress = progress;
        this.couleurBasique = couleurBasique;
        this.dataPaie = dataPaie;
        this.ecouteurPaie = ecouteurPaie;
        this.icones = new Icones();
        this.parent = parent;
        this.init();

        //Initialisaterus
        parametrerTableFicheDePaie();
        setIconesTabs();

        initMonnaieTotaux();
        actualiserTotaux();

        ecouterAgentSelectionne();

        ecouterClickDroit();
    }

    public NavigateurPages getNavigateurPagesFichePaie() {
        return navigateurPagesFichePaie;
    }

    public void ecouterClickDroit() {
        new GestionClickDroit(menuContextuel, tableListeFichesDePaie, scrollListeFichesDePaie).init();
    }

    public EcouteurActualisationPaie getEcouteurActualisationPaie() {
        return ecouteurActualisationPaie;
    }

    public void setEcouteurActualisationPaie(EcouteurActualisationPaie ecouteurActualisationPaie) {
        this.ecouteurActualisationPaie = ecouteurActualisationPaie;
    }

    public int getTailleResultatFiches() {
        if (modeleListeFiches != null) {
            return modeleListeFiches.getListeData().size();
        }
        return 0;
    }

    public void setBtEnregistrerNouveau() {
        if (mEnregistrer != null && btEnregistrer != null) {
            mEnregistrer.setCouleur(couleurBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
            btEnregistrer.setForeground(couleurBasique.getCouleur_foreground_objet_nouveau());
        }
    }

    public void setDonneesFichePaie(Fiche ficheDepaie) {
        if (modeleListeFiches != null && ficheDepaie != null) {
            modeleListeFiches.setDonneesFichePaie(ficheDepaie);
        }
        if (navigateurPagesFichePaie != null) {
            navigateurPagesFichePaie.patienter(false, "Prêt.");
        }
    }

    public void reiniliserFichePaie() {
        if (modeleListeFiches != null) {
            modeleListeFiches.reinitialiserListe();
        }
        if (navigateurPagesFichePaie != null) {
            navigateurPagesFichePaie.patienter(false, "Prêt.");
        }
    }

    public int getCategorie(String categorie) {
        switch (categorie) {
            case "ADMINISTRATION_1":
                return InterfaceAgent.CATEGORIE_ADMINISTRATION_1;
            case "ADMINISTRATION_2":
                return InterfaceAgent.CATEGORIE_ADMINISTRATION_2;
            case "MATERNELLE":
                return InterfaceAgent.CATEGORIE_MATERNELLE;
            case "PARTIEL":
                return InterfaceAgent.CATEGORIE_PARTIEL;
            case "PRIMAIRE":
                return InterfaceAgent.CATEGORIE_PRIMAIRE;
            case "PRIME":
                return InterfaceAgent.CATEGORIE_PRIME;
            case "SECONDAIRE":
                return InterfaceAgent.CATEGORIE_SECONDAIRE;
            case "SURVEILLANT":
                return InterfaceAgent.CATEGORIE_SURVEILLANT;
        }
        return -1;
    }

    public boolean search_verifier_motcle(Fiche Ifiche, String motcle) {
        if (Ifiche != null) {
            if (motcle.trim().length() == 0) {
                return true;
            } else if (UtilPaie.contientMotsCles(Ifiche.getMois(), motcle) == true || UtilPaie.contientMotsCles(getAgent_(Ifiche.getIdAgent()), motcle) == true) {
                return true;
            }
        }
        return false;
    }

    public boolean search_verifier_categorie(Fiche Ifiche, int idCategorie) {
        if (Ifiche != null) {
            if (idCategorie == -1) {
                return true;
            } else if (Ifiche.getCategorieAgent() == idCategorie) {
                return true;
            }
        }
        return false;
    }

    public String getAgent_(int idAgent) {
        for (Agent Icha : dataPaie.getParametreFichesDePaie().getAgents()) {
            if (Icha.getId() == idAgent) {
                return Icha.getNom() + " " + Icha.getPostnom() + " " + Icha.getPrenom();
            }
        }
        return "";
    }

    public boolean search_verifier_mois(Fiche Ifiche, String mois) {
        if (Ifiche != null) {
            if (mois.trim().equals("")) {
                return true;
            } else if (Ifiche.getMois().equals(mois)) {
                return true;
            }
        }
        return false;
    }

    public boolean search_verifier_periode(Fiche Ifiche, Date dateA, Date dateB) {
        if (Ifiche != null) {
            boolean apresA = (Ifiche.getDateEnregistrement().after(dateA) || Ifiche.getDateEnregistrement().equals(dateA));
            boolean avantB = (Ifiche.getDateEnregistrement().before(dateB) || Ifiche.getDateEnregistrement().equals(dateB));
            if (apresA == true && avantB == true) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /*
    private void reDrawData(String motcle) {
        modeleListeFiches.chercher(chMois.getSelectedItem() + "", UtilPaie.getDate_CeMatin(chEntre.getDate()), UtilPaie.getDate_ZeroHeure(chEt.getDate()), motcle, getCategorie(chCategorie.getSelectedItem() + ""));
        actualiserTotaux("activerMoteurRecherche");
    }
     */
    public Entreprise getEntreprise() {
        return this.dataPaie.getParametreFichesDePaie().getEntreprise();
    }

    public String getNomUtilisateur() {
        return this.dataPaie.getParametreFichesDePaie().getUtilisateur().getNom();
    }

    public String getTitreDoc() {
        if (typeExport == PanelPaie.TYPE_EXPORT_SELECTION) {
            return "BILLETIN DE PAIE";
        } else {
            return "ETAT DE PAIE DU PERSONNEL";
        }
    }

    public Date getDateDocument() {
        return new Date();
    }

    public int getTypeExport() {
        return typeExport;
    }

    private void initMonnaieTotaux() {
        String labTaux = "Taux de change: ";
        Monnaie monnaieLocal = null;
        combototMonnaie.removeAllItems();
        for (Monnaie monnaie : this.dataPaie.getParametreFichesDePaie().getMonnaies()) {
            combototMonnaie.addItem(monnaie.getCode() + " - " + monnaie.getNom());
            if (monnaie.getTauxMonnaieLocale() == 1) {
                monnaieLocal = monnaie;
            }
        }
        for (Monnaie monnaie : this.dataPaie.getParametreFichesDePaie().getMonnaies()) {
            if (monnaie != monnaieLocal) {
                labTaux += " 1 " + monnaie.getCode() + " = " + UtilPaie.getMontantFrancais(monnaie.getTauxMonnaieLocale()) + " " + monnaieLocal.getCode() + ", ";
            }
        }
        labTauxDeChange.setText(labTaux);
    }

    private Monnaie getSelectedMonnaieTotaux() {
        for (Monnaie monnaie : this.dataPaie.getParametreFichesDePaie().getMonnaies()) {
            if ((monnaie.getCode() + " - " + monnaie.getNom()).equals(combototMonnaie.getSelectedItem() + "")) {
                return monnaie;
            }
        }
        return null;
    }

    private Monnaie getMonnaie(int idMonnaie) {
        for (Monnaie monnaie : this.dataPaie.getParametreFichesDePaie().getMonnaies()) {
            if (monnaie.getId() == idMonnaie) {
                return monnaie;
            }
        }
        return null;
    }

    private void getMontantsMasse(Monnaie ImonnaieOutput, Fiche Ifiche) {
        if (Ifiche != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == Ifiche.getIdMonnaie()) {
                totNet += UtilPaie.getNetAPayer(Ifiche);
                totBrut += UtilPaie.getTotalAPayer(Ifiche);
                totRetenu += UtilPaie.getTotalRetenu(Ifiche);
            } else {
                Monnaie ImonnaieOrigine = getMonnaie(Ifiche.getIdMonnaie());
                if (ImonnaieOrigine != null) {
                    totNet += (UtilPaie.getNetAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totBrut += (UtilPaie.getTotalAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totRetenu += (UtilPaie.getTotalRetenu(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                }
            }
        }
    }

    private void getMontantsSelection(Monnaie ImonnaieOutput, Fiche Ifiche) {
        if (Ifiche != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == Ifiche.getIdMonnaie()) {
                totNetSel += UtilPaie.getNetAPayer(Ifiche);
                totBrutSel += UtilPaie.getTotalAPayer(Ifiche);
                totRetenuSel += UtilPaie.getTotalRetenu(Ifiche);
            } else {
                Monnaie ImonnaieOrigine = getMonnaie(Ifiche.getIdMonnaie());
                if (ImonnaieOrigine != null) {
                    totNetSel += (UtilPaie.getNetAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totBrutSel += (UtilPaie.getTotalAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totRetenuSel += (UtilPaie.getTotalRetenu(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                }
            }
        }
    }

    private void actualiserTotaux() {
        totBrut = 0;
        totBrutSel = 0;
        totNet = 0;
        totNetSel = 0;
        totRetenu = 0;
        totRetenuSel = 0;

        Monnaie ImonnaieOutput = null;
        if (modeleListeFiches != null) {
            ImonnaieOutput = getSelectedMonnaieTotaux();
            for (Fiche iFiche : modeleListeFiches.getListeData()) {
                getMontantsMasse(ImonnaieOutput, iFiche);
            }
        }

        //Pour la selection
        int[] tabLignesSelected = tableListeFichesDePaie.getSelectedRows();
        double totalSel = 0;
        for (int i = 0; i < tabLignesSelected.length; i++) {
            if (tabLignesSelected[i] != -1) {
                if (modeleListeFiches != null) {
                    Fiche iFiche = modeleListeFiches.getFiche(tabLignesSelected[i]);
                    if (iFiche != null && ImonnaieOutput != null) {
                        getMontantsSelection(ImonnaieOutput, iFiche);
                    }
                }
            }
        }

        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
        }

        //Le global
        labSalBrut.setText(UtilPaie.getMontantFrancais(totBrut) + " " + monnaieOutput);
        labSalRetenu.setText(UtilPaie.getMontantFrancais(totRetenu) + " " + monnaieOutput);
        labSalNet.setText(UtilPaie.getMontantFrancais(totNet) + " " + monnaieOutput);

        //L'élements séléctionné
        labSalBrutSelected.setText(UtilPaie.getMontantFrancais(totBrutSel) + " " + monnaieOutput);
        labSalRetenuSelected.setText(UtilPaie.getMontantFrancais(totRetenuSel) + " " + monnaieOutput);
        labSalNetSelected.setText(UtilPaie.getMontantFrancais(totNetSel) + " " + monnaieOutput);

        if (progress != null) {
            progress.setVisible(false);
            progress.setIndeterminate(false);
        }
    }

    public double getTotBrut() {
        return totBrut;
    }

    public double getTotRetenu() {
        return totRetenu;
    }

    public double getTotNet() {
        return totNet;
    }

    public double getTotBrutSel() {
        return totBrutSel;
    }

    public double getTotRetenuSel() {
        return totRetenuSel;
    }

    public double getTotNetSel() {
        return totNetSel;
    }

    public String getMonnaieOutput() {
        return this.monnaieOutput;
    }

    public String getTauxChange() {
        return labTauxDeChange.getText();
    }

    private void actualiserTotaux(String methode) {
        //System.out.println("actualiserTotaux - " + methode);
        actualiserTotaux();
    }

    private void parametrerTableFicheDePaie() {
        initModelTableFicheDePai();
        chargerDataTableFicheDePaie();
        fixerColonnesTableFicheDePaie(true);
        //filtrerTableFichePaie();
    }

    private void actualiserEditeur() {
        gestionEdition.reinitialiser();
        //Actualisation des listes
        modeleListeFiches.actualiser();
    }

    private void setEditionMode() {
        switch (indexTabSelected) {
            case 0:
                if (SelectedFichePaie != null && gestionEdition != null) {
                    if (gestionEdition.isEditable(SelectedFichePaie.getId(), indexTabSelected)) {
                        gestionEdition.setModeEdition(SelectedFichePaie.getId(), indexTabSelected, false);
                    } else {
                        gestionEdition.setModeEdition(SelectedFichePaie.getId(), indexTabSelected, true);
                    }
                    modeleListeFiches.actualiser();
                }
                break;
            default:
        }
    }

    private void initModelTableFicheDePai() {
        this.modeleListeFiches = new ModeleListeFiches(gestionEdition, progress, couleurBasique, scrollListeFichesDePaie, btEnregistrer, mEnregistrer, this.dataPaie.getParametreFichesDePaie(), new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null) {
                    ecouteurClose.onActualiser(modeleListeFiches.getRowCount() + " élement(s).", icones.getDossier_01());
                }
            }
        });
    }

    private void chargerDataTableFicheDePaie() {
        tableListeFichesDePaie.setModel(modeleListeFiches);
    }

    private void fixerColonnesTableFicheDePaie(boolean resizeTable) {
        //Parametrage du rendu de la table
        this.tableListeFichesDePaie.setDefaultRenderer(Object.class, new RenduTableFiche(gestionEdition, couleurBasique, icones.getModifier_01(), this.dataPaie.getParametreFichesDePaie(), modeleListeFiches));
        this.tableListeFichesDePaie.setRowHeight(25);

        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base", "Transport", "Logement", "Autres gains", "TOTAL BRUT", "Ipr", "Inss", "Syndicat", "Absence", "Cafétariat", "Av. Salaire", "Ordinateur", "TOTAL RETENUS", "NET A PAYER"};
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(0), 30, true, null);//N°
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(1), 110, true, new EditeurDate());//Date
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(2), 100, false, new EditeurMois(this.dataPaie.getParametreFichesDePaie()));//Mois
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(3), 200, false, new EditeurAgents(this.dataPaie.getParametreFichesDePaie()));//Agent
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(4), 150, false, null);//Catégorie
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(5), 80, false, new EditeurMonnaie(this.dataPaie.getParametreFichesDePaie()));//Monnaie
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(6), 100, true, null);//Salaire de base
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(7), 100, true, null);//Transport
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(8), 100, true, null);//Logement
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(9), 100, true, null);//Autres gains
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(10), 120, true, null);//TOTAL(+)
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(11), 100, true, null);//ipr
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(12), 100, true, null);//inss
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(13), 100, true, null);//Syndicat
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(14), 100, true, null);//Absence
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(15), 100, true, null);//Cafétariat
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(16), 100, true, null);//Avance sur salaire
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(17), 100, true, null);//Ordinateur
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(18), 120, true, null);//TOTAL(-)
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(19), 120, true, null);//NET A PAYER

        //On écoute les sélction
        this.tableListeFichesDePaie.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    ecouterAgentSelectionne();
                    actualiserTotaux("ecouterSelection - Table Fiche de paie");
                }
            }
        });

        if (resizeTable == true) {
            this.tableListeFichesDePaie.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    public Agent getSelectedAgent() {
        return SelectedAgent;
    }

    public Fiche getSelectedFichePaie() {
        return SelectedFichePaie;
    }

    private void ecouterAgentSelectionne() {
        int ligneSelected = tableListeFichesDePaie.getSelectedRow();
        if (ligneSelected != -1) {
            this.SelectedFichePaie = modeleListeFiches.getFiche(ligneSelected);
            if (SelectedFichePaie != null) {
                SelectedAgent = getAgent(SelectedFichePaie.getIdAgent());
                if (SelectedAgent != null) {
                    btPDFSynth.setText("Prod. Bulletin", 12, true);
                    btPDFSynth.appliquerDroitAccessDynamique(true);
                    mPDFSynth.setText("Produire le bulletin de " + SelectedAgent.getNom() + " " + SelectedAgent.getPrenom());
                    mPDFSynth.appliquerDroitAccessDynamique(true);
                    renameTitrePaneAgent("Sélection - " + SelectedAgent.getNom() + " " + SelectedAgent.getPostnom() + " " + SelectedAgent.getPrenom());

                    String SAgent = SelectedAgent.getNom() + " " + SelectedAgent.getPostnom() + " " + SelectedAgent.getPrenom();
                    InterfaceMonnaie Imon = getMonnaie(SelectedFichePaie.getIdMonnaie());
                    String monnaie = Imon.getCode();
                    double nett = UtilPaie.getNetAPayer(SelectedFichePaie);
                    String net = UtilPaie.getMontantFrancais(nett);
                    String netLettre = UtilPaie.getMontantLettres(nett, Imon.getNom());
                    this.ecouteurClose.onActualiser(SAgent + ", Paie de " + SelectedFichePaie.getMois() + ", Net à Payer: " + net + " " + monnaie + " (" + netLettre + ").", icones.getClient_01());

                } else {
                    desactiverBts();
                }
            } else {
                desactiverBts();
            }
        } else {
            desactiverBts();
        }
    }

    private void renameTitrePaneAgent(String titre) {
        panSelected.setBorder(javax.swing.BorderFactory.createTitledBorder(null, titre, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(51, 51, 255))); // NOI18N
    }

    private void desactiverBts() {
        if (btPDFSynth != null && mPDFSynth != null) {
            btPDFSynth.appliquerDroitAccessDynamique(false);
            mPDFSynth.appliquerDroitAccessDynamique(false);
            mPDFSynth.setText("Produire le billetin");
            renameTitrePaneAgent("Sélection");
        }
    }

    private Agent getAgent(int idAgent) {
        for (Agent Icha : this.dataPaie.getParametreFichesDePaie().getAgents()) {
            if (Icha.getId() == idAgent) {
                return Icha;
            }
        }
        return null;
    }

    private void filtrerTableFichePaie() {
        //gestionnaire de filtre
        TableRowSorter<ModeleListeFiches> sorter = new TableRowSorter<ModeleListeFiches>(modeleListeFiches);
        tableListeFichesDePaie.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKey = new ArrayList<>();
        sortKey.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKey);
        sorter.sort();
    }

    private void setTaille(TableColumn column, int taille, boolean fixe, TableCellEditor editor) {
        column.setPreferredWidth(taille);
        if (fixe == true) {
            column.setMaxWidth(taille);
            column.setMinWidth(taille);
        }
        if (editor != null) {
            column.setCellEditor(editor);
        }
    }

    public void init() {
        this.moi = this;
        this.labInfos.setIcon(icones.getInfos_01());
        this.labInfos.setText("Prêt.");

        this.ecouteurClose = new EcouteurUpdateClose() {
            @Override
            public void onFermer() {
                parent.remove(moi);
            }

            @Override
            public void onActualiser(String texte, ImageIcon icone) {
                labInfos.setText(texte);
                labInfos.setIcon(icone);
            }
        };

        ecouteurAjout = new EcouteurAjoutPaie() {
            @Override
            public void setAjoutFiche(ModeleListeFiches modeleListeFiches) {
                if (modeleListeFiches != null) {
                    if (dataPaie != null) {
                        Date dateEnreg = new Date();
                        int id = -1;
                        int idAgent = -1;
                        int idExercice = dataPaie.getParametreFichesDePaie().getExercice().getId();
                        int idEntreprise = dataPaie.getParametreFichesDePaie().getEntreprise().getId();
                        int idUtilisateur = dataPaie.getParametreFichesDePaie().getUtilisateur().getId();

                        Vector<Monnaie> listeMon = dataPaie.getParametreFichesDePaie().getMonnaies();
                        int idMonnaie = -1;
                        if (listeMon != null) {
                            if (!listeMon.isEmpty()) {
                                idMonnaie = listeMon.firstElement().getId();
                            }
                        }
                        int beta = InterfaceFiche.BETA_NOUVEAU;
                        String mois = UtilPaie.getDateFrancais_Mois(dateEnreg);

                        modeleListeFiches.AjouterFichet(new Fiche(id, idEntreprise, idUtilisateur, idExercice, idMonnaie, idAgent, InterfaceAgent.CATEGORIE_ADMINISTRATION_1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, dateEnreg, mois, beta));
                        //On sélectionne la première ligne
                        tableListeFichesDePaie.setRowSelectionInterval(0, 0);
                    }

                }
            }
        };

        setBoutons();
        setMenuContextuel();
    }

    public void activerBoutons(int selectedTab) {
        this.indexTabSelected = selectedTab;
        actualiserTotaux("activerBoutons");
        ecouterAgentSelectionne();
    }

    public void ajouter() {
        switch (indexTabSelected) {
            case 0: //Tab Encaissement
                this.ecouteurAjout.setAjoutFiche(modeleListeFiches);
                break;
        }
    }

    public void supprimer() {
        switch (indexTabSelected) {
            case 0: //Tab Encaissement
                modeleListeFiches.SupprimerFiche(tableListeFichesDePaie.getSelectedRow(), new EcouteurSuppressionElement() {
                    @Override
                    public void onSuppressionConfirmee(int idElement) {
                        ecouteurPaie.onDetruitElement(idElement);
                    }
                });
                break;
        }
    }

    public void vider() {
        this.ecouteurClose.onActualiser("Vidé!", icones.getInfos_01());
        switch (indexTabSelected) {
            case 0: //Tab Encaissement
                modeleListeFiches.viderListe();
                if (ecouteurPaie != null) {
                    ecouteurPaie.onDetruitTout(dataPaie.getParametreFichesDePaie().getExercice().getId());
                }
                break;
        }
    }

    private void setIconesTabs() {
        this.tabPrincipal.setIconAt(0, icones.getDossier_01());  //Liste des Fiches de paie
        this.llabSalBrut.setIcon(icones.getNombre_01());
        this.llabSalRetenu.setIcon(icones.getNombre_01());
        this.llabSalNet.setIcon(icones.getNombre_01());
        this.llabSalBrutSelected.setIcon(icones.getNombre_01());
        this.llabSalRetenuSelected.setIcon(icones.getNombre_01());
        this.llabSalNetSelected.setIcon(icones.getNombre_01());
    }

    private void setMenuContextuel() {
        mAjouter = new RubriqueSimple("Ajouter", 12, false, icones.getAjouter_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                ajouter();
            }
        });

        mSupprimer = new RubriqueSimple("Supprimer", 12, false, icones.getSupprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                supprimer();
            }
        });

        mEnregistrer = new RubriqueSimple("Enregistrer", 12, true, icones.getEnregistrer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                enregistrer();
            }
        });

        mVider = new RubriqueSimple("Vider", 12, false, icones.getAnnuler_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                vider();
            }
        });

        mImprimer = new RubriqueSimple("Imprimer", 12, false, icones.getImprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                imprimer();
            }
        });

        mFermer = new RubriqueSimple("Fermer", 12, false, icones.getFermer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                fermer();
            }
        });

        mPDF = new RubriqueSimple("Exporter tout", 12, false, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                typeExport = TYPE_EXPORT_TOUT;
                exporterPDF();
            }
        });

        mActualiser = new RubriqueSimple("Actualiser", 12, false, icones.getSynchroniser_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                actualiser();
            }
        });

        mPDFSynth = new RubriqueSimple("Export cette fiche de paie", 12, true, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                typeExport = TYPE_EXPORT_SELECTION;
                exporterPDF();
            }
        });

        mEdition = new RubriqueSimple("Editer", 12, false, icones.getModifier_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                setEditionMode();
            }
        });

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        menuContextuel = new MenuContextuel();
        if (dataPaie.getParametreFichesDePaie().getUtilisateur() != null) {
            Utilisateur user = dataPaie.getParametreFichesDePaie().getUtilisateur();
            
            if (user.getDroitPaie() == InterfaceUtilisateur.DROIT_CONTROLER) {
                menuContextuel.Ajouter(mEnregistrer);
                menuContextuel.Ajouter(mAjouter);
                menuContextuel.Ajouter(mEdition);
                menuContextuel.Ajouter(new JPopupMenu.Separator());
                menuContextuel.Ajouter(mSupprimer);
                menuContextuel.Ajouter(mVider);
            }
            menuContextuel.Ajouter(mActualiser);
            menuContextuel.Ajouter(new JPopupMenu.Separator());
            menuContextuel.Ajouter(mImprimer);
            menuContextuel.Ajouter(mPDF);
            menuContextuel.Ajouter(mPDFSynth);
            menuContextuel.Ajouter(new JPopupMenu.Separator());
            menuContextuel.Ajouter(mFermer);
        }
    }

    private void setBoutons() {
        btAjouter = new Bouton(12, "Ajouter", "Ajouter un élement", false, icones.getAjouter_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                ajouter();
            }
        });

        btSupprimer = new Bouton(12, "Supprimer", "Supprimer l'élement séléctionné", false, icones.getSupprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                supprimer();
            }
        });

        btEnregistrer = new Bouton(12, "Enregistrer", "Enregistrer les modifications", false, icones.getEnregistrer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                enregistrer();
            }
        });
        btEnregistrer.setGras(true);

        btVider = new Bouton(12, "Vider", "Vider cette liste", false, icones.getAnnuler_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                vider();
            }
        });

        btImprimer = new Bouton(12, "Imprimer", "Imprimer", false, icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        btFermer = new Bouton(12, "Fermer", "Fermer cet onglet", false, icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        btPDF = new Bouton(12, "Exp. Tout", "Tout exporter vers un PDF", false, icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = TYPE_EXPORT_TOUT;
                exporterPDF();
            }
        });

        btPDFSynth = new Bouton(12, "Exp. bulletin", "Produire le bulletin de paie", false, icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = TYPE_EXPORT_SELECTION;
                exporterPDF();
            }
        });

        btActualiser = new Bouton(12, "Actualiser", "Actualiser cette liste", false, icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiser();
            }
        });

        btEdition = new Bouton(12, "Edition", "", true, icones.getModifier_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                setEditionMode();
            }
        });

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        bOutils = new BarreOutils(barreOutils);
        if (dataPaie.getParametreFichesDePaie().getUtilisateur() != null) {
            Utilisateur user = dataPaie.getParametreFichesDePaie().getUtilisateur();

            if (user.getDroitPaie() == InterfaceUtilisateur.DROIT_CONTROLER) {
                bOutils.AjouterBouton(btEnregistrer);
                bOutils.AjouterBouton(btAjouter);
                bOutils.AjouterBouton(btEdition);
                bOutils.AjouterSeparateur();
                bOutils.AjouterBouton(btSupprimer);
                bOutils.AjouterBouton(btVider);
            }
            bOutils.AjouterBouton(btActualiser);
            bOutils.AjouterSeparateur();
            bOutils.AjouterBouton(btImprimer);
            bOutils.AjouterBouton(btPDF);
            bOutils.AjouterBouton(btPDFSynth);
            bOutils.AjouterSeparateur();
            bOutils.AjouterBouton(btFermer);
        }
    }

    private boolean mustBeSaved() {
        boolean rep = false;
        //On vérifie dans la liste d'encaissements
        for (Fiche Ienc : modeleListeFiches.getListeData()) {
            if (Ienc.getBeta() == InterfaceFiche.BETA_MODIFIE || Ienc.getBeta() == InterfaceFiche.BETA_NOUVEAU) {
                rep = true;
            }
        }
        return rep;
    }

    public void fermer() {
        //Vérifier s'il n'y a rien à enregistrer
        if (mustBeSaved() == true) {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous enregistrer les modifications et/ou ajouts apportés à ces données?", "Avertissement", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurPaie.onEnregistre(getSortiesFichesDePaies(btEnregistrer, mEnregistrer));
                this.ecouteurClose.onFermer();
            } else if (dialogResult == JOptionPane.NO_OPTION) {
                this.ecouteurClose.onFermer();
            }
        } else {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir fermer cette fenêtre?", "Avertissement", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurClose.onFermer();
            }
        }
    }

    public void imprimer() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir imprimer ce document?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesFichesDePaies sortie = getSortiesFichesDePaies(btImprimer, mImprimer);
                DocumentPDFPaie documentPDF = new DocumentPDFPaie(this, DocumentPDFPaie.ACTION_IMPRIMER, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ParametreFichesDePaie getParametreFichesDePaie() {
        return this.dataPaie.getParametreFichesDePaie();
    }

    public String getNomfichierPreuve() {
        return "Output.pdf";
    }

    private SortiesFichesDePaies getSortiesFichesDePaies(Bouton boutonDeclencheur, RubriqueSimple rubriqueDeclencheur) {
        EcouteurEnregistrement ecouteurEnregistrement = new EcouteurEnregistrement() {
            @Override
            public void onDone(String message) {
                ecouteurClose.onActualiser(message, icones.getAimer_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }

                //On redessine les tableaux afin que les couleurs se réinitialisent / Tout redevient noire
                if (modeleListeFiches != null) {
                    modeleListeFiches.redessinerTable();
                }
            }

            @Override
            public void onError(String message) {
                ecouteurClose.onActualiser(message, icones.getAlert_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }
            }

            @Override
            public void onUploading(String message) {
                ecouteurClose.onActualiser(message, icones.getInfos_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(false);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(false);
                }
            }
        };
        SortiesFichesDePaies sortiesFichesDePaies = new SortiesFichesDePaies(ecouteurEnregistrement, modeleListeFiches.getListeData());
        return sortiesFichesDePaies;
    }

    public void enregistrer() {
        if (ecouteurPaie != null) {
            btEnregistrer.setForeground(Color.black);
            mEnregistrer.setCouleur(Color.BLACK);

            SortiesFichesDePaies sortie = getSortiesFichesDePaies(btEnregistrer, mEnregistrer);
            this.ecouteurPaie.onEnregistre(sortie);

            actualiserEditeur();
        }
    }

    public void exporterPDF() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesFichesDePaies sortie = getSortiesFichesDePaies(btPDF, mPDF);
                DocumentPDFPaie docpdf = new DocumentPDFPaie(this, DocumentPDFPaie.ACTION_OUVRIR, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void actualiser() {
        if (navigateurPagesFichePaie != null) {
            navigateurPagesFichePaie.reload();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barreOutils = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();
        scrollListeFichesDePaie = new javax.swing.JScrollPane();
        tableListeFichesDePaie = new javax.swing.JTable();
        labInfos = new javax.swing.JLabel();
        panSelected = new javax.swing.JPanel();
        labSalBrutSelected = new javax.swing.JLabel();
        labSalRetenuSelected = new javax.swing.JLabel();
        labSalNetSelected = new javax.swing.JLabel();
        llabSalBrutSelected = new javax.swing.JLabel();
        llabSalRetenuSelected = new javax.swing.JLabel();
        llabSalNetSelected = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labSalBrut = new javax.swing.JLabel();
        labSalRetenu = new javax.swing.JLabel();
        labSalNet = new javax.swing.JLabel();
        llabSalBrut = new javax.swing.JLabel();
        llabSalRetenu = new javax.swing.JLabel();
        llabSalNet = new javax.swing.JLabel();
        labTauxDeChange = new javax.swing.JLabel();
        combototMonnaie = new javax.swing.JComboBox<>();
        navigateurPagesFichePaie = new Source.UI.NavigateurPages();

        setBackground(new java.awt.Color(255, 255, 255));

        barreOutils.setBackground(new java.awt.Color(255, 255, 255));
        barreOutils.setFloatable(false);
        barreOutils.setRollover(true);
        barreOutils.setAutoscrolls(true);
        barreOutils.setPreferredSize(new java.awt.Dimension(59, 61));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture02.png"))); // NOI18N
        jButton5.setText("Ajouter");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barreOutils.add(jButton5);

        tabPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        tabPrincipal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPrincipalStateChanged(evt);
            }
        });

        scrollListeFichesDePaie.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeFichesDePaie.setAutoscrolls(true);
        scrollListeFichesDePaie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeFichesDePaieMouseClicked(evt);
            }
        });

        tableListeFichesDePaie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Article", "Qunatité", "Unités", "Prix Unitaire HT", "Tva %", "Tva", "Total TTC"
            }
        ));
        tableListeFichesDePaie.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tableListeFichesDePaieMouseDragged(evt);
            }
        });
        tableListeFichesDePaie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeFichesDePaieMouseClicked(evt);
            }
        });
        tableListeFichesDePaie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableListeFichesDePaieKeyReleased(evt);
            }
        });
        scrollListeFichesDePaie.setViewportView(tableListeFichesDePaie);

        tabPrincipal.addTab("Grille de paie", scrollListeFichesDePaie);

        labInfos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labInfos.setText("Prêt.");

        panSelected.setBackground(new java.awt.Color(255, 255, 255));
        panSelected.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fiche de paie séléctionnée", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255))); // NOI18N

        labSalBrutSelected.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalBrutSelected.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSalBrutSelected.setText("0000000000 $ ");

        labSalRetenuSelected.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalRetenuSelected.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSalRetenuSelected.setText("0000000000 $ ");

        labSalNetSelected.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalNetSelected.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSalNetSelected.setText("0000000000 $ ");

        llabSalBrutSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabSalBrutSelected.setText("Salaire net");

        llabSalRetenuSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabSalRetenuSelected.setText("Retenus");

        llabSalNetSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabSalNetSelected.setText("Net à payer");

        javax.swing.GroupLayout panSelectedLayout = new javax.swing.GroupLayout(panSelected);
        panSelected.setLayout(panSelectedLayout);
        panSelectedLayout.setHorizontalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSelectedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(llabSalBrutSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(llabSalRetenuSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(llabSalNetSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSalNetSelected, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addComponent(labSalRetenuSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labSalBrutSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        panSelectedLayout.setVerticalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSelectedLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labSalBrutSelected)
                    .addComponent(llabSalBrutSelected))
                .addGap(0, 0, 0)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labSalRetenuSelected)
                    .addComponent(llabSalRetenuSelected))
                .addGap(0, 0, 0)
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labSalNetSelected)
                    .addComponent(llabSalNetSelected))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Masse Salariale", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255))); // NOI18N

        labSalBrut.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalBrut.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSalBrut.setText("00000000 $");

        labSalRetenu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalRetenu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSalRetenu.setText("00000000 $");

        labSalNet.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalNet.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labSalNet.setText("00000000 $");

        llabSalBrut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabSalBrut.setText("Total - Salaires bruts");

        llabSalRetenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabSalRetenu.setText("Total - Retenus");

        llabSalNet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        llabSalNet.setText("Total - Nets à payer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(llabSalRetenu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(llabSalBrut, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(llabSalNet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSalNet, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                    .addComponent(labSalRetenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labSalBrut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labSalBrut)
                    .addComponent(llabSalBrut))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labSalRetenu)
                    .addComponent(llabSalRetenu))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labSalNet)
                    .addComponent(llabSalNet))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        labTauxDeChange.setForeground(new java.awt.Color(51, 51, 255));
        labTauxDeChange.setText("Taux");

        combototMonnaie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combototMonnaie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combototMonnaieItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barreOutils, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPrincipal)
            .addComponent(navigateurPagesFichePaie, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(combototMonnaie, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(labTauxDeChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labInfos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(navigateurPagesFichePaie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(combototMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labTauxDeChange)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labInfos)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableListeFichesDePaieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_tableListeFichesDePaieMouseClicked

    private void scrollListeFichesDePaieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeFichesDePaieMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_scrollListeFichesDePaieMouseClicked

    private void tabPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPrincipalStateChanged
        // TODO add your handling code here:
        activerBoutons(((JTabbedPane) evt.getSource()).getSelectedIndex());
    }//GEN-LAST:event_tabPrincipalStateChanged

    private void tableListeFichesDePaieKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeFichesDePaieKeyReleased

    private void combototMonnaieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combototMonnaieItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            actualiserTotaux("combototMonnaieItemStateChanged");
        }
    }//GEN-LAST:event_combototMonnaieItemStateChanged

    private void tableListeFichesDePaieMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeFichesDePaieMouseDragged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutils;
    private javax.swing.JComboBox<String> combototMonnaie;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labInfos;
    private javax.swing.JLabel labSalBrut;
    private javax.swing.JLabel labSalBrutSelected;
    private javax.swing.JLabel labSalNet;
    private javax.swing.JLabel labSalNetSelected;
    private javax.swing.JLabel labSalRetenu;
    private javax.swing.JLabel labSalRetenuSelected;
    private javax.swing.JLabel labTauxDeChange;
    private javax.swing.JLabel llabSalBrut;
    private javax.swing.JLabel llabSalBrutSelected;
    private javax.swing.JLabel llabSalNet;
    private javax.swing.JLabel llabSalNetSelected;
    private javax.swing.JLabel llabSalRetenu;
    private javax.swing.JLabel llabSalRetenuSelected;
    private Source.UI.NavigateurPages navigateurPagesFichePaie;
    private javax.swing.JPanel panSelected;
    private javax.swing.JScrollPane scrollListeFichesDePaie;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeFichesDePaie;
    // End of variables declaration//GEN-END:variables
}
