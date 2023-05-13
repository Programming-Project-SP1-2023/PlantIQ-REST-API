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

//-----------------------------------------------------------------//
//                         EMAIL CLASS                             //
//-----------------------------------------------------------------//

//The email class is used to send emails to the users of PlantIQ.
//It grabs the correct structure of the emails by the templates located
//into the resources folder and sends them depending on the situation.

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

    //This method is used to detect the user that will be receiving the email.
    // This will allow the service to have access to their details such as
    //name and email.
    public Email setUser(User user){
        this.user = user;
        //Giving the email the necessary information about the receiver.
        this.message.getTo().addItem(new MailAddress(user.getEmail(), user.getFirstname()+" "+user.getSurname(), false));
        return this;
    }
    //This method is used to set the subject of the email.
    public Email setSubject(String subject){
        this.message.setSubject(subject);
        return this;
    }

    //This method is used to store the necessary variables
    //that need to be included into the email.
    public Email setVariables(HashMap<String, Object> variables){
        this.variables = variables;
        return this;
    }
    //This method is used to pick the correct template that will
    //structure the email.
    public Email setHtmlTemplate(String template){
        // Check if the template is not already in the templates map.
        if(!Email.templates.containsKey(template)){
            try {
                // Get the resource file associated with the template
                // and read its contents.
                InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(PlantIqServerApplication.class.getResourceAsStream(template)));
                //Creation of bufferedReader and StringBuilder to read adn
                // store the content of the file.
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String str;
                //Read each line of the file and append it to the StringBuilder
                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                //Store the template in the hashmap.
                Email.templates.put(template,sb.toString());
                //Set the HTML body of the email message to the contents of the file.
                this.message.setHtmlBody(sb.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            //If the template is already in the templates map, set the HTML body of the email message to its contents
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
