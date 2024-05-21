package fr.emse.master;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.query.*;

import java.io.*;
import java.util.*;

/**
 * This program written by Erfan and Hadi
 */

public class Main {
    static Date programExecution = new Date();
    //copy below the full path of data an example below
    public static String path = "C:\\Users\\Nude\\IdeaProjects\\semanticweb_enferad_tahini\\";
    //public static String path = "C:\\Users\\Hadi\\Desktop\\semanticweb_enferad_tahini\\";

    public static String jsonLD = path + "data\\jsonld.json";
    public static String links = path + "data\\links.txt";
    public static String initialData = path + "data\\initial_data.json";
    public static String finalRDFTTL = path + "data\\finalRDF.ttl";
    public static String fuseki = path + "apache-jena-fuseki-4.10.0\\";
    public static String rdf = path + "data\\RDF.ttl";
    public static String userPref = path + "data\\userPref.ttl";

    public static String shapeSHACL = path + "data\\SHACL_Shape.ttl";
    public static String fusekiURL = "http://localhost:6969/Root/data";
    public static File file;
    //public static String  =path +"";

    public static void main(String[] args) throws Exception {
        System.out.println(programExecution);
        System.out.println("Welcome to the RDF application of businesses,\n" +
                "this code is written by Hadi and Erfan.\n");

        //get all the links and store them in links.txt
        file = new File(links);
        if (file.exists()) {
            System.out.println("Links of the businesses detected in database.");
        } else if (!file.exists()) {
            Extractor.linkExcavator();
        }

        //get json-ld for all businesses and store them inside jsonld file
        file = new File(jsonLD);
        if (file.exists()) {
            System.out.println("JSON-LD detected for the links.");
        } else if (!file.exists()) {
            Extractor.jsonLdExcavator();
        }

        // Check if RDF file already exist
        //Note We have two classes to create rdf.ttl. a normal one and
        //one we used Google places API (to get reviews and ratings of the place)
        // and Geocoding service to obtain the city, postal code and country
        file = new File(rdf);
        if (file.exists()) {
            System.out.println("An RDF representation of the websites is detected in the database.");
        } else {
            System.out.println("An RDF representation of the websites is not detected in the database, it is being created!");
            Collect rdfCollect = new Collect();
            rdfCollect.rdfCollector();
        }

        // This daemon runs on threads and keeps the fuseki server on
        // we couldn't integrate the Fuseki to our code (Had days of debugging)
        // Instead we execute it with Process builder
        FusekiServerThread thread = new FusekiServerThread();
        thread.setDaemon(true); // Set thread to daemon
        thread.start();

        Thread.sleep(2000);

        // Here we upload the data in turtle rdf format to the server
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPut request = new HttpPut("http://localhost:6969/Root/data");
            FileEntity entity = new FileEntity(new File(rdf), ContentType.create("text/turtle"));
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                System.out.println("RDF turtle uploaded successfully");
            } else {
                System.out.println("Error uploading data: " + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Thread waiter = new Thread(new Help());

        //this class just prevents the code from exiting.
        waiter.start();


        queryByTime("Tuesday", "09:00", "13:00");

        queryByPrice(30.00);
        queryByLocation(48.6901, 6.17216);
        queryByClosest(3.345213, 48.193222);
        queryByRank("price", 3.345213, 48.193222);
        restaurantReview("THAUN_KROUN");
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the our service");


        while (true) {
            System.out.println("Select restaurant according to your preference: open-time | close-time | price | zone | closest | rank | check-review | city-restaurant");
            String pref = input.nextLine();
            if (pref.equals("open-time")) {
                System.out.println("Enter day");
                String day = input.nextLine();
                System.out.println("Enter open-time");
                String openTime = input.nextLine();
                queryByOpenTime(day, openTime);
            }
            if (pref.equals("close-time")) {
                System.out.println("Enter day");
                String day = input.nextLine();
                System.out.println("Enter close-time");
                String closeTime = input.nextLine();
                queryByCloseTime(day, closeTime);
            }
            if (pref.equals("price")) {
                System.out.println("Enter your desired price");
                double price = input.nextDouble();
                queryByPrice(price);
            }
            if (pref.equals("closest")) {
                System.out.println("Enter your logitude");
                double lon = input.nextDouble();
                System.out.println("Enter your latitude");
                double lat = input.nextDouble();
                queryByClosest(lon, lat);
            }
            if (pref.equals("zone")) {
                System.out.println("Enter your logitude");
                double lon = input.nextDouble();
                System.out.println("Enter your latitude");
                double lat = input.nextDouble();
                queryByLocation(lon, lat);
            }
            if (pref.equals("rank")) {
                System.out.println("Enter a rank criteria distance|price");
                String criteria = input.nextLine();
                if (criteria.equals("price")) {
                    queryByRank(criteria, 0, 0);

                } else {
                    System.out.println("Enter your logitude");
                    double lon = input.nextDouble();
                    System.out.println("Enter your latitude");
                    double lat = input.nextDouble();
                    queryByRank(criteria, lon, lat);
                }
            }
            if (pref.equals("check-review")) {
                System.out.println("Enter the restaurant name");
                String restaurantName = input.nextLine();
                restaurantReview(restaurantName);
            }
            if (pref.equals("city-restaurant")) {
                System.out.println("Enter the city name");
                String cityName = input.nextLine();
                restaurantsInCity(cityName);
            }
        }
    }

    public static void queryByTime(String dayOfWeek, String openTime, String closeTime) {
        String queryString = "PREFIX ex: <http://schema.org/>" +
                "SELECT ?name " +
                "WHERE {" +
                "    ?restaurant ex:name ?name ;" +
                "                ex:openingHours ?openingHours ." +
                "    ?openingHours ex:dayOfWeek ?dayOfWeek ;" +
                "                  ex:opens ?opens ;" +
                "                  ex:closes ?closes ." +
                "    FILTER (CONTAINS(str(?dayOfWeek), \'" + dayOfWeek + "\'))" +
                "    FILTER (CONTAINS(str(?opens), \'" + openTime + "\'))" +
                "    FILTER (CONTAINS(str(?closes), \'" + closeTime + "\'))" +
                "}";
        processResults(queryString);

    }

    public static void queryByOpenTime(String day, String openTime) {
        String queryString = "PREFIX ex: <http://schema.org/>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "SELECT ?name " +
                "WHERE {" +
                "    ?restaurant ex:name ?name ;" +
                "                ex:openingHours ?openingHours ." +
                "    ?openingHours ex:dayOfWeek ?dayOfWeek ;" +
                "                  ex:opens ?opens ." +
                "    FILTER (CONTAINS(str(?dayOfWeek), '" + day + "'))" +
                "    FILTER (CONTAINS(str(?opens), '" + openTime + "'))" +
                "}";

        processResults(queryString);
    }

    public static void queryByCloseTime(String day, String closeTime) {

        String queryString = "PREFIX ex: <http://schema.org/>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "SELECT ?name " +
                "WHERE {" +
                "    ?restaurant ex:name ?name ;" +
                "                ex:openingHours ?openingHours ." +
                "    ?openingHours ex:dayOfWeek ?dayOfWeek ;" +
                "                  ex:closes ?closes ." +
                "    FILTER (CONTAINS(str(?dayOfWeek), '" + day + "'))" +
                "    FILTER (CONTAINS(str(?closes), '" + closeTime + "'))" +
                "}";

        processResults(queryString);
    }

    public static void queryByLocation(double longitude, double latitude) {
        String queryString = "PREFIX ex: <http://schema.org/>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "PREFIX bif: <bif:>" +
                "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
                "SELECT ?restaurant ?name ?lat ?long " +
                "WHERE {" +
                "    ?restaurant ex:name ?name ;" +
                "                ex:address ?address ." +
                "    ?address ex:latitude ?lat ;" +
                "             ex:longitude ?long ." +
                "}" +
                "ORDER BY bif:st_distance(bif:st_point(xsd:float(?long), xsd:float(?lat)), bif:st_point(xsd:float(" + longitude + "), xsd:float(" + latitude + ")))" +
                "LIMIT 3";

        processResults(queryString);
    }

    public static void queryByClosest(double lon, double lat) {
        String queryString = "PREFIX ex: <http://schema.org/>" +
                "PREFIX bif: <bif:>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "SELECT ?restaurant ?name ?lat ?long ?distance " +
                "WHERE {" +
                "    {" +
                "        SELECT ?restaurant ?name ?lat ?long (bif:st_distance(bif:st_point(xsd:float(?long), xsd:float(?lat)), bif:st_point(" + lon + ", " + lat + ")) as ?distance)" +
                "        WHERE {" +
                "            ?restaurant ex:name ?name ;" +
                "                        ex:address ?address ." +
                "            ?address ex:latitude ?lat ;" +
                "                     ex:longitude ?long ." +
                "        }" +
                "        ORDER BY ?distance" +
                "    }" +
                "}";
        Query query = QueryFactory.create(queryString);
        String serviceUri = "http://localhost:6969/Root/sparql";

        try (QueryExecution qe = QueryExecutionFactory.sparqlService(serviceUri, query)) {
            ResultSet results = qe.execSelect();
            if (results.hasNext()) {
                QuerySolution qs = results.nextSolution();
                String name = qs.get("name").toString();
                double resultLat = Double.parseDouble(qs.get("lat").toString());
                double resultLon = Double.parseDouble(qs.get("long").toString());

                // Calculate the distance
                double distance = calculateDistance(lat, lon, resultLat, resultLon);

                System.out.println("Closest Restaurant: " + name + ", Distance: " + distance + " km");
            } else {
                System.out.println("No restaurants found.");
            }
        }

    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // convert to km
    }

    public static void queryByPrice(double price) {
        // Construct the SPARQL query based on the preferences
        String queryString = "PREFIX ex: <http://schema.org/>" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                "SELECT ?restaurant ?name ?price " +
                "WHERE {" +
                "    ?restaurant ex:name ?name ;" +
                "                ex:DeliveryChargeSpecification ?DeliveryChargeSpecification ." +
                "    ?DeliveryChargeSpecification ex:priceSpecification ?price ." +
                "    FILTER (xsd:decimal(?price) < " + price + ")" +
                "}";

        processResults(queryString);
    }

    public static void queryByRank(String rankCriteria, double lon, double lat) {
        String query = "PREFIX ex: <http://schema.org/>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX bif: <bif:>\n" +
                "SELECT ?name ?lat ?long ?price\n" +
                "WHERE {\n" +
                "    ?restaurant ex:name ?name ;\n" +
                "                ex:address ?address ;\n" +
                "                ex:DeliveryChargeSpecification ?DeliveryChargeSpecification .\n" +
                "    ?address ex:latitude ?lat ;\n" +
                "             ex:longitude ?long .\n" +
                "    ?DeliveryChargeSpecification ex:priceSpecification ?price .\n" +
                "    FILTER (xsd:decimal(?price) < 50)\n" +
                "}\n";

        if (rankCriteria.equals("distance")) {
            query += "ORDER BY bif:st_distance(bif:st_point(xsd:double(?long), xsd:double(?lat)), bif:st_point(" + lon + ", " + lat + "))\n";
        } else if (rankCriteria.equals("price")) {
            query += "ORDER BY xsd:decimal(?price)\n";
        }

        query += "LIMIT 20";

        processResults(query);
    }

    public static void restaurantReview(String restaurantName) {
        String restaurantURI = "<http://schema.org/" + restaurantName + ">";
        String query = String.format(
                "PREFIX schema: <http://schema.org/>\n" +
                        "SELECT ?author ?reviewBody ?reviewRating\n" +
                        "WHERE {\n" +
                        "  %s schema:Review ?reviewNode .\n" +
                        "  ?reviewNode schema:author ?author ;\n" +
                        "              schema:reviewBody ?reviewBody ;\n" +
                        "              schema:reviewRating ?reviewRating .\n" +
                        "}",
                restaurantURI
        );
        processResults(query);

    }

    public static void restaurantsInCity(String cityName) {
        String query = "PREFIX ex: <http://schema.org/>\n" +
                "SELECT ?name\n" +
                "WHERE {\n" +
                "    ?restaurant ex:name ?name ;\n" +
                "                ex:address ?address .\n" +
                "    ?address ex:City '" + cityName + "' .\n" +
                "}";
        processResults(query);

    }

    public static void processResults(String queryString) {
        // Send the query to the Fuseki server
        String serviceURI = "http://localhost:6969/Root/sparql";
        Query q = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceURI, queryString)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, q);
        }


    }
}
