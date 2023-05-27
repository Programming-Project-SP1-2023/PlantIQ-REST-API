#Using Models
PlantIQ's Model class has been designed to easily manage all the data present in the database. 
Each class of the model package will extend the Model class gaining access to the features to retrieve,
insert, modify, and remove record of data from the database. 


##How to insert data into the database
In order to insert data in the database, the method insert() must be called. This method requires two parameters,
that are respectively the table name and the data that is going to be inserted. The table name is passed as a simple string,
while the data that is going to be inserted is passed as a HashMap, where each key will correspond to an attribute in the database and each corresponding value
will be the attribute's value.

```java
//Example of HashMap
HashMap<String, Object> data = new HashMap<>();
data.put("firstname", "Alex");
data.put("surname", "Vorraro");
```
In order to call the insertion method, you will need to reference the type of model you are trying to insert into the database 
and call its parent insertion method, passing the correct parameters as displayed in the example below.
```java
User.insert("User",data);
```

The insertion method has also a built-in error check, making it return a true boolean if the insertion worked, while false if no row 
within the database was added. An example of how to use the error check correctly is displayed below.

```java
if (User.insert("User", data)) {
    //Logic for successful insertion
        }else{
        //Logic for unsuccessful insertion
        }
```

##How to update data into the database
Updating a record is similar to inserting a record into the database, however there are a few differences between the two. The update() method
will require a hashmap as a parameter with the same format as the previous method, but it will not require the table name. The child model object 
will need to be created to call this method, which will automatically set the table name.

```java
//Example
User user = User.collection().where("id",id).getFirst();

HashMap<String, Object> data = new HashMap<>();
data.put("firstname", "Alex");
data.put("surname", "Vorraro");

user.update(data)
```

Just like the insertion method, an automatic error check is built-in, making it return a true boolean if the update worked, while false if no row
within the database was modified.

##How to delete data from the database
PlantIQ's Model class offers three ways to delete records from a table of the database. It is possible to:
1. Delete by ID
2. Delete by an Attribute
3. Delete all records

###Delete by ID
The delete() method will delete a single record from any table that has the attribute 'id' as primary key. To delete the record, the object that is going to be deleted must be retrieved from the database, and the delete method must be called through it.
```java
//Example
User user = User.collection().where("id",id).getFirst();
user.delete();
```
Built-in error check is present, making the method return a true boolean if the record was deleted, while a false if no record was affected by the query.
###Delete by an Attribute

Some tables do not not have an attribute named exactly 'id'. In this case, it is possible to delete the record by passing an attribute as the parameter. 
The procedure will consist of creating the object of the record that is going to be deleted and call delete(String attribute) method through it, passing it the name of the primary attribute.

```java
//Example
Session session = Session.collection().where("token", token).orderBy("token").getFirst();
session.delete("token");
```

###Delete All
The third deletion option is used to delete all records that have the same value for a particular attribute. The method will take in three string as parameters
that are respectively the table name, the attribute name and the value name. Since the method is static, there is no need to create a reference to the object
we are going to delete, but we can call the method through the class, passing it the three parameters as displayed below.
```java
//Example
PlantData.deleteAll("PlantData", "smarthub_id", id);
```
This method has the built-in error check, making it return a true boolean if the record of the database have been affected by the query, while false if no row
was affected.