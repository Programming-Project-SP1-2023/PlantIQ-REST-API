//**** PACKAGE ****\\
//Declares the specific package this class is part of
package com.plantiq.plantiqserver.core;

//**** PACKAGE IMPORTS ****\\
//Imports the required classes from other packages.
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//-----------------------------------------------------------------//
//                          RULE CLASS                             //
//-----------------------------------------------------------------//

//Our rule class holds all the core logic that we use to validate
//incoming http requests to the server. Child rules will be created
//for each validate case and will extend this class. In each of the
//children setup methods they will declare the specific rules that
//each expected parameter must follow.

public abstract class Rule {

    //Rules hashmap, its values are set in the setup method.
    protected HashMap<String, ArrayList<String>> rules = new HashMap<>();

    //Error hashmap, its values are set by the validation method
    //and returned via this.getErrors().
    protected HashMap<String, Object> errors = new HashMap<>();

    //Regex email pattern, this regex pattern is used to validate
    //email addresses.
    private final Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    //-----------------------------------------------------------------//
    //                  Setup Abstract Method                          //
    //-----------------------------------------------------------------//

    //This method is abstract and will be implemented by the children classes
    //we can think of this setup method like a constructor, it will be called
    //prior to the validation starting and will declare what rules will apply
    //to this specific validation case.
    protected abstract void setup();

    //-----------------------------------------------------------------//
    //                       Validate Method                           //
    //-----------------------------------------------------------------//

    //Our validation method will accept a request in as a parameter and will
    //validate the http parameters stored with in it against the rules that
    //are declared in the setup method of the child class.

    public boolean validate(HttpServletRequest request){

        //Firstly we call the abstract setup method to initialize all
        //the rules we have declared for this validation case.
        this.setup();

        //Next we create our outcome variable, this is atomic to allow
        //it to interact better with the nested forEach loops below.
        AtomicBoolean outcome = new AtomicBoolean(true);

        //Now we loop over all the rules that are declared in this
        //object.
        this.rules.forEach((key,rules)->{

            //Firstly for this key create a hashmap of errors that will
            //be returned in the event the validation fails.
            HashMap<String,String> ruleErrors = new HashMap<>();

            //Now we declare our provided variable, this will be the
            //expected value from the http request or will default to
            //an empty string if no parameter is provided.
            String provided;
            if(request.getParameterMap().containsKey(key)){
                provided = request.getParameter(key);
            }else{
                provided = "";
            }

            //Now we are ready to loop over all our rules and validate
            //them one by one.
            rules.forEach((rule)->{

                //If our rule has a variable then split it at the ":" character
                List<String> stringList = Pattern.compile(":")
                        .splitAsStream(rule).toList();

                //Next declare our variable string and set this to be an empty
                //string.
                String variable = "";

                //if our stringList size is greater than 1 that means a variable
                //is provided, and we will set the rule and variable to those values.
                if(stringList.size() > 1){
                    rule = stringList.get(0);
                    variable = stringList.get(1);
                }

                //Next we need to ensure that for any key that has a variable it is valid input.
                //if they are missing a variable but contain the ":", then
                if(rule.equals("min:")){
                    rule = "min";
                }

                if(rule.equals("max:")){
                    rule = "max";
                }

                //Now we have performed all our pre-checks and split our data into variable
                //and rule we can then switch between the rules and perform our checks.
                switch (rule) {

                    //If the rule is required, validate as follows and set the error if false.
                    case "required" -> {
                        if (provided.isBlank()) {
                            ruleErrors.put(rule,"Validation failed for key " + key);
                            outcome.set(false);
                        }
                    }

                    //If the rule is min, validate as follows and set the error if false.
                    //Note: for rules with variables this is where we validate their values.
                    case "min"-> {

                        if(variable.isBlank()){
                            System.out.println("[RULE] Cannot validate 'min:x', variable cannot be null");
                        }else{

                            if(!this.isInteger(variable)){
                                System.out.println("[RULE] Cannot validate min:'x', variable must be a valid integer");
                            }else {
                                if (provided.length() < Integer.parseInt(variable)) {
                                    ruleErrors.put(rule + ":" + variable, "Validation failed for key " + key + " parameter should be at least " + variable + " characters long");
                                    outcome.set(false);
                                }
                            }
                        }
                    }


                    //If the rule is max, validate as follows and set the error if false.
                    //Note: for rules with variables this is where we validate their values.
                    case "max"->{
                        if(variable.isBlank()){
                            System.out.println("[RULE] Cannot validate 'max:x', variable cannot be null");
                        }else{

                            if(!this.isInteger(variable)){
                                System.out.println("[RULE] Cannot validate max:'x', variable must be a valid integer");
                            }else {
                                if (provided.length() > Integer.parseInt(variable)) {
                                    ruleErrors.put(rule + ":" + variable, "Validation failed for key " + key + " parameter should be no more than " + variable + " characters long");
                                    outcome.set(false);
                                }
                            }
                        }
                    }

                    //If the rule is regex, validate as follows and set the error if false.
                    case "regex.email"->{
                        Matcher matcher = emailPattern.matcher(provided);
                        if(!matcher.matches()){
                            ruleErrors.put(rule,"Validation failed for key " + key);
                            outcome.set(false);
                        }
                    }
                }

                //Lastly before we return the outcome we check if this key has had
                //any errors, if no we do nothing, else if an error is present
                //we add the errors to the main this.errors hashmap for this key.
                if(ruleErrors.size() > 0){
                    this.errors.put(key,ruleErrors);
                }
            });
        });

        //Finally at the end of this process we return the outcome value
        //this will always be true unless a validation rule fails.
        return outcome.get();
    }

    //-----------------------------------------------------------------//
    //                      Get Errors Method                          //
    //-----------------------------------------------------------------//

    //Our get Errors method will return the hashmap of errors to the
    //controller, this will then be displayed as JSON and returned as a
    //result of the HTTP request.
    public HashMap<String, Object> getErrors(){
        return this.errors;
    }


    //-----------------------------------------------------------------//
    //                      is Integer Method                          //
    //-----------------------------------------------------------------//

    //This private helper method will accept a string value and attempt
    //to convert it to an integer, if this fails it will return false,
    //if this succeeds it will return true!
    private boolean isInteger(String value){

        boolean outcome = true;

        try{
            Integer.parseInt(value);
        }catch (Exception e){
            outcome = false;
        }

        return outcome;
    }

}
