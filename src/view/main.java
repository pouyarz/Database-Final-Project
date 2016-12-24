package view;

//Import Package
import model.Sql;

//Import SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Import Array
import java.util.ArrayList;
import java.util.Arrays;

//Import GUI
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class main extends JPanel implements ActionListener {

    private Connection con;
    private final Sql sql;

    public String[] List1 = {"", "MUSICIANS", "ALBUM_PRODUCER", "SONGS_APPEARS"};
    public String[] List2 = {"", "MUSICIANS", "ALBUM_PRODUCER", "SONGS_APPEARS", "INSTRUMENTS", "PLAYS", "TELEPHONE_HOME", "LIVES", "PERFORM", "PLACES"};

    private static String col[];
    private String[] mus = {"ssn", "name"};
    private String[] alb = {"albumidentifier", "ssn", "copyrightDate", "speed", "title"};
    private String[] song = {"songid", "author", "title", "albumidentifier"};
    private String[] inst = {"instrid", "dname"};
    private String[] play = {"ssn", "instrid"};
    private String[] tel = {"phone", "address"};
    private String[] lives = {"ssn", "phone", "address"};
    private String[] perf = {"songid", "ssn"};
    private String[] places = {"address"};
    private static String[][] result;
    private JPanel Main;

    /*Cpnstructor main panel*/
    public main() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.err.println("Connected");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "HR", "hr");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        sql = new Sql(con);
        //Creating Window
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(1000,600));
        //Creating Main Panel
        JLabel La = new JLabel("Pouya RezaeiDivkolaei");
        Main = new JPanel();
        Main.setLayout(new BoxLayout(Main, BoxLayout.PAGE_AXIS));
        Main.add(La);
        Main.add(User_Pln());
        add(Main);
    }
    //GUI

    private static void GUI() {
        JFrame frame = new JFrame(" Notown Musical Store ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        JComponent component = new main();
        component.setOpaque(true);
        frame.setContentPane(component);
        frame.pack();
        frame.setVisible(true);
    }

    //Creating Choosing User Panel
    private JPanel User_Pln() {
        JRadioButton St, Cu;
        Cu = new JRadioButton("Customer");
        St = new JRadioButton("Staff");
        St.addActionListener(this);
        Cu.addActionListener(this);
        JPanel Pln = new JPanel();
        ButtonGroup Bu = new ButtonGroup();
        Bu.add(St);
        Bu.add(Cu);
        Pln.add(St);
        Pln.add(Cu);
        return Pln;
    }

    //Creating Customer Panel
    private JTable Tble;
    private JComboBox B;
    private JTextField text;
    private static DefaultTableModel DTble;

    private JPanel Cust_Pnl() {
        JLabel Lb1 = new JLabel("Table ");
        B = new JComboBox(List1);
        B.setSelectedIndex(0);
        text = new JTextField(15);
        JButton SearchCu = new JButton("Search");
        JLabel Lb2 = new JLabel("Search by Records ");
        SearchCu.addActionListener(new custBoxActionPerform());
        JPanel Pnl = new JPanel();
        Pnl.add(Lb1);
        Pnl.add(B);
        Pnl.add(Lb2);
        Pnl.add(text);
        Pnl.add(SearchCu);
        return Pnl;
    }

    //Creating Staff Panel
    private JPanel St_Pnl() {
        JLabel Lb1 = new JLabel("Table ");
        B = new JComboBox(List2);
        B.setSelectedIndex(0);
        JButton SearchSt = new JButton("Search");
        SearchSt.addActionListener(new staffBoxActionPerform());
        JLabel Lb2 = new JLabel("Search by Records ");
        Txt = new JTextField(15);
        JPanel Pnl = new JPanel();
        Pnl.add(Lb1);
        Pnl.add(B);
        Pnl.add(Lb2);
        Pnl.add(Txt);
        Pnl.add(SearchSt);
        return Pnl;
    }

    private ArrayList<String> colname;
    private ArrayList<String> selectedcolname;
    private ArrayList<Object> selectedvalues;
    private ArrayList<Object> values;
    private static JTextField Txt;

    /* staff search panel */
    private JPanel staffsearchpanel(String[][] n, String[] m) {

        DTble = new DefaultTableModel(n, m);
        Tble = new JTable(DTble);
        Tble.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        Tble.setAutoscrolls(true);
        JScrollPane pane = new JScrollPane(Tble);
        Tble.getModel().addTableModelListener(new TableModelListener() {
            public void TbleChanged(TableModelEvent e) {
                values = new ArrayList<Object>();
                colname = new ArrayList<String>();
                selectedcolname = new ArrayList<String>();
                selectedvalues = new ArrayList<Object>();
                for (int column = 0; column < Tble.getColumnCount(); column++) {
                    values.add(Tble.getModel().getValueAt(Tble.getSelectedRow(), column));
                    colname.add(Tble.getColumnName(column));
                    System.out.println(values + " " + colname);
                }
                selectedcolname.add(Tble.getColumnName(Tble.getSelectedColumn()));
                selectedvalues.add(Tble.getModel().getValueAt(Tble.getSelectedRow(), Tble.getSelectedColumn()));
            }

            @Override
            public void tableChanged(TableModelEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        JButton insert = new JButton("Insert");
        insert.addActionListener(new insertActionListener());
        JButton delete = new JButton("Delete");
        delete.addActionListener(new deleteListener());
        JButton update = new JButton("Update");
        update.addActionListener(new updateActionListener());
        JPanel Pnl = new JPanel();
        Pnl.add(pane);
        Pnl.add(delete);
        Pnl.add(insert);
        Pnl.add(update);
                return Pnl;
    }

    //Search Panel
    private JPanel custsearchpanel(String[][] n, String[] t) {
        JPanel Pnl = new JPanel();
        DTble = new DefaultTableModel(n, t);
        Tble = new JTable(DTble);
        Tble.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        Tble.setAutoscrolls(true);
        JScrollPane pane = new JScrollPane(Tble);
        Pnl.add(pane);
        return Pnl;
    }
    //Checking Password
    private static boolean Password(char[] input) {
        boolean Correct = true;
        char[] Pass = {'c', 's', '4', '3', '0', '@', 'S', 'I', 'U', 'C'};
        if (input.length != Pass.length) {
            Correct = false;
        } else {
            Correct = Arrays.equals(input, Pass);
        }
        Arrays.fill(Pass, '0');
        return Correct;
    }
    private static final String St = "Staff";
    private static final String Cu = "Customer";


    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (St.equals(cmd)) 
        {
            Main.add(St_Pnl());
            Main.revalidate();
            Main.repaint();

        } else if (Cu.equals(cmd)) { 
            Main.add(Cust_Pnl());
            Main.revalidate();
        }
    }

    //Main
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI();
            }
        });
    }

    
    public String[][] search(String name, String Tble) {
        String[][] result = sql.search(name, Tble);
        return result;
    }

    public String[][] search2(String Tble, String name) {
        String[][] result = sql.search2(Tble, name);
        return result;
    }

    public void delete(int a) {

        sql.delete(a);

    }

    public boolean IsValid() {

        return sql.Valid();

    }

    public void insert(ArrayList<String> x, ArrayList<Object> y) {
        sql.insert(x, y);
    }

    public void update(ArrayList<String> a, ArrayList<Object> b, int c) {
        sql.update(a, b, c);
    }
    public class restartListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {

                    GUI();
                }
            });
        }
    }
    public class custBoxActionPerform implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String jb = (String) B.getSelectedItem();
            String name = text.getText();

            if (List2[1].equals(jb)) {
                col = mus;
            } else if (List2[2].equals(jb)) {
                col = alb;
            } else if (List2[3].equals(jb)) {
                col = song;
            }

            for (int i = 1; i < List1.length; i++) {

                if (List1[i].equals(jb)) {

                    result = search(name, List1[i]);
                    Main.add(custsearchpanel(result, col));
                    Main.revalidate();

                }
            }
        }

    }

    public class staffBoxActionPerform implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String jb = (String) B.getSelectedItem();

            String text = Txt.getText();

            if (List2[1].equals(jb)) {
                col = mus;
            } else if (List2[2].equals(jb)) {
                col = alb;
            } else if (List2[3].equals(jb)) {
                col = song;
            } else if (List2[4].equals(jb)) {
                col = inst;
            } else if (List2[5].equals(jb)) {
                col = play;
            } else if (List2[6].equals(jb)) {
                col = tel;
            } else if (List2[7].equals(jb)) {
                col = lives;
            } else if (List2[8].equals(jb)) {
                col = perf;
            } else if (List2[9].equals(jb)) {
                col = places;
            }

            for (int i = 1; i < List2.length; i++) {

                if (List2[i].equals(jb)) {

                    result = search2(List2[i], text);
                    Main.add(staffsearchpanel(result, col));
                    Main.revalidate();

                }
            }
        }

    }

    private static int SRow;

    private class deleteListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String msg = JOptionPane.showInputDialog(null, "Password");
            char[] pass = new char[msg.length()];
            for (int i = 0; i < msg.length(); i++) {
                pass[i] = msg.charAt(i);

            }

            if (Password(pass)) {

                SRow = Tble.getSelectedRow() + 1;
                delete(SRow);

                System.out.println(IsValid());
                if (IsValid()) {
                    DTble.removeRow(Tble.getSelectedRow());
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot delete this row!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null,
                        "Wrong Password",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);

            }
        }
    }
    //ActionListener
    private class insertActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String msg = JOptionPane.showInputDialog(null, "Password");
            char[] pass = new char[msg.length()];
            for (int i = 0; i < msg.length(); i++) {
                pass[i] = msg.charAt(i);

            }

            if (Password(pass)) {

                insert(colname, values);

                System.out.println(IsValid());
                if (IsValid()) {
                    DTble.addRow(values.toArray());
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot update this row!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {

                JOptionPane.showMessageDialog(null,
                        "Wrong Password. Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }


    // action listener
    private class updateActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String msg = (String) JOptionPane.showInputDialog(null, "Password");
            char[] pass = new char[msg.length()];
            for (int i = 0; i < msg.length(); i++) {

                pass[i] = msg.charAt(i);

            }

            if (Password(pass)) {

                int selectedrow = Tble.getSelectedRow() + 1;
                update(colname, values, selectedrow);

                System.out.println(IsValid());
                if (!IsValid()) {
                    JOptionPane.showMessageDialog(null, "Cannot update this row!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } else {

                JOptionPane.showMessageDialog(null,
                        "Wrong Password. Try again.",
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);

            }

        }

    }

}
