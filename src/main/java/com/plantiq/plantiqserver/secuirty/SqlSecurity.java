package com.plantiq.plantiqserver.secuirty;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class SqlSecurity {

    private static final ArrayList<String> blacklist = new ArrayList<>();

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

    public static String sanitize(String input){

        AtomicReference<String> output = new AtomicReference<>(input);

        SqlSecurity.blacklist.forEach((word)-> output.set(output.get().replaceAll(word,"")));

        return output.get();
    }
}
