/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeurTable;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */
public class EditeurDate extends AbstractCellEditor implements TableCellEditor {

    private JDateChooser dateChooser = new JDateChooser();

    public EditeurDate() {
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return dateChooser.getDate();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        dateChooser.setDate((Date) value);
        return dateChooser;
    }

}
