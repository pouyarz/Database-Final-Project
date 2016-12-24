import java.util.HashSet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class Edit extends JFrame implements ActionListener {

    private final JButton Update = new JButton("Update");
    private UpdateTbl Tbl = null;

    public Edit() {
        super();

        Object[][] rows = {{"po", "rz"}, {"shi", "mov"}};
        String[] colNames = {"FName", "LName"};

        Tbl = new UpdateTbl(rows, colNames);
        JTable table = new JTable(Tbl);
        JScrollPane scroll = new JScrollPane(table);
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(Update, BorderLayout.SOUTH);
        Update.addActionListener(this);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void Action(ActionEvent e) {

        if (e.getSource() == Update) {
            int[] updatedIndexes = Tbl.UpdateRow();
            for (int a = 0; a < updatedIndexes.length; a++) {
                int idx = updatedIndexes[a];
                String name = Tbl.getValueAt(idx, 0).toString();
                System.out.println(name + " was updated.");
            }
        }
    }

    public static void main(String[] args) {
        Edit updates = new Edit();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class UpdateTbl extends DefaultTableModel {

    private final HashSet updatedRows = new HashSet();

    public UpdateTbl(Object[][] rows, Object[] colNames) {
        super(rows, colNames);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Object oldValue = getValueAt(row, col);
        if ((oldValue == null) && (value != null)) {
            updatedRows.add(new Integer(row));
        } else if (!oldValue.equals(value)) {
            updatedRows.add(new Integer(row));
        }
        super.setValueAt(value, row, col);
    }

    public int[] UpdateRow() {
        Integer[] keys = (Integer[]) updatedRows.toArray(new Integer[0]);
        int[] indexes = new int[keys.length];
        for (int a = 0; a < keys.length; a++) {
            indexes[a] = keys[a];
        }
        return indexes;
    }
}
