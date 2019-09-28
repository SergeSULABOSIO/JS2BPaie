/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeurTable_Paie;

import SOURCES.Utilitaires_Paie.ParametreFichesDePaie;
import SOURCES.Utilitaires_Paie.UtilPaie;
import Source.Objet.Exercice;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */
public class EditeurMois extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreFichesDePaie parametreFichesDePaie;

    public EditeurMois(ParametreFichesDePaie parametreFichesDePaie) {
        this.parametreFichesDePaie = parametreFichesDePaie;
        initCombo();
    }

    public void initCombo() {
        this.champEditionCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Clic: " + e.getActionCommand());
                fireEditingStopped();
            }
        });
        this.champEditionCombo.removeAllItems();
        this.champEditionCombo.addItem("");
        if (this.parametreFichesDePaie != null) {
            Exercice Iexerc = this.parametreFichesDePaie.getExercice();
            if (Iexerc != null) {
                Vector<String> listeMois = UtilPaie.getListeMois(Iexerc.getDebut(), Iexerc.getFin());
                for (String Omois : listeMois) {
                    this.champEditionCombo.addItem(Omois);
                }
            }
        }
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        if (champEditionCombo.getItemCount() != 0) {
            return champEditionCombo.getSelectedItem();
        } else {
            return null;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        champEditionCombo.setSelectedItem(value + "");
        return champEditionCombo;
    }

}

