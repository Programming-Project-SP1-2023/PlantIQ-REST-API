package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.model.Session;

import java.util.ArrayList;
import java.util.HashMap;

public class SessionService {

    //Method to create session keys. The key will be equal to a
    //SHA256 hash of the user_id and the current time.
    public static String create(String user_id){

        //Delete any previous sessions the user may have already created, to
        //ensure that we do not end up with any duplicated sessions we will
        //get an arraylist of them and delete all entries, this should be 1
        //only.
        ArrayList<Session> sessions = Session.collection().where("user_id",user_id).get();

        //Delete all sessions for this user.
        sessions.forEach((n)-> n.delete("token"));

        //Use the user_id and time to create the hash value.
        String data = user_id+TimeService.now();
        String hash = HashService.generateSHA1(data);

        //Create the new session object data map.
        HashMap<String,Object> sessionData = new HashMap<>();

        sessionData.put("token",hash);
        sessionData.put("user_id",user_id);
        sessionData.put("expiry",TimeService.nowPlusDays(14));

        //Insert the new session into the database
        boolean outcome = Session.insert("Session",sessionData);

        //If this fails then print to console.
        if(!outcome){
            System.out.println("[SessionService] failed to insert session key into database!");
        }

        //Lastly we return the hash value back to the caller, this allows
        //for the front end to get it!
        return hash;
    }


}
