package fr.emse.master;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
/** In this code we extract information about postal code, country and coordination*/
public class GeocodingService {
    // I removed my key because it is a paid service, but you generate a new key for free and put it here
    private static final String API_KEY = "";

    public static GeocodingResult getGeocodingDetails(double latitude, double longitude) throws Exception {
        GeocodingResult result = null;

        try {
            JsonNode response = Unirest.get("https://api.opencagedata.com/geocode/v1/json")
                    .queryString("key", API_KEY)
                    .queryString("q", latitude + "," + longitude)
                    .queryString("pretty", "1")
                    .asJson()
                    .getBody();
            result = new GeocodingResult();
            result.setPostalCode(response.getObject().getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("components")
                    .optString("postcode"));
            result.setCity(response.getObject().getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("components")
                    .optString("city"));
            result.setCountry(response.getObject().getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("components")
                    .optString("country"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

/** This class originally creates for user preference, it gets user's likes city and find the coordinates for it*/
    public static double[] getCoordinatesForCity(String cityName) throws Exception {
        double[] coordinates = new double[2];

        try {
            JsonNode response = Unirest.get("https://api.opencagedata.com/geocode/v1/json")
                    .queryString("key", API_KEY)
                    .queryString("q", cityName)
                    .queryString("pretty", "1")
                    .asJson()
                    .getBody();
            coordinates[0] = response.getObject().getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getDouble("lat");
            coordinates[1] = response.getObject().getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getDouble("lng");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return coordinates;
    }
}

class GeocodingResult {
    private String postalCode;
    private String city;
    private String country;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
