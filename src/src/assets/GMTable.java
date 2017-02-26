/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assets;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * JTable that has been customised to match any theme changes made
 * the table also self modifies column widths to calculate spacing where necessary.
 * @author mafz
 */
public class GMTable extends JTable {

    public GMTable() {
        super();
        init();
    }

    public GMTable(TableModel dm) {
        super(dm);
        init();
    }
    
    private void init() {
        this.setFocusable(true);
        this.setColumnSelectionAllowed(false);
        // Columns to just fit
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // disable reordering columns
        this.getTableHeader().setReorderingAllowed(false);
        this.setRowSelectionAllowed(true);
        // prevent user from resizing columns
        this.getTableHeader().setResizingAllowed(false);
        // set selection colors
        this.setSelectionBackground(Res.BKG_COLOR);
        this.setSelectionForeground(Res.BTN_FONT_COLOR);
    }
    
    /**
     * Update table
     */
    public void updateTable() {
        for(int i=0; i<this.getColumnCount(); i++)
            adjustColumnSizes(i, 2);
        this.revalidate();
    }
    
    /**
     * Modifies a the JTable so that the columns resize to fit each column to its required length;
     * http://stackoverflow.com/questions/13013989/how-to-adjust-jtable-columns-to-fit-the-longest-content-in-column-cells
     * @param columnNo - number of columns in the table
     * @param margin - required margin for each column
     */
    private void adjustColumnSizes(int columnNo, int margin) {
        // get table column
        DefaultTableColumnModel columnModel = (DefaultTableColumnModel) this.getColumnModel();
        TableColumn column = columnModel.getColumn(columnNo);
        
        // get table renderer
        TableCellRenderer renderer = column.getHeaderRenderer();
        if(renderer == null)
            renderer =this.getTableHeader().getDefaultRenderer();
        
        // get width of column
        Component comp = renderer.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, 0, 0);
        int width = comp.getPreferredSize().width;
        
        for(int row=0; row<this.getRowCount(); row++) {
            renderer = this.getCellRenderer(row, columnNo);
            comp = renderer.getTableCellRendererComponent(this, this.getValueAt(row, columnNo), false, false, row, columnNo);
            int currentWidth = comp.getPreferredSize().width;
            width = Math.max(width, currentWidth);
        } 
       
        width += 3 * margin;
        
        column.setPreferredWidth(width);
        column.setWidth(width);
        
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
            
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return getPreferredSize().width < getParent().getWidth();
    }
}
