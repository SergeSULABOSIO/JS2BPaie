/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.GenerateurPDF_Paie;


import SOURCES.UI_Paie.PanelPaie;
import SOURCES.Utilitaires_Paie.SortiesFichesDePaies;
import SOURCES.Utilitaires_Paie.UtilPaie;
import Source.Interface.InterfaceAgent;
import Source.Interface.InterfaceFiche;
import Source.Objet.Agent;
import Source.Objet.Entreprise;
import Source.Objet.Fiche_paie;
import Source.Objet.Monnaie;
import Source.Objet.UtilObjet;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Gateway
 */
public class DocumentPDFPaie extends PdfPageEventHelper {

    private Document document = new Document(PageSize.A4);
    private Font Font_Titre1 = null;
    private Font Font_Titre2 = null;
    private Font Font_Titre3 = null;
    private Font Font_TexteSimple = null;
    private Font Font_TexteSimple_petit, Font_TexteSimple_petit_Gras = null;
    private Font Font_TexteSimple_Gras = null;
    private Font Font_TexteSimple_Italique = null;
    private Font Font_TexteSimple_Gras_Italique = null;
    public static final int ACTION_IMPRIMER = 0;
    public static final int ACTION_OUVRIR = 1;
    private SortiesFichesDePaies sortiesFichesDePaies = null;
    private PanelPaie gestionnairePaie;
    private String monnaie = "";

    public DocumentPDFPaie(PanelPaie panel, int action, SortiesFichesDePaies sortiesFichesDePaies) {
        try {
            init(panel, action, sortiesFichesDePaies);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(PanelPaie panel, int action, SortiesFichesDePaies sortiesFichesDePaies) {
        this.gestionnairePaie = panel;
        this.sortiesFichesDePaies = sortiesFichesDePaies;

        if (gestionnairePaie.getTypeExport() == PanelPaie.TYPE_EXPORT_SELECTION) {
            document = new Document(PageSize.A4);
        } else {
            document = new Document(PageSize.A4.rotate());
        }

        parametre_initialisation_fichier();
        parametre_construire_fichier();
        if (action == ACTION_OUVRIR) {
            parametres_ouvrir_fichier();
        } else if (action == ACTION_IMPRIMER) {
            parametres_imprimer_fichier();
        }
    }

    private void parametre_initialisation_fichier() {
        //Les titres du document
        this.Font_Titre1 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.DARK_GRAY);
        this.Font_Titre2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
        this.Font_Titre3 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

        //Les textes simples
        this.Font_TexteSimple = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
        this.Font_TexteSimple_Gras_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, BaseColor.BLACK);
    }

    private void parametre_construire_fichier() {
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(UtilObjet.SYSTEM_FICHIER_OUTPUT_PDF));
            writer.setPageEvent(new MarqueS2B());
            this.document.open();
            setDonneesBibliographiques();
            setContenuDeLaPage();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gestionnairePaie, "Impossible d'effectuer cette opération.\nAssurez vous qu'aucun fichier du même nom ne soit actuellement ouvert.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parametres_ouvrir_fichier() {
        File fic = new File(UtilObjet.SYSTEM_FICHIER_OUTPUT_PDF);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().open(fic);
                if (sortiesFichesDePaies != null) {
                    sortiesFichesDePaies.getEcouteurEnregistrement().onDone("PDF ouvert avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'ouvrir le fichier !";
                JOptionPane.showMessageDialog(gestionnairePaie, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesFichesDePaies != null) {
                    sortiesFichesDePaies.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void parametres_imprimer_fichier() {
        File fic = new File(UtilObjet.SYSTEM_FICHIER_OUTPUT_PDF);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().print(fic);
                if (sortiesFichesDePaies != null) {
                    sortiesFichesDePaies.getEcouteurEnregistrement().onDone("Impression effectuée avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'imprimer les données ";
                JOptionPane.showMessageDialog(gestionnairePaie, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesFichesDePaies != null) {
                    sortiesFichesDePaies.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void setDonneesBibliographiques() {
        this.document.addTitle("Document généré par JS2BTresorerie");
        this.document.addSubject("Etat");
        this.document.addKeywords("Java, PDF, Tresorerie");
        this.document.addAuthor("S2B. Simple.Intuitif");
        this.document.addCreator("SULA BOSIO Serge, S2B, sulabosiog@gmail.com");
    }

    private void ajouterLigne(int number) throws Exception {
        Paragraph paragraphe = new Paragraph();
        for (int i = 0; i < number; i++) {
            paragraphe.add(new Paragraph(" "));
        }
        this.document.add(paragraphe);
    }

    private void setTitreEtDateDocument() throws Exception {
        Paragraph preface = new Paragraph();
        String titre = gestionnairePaie.getTitreDoc() + "";

        if (gestionnairePaie != null) {
            preface.add(getParagraphe("Date: " + UtilPaie.getDateFrancais(gestionnairePaie.getDateDocument()), Font_Titre3, Element.ALIGN_RIGHT));
            if (gestionnairePaie.getTypeExport() == PanelPaie.TYPE_EXPORT_TOUT) {
                preface.add(getParagraphe(titre, Font_Titre1, Element.ALIGN_CENTER));
            }else{
                preface.add(getParagraphe(titre + "\n" + getTitreMois(gestionnairePaie.getSelectedFichePaie().getMois()), Font_Titre1, Element.ALIGN_CENTER));
            }
        } else {
            preface.add(getParagraphe("Date: " + UtilPaie.getDateFrancais(new Date()), Font_Titre3, Element.ALIGN_RIGHT));
            preface.add(getParagraphe("Facture n°XXXXXXXXX/2018", Font_Titre1, Element.ALIGN_CENTER));
        }
        this.document.add(preface);
    }

    private void setSignataire() throws Exception {
        if (gestionnairePaie != null) {
            this.document.add(getParagraphe(""
                    + "Produit par " + gestionnairePaie.getNomUtilisateur() + "\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        } else {
            this.document.add(getParagraphe(""
                    + "Produit par Serge SULA BOSIO\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        }

    }

    private void setBasDePage() throws Exception {
        if (gestionnairePaie != null) {
            Entreprise entreprise = gestionnairePaie.getEntreprise();
            if (entreprise != null) {
                this.document.add(getParagraphe(entreprise.getNom() + "\n" + entreprise.getAdresse() + " | " + entreprise.getTelephone() + " | " + entreprise.getEmail() + " | " + entreprise.getSiteWeb(), Font_TexteSimple, Element.ALIGN_CENTER));
            } else {
                addDefaultEntreprise();
            }
        } else {
            addDefaultEntreprise();
        }
    }

    private void addDefaultEntreprise() throws Exception {
        this.document.add(getParagraphe(""
                + "UAP RDC Sarl. Courtier d’Assurances n°0189\n"
                + "Prins van Luikschool, Av de la Gombe, Gombe, Kinshasa, DRC | (+243) 975 33 88 33 | info@aib-brokers.com", Font_TexteSimple, Element.ALIGN_CENTER));

    }

    private Paragraph getParagraphe(String texte, Font font, int alignement) {
        Paragraph par = new Paragraph(texte, font);
        par.setAlignment(alignement);
        return par;
    }

    private Phrase getPhrase(String texte, Font font) {
        Phrase phrase = new Phrase(texte, font);
        return phrase;
    }

    private void setLogoEtDetailsEntreprise() {
        try {
            PdfPTable tableauEnteteFacture = new PdfPTable(2);
            int[] dimensionsWidthHeight = {320, 1460};
            tableauEnteteFacture.setWidths(dimensionsWidthHeight);
            tableauEnteteFacture.setHorizontalAlignment(Element.ALIGN_LEFT);

            //CELLULE DU LOGO DE L'ENTREPRISE
            PdfPCell celluleLogoEntreprise = null;
            String logo = "";
            if (gestionnairePaie != null) {
                logo = (new File(this.gestionnairePaie.getEntreprise().getLogo())).getName();
                System.out.println("Fic logo: " + logo);
            }
            File ficLogo = new File(UtilObjet.SYSTEM_USER_HOME+"/"+logo);
            System.out.println("Fichier Logo: " + ficLogo.getAbsolutePath());
            if (ficLogo.exists() == true) {
                //System.out.println("Fichier Logo: " + ficLogo.getAbsolutePath() + " - Trouvé!");
                //Chargement du logo et redimensionnement afin que celui-ci convienne dans l'espace qui lui est accordé
                Image Imglogo = Image.getInstance(ficLogo.getAbsolutePath());
                Imglogo.scaleAbsoluteWidth(70);
                Imglogo.scaleAbsoluteHeight(70);
                celluleLogoEntreprise = new PdfPCell(Imglogo);
            } else {
                celluleLogoEntreprise = new PdfPCell();
            }
            celluleLogoEntreprise.setPadding(2);
            celluleLogoEntreprise.setBorderWidth(0);
            celluleLogoEntreprise.setBorderColor(BaseColor.BLACK);
            tableauEnteteFacture.addCell(celluleLogoEntreprise);

            //CELLULE DES DETAILS SUR L'ENTREPRISE - TEXTE (Nom, Adresse, Téléphone, Email, etc)
            PdfPCell celluleDetailsEntreprise = new PdfPCell();
            celluleDetailsEntreprise.setPadding(2);
            celluleDetailsEntreprise.setPaddingLeft(5);
            celluleDetailsEntreprise.setBorderWidth(0);
            celluleDetailsEntreprise.setBorderWidthLeft(1);
            celluleDetailsEntreprise.setBorderColor(BaseColor.BLACK);
            celluleDetailsEntreprise.setHorizontalAlignment(Element.ALIGN_TOP);

            if (gestionnairePaie != null) {
                Entreprise entreprise = gestionnairePaie.getEntreprise();
                if (entreprise != null) {
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getNom(), Font_Titre2, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getAdresse(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getSiteWeb() + " | " + entreprise.getEmail() + " | " + entreprise.getTelephone(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe("RCC : " + entreprise.getRccm()+ "\nID. NAT : " + entreprise.getIdnat()+ "\nNIF : " + entreprise.getNumeroImpot(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                }
            } else {
                celluleDetailsEntreprise.addElement(getParagraphe("UAP RDC Sarl, Courtier d'Assurances n°0189", Font_Titre2, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("Avenue de la Gombe, Kinshasa/Gombe", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("https://www.aib-brokers.com | info@aib-brokers.com | (+243)84 480 35 14 - (+243)82 87 27 706", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("RCC : CDF/KIN/2015-1245\nID. NAT : 0112487789\nNIF : 012245", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            }
            tableauEnteteFacture.addCell(celluleDetailsEntreprise);

            //On insère le le tableau entete (logo et détails de l'entreprise) dans la page
            document.add(tableauEnteteFacture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell getCelluleTableau(String texte, float BorderWidth, BaseColor background, BaseColor textColor, int alignement, Font font) {
        PdfPCell cellule = new PdfPCell();
        cellule.setBorderWidth(BorderWidth);
        if (background != null) {
            cellule.setBackgroundColor(background);
        } else {
            cellule.setBackgroundColor(BaseColor.WHITE);
        }
        if (textColor != null) {
            font.setColor(textColor);
        } else {
            font.setColor(BaseColor.BLACK);
        }
        cellule.setHorizontalAlignment(alignement);
        cellule.setPhrase(getPhrase(texte, font));
        return cellule;
    }

    private PdfPTable getTableau(float totalWidth, String[] titres, int[] widths, int alignement, float borderWidth) {
        try {
            PdfPTable tableau = new PdfPTable(widths.length);
            if (totalWidth == -1) {
                if (gestionnairePaie.getTypeExport() == PanelPaie.TYPE_EXPORT_SELECTION) {
                    tableau.setTotalWidth(PageSize.A4.getWidth() - 72);
                } else {
                    tableau.setTotalWidth(PageSize.A4.rotate().getWidth() - 72);
                }
            } else {
                tableau.setTotalWidth(totalWidth);
            }
            tableau.setLockedWidth(true);
            tableau.setWidths(widths);
            tableau.setHorizontalAlignment(alignement);
            if (titres != null) {
                tableau.setSpacingBefore(3);
                for (String titre : titres) {
                    if (gestionnairePaie.getTypeExport() == PanelPaie.TYPE_EXPORT_SELECTION) {
                        tableau.addCell(getCelluleTableau(titre, borderWidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_CENTER, Font_TexteSimple_Gras));
                    } else {
                        tableau.addCell(getCelluleTableau(titre, borderWidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_CENTER, Font_TexteSimple_petit_Gras));
                    }
                }
            }

            return tableau;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAgent(int idAgent) {
        for (InterfaceAgent Iagent : gestionnairePaie.getParametreFichesDePaie().getAgents()) {
            if (idAgent == Iagent.getId()) {
                String initialPren = Iagent.getPrenom().trim();
                if (initialPren.length() != 0) {
                    initialPren = initialPren.substring(0, 1) + ".";
                }
                return Iagent.getNom() + " " + Iagent.getPostnom() + " " + initialPren;
            }
        }
        return "";
    }

    public int getCategorieAgentS(String categorie) {
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

    private String getMonnaie(int idMonnaie) {
        for (Monnaie Ich : gestionnairePaie.getParametreFichesDePaie().getMonnaies()) {
            if (idMonnaie == Ich.getId()) {
                return Ich.getCode();
            }
        }
        return "";
    }
    
    private String getMonnaieNom(int idMonnaie) {
        for (Monnaie Ich : gestionnairePaie.getParametreFichesDePaie().getMonnaies()) {
            if (idMonnaie == Ich.getId()) {
                return Ich.getNom();
            }
        }
        return "";
    }

    private String getCategorieAgentI(int categorie) {
        switch (categorie) {
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
        return "CATEGORIES D'AGENTS(*)";
    }

    private boolean contientCategorie(int categorie) {
        for (InterfaceFiche Iagent : sortiesFichesDePaies.getListeFichesDePaie()) {
            if (categorie == Iagent.getCategorieAgent()) {
                return true;
            }
        }
        return false;
    }

    private boolean contientMois(String mois) {
        for (InterfaceFiche Iagent : sortiesFichesDePaies.getListeFichesDePaie()) {
            if (Iagent.getMois().equals(mois)) {
                return true;
            }
        }
        return false;
    }

    private String getTitreMois(String mois) {
        if (mois.toLowerCase().trim().startsWith("a") || mois.toLowerCase().trim().startsWith("o")) {
            return "Paie du mois d'" + mois;
        } else {
            return "Paie du mois de " + mois;
        }
    }

    private void setTableauGrilleDePaie() {
        try {
            if (sortiesFichesDePaies != null) {
                String criPeriode = gestionnairePaie.getNavigateurPagesFichePaie().toStringCriteres();
                for (String mois : UtilPaie.getListeMois(gestionnairePaie.dataPaie.getParametreFichesDePaie().getExercice().getDebut(), gestionnairePaie.dataPaie.getParametreFichesDePaie().getExercice().getFin())) {
                    if (contientMois(mois) == true) {
                        document.add(getParagraphe(getTitreMois(mois) + " (" + criPeriode + ")", Font_TexteSimple_Gras_Italique, Element.ALIGN_LEFT));
                        for (int categorie = 0; categorie < 8; categorie++) {
                            if (contientCategorie(categorie)) {
                                chargerElements(categorie, mois);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerElements(int categorie, String mois) throws Exception {
        //private String[] titreColonnes = {};
        PdfPTable tableEtatDePaie = getTableau(
                -1,
                new String[]{"N°", "Date", "Mois", "Agents", "Base", "Transp.", "Logement", "Autr. gains", "TOTAL", "Ipr", "Inss", "Syndicat", "Absence", "Cafetariat", "Av. Salaire", "Ordinat.", "TOTAL", "NET"},
                new int[]{30, 110, 100, 230, 100, 100, 100, 100, 110, 100, 100, 100, 100, 100, 100, 100, 110, 110},
                Element.ALIGN_CENTER,
                0.2f
        );
        int iLigne = 0;
        double base = 0, transp = 0, loge = 0, gains = 0;
        double ipr = 0, inss = 0, syndicat = 0, cafetariat = 0, avanceSal = 0, ordinateur = 0, absence = 0;
        double totB = 0, totR = 0, totN = 0;
        boolean isEmpty = true;

        for (Fiche_paie Ifiche : sortiesFichesDePaies.getListeFichesDePaie()) {
            if (Ifiche.getCategorieAgent() == categorie && mois.equals(Ifiche.getMois())) {
                totB += UtilPaie.getTotalAPayer(Ifiche);
                totR += UtilPaie.getTotalRetenu(Ifiche);
                totN += UtilPaie.getNetAPayer(Ifiche);
                String monnaieLine = getMonnaie(Ifiche.getIdMonnaie());
                base += Ifiche.getSalaireBase();
                transp += Ifiche.getTransport();
                loge += Ifiche.getLogement();
                gains += Ifiche.getAutresGains();
                ipr += Ifiche.getRetenu_IPR();
                inss += Ifiche.getRetenu_INSS();
                syndicat += Ifiche.getRetenu_SYNDICAT();
                cafetariat += Ifiche.getRetenu_CAFETARIAT();
                absence += Ifiche.getRetenu_ABSENCE();
                avanceSal += Ifiche.getRetenu_AVANCE_SALAIRE();
                ordinateur += Ifiche.getRetenu_ORDINATEUR();

                tableEtatDePaie.addCell(getCelluleTableau("" + (iLigne + 1), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(UtilPaie.getDateFrancais(Ifiche.getDateEnregistrement()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(Ifiche.getMois(), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(getAgent(Ifiche.getIdAgent()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(Ifiche.getSalaireBase()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(Ifiche.getTransport()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(Ifiche.getLogement()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(Ifiche.getAutresGains()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(UtilPaie.getTotalAPayer(Ifiche)) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(Ifiche.getRetenu_IPR()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(Ifiche.getRetenu_INSS()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(Ifiche.getRetenu_SYNDICAT()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(Ifiche.getRetenu_ABSENCE()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(Ifiche.getRetenu_CAFETARIAT()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(Ifiche.getRetenu_AVANCE_SALAIRE()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(Ifiche.getRetenu_ORDINATEUR()) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit));
                tableEtatDePaie.addCell(getCelluleTableau("-" + UtilPaie.getMontantFrancais(UtilPaie.getTotalRetenu(Ifiche)) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
                tableEtatDePaie.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(UtilPaie.getNetAPayer(Ifiche)) + " " + monnaieLine, 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
                //incrémentaion
                iLigne++;
                isEmpty = false;
            }
        }
        if (isEmpty == false) {
            //La dernière ligne du table
            double toBrut = 0;
            double toRet = 0;

            String[] bruts = new String[6];
            bruts[0] = UtilPaie.getMontantFrancais(base) + " " + gestionnairePaie.getMonnaieOutput();
            bruts[1] = UtilPaie.getMontantFrancais(transp) + " " + gestionnairePaie.getMonnaieOutput();
            bruts[2] = UtilPaie.getMontantFrancais(loge) + " " + gestionnairePaie.getMonnaieOutput();
            bruts[3] = UtilPaie.getMontantFrancais(gains) + " " + gestionnairePaie.getMonnaieOutput();

            toBrut = base + transp + loge + gains;

            String[] retenus = new String[7];
            retenus[0] = ipr + " " + gestionnairePaie.getMonnaieOutput();
            retenus[1] = inss + " " + gestionnairePaie.getMonnaieOutput();
            retenus[2] = syndicat + " " + gestionnairePaie.getMonnaieOutput();
            retenus[3] = absence + " " + gestionnairePaie.getMonnaieOutput();
            retenus[4] = cafetariat + " " + gestionnairePaie.getMonnaieOutput();
            retenus[5] = avanceSal + " " + gestionnairePaie.getMonnaieOutput();
            retenus[6] = ordinateur + " " + gestionnairePaie.getMonnaieOutput();

            toRet = ipr + inss + syndicat + absence + cafetariat + avanceSal + ordinateur;

            String totBrut = UtilPaie.getMontantFrancais(toBrut) + " " + gestionnairePaie.getMonnaieOutput();
            String totRetenus = UtilPaie.getMontantFrancais(toRet) + " " + gestionnairePaie.getMonnaieOutput();
            String totNetAPayer = UtilPaie.getMontantFrancais(toBrut - toRet) + " " + gestionnairePaie.getMonnaieOutput();
            setDerniereLigne(tableEtatDePaie, bruts, totBrut, retenus, totRetenus, totNetAPayer);

            document.add(getParagraphe("PERSONNEL " + getCategorieAgentI(categorie), Font_TexteSimple_petit, Element.ALIGN_LEFT));
            document.add(tableEtatDePaie);
        }
    }

    private void setDerniereLigne(PdfPTable table, String[] bruts, String totBrut, String[] retenus, String totRetenus, String totNetAPayer) {
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("Total", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau(bruts[0], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau(bruts[1], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau(bruts[2], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau(bruts[3], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau(totBrut, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + retenus[0], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + retenus[1], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + retenus[2], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + retenus[3], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + retenus[4], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + retenus[5], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + retenus[6], 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau("-" + totRetenus, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));
        table.addCell(getCelluleTableau(totNetAPayer, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_petit_Gras));

    }

    private void setLigneSeparateur() {
        try {
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            document.add(linebreak);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauSynthese() {
        try {
            Paragraph paragraphe = new Paragraph();
            PdfPTable tableSynthese = getTableau(
                    150f,
                    new String[]{"Synthèse", ""},
                    new int[]{100, 120},
                    Element.ALIGN_RIGHT,
                    0
            );
            if (gestionnairePaie != null) {
                paragraphe.add(getParagraphe(gestionnairePaie.getTauxChange(), Font_TexteSimple_petit, Element.ALIGN_RIGHT));
                setLignesTabSynthese(tableSynthese, 0, gestionnairePaie.getMonnaieOutput(), gestionnairePaie.getTotBrut(), gestionnairePaie.getTotRetenu(), gestionnairePaie.getTotNet());
            } else {
                setLignesTabSynthese(tableSynthese, 0, "$", 1200, 200, 1000);
            }
            document.add(tableSynthese);
            document.add(paragraphe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauBulletinPaieAgentSelectionne() {
        try {
            PdfPTable tableBulletinDePaie = getTableau(
                    200f,
                    new String[]{"DESCRIPTIONS", "VALEURS"},
                    new int[]{100, 100},
                    Element.ALIGN_CENTER,
                    0.2f
            );
            if (gestionnairePaie != null) {
                Agent AgentSeleted = gestionnairePaie.getSelectedAgent();
                Fiche_paie FicheDePaieSelected = gestionnairePaie.getSelectedFichePaie();
                
                if (FicheDePaieSelected != null && AgentSeleted != null) {
                    String monnaieFiche = getMonnaieNom(FicheDePaieSelected.getIdMonnaie());
                    setDataPaieAgent(tableBulletinDePaie, 0.2f, FicheDePaieSelected);
                    String detailsAgent = (AgentSeleted.getSexe() == InterfaceAgent.SEXE_MASCULIN ? "Mr. " : "Mme. ") + AgentSeleted.getNom() + " " + AgentSeleted.getPostnom() + " " + AgentSeleted.getPrenom() + "";
                    
                    ajouterLigne(1);
                    document.add(getParagraphe(detailsAgent, Font_TexteSimple_Gras, Element.ALIGN_CENTER));
                    document.add(tableBulletinDePaie);
                    document.add(getParagraphe("En lettre: " + UtilPaie.getMontantLettres(UtilPaie.getNetAPayer(FicheDePaieSelected), monnaieFiche) + "\n" + gestionnairePaie.getTauxChange(), Font_TexteSimple_petit, Element.ALIGN_CENTER));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataPaieAgent(PdfPTable tableau, float borderwidth, Fiche_paie fichePaie) {
        String monnaie = getMonnaie(fichePaie.getIdMonnaie());
        //Date
        tableau.addCell(getCelluleTableau("Date de production", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getDateFrancais(fichePaie.getDateEnregistrement()), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));

        //Mois de paie
        tableau.addCell(getCelluleTableau("Mois payé", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(fichePaie.getMois(), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));

        //Catégorie du personnel
        tableau.addCell(getCelluleTableau("Catégorie", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(getCategorieAgentI(fichePaie.getCategorieAgent()), borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));

        //Salaire de Base
        tableau.addCell(getCelluleTableau(" + Salaire de base", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getSalaireBase()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //Transport
        tableau.addCell(getCelluleTableau(" + Transport", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getTransport()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //Logement
        tableau.addCell(getCelluleTableau(" + Logemet", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getLogement()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //Autres gains
        tableau.addCell(getCelluleTableau(" + Autres gains", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getAutresGains()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //TOTAL BRUT
        tableau.addCell(getCelluleTableau("TOTAL BRUTS (+)", borderwidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(UtilPaie.getTotalAPayer(fichePaie)) + " " + monnaie + " ", borderwidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));

        //IPR
        tableau.addCell(getCelluleTableau(" - Impôt (ipr)", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getRetenu_IPR()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //INSS
        tableau.addCell(getCelluleTableau(" - Séc. Sociale (inss)", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getRetenu_INSS()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //SYNDICAT
        tableau.addCell(getCelluleTableau(" - Syndicat", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getRetenu_SYNDICAT()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //Absence
        tableau.addCell(getCelluleTableau(" - Absence", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getRetenu_ABSENCE()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //Cafétariat
        tableau.addCell(getCelluleTableau(" - Cafétariat", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getRetenu_CAFETARIAT()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //Avance sur salaire
        tableau.addCell(getCelluleTableau(" - Avance sur salaire", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getRetenu_AVANCE_SALAIRE()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //Ordinateur
        tableau.addCell(getCelluleTableau(" - Ordinateur", borderwidth, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(fichePaie.getRetenu_ORDINATEUR()) + " " + monnaie + " ", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));

        //TOTAL RETENUS
        tableau.addCell(getCelluleTableau("TOTAL RETENUS (-)", borderwidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(UtilPaie.getTotalRetenu(fichePaie)) + " " + monnaie + " ", borderwidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));

        //NET A PAYER
        tableau.addCell(getCelluleTableau("NET A PAYER (+/-)", borderwidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(UtilPaie.getNetAPayer(fichePaie)) + " " + monnaie + " ", borderwidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));

    }

    private void setLignesTabSynthese(PdfPTable tableau, float borderwidth, String monnaie, double totBrut, double totRetenu, double totNet) {
        //Total Salaire Brut
        tableau.addCell(getCelluleTableau("Salaires Bruts:", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(totBrut) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        //Total Retenus
        tableau.addCell(getCelluleTableau("Retenus:", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(totRetenu) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        //Total Net à Payer
        tableau.addCell(getCelluleTableau("Nets à Payer:", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(UtilPaie.getMontantFrancais(totNet) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
    }

    private void setContenuDeLaPage() throws Exception {
        if (sortiesFichesDePaies != null) {
            sortiesFichesDePaies.getEcouteurEnregistrement().onUploading("Construction du contenu...");
        }
        setLogoEtDetailsEntreprise();
        setTitreEtDateDocument();
        //Corps
        if (gestionnairePaie.getTypeExport() == PanelPaie.TYPE_EXPORT_TOUT) {
            //Lorsqu'il s'agit d'une fiche de paie d'un agent en particulier
            setTableauSynthese();
            setTableauGrilleDePaie();
        } else {
            //Lorsqu'il s'agit de la grille de paie de plusieurs agents en même temps
            setTableauBulletinPaieAgentSelectionne();
        }
        //Fin du corps
        ajouterLigne(1);
        setSignataire();
        setBasDePage();
        if (sortiesFichesDePaies != null) {
            sortiesFichesDePaies.getEcouteurEnregistrement().onUploading("Finalisation...");
        }
    }

    public static void main(String[] a) {
        //Exemple
        DocumentPDFPaie docpdf = new DocumentPDFPaie(null, ACTION_OUVRIR, null);
    }

}




















