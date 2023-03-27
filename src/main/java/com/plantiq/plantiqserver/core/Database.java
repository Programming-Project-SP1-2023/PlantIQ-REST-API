package com.plantiq.plantiqserver.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Database{

    public static ArrayList<HashMap<String, String>> query(String query){

        ArrayList<HashMap<String,String>> data = new ArrayList<>();

        Connection connection = null;
        ResultSet resultSet = null;

        try{

            connection = DriverManager.getConnection("jdbc:mysql://localhost/plantiq?user=root&password=");

            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                HashMap<String, String> data2 = new HashMap<>();
                int i = 1;
                while(i < resultSet.getMetaData().getColumnCount()+1){
                    data2.put(resultSet.getMetaData().getColumnName(i),resultSet.getString(i));
                    i++;
                }

                data.add(data2);
            }

            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return data;
    }

}
