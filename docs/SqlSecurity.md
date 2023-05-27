[Back to documentation home](https://github.com/Programming-Project-SP1-2023/Backend-REST-API)

#Sanitizing User's input
Users with malicious intent may try to inject SQL queries to our database when posting data. To avoid this severe problem, PlantIQ's SqlSecurity class has been developed with the scope of sanitizing user's input.

To use the SqlSecurity functionality, the class must be imported as following:
```java
import com.plantiq.plantiqserver.secuirty.SqlSecurity;
```

A blacklist containing all the malicious words that must not be present within the user's answer has been implemented within this class, and is used
to search for possible threats. The blacklist is the following:
- RIGHT
- JOIN
- SELECT
- FROM
- DROP
- DELETE
- INNER JOIN
- TABLE
- UNION
- WHERE
- '
- \"
- =
- \\\\*

##How to add words to the blacklist
Within the SqlSecurity class, there is a method named initialize, which will run as soon as the application is booted. To add a banned word to the existing list, at the end of the current list a new line of code stating blacklist.add("*BANNEDWORD*) must be added.
```java
    //Example
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
        blacklist.add("*NEW BANNED WORD*");
}
```
###How to sanitize user's input
The method sanitize(String input)  is used to sanitize user's input. The method will search within the string input if there are any of the blacklisted words and remove them, to then return the string without any threats.
The method is static, so the various classes can use this functionality without the creation of the SqlSecurity Object.
```java
//EXAMPLE USAGE
String SanitizedInput = SqlSecurity.sanitize(input)
```

```java
//SANITIZATION EXAMPLE

//INPUT
DELETE PASSWORD FROM USER
//SANITIZED RESULT
PASSWORD USER
```