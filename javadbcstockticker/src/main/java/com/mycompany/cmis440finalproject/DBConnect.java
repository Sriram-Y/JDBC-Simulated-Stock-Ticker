/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cmis440finalproject;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author sriram
 */
public class DBConnect {
    private static String dbURL = "jdbc:derby://localhost:1527/contact;create=true;user=nbuser;password=nbuser";
    private static String tableName = "cars";
    private static Connection conn = null;
    private static Statement stmt = null;
    
    public static ArrayList<String> data = new ArrayList<>();
    
    public static void getData(){
        createConnection();
        createTable();
        insertData(1, "S99F5KCI8DKL78M88", "Toyota", "Corolla", "1998", "Green");
        insertData(2, "OF1H44H9HXXFGXZR6", "BMW", "X3", "2021", "Black");
        insertData(3, "4ZQLA4D2T2QHNUQFQ", "Honda", "Civic", "2007", "Brown");
        insertData(4, "UUWNNQPVZIJF7HLAC", "Toyota", "Camry", "2002", "White");
        insertData(5, "P2EA3V5345VTVXK2K", "Jeep", "Wrangler", "2012", "Purple");
        insertData(6, "2B1BJCETGK4BCA0PE", "Honda", "CRV", "2013", "Silver");
        insertData(7, "WOMZV07QHN23GFPM3", "Honda", "CRV", "2011", "Silver");
        insertData(8, "ZOP7DAV5QXCFA96FC", "Nissan", "Ultima", "1999", "Gold");
        insertData(9, "Y5JZJ1RD50IG9UDO8", "Nissan", "Rogue", "2012", "Brown");
        insertData(10, "O78ASEY95XOGBSQ7Y", "Chevrolet", "Camaro", "2003", "Yellow");
        insertData(11, "I7RXJ7DPKADJN29UM", "Jeep", "Cherokee", "2013", "Red");
        insertData(12, "9DX3ZMON71XBZEL5Z", "BMW", "M3", "1997", "Blue");
        insertData(13, "TOYBFNZHYJRGWBBFJ", "Volkswagen", "GTI", "2013", "White");
        insertData(14, "KPY8JPWIZA0ZOX3M5", "Hyundai", "Veloster", "2020", "Orange");
        insertData(15, "6AGKR22M2L01IMF86", "Mercedez Benz", "C63", "2012", "Silver");
        insertData(16, "O4MW6NL3QDTE84CG5", "Honda", "Odyssey", "2013", "Brown");
        insertData(17, "X9E4TOBGTTHD1ERHZ", "Honda", "Civic", "2011", "Silver");
        insertData(18, "JX0MPVOAT2NYZ2V9P", "Volkswagen", "Golf R", "2016", "Blue");
        insertData(19, "STGREFB3XRQWALWBD", "Nissan", "Rogue", "2011", "Brown");
        insertData(20, "GSTACFMM7GBYGO1UY", "Toyota", "Sienna", "2015", "Blue");
        selectData();
        shutdown();
    }
    
    private static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL);
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
    
    private static void createTable(){
        try{
            stmt = conn.createStatement();
            
            stmt.execute("create table " + tableName + " ("
                    + "id integer not null primary key, "
                    + "vin varchar(30), "
                    + "make varchar(30), "
                    + "model varchar(30), "
                    + "carYear varchar(30), "
                    + "color varchar(30))");
        }
        catch(Exception except){
            except.printStackTrace();
        }
    }
    
    private static void insertData(int id, String vin, String make, String model, String year, String color)
    {
        try
        {
            stmt = conn.createStatement();
            stmt.execute("insert into " + tableName + " values (" +
                    id + ",'" + vin + "','" + make + "','" + model + "','" + year + "','" + color + "')");
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void selectData()
    {
        try
        {
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery("select * from " + tableName);
            ResultSetMetaData rsmd = results.getMetaData();
            int numberCols = rsmd.getColumnCount();
            for (int i=1; i<=numberCols; i++)
            {
                //print Column Names
                System.out.print(rsmd.getColumnLabel(i)+"\t\t");  
            }

            System.out.println("\n-------------------------------------------------");

            while(results.next())
            {
                int id = results.getInt(1);
                String vin = results.getString(2);
                String make = results.getString(3);
                String model = results.getString(4);
                String year = results.getString(5);
                String color = results.getString(6);
                
                data.add(String.valueOf(id));
                data.add(vin);
                data.add(make);
                data.add(model);
                data.add(year);
                data.add(color);
                
                System.out.println(id + "\t\t" + vin + "\t\t" + make + "\t\t" + model + "\t\t" + year + "\t\t" + color + "\t\t");
            }
            results.close();
            stmt.close();
        }
        catch (SQLException sqlExcept)
        {
            sqlExcept.printStackTrace();
        }
    }
    
    private static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
}
