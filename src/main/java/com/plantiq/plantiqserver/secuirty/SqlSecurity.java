package com.plantiq.plantiqserver.secuirty;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

//********************************************************************************
//*                                Security Class                                *
//********************************************************************************

//The Following class is used to stop malicious users from posting query language
// within the forms.
public class SqlSecurity {
    //List of words and symbols that will not be allowed as within user's responses
    private static final ArrayList<String> blacklist = new ArrayList<>();
    //Addition of banned words and symbols to the list.
    public static void initialize(){
        blacklist.add("RIGHT");
        blacklist.add("JOIN");
        blacklist.add("SELECT");
        blacklist.add("FROM");
        blacklist.add("DROP");
        blacklist.add("DELETE");
        blacklist.add("INNER JOIN");
        blacklist.add("TABLE");
        blacklist.add("UNION");
        blacklist.add("WHERE");
        blacklist.add("'");
        blacklist.add("\"");
        blacklist.add("=");
        blacklist.add("\\*");
    }
    //Method used to sanitize the user's response. If any of the
    //banned words/symbols are found, they will be removed.
    public static String sanitize(String input){
        //AtomicReference is used to easily modify the content of the input
        AtomicReference<String> output = new AtomicReference<>(input);
        //Looping through each one of the banned words and searching for them in the input.
        //If any words are found they will be removed by replacing them with "". If no
        //banned words are found the input will not see any modification.
        SqlSecurity.blacklist.forEach((word)-> output.set(output.get().replaceAll(word,"")));
        //Return of the input after sanitization.
        return output.get();
    }
}
