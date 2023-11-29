package edu.ucalgary.oop;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * This is a service class that is used to create a table model that can be edited.
 * @author Celina Hy
 * @version 1.0
 * @since 1.0
 */
class EditableTableModel extends AbstractTableModel
{
    String[] columnTitles;
    Object[][] dataEntries;
    boolean[] canEdit = new boolean[]{false, false, false, true};


    /**
     * This constructor creates a new EditableTableModel object.
     * @param columnTitles The titles of the columns.
     * @param dataEntries The data entries in the table.
     */
    public EditableTableModel(String[] columnTitles, Object[][] dataEntries)
    {
        this.columnTitles = columnTitles;
        this.dataEntries = dataEntries;
    }

    /**
     * This method returns the number of rows in the table.
     * @return The number of rows in the table.
     */
    public int getRowCount()
    {
        return dataEntries.length;
    }

    /**
     * This method returns the number of columns in the table.
     * @return The number of columns in the table.
     */
    public int getColumnCount()
    {
        return columnTitles.length;
    }

    /**
     * This method returns the value at the specified row and column.
     * @param row        the row whose value is to be queried
     * @param column     the column whose value is to be queried
     * @return          the value Object at the specified cell
     */
    public Object getValueAt(int row, int column)
    {
        return dataEntries[row][column];
    }

    /**
     * This method returns the name of the column at the specified index.
     * @param column     the column whose name is to be queried
     * @return           the name of the column at the specified index.
     */
    public String getColumnName(int column)
    {
        return columnTitles[column];
    }

    /**
     * This method returns the class of the column at the specified index.
     * @param column the column whose class is to be queried.
     * @return the class of the column at the specified index.
     */
    public Class getColumnClass(int column)
    {
        return getValueAt(0, column).getClass();
    }

    /**
     * This method returns whether the cell at the specified row and column is editable.
     * @param row        the row whose value is to be queried.
     * @param column     the column whose value is to be queried.
     * @return true if the cell is editable, false otherwise.
     */
    public boolean isCellEditable(int row, int column)
    {
        return canEdit[column];
    }

    /**
     * This method sets the value at the specified row and column.
     * @param value      the new value.
     * @param row        the row whose value is to be changed.
     * @param column     the column whose value is to be changed.
     */
    public void setValueAt(Object value, int row, int column)
    {

        if (value instanceof Integer) {
            int x = ((Integer) value).intValue();

            if (0 <= x && x <= 23) {
                dataEntries[row][column] = value;
                fireTableCellUpdated(row, column);
            }
            else {
                int newValue;
                if (x < 0) {
                    newValue = 0;
                }
                else {
                    newValue = 23;
                }

                String oldValuetoString = Integer.toString(x);
                String newValuetoString = Integer.toString(newValue);

                String outputError = "Your start hour was " + oldValuetoString + ". It has been changed to " + newValuetoString + ".";

                JOptionPane.showMessageDialog(null, outputError);
                dataEntries[row][column] = newValue;
                fireTableCellUpdated(row, column);
            }
        }

    }
}