package com.plantiq.plantiqserver.core;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Rule {

    protected HashMap<String, ArrayList<String>> rules = new HashMap<>();
    protected HashMap<String, Object> errors = new HashMap<>();

    private final Pattern pattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    protected abstract void setup();

    public boolean validate(HttpServletRequest request){

        this.setup();

        AtomicBoolean outcome = new AtomicBoolean(true);

        this.rules.forEach((key,rules)->{

            HashMap<String,String> ruleErrors = new HashMap<>();

            String provided;
            if(request.getParameterMap().containsKey(key)){

                provided = request.getParameter(key);
            }else{
                provided = "";
            }

            rules.forEach((rule)->{
                switch (rule) {
                    case "required" -> {
                        if (provided.isEmpty()) {
                            ruleErrors.put(rule,"Validation failed for key " + key);
                            outcome.set(false);
                        }
                    }
                    case "min:8" -> {
                        if (provided.length() < 8) {
                            ruleErrors.put(rule,"Validation failed for key " + key);
                            outcome.set(false);
                        }
                    }
                    case "max:50"->{
                        if(provided.length() > 50){
                            ruleErrors.put(rule,"Validation failed for key " + key);
                            outcome.set(false);
                        }
                    }
                    case "regex.email"->{
                        Matcher matcher = pattern.matcher(provided);
                        if(!matcher.matches()){
                            ruleErrors.put(rule,"Validation failed for key " + key);
                            outcome.set(false);
                        }
                    }
                }

                if(ruleErrors.size() > 0){
                    this.errors.put(key,ruleErrors);
                }
            });


        });


        return outcome.get();
    }

    public HashMap<String, Object> getErrors(){
        return this.errors;
    }

}
