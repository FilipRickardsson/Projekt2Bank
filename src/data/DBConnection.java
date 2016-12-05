package data;

import bank.Customer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private static DBConnection dbConnection;
    private Connection con;
    private Statement st;

    private DBConnection() {
        connectToDB();
    }

    public static DBConnection getDBConnection() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    private void connectToDB() {
        String url = "jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false";
        String user = "newtonbank";
        String password = "kaffekopp";

        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            st.executeQuery("USE newtonbank");
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList getCustomers() {
        ArrayList<Customer> customers = new ArrayList();
        try {
            ResultSet rs = st.executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                customers.add(new Customer(rs.getString(20), rs.getInt(1)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return customers;
    }

}