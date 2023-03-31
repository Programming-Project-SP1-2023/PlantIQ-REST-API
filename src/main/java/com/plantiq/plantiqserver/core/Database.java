package com.plantiq.plantiqserver.core;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database{

    private static int rowsAffected;

    public static ArrayList<HashMap<String, String>> query(String query){

        ArrayList<HashMap<String,String>> data = new ArrayList<>();


        Connection connection = null;
        ResultSet resultSet = null;

        try{

            connection = DriverManager.getConnection("jdbc:sqlserver://db-plantiq.database.windows.net:1433;database=plantiq-backend;user=spring@db-plantiq;password={PASSWORD};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30");

            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                HashMap<String, String> row = new HashMap<>();
                int i = 1;
                while(i < resultSet.getMetaData().getColumnCount()+1){
                    row.put(resultSet.getMetaData().getColumnName(i),resultSet.getString(i));
                    i++;
                }
                row.put("_table",resultSet.getMetaData().getTableName(1));
                data.add(row);
            }

            Database.rowsAffected = statement.getUpdateCount();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("[DATABASE] Performed query "+query);



        return data;
    }

    public static int getAndResetRowsAffected(){
        int rows =  Database.rowsAffected;
        Database.rowsAffected = 0;
        return rows;
    }



}
