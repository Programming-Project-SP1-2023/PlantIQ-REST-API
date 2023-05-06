package com.plantiq.plantiqserver.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//---------------------------------Service Class---------------------------------
//The HashService class has been created with the scope of encrypting data using
//a hash method.
//-------------------------------------------------------------------------------
public class HashService {

    //This method uses a hash function to encrypt data. It takes in a String as
    //a parameter and uses SHA1 to hash to data
    public static String generateSHA1(String data){
        try {
            //Get an instance of the SHA-1 algorithm for message digest
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            //Compute the hash value of the input data using the SHA-1 algorithm
            byte[] hash = digest.digest(data.getBytes());

            //Since the hash at this stage is in binary, this needs to be converted
            //to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            //Return the hexadecimal string representation of the hash value
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            //Throw a runtime exception if the SHA-1 algorithm is not available
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }


}
