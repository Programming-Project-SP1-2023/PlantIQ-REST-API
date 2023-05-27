[Back to documentation home](https://github.com/Programming-Project-SP1-2023/Backend-REST-API)

# Genereating Hashes
PlantIQ's HashService is a class designed to produce SHA1 hashes. The class contains one single method which takes in as a parameter a string of data. Within the method, this string of data is hashed and finally returned.
Since the method is static, it is possible for each of the classes using the hashing functionality to call the method without the need of creating the HashService object.

To use the HashService functionality, the class must be imported as following:
```java
import com.plantiq.plantiqserver.service.HashService;
```
```java
//EXAMPLE USAGE
HashService.generateSHA1("example")
//EXAMPLE RESULT
bed5451ada0d4ebbcf8a92ae495bb08b5a8e5539
```

