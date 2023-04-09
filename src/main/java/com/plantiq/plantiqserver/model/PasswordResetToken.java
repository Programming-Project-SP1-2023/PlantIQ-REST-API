package com.plantiq.plantiqserver.model;

import com.plantiq.plantiqserver.core.Model;
import com.plantiq.plantiqserver.core.ModelCollection;

import java.util.HashMap;

public class PasswordResetToken extends Model {


    public PasswordResetToken(HashMap<String, Object> data) {
        super(data);
    }

    public static ModelCollection<PasswordResetToken> collection() {
        return new ModelCollection<>(PasswordResetToken.class);
    }

    public String token(){
        return (String) this.data.get("token");
    }

    public String email(){
        return (String) this.data.get("email");
    }

    public int expirationDate(){
        return Integer.parseInt( (String) this.data.get("expirationDate"));
    }
    public int createdDate(){
        return Integer.parseInt( (String) this.data.get("createdDate"));
    }


}
