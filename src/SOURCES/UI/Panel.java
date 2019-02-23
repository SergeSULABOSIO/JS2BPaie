/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.CallBack.EcouteurAjout;
import SOURCES.CallBack.EcouteurEnregistrement;
import SOURCES.CallBack.EcouteurPaie;
import SOURCES.CallBack.EcouteurUpdateClose;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.EditeurTable.EditeurAgents;
import SOURCES.EditeurTable.EditeurDate;
import SOURCES.EditeurTable.EditeurMois;
import SOURCES.EditeurTable.EditeurMonnaie;
import SOURCES.Interface.InterfaceAgent;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceFiche;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelsTables.ModeleListeFiches;
import SOURCES.MoteurRecherche.MoteurRecherche;
import SOURCES.RendusTables.RenduTableFiche;
import SOURCES.Utilitaires.DonneesFicheDePaie;
import SOURCES.Utilitaires.ParametreFichesDePaie;
import SOURCES.Utilitaires.SortiesFichesDePaies;
import SOURCES.Utilitaires.Util;
import SOURCES.Utilitaires.XX_Fiche;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
public class Panel extends javax.swing.JPanel {

    /**
     * Creates new form Panel
     */
    //public int indexTabSelected = 0;
    private Icones icones = null;
    private final JTabbedPane parent;
    private Panel moi = null;
    private EcouteurUpdateClose ecouteurClose = null;
    private EcouteurAjout ecouteurAjout = null;
    public Bouton btEnregistrer, btAjouter, btSupprimer, btVider, btImprimer, btPDF, btFermer, btActualiser, btPDFSynth;
    public RubriqueSimple mEnregistrer, mAjouter, mSupprimer, mVider, mImprimer, mPDF, mFermer, mActualiser, mPDFSynth;
    private MenuContextuel menuContextuel = null;
    private BarreOutils bOutils = null;
    private EcouteurPaie ecouteurPaie = null;

    private ModeleListeFiches modeleListeFiches;
    private MoteurRecherche gestionnaireRecherche = null;
    public int indexTabSelected = -1;
    public DonneesFicheDePaie donneesFiche;
    public ParametreFichesDePaie parametreFichesDePaie;
    public double totBrut, totRetenu, totNet = 0;
    public double totBrutSel, totRetenuSel, totNetSel = 0;

    public String monnaieOutput = "";
    public static final int TYPE_EXPORT_TOUT = 0;
    public static final int TYPE_EXPORT_SELECTION = 1;
    public int typeExport = TYPE_EXPORT_TOUT;

    public Panel(JTabbedPane parent, DonneesFicheDePaie donneesFiche, ParametreFichesDePaie parametreFichesDePaie, EcouteurPaie ecouteurPaie) {
        this.initComponents();
        this.icones = new Icones();
        this.parent = parent;
        this.init();
        this.donneesFiche = donneesFiche;
        this.parametreFichesDePaie = parametreFichesDePaie;
        this.ecouteurPaie = ecouteurPaie;

        //Initialisaterus
        parametrerTableFicheDePaie();
        setIconesTabs();

        initMonnaieTotaux();
        actualiserTotaux();
        activerCriteres();

        initComposantsMoteursRecherche();
        activerMoteurRecherche();
        ecouterAgentSelectionne();
    }

    private void initComposantsMoteursRecherche() {
        //Composants pour Encaissements
        chRecherche.setTextInitial("Recherche : Saisissez votre mot clé ici, puis tapez ENTER");
        activerCriteres();
    }

    private void afficherCriterePlus() {
        panelCriteres_categorie.setVisible(true);
    }

    private void activerCriteres() {
        afficherCriterePlus();
    }

    public int getCategorie(String categorie) {
        switch (categorie) {
            case "CATEGORIE - ADMINISTRATION_1":
                return InterfaceAgent.CATEGORIE_ADMINISTRATION_1;
            case "CATEGORIE - ADMINISTRATION_2":
                return InterfaceAgent.CATEGORIE_ADMINISTRATION_2;
            case "CATEGORIE - MATERNELLE":
                return InterfaceAgent.CATEGORIE_MATERNELLE;
            case "CATEGORIE - PARTIEL":
                return InterfaceAgent.CATEGORIE_PARTIEL;
            case "CATEGORIE - PRIMAIRE":
                return InterfaceAgent.CATEGORIE_PRIMAIRE;
            case "CATEGORIE - PRIME":
                return InterfaceAgent.CATEGORIE_PRIME;
            case "CATEGORIE - SECONDAIRE":
                return InterfaceAgent.CATEGORIE_SECONDAIRE;
            case "CATEGORIE - SURVEILLANT":
                return InterfaceAgent.CATEGORIE_SURVEILLANT;
        }
        return -1;
    }

    private void activerMoteurRecherche() {
        gestionnaireRecherche = new MoteurRecherche(icones, chRecherche, ecouteurClose) {
            
            @Override
            public void chercher(String motcle) {
                //On extrait les critère de filtrage des Encaissements

                int idcategorie = getCategorie(chCategorie.getSelectedItem() + "");
                
                //ModeleListeFiches.chercher(chDateAEnc.getDate(), chDateBEnc.getDate(), motcle, idMonnaie, idDest, idRevenu);
                
                actualiserTotaux();
                actualiserTotaux("activerMoteurRecherche");
            }
        };

    }

    public InterfaceEntreprise getEntreprise() {
        return parametreFichesDePaie.getEntreprise();
    }

    public String getNomUtilisateur() {
        return parametreFichesDePaie.getNomUtilisateur();
    }

    public String getTitreDoc() {
        return "FICHE";
    }

    public Date getDateDocument() {
        return new Date();
    }

    private void initMonnaieTotaux() {
        String labTaux = "Taux de change des monnaies enregistrées: ";
        InterfaceMonnaie monnaieLocal = null;
        combototMonnaie.removeAllItems();
        for (InterfaceMonnaie monnaie : parametreFichesDePaie.getMonnaies()) {
            combototMonnaie.addItem(monnaie.getCode() + " - " + monnaie.getNom());
            if (monnaie.getTauxMonnaieLocale() == 1) {
                monnaieLocal = monnaie;
            }
        }
        for (InterfaceMonnaie monnaie : parametreFichesDePaie.getMonnaies()) {
            if (monnaie != monnaieLocal) {
                labTaux += " 1 " + monnaie.getCode() + " = " + Util.getMontantFrancais(monnaie.getTauxMonnaieLocale()) + " " + monnaieLocal.getCode() + ", ";
            }
        }
        labTauxDeChange.setText(labTaux);
    }

    private InterfaceMonnaie getSelectedMonnaieTotaux() {
        for (InterfaceMonnaie monnaie : parametreFichesDePaie.getMonnaies()) {
            if ((monnaie.getCode() + " - " + monnaie.getNom()).equals(combototMonnaie.getSelectedItem() + "")) {
                return monnaie;
            }
        }
        return null;
    }

    private InterfaceMonnaie getMonnaie(int idMonnaie) {
        for (InterfaceMonnaie monnaie : parametreFichesDePaie.getMonnaies()) {
            if (monnaie.getId() == idMonnaie) {
                return monnaie;
            }
        }
        return null;
    }

    private void getMontantsMasse(InterfaceMonnaie ImonnaieOutput, InterfaceFiche Ifiche) {
        if (Ifiche != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == Ifiche.getIdMonnaie()) {
                totNet += Util.getNetAPayer(Ifiche);
                totBrut += Util.getTotalAPayer(Ifiche);
                totRetenu += Util.getTotalRetenu(Ifiche);
            } else {
                InterfaceMonnaie ImonnaieOrigine = getMonnaie(Ifiche.getIdMonnaie());
                if (ImonnaieOrigine != null) {
                    totNet += (Util.getNetAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totBrut += (Util.getTotalAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totRetenu += (Util.getTotalRetenu(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                }
            }
        }
    }
    
    private void getMontantsSelection(InterfaceMonnaie ImonnaieOutput, InterfaceFiche Ifiche) {
        if (Ifiche != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == Ifiche.getIdMonnaie()) {
                totNetSel += Util.getNetAPayer(Ifiche);
                totBrutSel += Util.getTotalAPayer(Ifiche);
                totRetenuSel += Util.getTotalRetenu(Ifiche);
            } else {
                InterfaceMonnaie ImonnaieOrigine = getMonnaie(Ifiche.getIdMonnaie());
                if (ImonnaieOrigine != null) {
                    totNetSel += (Util.getNetAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totBrutSel += (Util.getTotalAPayer(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
                    totRetenuSel += (Util.getTotalRetenu(Ifiche) * ImonnaieOrigine.getTauxMonnaieLocale() / ImonnaieOutput.getTauxMonnaieLocale());
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
        
        InterfaceMonnaie ImonnaieOutput = null;
        if (modeleListeFiches != null) {
            ImonnaieOutput = getSelectedMonnaieTotaux();
            for (InterfaceFiche iFiche : modeleListeFiches.getListeData()) {
                getMontantsMasse(ImonnaieOutput, iFiche);
            }
        }

        //Pour la selection
        int[] tabLignesSelected = tableListeFichesDePaie.getSelectedRows();
        double totalSel = 0;
        for (int i = 0; i < tabLignesSelected.length; i++) {
            if (tabLignesSelected[i] != -1) {
                if (modeleListeFiches != null) {
                    InterfaceFiche iFiche = modeleListeFiches.getFiche(tabLignesSelected[i]);
                    if (iFiche != null && ImonnaieOutput != null) {
                        getMontantsSelection(ImonnaieOutput, iFiche);
                    }
                }
            }
        }

        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
        }
        
        labSalBrut.setText("Total Brut: " + Util.getMontantFrancais(totBrut) + " " + monnaieOutput);
        labSalRetenu.setText("Total Retenu: " + Util.getMontantFrancais(totRetenu) + " " + monnaieOutput);
        labSalNet.setText("Masse Salariale: " + Util.getMontantFrancais(totNet) + " " + monnaieOutput);
        
        labSalBrutSelected.setText("Salaire Brut: " + Util.getMontantFrancais(totBrutSel) + " " + monnaieOutput);
        labSalRetenuSelected.setText("Retenus: " + Util.getMontantFrancais(totRetenuSel) + " " + monnaieOutput);
        labSalNetSelected.setText("Net à Payer: " + Util.getMontantFrancais(totNetSel) + " " + monnaieOutput);
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
        System.out.println("actualiserTotaux - " + methode);
        actualiserTotaux();
    }

    private void parametrerTableFicheDePaie() {
        initModelTableFicheDePai();
        chargerDataTableFicheDePaie();
        fixerColonnesTableFicheDePaie(true);
        //filtrerTableFichePaie();
    }

    private void initModelTableFicheDePai() {
        this.modeleListeFiches = new ModeleListeFiches(scrollListeFichesDePaie, btEnregistrer, mEnregistrer, this.parametreFichesDePaie, new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null) {
                    ecouteurClose.onActualiser(modeleListeFiches.getRowCount() + " élement(s).", icones.getDossier_01());
                }
            }
        });
        //gestionnaire de filtre
        //tableListeFichesDePaie.setAutoCreateRowSorter(true);
    }

    private void chargerDataTableFicheDePaie() {
        tableListeFichesDePaie.setModel(modeleListeFiches);
        if (donneesFiche != null) {
            if (!donneesFiche.getFiches().isEmpty()) {
                modeleListeFiches.setListeFiches(donneesFiche.getFiches());
            }
        }
    }

    private void fixerColonnesTableFicheDePaie(boolean resizeTable) {
        //Parametrage du rendu de la table
        this.tableListeFichesDePaie.setDefaultRenderer(Object.class, new RenduTableFiche(icones.getModifier_01(), parametreFichesDePaie, modeleListeFiches));
        this.tableListeFichesDePaie.setRowHeight(25);

        //{"N°", "Date", "Mois", "Agent", "Catégorie", "Monnaie", "Sal. de Base(+)", "Transport(+)", "Logement(+)", "Autres gains(+)", "TOTAL(+)", "Ipr(-)", "Inss(-)", "Syndicat(-)", "Cafétariat(-)", "Av. Salaire(-)", "Ordinateur(-)", "TOTAL(-)", "NET A PAYER"};
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(0), 30, true, null);//N°
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(1), 110, true, new EditeurDate());//Date
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(2), 100, false, new EditeurMois());//Mois
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(3), 200, false, new EditeurAgents(parametreFichesDePaie));//Agent
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(4), 150, false, null);//Catégorie
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(5), 80, false, new EditeurMonnaie(parametreFichesDePaie));//Monnaie
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(6), 100, true, null);//Salaire de base
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(7), 100, true, null);//Transport
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(8), 100, true, null);//Logement
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(9), 100, true, null);//Autres gains
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(10), 120, true, null);//TOTAL(+)
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(11), 100, true, null);//ipr
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(12), 100, true, null);//inss
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(13), 100, true, null);//Syndicat
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(14), 100, true, null);//Cafétariat
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(15), 100, true, null);//Avance sur salaire
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(16), 100, true, null);//Ordinateur
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(17), 120, true, null);//TOTAL(-)
        setTaille(this.tableListeFichesDePaie.getColumnModel().getColumn(18), 120, true, null);//NET A PAYER

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

    private void ecouterAgentSelectionne() {
        int ligneSelected = tableListeFichesDePaie.getSelectedRow();
        if (ligneSelected != -1) {
            InterfaceFiche Ifiche = modeleListeFiches.getFiche(ligneSelected);
            if (Ifiche != null) {
                InterfaceAgent IAgent = getAgent(Ifiche.getIdAgent());
                if (IAgent != null) {
                    btPDFSynth.setText("Prod. Bulletin", 12, true);
                    btPDFSynth.appliquerDroitAccessDynamique(true);
                    mPDFSynth.setText("Produire le bulletin de " + IAgent.getNom() + " " + IAgent.getPrenom());
                    mPDFSynth.appliquerDroitAccessDynamique(true);
                    renameTitrePaneAgent("Sélection - " + IAgent.getNom()+" " + IAgent.getPostnom()+" " + IAgent.getPrenom());
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
    
    private void renameTitrePaneAgent(String titre){
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

    private InterfaceAgent getAgent(int idAgent) {
        for (InterfaceAgent Icha : this.parametreFichesDePaie.getAgents()) {
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

    private void setBoutons() {
        btAjouter = new Bouton(12, "Ajouter", icones.getAjouter_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                ajouter();
            }
        });

        btSupprimer = new Bouton(12, "Supprimer", icones.getSupprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                supprimer();
            }
        });

        btEnregistrer = new Bouton(12, "Enregistrer", icones.getEnregistrer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                enregistrer();
            }
        });
        btEnregistrer.setGras(true);

        btVider = new Bouton(12, "Vider", icones.getAnnuler_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                vider();
            }
        });

        btImprimer = new Bouton(12, "Imprimer", icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        btFermer = new Bouton(12, "Fermer", icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        btPDF = new Bouton(12, "Exp. Tout", icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = TYPE_EXPORT_TOUT;
                exporterPDF();
            }
        });

        btPDFSynth = new Bouton(12, "Exp. bulletin", icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = TYPE_EXPORT_SELECTION;
                exporterPDF();
            }
        });

        btActualiser = new Bouton(12, "Actualiser", icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiser();
            }
        });

        bOutils = new BarreOutils(barreOutils);
        bOutils.AjouterBouton(btEnregistrer);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btAjouter);
        bOutils.AjouterBouton(btSupprimer);
        bOutils.AjouterBouton(btVider);
        bOutils.AjouterBouton(btActualiser);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btImprimer);
        bOutils.AjouterBouton(btPDF);
        bOutils.AjouterBouton(btPDFSynth);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btFermer);
    }

    private void ecouterMenContA(java.awt.event.MouseEvent evt, int tab) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            switch (tab) {
                case 0: //Tab Monnaie
                    menuContextuel.afficher(scrollListeFichesDePaie, evt.getX(), evt.getY());
                    break;
            }
        }
        switch (tab) {
            case 0://Tab Fiche de paie
                InterfaceFiche enc = modeleListeFiches.getFiche(tableListeFichesDePaie.getSelectedRow());
                if (enc != null) {
                    this.ecouteurClose.onActualiser("Fiche...:" + enc.getMois() + " " + enc.getIdAgent() + " " + Util.getMontantFrancais(Util.getNetAPayer(enc)) + " " + getMonnaie(enc.getIdMonnaie()).getCode() + ".", icones.getClient_01());
                }
                break;
        }
    }

    public void init() {
        this.moi = this;
        this.chRecherche.setIcon(icones.getChercher_01());
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

        ecouteurAjout = new EcouteurAjout() {
            @Override
            public void setAjoutFiche(ModeleListeFiches modeleListeFiches) {
                if (modeleListeFiches != null) {
                    int index = (modeleListeFiches.getRowCount() + 1);

                    Date dateEnreg = new Date();
                    int id = -1;
                    int idAgent = -1;
                    int idExercice = parametreFichesDePaie.getExercice().getId();
                    int idEntreprise = parametreFichesDePaie.getEntreprise().getId();
                    int idUtilisateur = parametreFichesDePaie.getIdUtilisateur();
                    int idMonnaie = parametreFichesDePaie.getMonnaies().firstElement().getId();
                    int beta = InterfaceFiche.BETA_NOUVEAU;
                    String mois = Util.getDateFrancais_Mois(dateEnreg);

                    modeleListeFiches.AjouterFichet(new XX_Fiche(id, idEntreprise, idUtilisateur, idExercice, idMonnaie, idAgent, InterfaceAgent.CATEGORIE_ADMINISTRATION_1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, dateEnreg, mois, beta));
                    //On sélectionne la première ligne
                    tableListeFichesDePaie.setRowSelectionInterval(0, 0);
                }
            }
        };

        setBoutons();
        setMenuContextuel();
    }

    public void activerBoutons(int selectedTab) {
        this.indexTabSelected = selectedTab;
        actualiserTotaux("activerBoutons");
        afficherCriterePlus();
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
                modeleListeFiches.SupprimerFiche(tableListeFichesDePaie.getSelectedRow());
                break;
        }
    }

    public void vider() {
        this.ecouteurClose.onActualiser("Vidé!", icones.getInfos_01());
        this.chRecherche.setText("");
        switch (indexTabSelected) {
            case 0: //Tab Encaissement
                modeleListeFiches.viderListe();
                break;
        }
    }

    private void setIconesTabs() {
        this.tabPrincipal.setIconAt(0, icones.getDossier_01());  //Liste des Fiches de paie
        this.labSalBrut.setIcon(icones.getNombre_01());
        this.labSalRetenu.setIcon(icones.getNombre_01());
        this.labSalNet.setIcon(icones.getNombre_01());
        this.labSalBrutSelected.setIcon(icones.getNombre_01());
        this.labSalRetenuSelected.setIcon(icones.getNombre_01());
        this.labSalNetSelected.setIcon(icones.getNombre_01());
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

        menuContextuel = new MenuContextuel();
        menuContextuel.Ajouter(mEnregistrer);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mAjouter);
        menuContextuel.Ajouter(mSupprimer);
        menuContextuel.Ajouter(mVider);
        menuContextuel.Ajouter(mActualiser);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mImprimer);
        menuContextuel.Ajouter(mPDF);
        menuContextuel.Ajouter(mPDFSynth);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mFermer);
    }

    private boolean mustBeSaved() {
        boolean rep = false;
        //On vérifie dans la liste d'encaissements
        for (InterfaceFiche Ienc : modeleListeFiches.getListeData()) {
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
                //DocumentPDF documentPDF = new DocumentPDF(this, DocumentPDF.ACTION_IMPRIMER, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ParametreFichesDePaie getParametreTresorerie() {
        return parametreFichesDePaie;
    }

    public String getNomfichierPreuve() {
        return "FicheDePaieS2B.pdf";
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
            btEnregistrer.setCouleur(Color.black);
            mEnregistrer.setCouleur(Color.BLACK);

            SortiesFichesDePaies sortie = getSortiesFichesDePaies(btEnregistrer, mEnregistrer);
            this.ecouteurPaie.onEnregistre(sortie);
        }
    }

    public void exporterPDF() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesFichesDePaies sortie = getSortiesFichesDePaies(btPDF, mPDF);
                //DocumentPDF docpdf = new DocumentPDF(this, DocumentPDF.ACTION_OUVRIR, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void actualiser() {
        switch (indexTabSelected) {
            case 0: //Encaissements
                modeleListeFiches.actualiser();
                ecouterAgentSelectionne();
                break;
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
        chRecherche = new UI.JS2bTextField();
        panelTotaux = new javax.swing.JPanel();
        combototMonnaie = new javax.swing.JComboBox<>();
        labTauxDeChange = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        labSalBrut = new javax.swing.JLabel();
        labSalRetenu = new javax.swing.JLabel();
        labSalNet = new javax.swing.JLabel();
        panSelected = new javax.swing.JPanel();
        labSalBrutSelected = new javax.swing.JLabel();
        labSalRetenuSelected = new javax.swing.JLabel();
        labSalNetSelected = new javax.swing.JLabel();
        panelCriteres_categorie = new javax.swing.JPanel();
        chCategorie = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));

        barreOutils.setBackground(new java.awt.Color(255, 255, 255));
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

        tabPrincipal.addTab("Encaissements", scrollListeFichesDePaie);

        labInfos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labInfos.setText("Prêt.");

        chRecherche.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        chRecherche.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        chRecherche.setTextInitial("Recherche");

        panelTotaux.setBackground(new java.awt.Color(255, 255, 255));
        panelTotaux.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));

        combototMonnaie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combototMonnaie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combototMonnaieItemStateChanged(evt);
            }
        });

        labTauxDeChange.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        labTauxDeChange.setForeground(new java.awt.Color(51, 51, 255));
        labTauxDeChange.setText("Taux");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Masse Salariale", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(51, 51, 255))); // NOI18N

        labSalBrut.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalBrut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labSalBrut.setText("Total : 0000000000 $ ");

        labSalRetenu.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalRetenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labSalRetenu.setText("Total : 0000000000 $ ");

        labSalNet.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalNet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labSalNet.setText("Total : 0000000000 $ ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSalBrut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labSalRetenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labSalNet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labSalBrut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labSalRetenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labSalNet)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panSelected.setBackground(new java.awt.Color(255, 255, 255));
        panSelected.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fiche de paie séléctionnée", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(51, 51, 255))); // NOI18N

        labSalBrutSelected.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalBrutSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labSalBrutSelected.setText("Total : 0000000000 $ ");

        labSalRetenuSelected.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalRetenuSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labSalRetenuSelected.setText("Total : 0000000000 $ ");

        labSalNetSelected.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labSalNetSelected.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labSalNetSelected.setText("Total : 0000000000 $ ");

        javax.swing.GroupLayout panSelectedLayout = new javax.swing.GroupLayout(panSelected);
        panSelected.setLayout(panSelectedLayout);
        panSelectedLayout.setHorizontalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSelectedLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labSalBrutSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labSalRetenuSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labSalNetSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panSelectedLayout.setVerticalGroup(
            panSelectedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSelectedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labSalBrutSelected)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labSalRetenuSelected)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labSalNetSelected)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelTotauxLayout = new javax.swing.GroupLayout(panelTotaux);
        panelTotaux.setLayout(panelTotauxLayout);
        panelTotauxLayout.setHorizontalGroup(
            panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotauxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTotauxLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panSelected, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelTotauxLayout.createSequentialGroup()
                        .addComponent(combototMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labTauxDeChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelTotauxLayout.setVerticalGroup(
            panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotauxLayout.createSequentialGroup()
                .addGroup(panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combototMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTauxDeChange))
                .addGap(5, 5, 5)
                .addGroup(panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        panelCriteres_categorie.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Catégorie d'Agents", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13), new java.awt.Color(102, 102, 102))); // NOI18N

        chCategorie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TOUTES LES CATEGORIES D'AGENTS", "CATEGORIE - ADMINISTRATION_1", "CATEGORIE - ADMINISTRATION_2", "CATEGORIE - MATERNELLE", "CATEGORIE - PARTIEL", "CATEGORIE - PRIMAIRE", "CATEGORIE - PRIME", "CATEGORIE - SECONDAIRE ", "CATEGORIE - SURVEILLANT" }));
        chCategorie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chCategorieItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelCriteres_categorieLayout = new javax.swing.GroupLayout(panelCriteres_categorie);
        panelCriteres_categorie.setLayout(panelCriteres_categorieLayout);
        panelCriteres_categorieLayout.setHorizontalGroup(
            panelCriteres_categorieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelCriteres_categorieLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chCategorie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelCriteres_categorieLayout.setVerticalGroup(
            panelCriteres_categorieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCriteres_categorieLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(chCategorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barreOutils, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 793, Short.MAX_VALUE)
            .addComponent(panelTotaux, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labInfos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chRecherche, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(panelCriteres_categorie, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(panelCriteres_categorie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTotaux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labInfos)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableListeFichesDePaieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 0);
    }//GEN-LAST:event_tableListeFichesDePaieMouseClicked

    private void scrollListeFichesDePaieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeFichesDePaieMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 0);
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
            actualiserTotaux();
            actualiserTotaux("combototMonnaieItemStateChanged");
        }
    }//GEN-LAST:event_combototMonnaieItemStateChanged

    private void tableListeFichesDePaieMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeFichesDePaieMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeFichesDePaieMouseDragged

    private void chCategorieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chCategorieItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (gestionnaireRecherche != null) {
                gestionnaireRecherche.demarrerRecherche();
            }
        }
    }//GEN-LAST:event_chCategorieItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutils;
    private javax.swing.JComboBox<String> chCategorie;
    private UI.JS2bTextField chRecherche;
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
    private javax.swing.JPanel panSelected;
    private javax.swing.JPanel panelCriteres_categorie;
    private javax.swing.JPanel panelTotaux;
    private javax.swing.JScrollPane scrollListeFichesDePaie;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeFichesDePaie;
    // End of variables declaration//GEN-END:variables
}
