#How to create a Session
PlantIQ's SessionService is a class designed to create user sessions. The user will be able to navigate through the webapp 
for 14 days with the same session token, before they will need to login again. Each time the user logs in, the SessionService's 
method create() is called. This method, which requires the user_id to be passed as a parameter, will generate the session token and save the token, the user's id 
and the expiry date into the database. At the same time it will delete all previous tokens for this user.

To use the SessionService functionality, the class must be imported as following:
```java
import com.plantiq.plantiqserver.service.SessionService;
```

A token is made up from hashing the user_id and the current timestamp, so that each token is always different. Since the method is
static there is no need to create the SessionService object to call the create() method. 

```java
SessionService.create(user_id)
```
This method will return the hashed token so that it can be used by the calling controller.

```java
//Example of token
b8197eb9473c2e85166cbabebe535048ff2ba4a9
```