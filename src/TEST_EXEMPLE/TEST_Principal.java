/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.CallBack.EcouteurPaie;
import SOURCES.Interface.InterfaceAgent;
import SOURCES.Interface.InterfaceFiche;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.UI.Panel;
import SOURCES.Utilitaires.DonneesFicheDePaie;
import SOURCES.Utilitaires.ParametreFichesDePaie;
import SOURCES.Utilitaires.SortiesFichesDePaies;
import SOURCES.Utilitaires.Util;
import static java.lang.Thread.sleep;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class TEST_Principal extends javax.swing.JFrame {

    /**
     * Creates new form TEST_Principal
     */
    public Panel gestionnairePaie = null;
    public ParametreFichesDePaie parametreFichesDePaie = null;
    public DonneesFicheDePaie donneesFicheDePaie = null;
    public int idUtilisateur = 1;
    public String nomUtilisateur = "Serge SULA BOSIO";
    public int idMonnaie = 1;
    public String monnaie = "$";

    public TEST_Entreprise entreprise = new TEST_Entreprise(1, "ECOLE CARESIENNE DE KINSHASA", "7e Rue Limeté Industrielle, Kinshasa/RDC", "+243844803514", "infos@cartesien.org", "wwww.cartesien.org", "Equity Bank Congo SA", "Cartesien de Kinshasa", "00122114557892554", "IBN0012455", "CDKIN0012", "logo.png", "RCCM/KD/CD/4513", "IDN00111454", "IMP00124100");
    public TEST_Exercice exercice = new TEST_Exercice(12, entreprise.getId(), idUtilisateur, nomUtilisateur, new Date(), Util.getDate_AjouterAnnee(new Date(), 1), InterfaceAgent.BETA_EXISTANT);
    public TEST_Monnaie defaultMonnaie = new TEST_Monnaie(idMonnaie, entreprise.getId(), idUtilisateur, exercice.getId(), "Dollars Américains", monnaie, InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1620, new Date().getTime(), InterfaceMonnaie.BETA_EXISTANT);
    public TEST_Agent defaultAgent = new TEST_Agent(10, entreprise.getId(), idUtilisateur, exercice.getId(), "SULA", "BOSIO", "Serge", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_LICENCIE, new Date().getTime() + 2, InterfaceAgent.CATEGORIE_SECONDAIRE, InterfaceAgent.BETA_EXISTANT);

    public TEST_Principal() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Ouvrir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(622, Short.MAX_VALUE))
            .addComponent(tabPrincipal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ouvrir();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TEST_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TEST_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TEST_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TEST_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TEST_Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JTabbedPane tabPrincipal;
    // End of variables declaration//GEN-END:variables

    private void ouvrir() {
        gestionnairePaie = getGestionnairePaie();
        //Chargement du gestionnaire sur l'onglet
        tabPrincipal.addTab("Gestionnaire de paie", gestionnairePaie);

        //On séléctionne l'onglet actuel
        tabPrincipal.setSelectedComponent(gestionnairePaie);
    }

    private Panel getGestionnairePaie() {
        return new Panel(tabPrincipal, getDonnees(), getParametres(), new EcouteurPaie() {
            @Override
            public void onEnregistre(SortiesFichesDePaies sortiesFichesDePaies) {
                //Ce que le système devra faire lorsque l'on clique sur le bouton ENREGISTRER

                Thread th = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sortiesFichesDePaies.getEcouteurEnregistrement().onUploading("Chargement...");
                            sleep(10);

                            sortiesFichesDePaies.getListeFichesDePaie().forEach((Ofiche) -> {
                                if (Ofiche.getBeta() == InterfaceFiche.BETA_MODIFIE || Ofiche.getBeta() == InterfaceFiche.BETA_NOUVEAU) {
                                    System.out.println(" * " + Ofiche.toString());

                                    //Après enregistrement
                                    Ofiche.setBeta(InterfaceFiche.BETA_EXISTANT);
                                }
                            });

                            sortiesFichesDePaies.getEcouteurEnregistrement().onDone("Enregistré!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                th.start();

            }
        });
    }

    private DonneesFicheDePaie getDonnees() {
        Vector<InterfaceFiche> listeFiches = new Vector<>();
        listeFiches.addElement(new TEST_Fiche(12, entreprise.getId(), idUtilisateur, exercice.getId(), idMonnaie, defaultAgent.getId(), InterfaceAgent.CATEGORIE_ADMINISTRATION_1, 2500, 120, 250, 40, 90, 35, 25, 0, 5, 100, 0, new Date(), Util.getDateFrancais_Mois(new Date()), InterfaceAgent.BETA_EXISTANT));

        this.donneesFicheDePaie = new DonneesFicheDePaie(listeFiches);
        return this.donneesFicheDePaie;
    }

    private ParametreFichesDePaie getParametres() {
        //Les types des monnaies
        Vector<InterfaceMonnaie> monnaies = new Vector<>();
        monnaies.addElement(defaultMonnaie);
        monnaies.addElement(new TEST_Monnaie(2, entreprise.getId(), idUtilisateur, exercice.getId(), "Francs congolais", "Fc", InterfaceMonnaie.NATURE_MONNAIE_LOCALE, 1, new Date().getTime() + 1, InterfaceMonnaie.BETA_EXISTANT));
        monnaies.addElement(new TEST_Monnaie(3, entreprise.getId(), idUtilisateur, exercice.getId(), "Euro", "Euro", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1800, new Date().getTime() + 1, InterfaceMonnaie.BETA_EXISTANT));

        //Les types des ragents
        Vector<InterfaceAgent> agents = new Vector<>();
        agents.add(defaultAgent);
        agents.addElement(new TEST_Agent(12, entreprise.getId(), idUtilisateur, exercice.getId(), "SULA", "ESUA", "Yannick", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_GRADUE, new Date().getTime() + 15, InterfaceAgent.CATEGORIE_SECONDAIRE, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(13, entreprise.getId(), idUtilisateur, exercice.getId(), "SULA", "OKONDJI", "Hermine", InterfaceAgent.SEXE_FEMININ, InterfaceAgent.NIVEAU_ETUDE_LICENCIE, new Date().getTime() + 16, InterfaceAgent.CATEGORIE_ADMINISTRATION_1, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(14, entreprise.getId(), idUtilisateur, exercice.getId(), "MUTA", "KANKUNGWALA", "Christian", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_MASTER, new Date().getTime() + 17, InterfaceAgent.CATEGORIE_ADMINISTRATION_2, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(15, entreprise.getId(), idUtilisateur, exercice.getId(), "MAKULA", "BOFANDO", "Alain", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_MASTER, new Date().getTime() + 18, InterfaceAgent.CATEGORIE_PRIMAIRE, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(16, entreprise.getId(), idUtilisateur, exercice.getId(), "OPOTHA", "LOFUNGOLA", "Emmanuel", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_LICENCIE, new Date().getTime() + 19, InterfaceAgent.CATEGORIE_SECONDAIRE, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(17, entreprise.getId(), idUtilisateur, exercice.getId(), "KASHONGWE", "KASHONGWE", "Deo", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_LICENCIE, new Date().getTime() + 21, InterfaceAgent.CATEGORIE_PRIME, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(18, entreprise.getId(), idUtilisateur, exercice.getId(), "MVUMU", "MPUKUTA", "Julien", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_MASTER, new Date().getTime() + 22, InterfaceAgent.CATEGORIE_SURVEILLANT, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(19, entreprise.getId(), idUtilisateur, exercice.getId(), "BONDEKWE", "AMA", "Rufin", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_LICENCIE, new Date().getTime() + 23, InterfaceAgent.CATEGORIE_ADMINISTRATION_1, InterfaceAgent.BETA_EXISTANT));
        agents.addElement(new TEST_Agent(20, entreprise.getId(), idUtilisateur, exercice.getId(), "IKEKA", "LOPOKO", "Mdeste", InterfaceAgent.SEXE_MASCULIN, InterfaceAgent.NIVEAU_ETUDE_LICENCIE, new Date().getTime() + 24, InterfaceAgent.CATEGORIE_PARTIEL, InterfaceAgent.BETA_EXISTANT));
        
        this.parametreFichesDePaie = new ParametreFichesDePaie(entreprise, exercice, agents, monnaies, nomUtilisateur, idUtilisateur);
        return this.parametreFichesDePaie;
    }
}
