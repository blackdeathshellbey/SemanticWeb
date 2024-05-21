package fr.emse.master;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Below we have the class to extract the information using google place API
 * We add some info for our triples such as possible reviews and ratings (when I want to but something I read reviews).
 * We extract more information's but due to the time limitations we did not add everything
 */
public class GooglePlacesAPI {

    // I removed my key but you can generate one for free and put it here
    public static String apiKey = "";
    public static Map<String, Object> reviewHandler(String location) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8.toString());
        encodedLocation = location.replace(" ", "%20");
        try {
            String placeId = getPlaceId(apiKey, encodedLocation);

            if (placeId != null) {
                JsonObject placeDetails = getPlaceDetails(apiKey, placeId);

                if (placeDetails != null) {
                    String name = placeDetails.getAsJsonObject("result").get("name").getAsString();
                    JsonElement ratingElement = placeDetails.getAsJsonObject("result").get("rating");
                    double rating = (ratingElement != null && !ratingElement.isJsonNull()) ? ratingElement.getAsDouble() : 0.0;
                    JsonArray reviews = placeDetails.getAsJsonObject("result").getAsJsonArray("reviews");

                    List<String> reviewAuthors = new ArrayList<>();
                    List<Integer> reviewRatings = new ArrayList<>();
                    List<String> reviewTexts = new ArrayList<>();/*
                    List<String> authorUrls = new ArrayList<>();
                    List<String> languages = new ArrayList<>();
                    List<String> originalLanguages = new ArrayList<>();
                    List<String> profilePhotoUrls = new ArrayList<>();
                    List<String> relativeTimeDescriptions = new ArrayList<>();
                    List<String> times = new ArrayList<>();
                    List<Boolean> translatedList = new ArrayList<>();*/

                    if (reviews != null) {
                        for (int i = 0; i < reviews.size(); i++) {
                            JsonObject review = reviews.get(i).getAsJsonObject();
                            String authorName = review.get("author_name").getAsString();

                            JsonElement reviewRatingElement = review.get("rating");
                            int reviewRating = (reviewRatingElement != null && !reviewRatingElement.isJsonNull()) ? reviewRatingElement.getAsInt() : 0;

                            String reviewText = review.get("text").getAsString();
                            String authorUrl = getStringOrDefault(review, "author_url", "N/A");
                            String language = getStringOrDefault(review, "language", "N/A");
                            String originalLanguage = getStringOrDefault(review, "original_language", "N/A");
                            String profilePhotoUrl = getStringOrDefault(review, "profile_photo_url", "N/A");
                            String relativeTimeDescription = getStringOrDefault(review, "relative_time_description", "N/A");
                            long time = getLongOrDefault(review, "time", 0L);
                            boolean translated = getBooleanOrDefault(review, "translated", false);
                            reviewAuthors.add(authorName);
                            reviewRatings.add(reviewRating);
                            reviewTexts.add(reviewText);
                        /*
                        authorUrls.add(authorUrl);
                        languages.add(language);
                        originalLanguages.add(originalLanguage);
                        profilePhotoUrls.add(profilePhotoUrl);
                        relativeTimeDescriptions.add(relativeTimeDescription);
                        times.add(String.valueOf(time));
                        translatedList.add(translated);*/
                        }

                        // These are the informations we are extracting, Name of the person and informations about their rate
                        result.put("name", name);
                        result.put("rating", rating);
                        result.put("reviewAuthors", reviewAuthors);
                        result.put("reviewRatings", reviewRatings);
                        result.put("reviewTexts", reviewTexts);
                    } else {
                        return null;
                    }
                } else {
                    System.out.println("Failed to read JSON file.");
                }
            } else {
                System.out.println("Place ID is null. No results found for the location.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**This code gets and String containing of (City, Address , Country) and generates a placeId to be able extract the data from Google*/
    private static String getPlaceId(String apiKey, String encodedLocation) throws IOException {
        String apiUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" +
                "input=" + encodedLocation +
                "&inputtype=textquery" +
                "&fields=place_id" +
                "&key=" + apiKey;

        System.out.println("API Request URL: " + apiUrl);
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        System.out.println("API Response: " + jsonResponse.toString());
        if (jsonResponse.has("candidates") && jsonResponse.getAsJsonArray("candidates").size() > 0) {
            return jsonResponse.getAsJsonArray("candidates").get(0).getAsJsonObject().get("place_id").getAsString();
        } else {
            System.out.println("Error or invalid request. Check the response: " + response.toString());
            return null;
        }
    }

    /**This code gets the placeID and retrieves the information (reviews in our case) about the place*/
    private static JsonObject getPlaceDetails(String apiKey, String placeId) throws IOException {
        String encodedPlaceId = URLEncoder.encode(placeId, StandardCharsets.UTF_8.toString());
        if (placeId == null) {
            System.out.println("Error: placeId is null");
            return null;
        }
        String apiUrl = "https://maps.googleapis.com/maps/api/place/details/json?" +
                "place_id=" + encodedPlaceId +
                "&fields=name,rating,reviews" +
                "&key=" + apiKey;
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("HTTP error code: " + responseCode);
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();
        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
        if (jsonResponse.has("error_message")) {
            String errorMessage = jsonResponse.get("error_message").getAsString();
            System.out.println("Error Message: " + errorMessage);
            return null;
        } else {
            System.out.println("API Response: " + response.toString());
            return jsonResponse;
        }
    }

    private static void saveToJsonFile(JsonArray jsonObject, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(jsonObject.toString());
            System.out.println("Data saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject readJsonFile(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private static String getStringOrDefault(JsonObject jsonObject, String key, String defaultValue) {
        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull()
                ? jsonObject.get(key).getAsString()
                : defaultValue;
    }

    private static int getIntOrDefault(JsonObject jsonObject, String key, int defaultValue) {
        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull()
                ? jsonObject.get(key).getAsInt()
                : defaultValue;
    }

    private static long getLongOrDefault(JsonObject jsonObject, String key, long defaultValue) {
        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull()
                ? jsonObject.get(key).getAsLong()
                : defaultValue;
    }

    private static boolean getBooleanOrDefault(JsonObject jsonObject, String key, boolean defaultValue) {
        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull()
                ? jsonObject.get(key).getAsBoolean()
                : defaultValue;
    }
}
