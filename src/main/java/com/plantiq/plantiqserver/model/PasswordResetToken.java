package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class PasswordResetToken extends Model {

    //|================================================|
    //|              STATIC CRUD METHODS               |
    //|================================================|

    //Our collection method is the only method that has to be
    //present in the child classes as it must inject the child
    //class type to ensure they are returned correctly.

    //This method will create a ModelCollection object using
    //the current type via generics and by providing the class
    //directly into the constructor, this collection object
    //allows us to build our SQL query and return our values.
    public static ModelCollection<PasswordResetToken> collection() {
        return new ModelCollection<>(PasswordResetToken.class);
    }

    //|================================================|
    //|                  CLASS METHODS                 |
    //|================================================|

    //Constructor
    public PasswordResetToken(HashMap<String, Object> data) {
        super(data);
    }

    //Get token method
    public String token(){
        return (String) this.data.get("token");
    }

    //Get Email method
    public String email(){
        return (String) this.data.get("email");
    }

    //Get expiration date method
    public int expirationDate(){
        return Integer.parseInt( (String) this.data.get("expirationDate"));
    }

    //Get creation date method
    public int createdDate(){
        return Integer.parseInt( (String) this.data.get("createdDate"));
    }


}
