package com.example.csit228_f1_v2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnection {
    //TODO: Change port and database name
    public static final String URL = "jdbc:mysql://localhost:8081/dbatayf1";

    static Connection getConnection(){
        Connection c = null;

        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL, "root", "");
            System.out.println("Connection Success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        catch (ClassNotFoundException e){
//            e.printStackTrace();
//        }

        return c;
    }

}