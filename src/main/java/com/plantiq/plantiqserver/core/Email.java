package com.plantiq.plantiqserver.core;

import com.aspose.email.MailAddress;
import com.aspose.email.MailMessage;
import com.aspose.email.SecurityOptions;
import com.aspose.email.SmtpClient;
import com.plantiq.plantiqserver.PlantIqServerApplication;
import com.plantiq.plantiqserver.model.User;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class Email {

    private static final HashMap<String, String> templates = new HashMap<>();

    private User user;
    private String subject;

    private MailMessage message;

    private String template;

    private HashMap<String,Object> variables;

    public Email(){
        this.variables = new HashMap<>();
        this.message = new MailMessage();
        this.message.setFrom(new MailAddress("help-plantiq@outlook.com", "PlantIQ Help", false));
    }

    public Email setUser(User user){
        this.user = user;
        this.message.getTo().addItem(new MailAddress(user.getEmail(), user.getFirstname()+" "+user.getSurname(), false));
        return this;
    }

    public Email setSubject(String subject){
        this.message.setSubject(subject);
        return this;
    }

    public Email setVariables(HashMap<String, Object> variables){
        this.variables = variables;
        return this;
    }

    public Email setHtmlTemplate(String template){

        if(!Email.templates.containsKey(template)){
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(PlantIqServerApplication.class.getResourceAsStream(template)));

                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                Email.templates.put(template,sb.toString());
                this.message.setHtmlBody(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            this.message.setHtmlBody(Email.templates.get(template));
        }

        return this;
    }

    public void send(){

        Thread t1 = new Thread(() -> {
            this.message.setHtmlBody(this.message.getHtmlBody().replace("{{user.fullname}}",this.user.getFirstname()+" "+this.user.getSurname()));
            this.message.setHtmlBody(this.message.getHtmlBody().replace("{{user.email}}",this.user.getEmail()));

            if(this.variables != null) {
                this.variables.forEach((key, value) -> {
                    if (this.message.getHtmlBody().contains("{{" + key + "}}")) {
                        this.message.setHtmlBody(this.message.getHtmlBody().replace("{{" + key + "}}", value.toString()));
                    }
                });
            }

            // Create an instance of SmtpClient Class
            SmtpClient client = new SmtpClient();

            // Specify your mailing host server, Username, Password, Port
            client.setHost("smtp-mail.outlook.com");
            client.setUsername("help-plantiq@outlook.com");
            client.setPassword(PlantIqServerApplication.emailPassword);
            client.setPort(587);
            client.setSecurityOptions(SecurityOptions.Auto);

            try
            {
                // Client.Send will send this message
                client.beginSend(this.message);
            }

            catch (Exception ex)
            {
                ex.printStackTrace();
            }

        });
        t1.start();
        System.out.println("[Email] Sent "+this.message.getSubject()+" email to "+this.user.getEmail());
    }





}
