package com.plantiq.plantiqserver.service;

import com.plantiq.plantiqserver.model.Session;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.HashMap;

public class SessionService {

    //Method to create session keys. The key will be equal to a
    //SHA256 hash of the user_id and the current time.
    public static String create(String user_id){

        String currentTime = (TimeService.now()).toString();

        String data = user_id+TimeService.now();
        String hash = hashUsingSHA1(data);

        HashMap<String,Object> sessionData = new HashMap<>();

        sessionData.put("token",hash);
        sessionData.put("user_id",user_id);
        sessionData.put("expiry",TimeService.nowPlusDays(14));
        boolean outcome = Session.insert("Session",sessionData);

        if(!outcome){
            System.out.println("[SessionService] failed to insert session key into database!");
        }

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
