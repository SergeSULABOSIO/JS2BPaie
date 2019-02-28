/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeurTable;

import SOURCES.Interface.InterfaceAgent;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Utilitaires.ParametreFichesDePaie;
import SOURCES.Utilitaires.Util;
import java.awt.Component;
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
        this.champEditionCombo.removeAllItems();
        this.champEditionCombo.addItem("");
        if (this.parametreFichesDePaie != null) {
            InterfaceExercice Iexerc = this.parametreFichesDePaie.getExercice();
            if (Iexerc != null) {
                Vector<String> listeMois = Util.getListeMois(Iexerc.getDebut(), Iexerc.getFin());
                for (String Omois : listeMois) {
                    this.champEditionCombo.addItem(Omois);
                }
            }
        }
    }

    

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return champEditionCombo.getSelectedItem();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        champEditionCombo.setSelectedItem(value + "");
        return champEditionCombo;
    }

}
