package fr.emse.master;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFLanguages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class creates user preference rdf called userPerf.ttl
 */
public class UserPreferences {

    public static void main(String[] args) {
        Random random = new Random();
        int userId = random.nextInt(Integer.MAX_VALUE - 1) + 1;

        Scanner scanner = new Scanner(System.in);
        System.out.println("User name?");
        String userName = scanner.next();
        userName = userName.toLowerCase();
        System.out.print("Where do you live? Enter your longitude: ");
        double longitude = scanner.nextDouble();
        System.out.print("Enter your latitude: ");
        double latitude = scanner.nextDouble();
        scanner.nextLine();

        String favoriteCity;
        do {
            System.out.print("What is your favorite city? ");
            favoriteCity = scanner.nextLine().trim();
            if (hasIllegalCharacters(favoriteCity)) {
                System.out.println("Error: Illegal characters detected. Please enter a valid city name.");
            }
        } while (hasIllegalCharacters(favoriteCity));
        favoriteCity = favoriteCity.replace(" ", "_");

        Model model = ModelFactory.createDefaultModel();
        String userBaseURI = "http://schema.org/" + userName;
        String userURI = userBaseURI + userId;

        Resource userResource = model.createResource(userURI);
        userResource.addProperty(model.createProperty("http://schema.org/longitude"), String.valueOf(longitude));
        userResource.addProperty(model.createProperty("http://schema.org/latitude"), String.valueOf(latitude));
        userResource.addProperty(model.createProperty("http://schema.org/City"), favoriteCity);
        try (FileWriter output = new FileWriter(Main.userPref)) {
            model.write(output, RDFLanguages.TTL.getName());
            System.out.println("User preferences RDF graph has been written");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        Main.file = new File(Main.userPref);
        if (Main.file.exists()) {
            try {
                HttpPut request = new HttpPut("http://localhost:6969/Root/data");
                FileEntity entity = new FileEntity(new File(Main.userPref), ContentType.create("text/turtle"));
                request.setEntity(entity);

                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    System.out.println("RDF preferences uploaded successfully");
                } else {
                    System.out.println("Error uploading data: " + statusCode);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        scanner.close();
    }

    /**Checks for illegal character in city name*/
    public static boolean hasIllegalCharacters(String input) {
        String allowedCharactersRegex = "^[a-zA-Z0-9\\s]+$";
        Pattern pattern = Pattern.compile(allowedCharactersRegex);
        return !pattern.matcher(input).matches();
    }
}
