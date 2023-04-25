package com.plantiq.plantiqserver.core;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database{

    private static int rowsAffected;

    public static ArrayList<HashMap<String, String>> query(String query){

        String[] queryData = query.split(" ");

        ArrayList<HashMap<String,String>> data = new ArrayList<>();

        Connection connection = null;
        ResultSet resultSet = null;

        try{

            connection = null;

            Statement statement = connection.createStatement();


            if(!queryData[0].equals("SELECT")){
                statement.execute(query);
                System.out.println("[DATABASE] Performed query "+query);
                return data;
            }

            resultSet = statement.executeQuery(query);



            while(resultSet.next()){
                HashMap<String, String> row = new HashMap<>();
                int i = 1;
                while(i < resultSet.getMetaData().getColumnCount()+1){
                    row.put(resultSet.getMetaData().getColumnName(i),resultSet.getString(i));
                    i++;
                }
                if(queryData[1].equals("TOP")){
                    row.put("_table",queryData[5].substring(7,queryData[5].length()-1));
                }else{
                    row.put("_table",queryData[3].substring(7,queryData[3].length()-1));
                }
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
