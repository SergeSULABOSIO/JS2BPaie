/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeurTable;


import SOURCES.Interface.InterfaceAgent;
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
public class EditeurAgents extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreFichesDePaie parametreFichesDePaie;
    
    public EditeurAgents(ParametreFichesDePaie parametreFichesDePaie) {
        this.parametreFichesDePaie = parametreFichesDePaie;
        initCombo();
    }

    public void initCombo() {
        this.champEditionCombo.removeAllItems();
        this.champEditionCombo.addItem("");
        if (this.parametreFichesDePaie != null) {
            Vector<InterfaceAgent> listeAgents = this.parametreFichesDePaie.getAgents();
            if(listeAgents != null){
                for(InterfaceAgent agent : listeAgents){
                    this.champEditionCombo.addItem(agent.getNom()+" " + agent.getPostnom()+" " + agent.getPrenom());
                }
            }
        }
    }
    
    
    private int getIdAgent(String nomAgent){
        for(InterfaceAgent Icha : this.parametreFichesDePaie.getAgents()){
            if((Icha.getNom()+" " + Icha.getPostnom()+" " + Icha.getPrenom()).trim().toUpperCase().equals(nomAgent.trim().toUpperCase())){
                return Icha.getId();
            }
        }
        return -1;
    }
    
    private String getAgent(int idAgent){
        for(InterfaceAgent Icha : this.parametreFichesDePaie.getAgents()){
            if(Icha.getId() == idAgent){
                return Icha.getNom()+" " + Icha.getPostnom()+" " + Icha.getPrenom();
            }
        }
        return "";
    }
    
    
    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return getIdAgent(champEditionCombo.getSelectedItem() + "");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        String defaultSelection = getAgent(Integer.parseInt(value+""));
        champEditionCombo.setSelectedItem(defaultSelection);
        return champEditionCombo;
    }

}