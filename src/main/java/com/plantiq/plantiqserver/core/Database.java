package com.plantiq.plantiqserver.core;


import com.plantiq.plantiqserver.PlantIqServerApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


//-----------------------------------------------------------------//
//                        Database Class                           //
//-----------------------------------------------------------------//

//The database class is one of the core component of the project. It will manage
//the connection with the database and query the database. This class is also able to
//keeps track of how many rows have been affected by the query, which is a smart way
//to deduct if the query worked.
public class Database{
    //Integer which will keep track of how many rows have been affected by the query.
    private static int rowsAffected;

    //------------------------------------------------------|
    //                     QUERY METHOD                     |
    //------------------------------------------------------|

    //This method takes in as a parameter a String which will be the SQL query.
    //It will initially establish the connection with the database, to then
    //move on executed the query and retrieving the data.
    public static ArrayList<HashMap<String, String>> query(String query){

        //This array will contain each work of the query
        String[] queryData = query.split(" ");

        ArrayList<HashMap<String,String>> data = new ArrayList<>();

        Connection connection = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(PlantIqServerApplication.getInstance().getDatabaseConnectionLink());

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
    //------------------------------------------------------|
    //          GET AND RESET ROWS AFFECTED METHOD          |
    //------------------------------------------------------|

    //This method gets how many rows have been affected by
    //the query and resets the public value so that next count
    //will not be influence by the previous
    public static int getAndResetRowsAffected(){
        //Variable that will store the number of rowsAffected
        int rows =  Database.rowsAffected;
        //Reset of the public variable to not alter next count
        Database.rowsAffected = 0;
        return rows;
    }



}
