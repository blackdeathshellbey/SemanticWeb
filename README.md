# SemanticWeb

This project created and written by Erfan and Hadi.

To execute just use the main class. It is required to copy and pasted the definitive location of the project to below parameter:<br>

```
public static String path = "C:\\Path\\to\\the\\project";
```
This is a Java code.

```
Java Version 17
```

Fuseki server lunches automatically on memory and the rdf.ttl automatically uploads to server. 

We have two APIs in our project, `Google Place API` for getting the reviews of the places, and `OpenCage geocoding API` for obtaining more information regarding the addresses.

All the files and information are stored in `\data` directory.

# Data Extraction and RDF Conversion Process

Initially, we extracted all the URLs from a JSON file loaded in the Coopcycle. Subsequently, from each page, we extracted the embedded JSON-LD data and converted it into RDF triples.

# Program Queries

Our program includes multiple queries:

1. *Time-based Query:* Retrieve restaurants that open or close at a specific time.

2. *Delivery Price Query:* Retrieve all restaurants under a specific delivery price.

3. *Zone-based Query:* Retrieve restaurants in a specific zone.

4. *Closest Restaurant Query:* Retrieve the closest restaurant to a specific location.

5. *Ranking Query:* Rank restaurants based on price or distance.

6. *Review Query:* Retrieve reviews for the restaurants.

7. *City-based Query:* Retrieve all restaurants in a specific city.

`Main` call for executing everything automatically.

`Validator` class validates rdf data in our code with a representative shape.

`UserPreferences` class can get the information from user, make and rdf file and upload them to the server. Also can be executed several times.
<br><br>![Screenshot 2024-01-10 210531](https://github.com/blackdeathshellbey/SemanticWeb/assets/17174907/e196f352-71bc-45bb-986c-7eacc5d12cb9)
<center>This is the main structure of our classes.</center>


<br>![Screenshot 2024-01-10 224151](https://github.com/blackdeathshellbey/SemanticWeb/assets/17174907/0d464678-e4a0-4666-a1d2-3ece57c31247)
<center>This is the function in our classes.</center>

<br>![Screenshot 2024-01-10 224359](https://github.com/blackdeathshellbey/SemanticWeb/assets/17174907/553b46d1-99ce-4756-83b0-0508d7bad262)
<center>Functions and methods in our Main and Collect code.</center>


<br>![Screenshot 2024-01-10 224645](https://github.com/blackdeathshellbey/SemanticWeb/assets/17174907/c2a7ff77-693a-4440-8210-2f4ceb31c2db)
<center>Memory insights from profiler about classes that we call.</center>
