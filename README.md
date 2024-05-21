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

All the files and informations are stored in `\data` directory.

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

`Main` call for executing every thing automatically.

`Validator` class validates rdf data in our code with a representative shape.

`UserPreferences` class can get the information from user, make and rdf file and upload them to the server. Also can be executed several times.
<br><br>![Screenshot 2024-01-10 210531.png](..%2F..%2FPictures%2FScreenshots%2FScreenshot%202024-01-10%20210531.png)
<center>This is the main structure of our classes.</center>

<br>![Screenshot 2024-01-10 224151.png](..%2F..%2FPictures%2FScreenshots%2FScreenshot%202024-01-10%20224151.png)
<center>This is the function in our classes.</center>

<br>![Screenshot 2024-01-10 224359.png](..%2F..%2FPictures%2FScreenshots%2FScreenshot%202024-01-10%20224359.png)
<center>Functions and methods in our Main and Collect code.</center>


<br>![Screenshot 2024-01-10 224645.png](..%2F..%2FPictures%2FScreenshots%2FScreenshot%202024-01-10%20224645.png)
<center>Memory insights from profiler about classes that we call.</center>
