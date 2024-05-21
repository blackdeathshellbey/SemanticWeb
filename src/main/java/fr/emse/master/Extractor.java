package fr.emse.master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
/** This class handles the initial link extraction from first turtle file*/
public class Extractor {
    public static void linkExcavator() throws Exception {
        System.out.println("Links to the businesses did not detected in database,\n" +
                "Initializing the link extraction for the businesses website.");
        String json = new String(Files.readAllBytes(Paths.get(Main.initialData)));
        JSONArray arr = new JSONArray(json);
        BufferedWriter writer = new BufferedWriter(new FileWriter(Main.links));
        for (int i = 0; i < arr.length(); i++) {
            try {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.has("coopcycle_url")) {
                    String url = obj.getString("coopcycle_url");
                    try {
                        Document doc = Jsoup.connect(url).get();
                        Elements links = doc.select("a[data-restaurant-path]");
                        for (Element link : links) {
                            String href = link.attr("abs:href");
                            writer.write(href);
                            writer.newLine();
                        }
                    } catch (IOException ignored) {
                    }
                }
            } catch (JSONException ignored) {
                System.out.println("er");

            }
        }
        writer.close();
    }
/** This class is in charge of converting the links into jsonLD*/
    public static void jsonLdExcavator() throws Exception {
        System.out.println("A json-LD for the links did not detected therefore," +
                "\nExecuting the extraction of JSON-LD from the links.");
        List<String> urls = Files.readAllLines(Paths.get(Main.links));
        JSONArray jsonLds = new JSONArray();

        int totalUrls = urls.size();
        int processedUrls = 0;

        for (String url : urls) {
            try {
                Document doc = Jsoup.connect(url).get();
                Elements scripts = doc.select("script[type=application/ld+json]");
                for (Element script : scripts) {
                    JSONObject jsonLd = new JSONObject(script.data());
                    jsonLds.put(jsonLd);
                }
            } catch (IOException | JSONException ignored) {
            }

            processedUrls++;
            System.out.println("Progress: " + processedUrls + "/" + totalUrls);
        }
        Files.write(Paths.get(Main.jsonLD), jsonLds.toString(4).getBytes());
    }
}
