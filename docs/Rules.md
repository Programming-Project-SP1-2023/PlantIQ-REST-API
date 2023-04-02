[Back to documentation home](https://github.com/Programming-Project-SP1-2023/Backend-REST-API)

# Creating Rules & Validating Requests
PlantIQ's rule system is designed to be simply and easy to use, all incoming requests should be
validated and have a matching rule object. For example a login request should have its endpoint 
and a LoginRequestRule object that declares what criteria the login request should contain to be 
a valid server request.

##  Creating a new request rule
To create a new rule simply navigate to the rules package and create a new class called "MyRequestRule",
make sure to have this class extent the core Rule parent class, it will then ask you to implement the unimplemented
methods, click yes.

You should now be greeted with a class that looks like this:
````java

package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

public class MyRequestRule extends Rule {
    @Override
    protected void setup() {
        //add code here!
    }
}

````

### Setting up the class


Lets for an example state that this rule will validate a login request and thus should contain both the email and
password of a user. We would add them by creating a ArrayList of Strings for each of them. Then next add them to the main
rules array by calling "this.rules.put('email',email)".

It's important to make sure that the key you give the rules when adding it to the "this.rules" matches the parameter name
that you will be sending to the server!

When adding rules we can stack as many present as we want, we are not limited to using one rule per parameter or input.

````java

package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class MyRequestRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> email = new ArrayList<>();
        
        
        ArrayList<String> password = new ArrayList<>();
        
        
        this.rules.put("email",email);
        this.rules.put("password",password);
    }
}

````

### Adding email rules

Now we have set up our rule class we can start to add rules to each of our parameters, lets say for the email it must be
required and that it must match the regex for an email, we would then add the keys of "required" and "regex:email"

````java

package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class MyRequestRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> email = new ArrayList<>();
        
        email.add("required");
        email.add("regex:email");
        
        ArrayList<String> password = new ArrayList<>();
        
        
        this.rules.put("email",email);
        this.rules.put("password",password);
    }
}

````
### Adding password rules

We might then state that our password should be no less than 8 characters long, in addition we know that we are storing
it as a varchar(50) and so its maximum would be 50 characters. We can then add these using the "min:x" and "max:x" rules.

````java

package com.plantiq.plantiqserver.rules;

import com.plantiq.plantiqserver.core.Rule;

import java.util.ArrayList;

public class MyRequestRule extends Rule {
    @Override
    protected void setup() {

        ArrayList<String> email = new ArrayList<>();
        
        email.add("required");
        email.add("regex:email");
        
        ArrayList<String> password = new ArrayList<>();
        
        password.add("min:8");
        password.add("max:50");
        
        this.rules.put("email",email);
        this.rules.put("password",password);
    }
}

````

Both our "min:x" and "max:x" rules can accept any integer as the second parameter. Now you have your first rule set up the next
section will cover all the rule key types you can use to validate requests!

### Using this new rule

To use this new rule inside a declared route we can simply create an instance of the object and then pass the validate
method the http request. If this fails we can show the errors else we proceed.

````java
    @PostMapping("/test")
    public HashMap<String,Object> test(HttpServletRequest request) {

        //Create our response object, this is returned as JSON.
        HashMap<String, Object> response = new HashMap<>();

        //Create our rule
        MyRequestRule rule = new MyRequestRule();

        //Validate our request and return the errors if present.
        if(!rule.validate(request)){
            response.put("outcome",false);
            response.put("errors",rule.getErrors());
            return response;
        }
        
        
        //else if we get to hear and dont return then its valid!
        
        return response;
    }
````

## Rule key's

There are a number of rule keys that you can use to validate your requests.

### 1) min:x
Our minimum rule is denoted by "min:x" where x represents any integer value that you wish for the minimum characters to be.

Example:
````java
parameter.add("min:16");
````

### 2) max:x
The maximum rule works extremely similar to its minimum counterpart except it will denote the maximum amount of characters
a parameter can have to be considered valid. Maximum can also accept any valid integer value as its variable.

Example:
````java
parameter.add("max:72");
````

### 3) required
The required rule is really simple, it's the perfect use case for if you want to ensure a parameter is present but do
not care about what it contains! Required will simply ensure that something is present, and it's not null or a blank string.

Example:
````java
parameter.add("required");
````

### 4) regex:x
The regex key will allow you to validate agenst any number of regex strings, at this stage the only one present is
email, this can be called with "regex:email" and will validate that the input follows this format "test@example.com".


Example:
````java
parameter.add("regex.email");
````

### 5) unique:Table.column
The unique rule will need to accompanied by both a table name and column name, it will then use the database to ensure
that the data provided is unique and is not present in any other rows in that column. As an example we can use this to
validate email address as they should be unique to each user.

Example:
````java
parameter.add("unique:User.email");
````

The unique rule also allows for a not version to check for a value that is present, we can simply add a "!" to the
front of the string, an example may be checking if a token exists in the sessions table. This would result in a fail of
the validation if the provided data does not match one that is present.

Example:
````java
parameter.add("!unique:Session.token");
````

### 6) integer
This simple rule will validate that the data provided is that of or is compatible with the integer data type.

Example:
````java
parameter.add("intger");
````

### 7) float
Lastly our float rule will validate that the data provided is that of or is compatible with the float data type.

Example:
````java
parameter.add("float");
````