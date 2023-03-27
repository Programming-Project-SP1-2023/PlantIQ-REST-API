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

            connection = DriverManager.getConnection("jdbc:mysql://localhost/plantiq?user=root&password=");

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


        return data;
    }

    public static int getAndResetRowsAffected(){
        int rows =  Database.rowsAffected;
        Database.rowsAffected = 0;
        return rows;
    }



}
