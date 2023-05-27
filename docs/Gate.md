#How to manage user access to the webapp
PlantIQ's Gate class is designed to manage the permissions of the users using our webapp. Most functionalities simply require
logged-in users, while others require the user to be an admin. The Gate class will check if the requirements are met, allowing the 
user to continue their navigation or to block their access. 

The class has a secondary feature accomplished through the method getCurrentUser(), which returns the User object of the currently 
logged-in user.  


##Authorizing Users and Admins
Each time a user logs in to the webapp, a session token will be generated. This token allows the user to navigate through the various
functionalities offered by PlantIQ. This token will be used to grant access for both, users and admins.
###Authorizing Users
To check is a user is currently authorized to use one of the PlantIQ's features, the method authorized(), which requires the
HttpServletRequest as the parameter, should be called. This method will check if the user has an active account and if the session
is valid. If these two requirements are met, a true boolean will be returned by the method, while a false outcome will be returned if one 
or both requirements are not met. 

```java
Gate.authorized(request);
```
Whilst this method checks if the user currently has or does not have the permission to use various functionalities, it does not stop the
user from using PlantIQ. The method abortUnauthorized() is the method that will return a 401 http status code, a false outcome and an error
message stating "You are not authorized to access the requested resource" when the user in navigating in unauthorized ground.

```java
Gate.abortUnauthorized();
```

The two method displayed above are used together to allow the user to continue their navigation if their account is active and their session
is valid or stop through an error if the user does not have permission to proceed. 

```java
if (!Gate.authorized(request)) {
    return Gate.abortUnauthorized();
}
//Code for authorized users..
```

Note: Admins are part of the users, so this check will work for them as well.

###Authorizing Admins
If a functionality is allowed only to admins it is possible to call the method authorizedAsAdmin(). This method works exactly like authorized() with
only difference that there is a further check to ensure that the current logged-in user has an admin account. 
```java
if(!Gate.authorizedAsAdmin(request)){
    return Gate.abortUnauthorized();
}
//Code for authorized admins..
```
##Getting the current User
PlantIQ's Gate class has a secondary feature that allows the retrieval of the currently logged-in user. 
The method used to retrieve the User object is getCurrentuser().
```java
User user = Gate.getCurrentUser();
```
