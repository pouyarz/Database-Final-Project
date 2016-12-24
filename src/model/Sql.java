package model;

import view.main;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.*;

public class Sql {

    private final String[] List1 = {"", "MUSICIANS", "ALBUM_PRODUCER", "SONGS_APPEARS"};
    private final String[] List2 = {"", "MUSICIANS", "ALBUM_PRODUCER", "SONGS_APPEARS", "INSTRUMENTS", "PLAYS", "TELEPHONE_HOME", "LIVES", "PERFORM", "PLACES"};
    private final Connection conn;
    private main cnt;

    public Sql(Connection con) {

        conn = con;
    }
    private Statement statement;
    private ResultSet Result;
    private static String query;

    

    public String[] columname;

    public String[][] search(String n, String table) {
        cnt = new main();

        String[][] result = null;

        for (int i = 1; i < List1.length; i++) {
            if (table.equals(List1[1])) {
                if (n.equals("")) {
                    query = "select ssn,name from musicians ";
                } else {
                    query = "select ssn,name from musicians where name = " + "'" + n + "'";
                }
            } else if (table.equals(List1[2])) {
                if (n.equals("")) {
                    query = "ALBUMIDENTIFIER,ssn,copyrightdate,speed,title from Album_Producer ";
                } else {
                    query = "select  ALBUMIDENTIFIER,ssn,copyrightdate,speed,title from Album_Producer where title = " + "'" + n + "'";
                }
            } else if (table.equals(List1[3])) {
                if (n.equals("")) {
                    query = "select songid,author,title,ALBUMIDENTIFIER  from Songs_Appears";
                } else {
                    query = "select songid,author,title,ALBUMIDENTIFIER  from Songs_Appears  where title = " + "'" + n + "'";
                }
            }

            try {

                statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Result = statement.executeQuery(query);
                result = convert2D(Result);

                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }
    private boolean valid = false;
    public boolean Valid() {
        return valid;
    }

    public void delete(int a) {

        valid = false;

        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Result = statement.executeQuery(query);

            Result.absolute(a);
            System.out.println(a + "was deleted");

            Result.deleteRow();
            valid = true;
            statement.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void insert(ArrayList<String> a, ArrayList<Object> b) {
        valid = false;
        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Result = statement.executeQuery(query);

            Result.moveToInsertRow();

            for (int i = 0; i < a.size(); i++) {
                System.out.println(a.get(i));
                System.out.println(b.get(i));

                if (b.get(i).equals(java.util.Date.class)) {
                    sqlBirthDate = getADate((String) b.get(i));
                    Result.updateDate(a.get(i), (Date) sqlBirthDate);
                } else {
                    Result.updateString(a.get(i), b.get(i).toString());
                }

            }

            Result.insertRow();
            valid = true;
            Result.updateRow();

            statement.close();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

    }
    static java.util.Date dateBirthDate, sqlBirthDate, rn;

    public static java.util.Date getADate(String sDate) {

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        try {
            dateBirthDate = dateFormatter.parse(sDate);
            rn = new java.sql.Date(dateBirthDate.getTime());
        } catch (ParseException e1) {

            e1.printStackTrace();
        }

        return rn;

    }

    public void update(ArrayList<String> a, ArrayList<Object> b, int d) {
        valid = false;

        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            Result = statement.executeQuery(query);

            Result.absolute(d);
            for (int i = 0; i < a.size(); i++) {
                if (b.get(i).equals(java.util.Date.class)) {
                    sqlBirthDate = getADate((String) b.get(i));
                    Result.updateDate(a.get(i), (Date) sqlBirthDate);
                } else {
                    Result.updateString(a.get(i), b.get(i).toString());
                }

            }

            Result.updateRow();
            valid = true;

            statement.close();
        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

    }

    public String[][] search2(String table, String n) {

        cnt = new main();

        String[][] result = null;

        for (int i = 1; i < List2.length; i++) {
            if (table.equals(List2[1])) {
                if (n.equals("")) {
                    query = "select ssn,name  from musicians ";
                } else {
                    query = "select ssn,name  from musicians where ssn=" + "'" + n + "'" + " or name=" + "'" + n + "'";
                }

            }
            if (table.equals(List2[2])) {
                if (n.equals("")) {
                    query = "select ALBUMIDENTIFIER,ssn,copyrightdate,speed,title  from Album_Producer";
                } else {
                    query = "select ALBUMIDENTIFIER,ssn,copyrightdate,speed,title  from Album_Producer where ALBUMIDENTIFIER=" + "'" + n + "'" + "or ssn=" + "'" + n + "'" + " or speed=" + "'" + n + "' or title=" + "'" + n + "'";
                }
            }
            if (table.equals(List2[3])) {
                if (n.equals("")) {
                    query = "select songid,author,title,ALBUMIDENTIFIER from Songs_Appears ";
                } else {
                    query = "select songid,author,title,ALBUMIDENTIFIER from Songs_Appears where songid=" + "'" + n + "' or author=" + "'" + n + "' or title=" + "'" + n + "' or ALBUMIDENTIFIER=" + "'" + n + "'";
                }
            }
            if (table.equals(List2[4])) {
                if (n.equals("")) {
                    query = "select instrid,dname,key from instruments";
                } else {
                    query = "select instrid,dname,key from instruments where instrid=" + "'" + n + "' or dname=" + "'" + n + "' or key='" + n + "'";
                }
            }
            if (table.equals(List2[5])) {
                if (n.equals("")) {
                    query = "select ssn,instrid from plays ";
                } else {
                    query = "select ssn,instrid from plays where ssn='" + n + "' or instrid='" + n + "'";
                }
            }
            if (table.equals(List2[6])) {
                if (n.equals("")) {
                    query = "select phone,address from telephone_Home ";
                } else {
                    query = "select phone,address from telephone_Home where phone='" + n + "' or address='" + n + "'";
                }
            }
            if (table.equals(List2[7])) {
                if (n.equals("")) {
                    query = "select ssn,phone,address from lives ";
                } else {
                    query = "select ssn,phone,address from lives where ssn='" + n + "' or phone='" + n + "' or address='" + n + "'";
                }
            }
            if (table.equals(List2[8])) {
                if (n.equals("")) {
                    query = "select songid,ssn from perform ";
                } else {
                    query = "select songid,ssn from perform where songid='" + n + "' or ssn='" + n + "'";
                }
            }
            if (table.equals(List2[9])) {
                if (n.equals("")) {
                    query = "select address  from places ";
                } else {
                    query = "select address  from places where address='" + n + "'";
                }
            }

            try {

                statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                Result = statement.executeQuery(query);

                result = convert2D(Result);
                statement.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }


    /*  A method for converting queroies to tow dimentional array*/
    public String[][] convert2D(ResultSet Result) {
        String[] result = null;

        String[][] finalResult = null;

        try {
            ResultSetMetaData rsmd = Result.getMetaData();
            int numberofcolumn = rsmd.getColumnCount();
            columname = new String[numberofcolumn];

            result = new String[numberofcolumn];
            finalResult = new String[100][numberofcolumn];
            int j = 0;

            while (Result.next()) {

                for (int i = 1; i <= numberofcolumn; i++) {
                    String s = Result.getString(i);

                    finalResult[j][i - 1] = s;
                }
                j++;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return finalResult;
    }

}
