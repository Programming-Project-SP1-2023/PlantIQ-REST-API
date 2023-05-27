[Back to documentation home](https://github.com/Programming-Project-SP1-2023/Backend-REST-API)

# Creating Queries
PlantIQ's ModelCollection is a class designed to simplify the creation of a query throughout the entire project. 
Apposite methods have been created for each SQL clause so that the correct SQL language is inserted into the final query.
## SQL Clauses available
- Where
- Order By
- Right Join
- Limit
- Offset
###Where Clause
The Where clause is used to match an compare the value of a record's attribute to a given value. To simplify the process of using the where clause, we subdivided this query in multiple methods.
Each of these methods will accept two parameters where the first one will be the name of the attribute, while the second string will represent the value we would like to compare it with.

- where(String attributeName, String value)

This method is used to check for a match of values. The logic SQL of the method is the following: 
```sql
WHERE attributeName = 'value'.
``` 
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
where("name","Mike")
```

- whereGreaterThan(String attributeName, String value)

This method is used to check if a value is greater than another. The SQL logic of the method is the following: 
```sql
    WHERE attributeName > value.
``` 
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
whereGreaterThan("humidity","3")
```
- whereGreaterOrEqualThan(String attributeName, String value)

This method is used to check if a value is greater or equal than another. The SQL logic of the method is the following:
```sql
    WHERE attributeName >= value.
``` 
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
whereGreaterOrEqualThan("temperature","15")
```
- whereLessThan(String attributeName, String value)

This method is used to check if a value is less than another. The SQL logic of the method is the following: 
```sql
    WHERE attributeName < value.
``` 
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
whereLessThan("temperature","18")
```
- whereLessOrEqualThan(String attributeName, String value)

This method is used to check if a value is less or equal than another. The SQL logic of the method is the following: 
```sql
    WHERE attributeName <= value.
``` 
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
whereLessOrEqualThan("humidity","21")
```
### Order By Clause
The Order By clause is used to order the records by one or multiple attributes. Each attribute can be sorted in ascending or descending order. The methods for this clause is:
- orderBy(String attributeName, Sort type)

This will accept two parameters, the first will be the name of the attribute we are sorting by, the second will be ordering type. 
The sorting type which is required as a parameter must have one of the two following structures: 

Sort.ASC or Sort.DESC

The SQL logic of the method is the following:
```sql
    ORDER BY attributeName type
``` 
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
orderBy("name",Sort.ASC)
```
### Right Join Clause
The Right Join Clause is used to query multiple tables at the same time. To do so the following method must be called:

- rightJoin(String tablename, String value1, String value2)

This method will accept three parameters which are respectively the name of the new table and the two attributes which must match from the new and an old table
The SQL logic of the method is the following:
 ```sql   
    RIGHT JOIN tablename2 ON value1 = value2
``` 
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
rightJoin("users","admins.ID","users.ID")
```
### Limit Clause
The Limit method is used to limit the records produced by the query. To set a limit call the subsequent method:

- limit(int LimitValue)

This method will accept and int as a parameter, which will be the limit you are willing to set.
The SQL logic of this method is the following:
```sql
    LIMIT limitvalue
```
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
limit("3")
```
### Offset
The Offset method is used to skip a number of record produced by the query. To use this functionality, the following method must be called:

- offset(int offsetValue)

This method will accept an int value which will be the number of record to skip. The SQL logic of this method is the following:
```sql
    OFFSET offsetValue
```
```java
//EXAMPLE
//In order to obtain any result, follow the 'How to Query the Database' section of the document
offset(5)
```
## How to Query the Database
The above methods are those used to develop the query. In order actually run a query, you can use one of the two following methods:

1) A method named 'get' which translates all the inputted data into SQL, queries the database and returns an ArrayList of Objects that match the given criteria.
2) A method named 'getFirst' which translates all the inputted data into SQL, queries the database and returns a single Object that matches the given criteria.

The first step to use one of these two functions, is to create an object of the type you are expecting to receive (which must extend the Model Class). This must be equal to the name of its class and call the 'collection' method.
This will ensure dictate the type of object the 'get' and 'getFirst' methods will produce.
Following you will be adding all the clauses you require, to finally end the line of code with the 'get' or 'getFirst' method.

```java
//EXAMPLE
//In this example, a user with the matching criteria will be returned from the database.
User user = User.collection().where("email", "email@example.com").where("password", "passwordExample").getFirst();
```

