[Back to documentation home](https://github.com/Programming-Project-SP1-2023/Backend-REST-API)

# Sending Emails
PlantIQ's Email is class designed to generate and send email to our users. In order to successfully send an email, a set of steps must be followed.
0. Import Class
1. Create Email Object
2. Set User
3. Set Subject
4. Set Template
5. Set Variables
6. Send

###0. Import Class
To use the Email class within another class, the Email class must be imported as displayed below.
```java
import com.plantiq.plantiqserver.core.Email;
```
###1. Create Email Object
The first step to send an email is the creation of the Email object within the method of the class that will 
be sending out emails.
This will guarantee access to all its functionalities. Example of how to create the Email object is shown below.

```java
//Within method
Email email = new Email();
```


###2. Set User
After the creation of the Email object, various aspects of the email need to be set. In this step we will be setting the receiver of the email.
The first step to set our receiver is to retrieve their object from the database.
```java
//For further understanding of how to retrieve an object from the database, consult ModelCollection.md
User user = User.collection().where("id", "exampleID").getFirst();
```
This object will contain necessary information for the user, such as their email address, name and surname.
Once the user object is created, Email's method setUser() must be called and the User object must be passed as the parameter.


```java
email.setUser(user));
```

###3. Set Subject
Emails are made of a subject and a message. The message will be set through the email template, while the subject is set by calling the 
method setSubject() and passing a string containing the email's title as a parameter.

```java
email.setSubject("Email Subject!");
```

###4. Set Template
The content of the email sent by PlantIQ'S Email class will depend on the chosen template. 
The available emails can be found within the 'emails' folder situated in the 'resources' folder. Setting a html template for 
the email is possible by calling the method setHtmlTemplate and passing the file path of the template as the parameter.

```java
email.setHtmlTemplate("/emails/outOfRangeNotification.html");
```

#### How to create a new HTML Template
To create a new template, a new 
html file will need to be created in /resources/email. The path of the new html file will then need to be passed as a parameter when
setHtmlTemplate() is called. To properly understand how to use variables in your new template, read section 5.
###5. Set Variables
The variables of an email will contain all the words that will differ among users. Some examples may be the user's name, the value out of range or the SmartHomeHub id.
To pass variables to a template, you will need to create a HashMap<String, Object> in the method that will be sending the email. The string keys of the HashMap
will become the name of the variables and will be referenced by typing {{keyName}}, while the Object values will become the value's of the corresponding keys.

The variables of users are set during step 2 and are accessible through {{user.variableName}}
```java
//EXAMPLE OF HASHMAP
HashMap<String, Object> data = new HashMap<>();
data.put("id", "123456";
data.put("name", "ExampleName");

//How to reference name, id in HtmlTemplate and user's email
 {{id}}
 {{name}}
 {{user.email}}
```

###6. Send
The last step to successfully send an email is to invoke the method send(). 
This method will compile the email and send it to the receiver.
The send() method must be the last method called in order to not encounter any issues.

```java
email.send();
```

```java
//Complete Example
Email email = new Email();
email.setUser(user);
email.setSubject("Plant reports its out of range!");
email.setVariables(errors);
email.setHtmlTemplate("/emails/outOfRangeNotification.html");
email.send();
```