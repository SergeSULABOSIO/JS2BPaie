/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeurTable;

import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Utilitaires.ParametreFichesDePaie;
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
public class EditeurMonnaie extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreFichesDePaie parametreFichesDePaie;

    public EditeurMonnaie(ParametreFichesDePaie parametreFichesDePaie) {
        this.parametreFichesDePaie = parametreFichesDePaie;
        initCombo();
    }

    public void initCombo() {
        this.champEditionCombo.removeAllItems();
        if (this.parametreFichesDePaie != null) {
            Vector<InterfaceMonnaie> listeMonnaies = this.parametreFichesDePaie.getMonnaies();
            if (listeMonnaies != null) {
                for (InterfaceMonnaie monnaie : listeMonnaies) {
                    this.champEditionCombo.addItem(monnaie.getCode());
                }
            }
        }
    }

    private int getIdMonnaie(String monnaie) {
        for (InterfaceMonnaie Imonnaie : this.parametreFichesDePaie.getMonnaies()) {
            if (Imonnaie.getCode().trim().toUpperCase().equals(monnaie.trim().toUpperCase())) {
                return Imonnaie.getId();
            }
        }
        return -1;
    }

    private String getMonnaie(int idMonnaie) {
        for (InterfaceMonnaie Imonnaie : this.parametreFichesDePaie.getMonnaies()) {
            if (Imonnaie.getId() == idMonnaie) {
                return Imonnaie.getCode();
            }
        }
        return "";
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return getIdMonnaie(champEditionCombo.getSelectedItem() + "");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        String defaultSelection = getMonnaie(Integer.parseInt(value + ""));
        champEditionCombo.setSelectedItem(defaultSelection);
        return champEditionCombo;
    }

}
