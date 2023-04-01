package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.model.Session;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        String hash = hashUsingSHA1(data);

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


    //This method is used to create an SHA-1 hash of the user_id+timestamp.
    private static String hashUsingSHA1(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }

}
