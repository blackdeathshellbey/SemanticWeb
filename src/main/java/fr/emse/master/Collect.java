package fr.emse.master;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFLanguages;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * In this code we interpret data from JsonLD to a turtle rdf file In professor Charpaney's given file there was rating thats why we have Google Place API to get ratings and reviews
 */
/** In professor Charpaney's given file there was rating thats why we have Google Place API to get ratings and reviews*/

/** Sincerely, This is our crown jewel and where boys turn men*/
class Collect {
    static class Collector {
        String orderActionType;
        String entryPointActionPlatform;
        String entryPointUrlTemplate;
        String entryPointInLanguage;
        String name;
        String address;
        String coordinates;
        String telephone;
        String description;
        String openingHours;
        String deliveryMethods;
        String deliveryPrice;
        String url;
        String sameAs;

        private String image;

        public String getOrderActionType() {
            return orderActionType;
        }

        public void setOrderActionType(String orderActionType) {
            this.orderActionType = orderActionType;
        }

        public String getEntryPointActionPlatform() {
            return entryPointActionPlatform;
        }

        public void setEntryPointActionPlatform(String entryPointActionPlatform) {
            this.entryPointActionPlatform = entryPointActionPlatform;
        }

        public String getEntryPointUrlTemplate() {
            return entryPointUrlTemplate;
        }

        public void setEntryPointUrlTemplate(String entryPointUrlTemplate) {
            this.entryPointUrlTemplate = entryPointUrlTemplate;
        }

        public String getEntryPointInLanguage() {
            return entryPointInLanguage;
        }

        public void setEntryPointInLanguage(String entryPointInLanguage) {
            this.entryPointInLanguage = entryPointInLanguage;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(String coordinates) {
            this.coordinates = coordinates;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getOpeningHours() {
            return openingHours;
        }

        public void setOpeningHours(String openingHours) {
            this.openingHours = openingHours;
        }

        public String getDeliveryMethods() {
            return deliveryMethods;
        }

        public void setDeliveryMethods(String deliveryMethods) {
            this.deliveryMethods = deliveryMethods;
        }

        public String getDeliveryPrice() {
            return deliveryPrice;
        }

        public void setDeliveryPrice(String deliveryPrice) {
            this.deliveryPrice = deliveryPrice;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSameAs() {
            return sameAs;
        }

        public void setSameAs(String sameAs) {
            this.sameAs = sameAs;
        }

        Collector(String name) {
            this.name = name;
            this.orderActionType = null;
            this.entryPointActionPlatform = null;
            this.entryPointUrlTemplate = null;
            this.entryPointInLanguage = null;
        }
    }

    public static String apiKey = "AIzaSyBESg6MM27VpqjawT3PADRX3S-YH9i_nUs";

    public static void rdfCollector() throws Exception {

        Map<String, Object> reviews = new HashMap<>();
        Set<String> uniqueDeliveryPrices = new HashSet<>();
        Set<String> uniqueAddresses = new HashSet<>();
        Set<String> uniqueOpeningHours = new HashSet<>();

        String filePath = Main.jsonLD;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        /** Extract data from jsonLD*/
        List<Busniness> businessList = objectMapper.readValue(new File(filePath), new TypeReference<>() {
        });
        List<Collector> collectorList = new ArrayList<>();

        Model model = ModelFactory.createDefaultModel();
        String businessBaseURI = "http://schema.org";
        Map<String, Set<String>> uniqueEntryPoints = new HashMap<>();

        for (Busniness busniness : businessList) {
            Collector collector = new Collector(busniness.getName());
            // Extract and set the delivery methods
            if (busniness.getPotentialAction() != null && busniness.getPotentialAction().getDeliveryMethod() != null) {
                collector.setDeliveryMethods(String.join(", ", busniness.getPotentialAction().getDeliveryMethod()));
            }
            if (busniness.getType() != null) {
                collector.setOrderActionType(busniness.getType());
            }
            collector.setOrderActionType((busniness.getPotentialAction().getType() != null) ?
                    busniness.getPotentialAction().getType() : null);
            collector.setAddress(busniness.getAddress() != null ? busniness.getAddress().getStreetAddress() : null);
            collector.setCoordinates((busniness.getAddress() != null && busniness.getAddress().getGeo() != null) ?
                    "Latitude: " + busniness.getAddress().getGeo().getLatitude() + ", Longitude: " + busniness.getAddress().getGeo().getLongitude() : null);
            collector.setTelephone((busniness.getAddress() != null) ? busniness.getAddress().getTelephone() : null);
            collector.setDescription(busniness.getDescription());

            collector.setImage(busniness.getImage());
            StringBuilder openingHoursStringBuilder;
            if (busniness.getOpeningHoursSpecification() != null) {
                openingHoursStringBuilder = new StringBuilder();
                for (OpeningHoursSpecification openingHours : busniness.getOpeningHoursSpecification()) {
                    if (openingHours != null) {
                        String dayOfWeek = String.join(", ", openingHours.getDayOfWeek());
                        String opens = openingHours.getOpens();
                        String closes = openingHours.getCloses();
                        openingHoursStringBuilder.append(dayOfWeek).append(": ").append(opens).append(" - ").append(closes).append(", ");
                    }
                }
                if (openingHoursStringBuilder.length() > 0) {
                    openingHoursStringBuilder.setLength(openingHoursStringBuilder.length() - 2); // Remove the trailing comma and space
                    collector.setOpeningHours(openingHoursStringBuilder.toString());
                }
            }

            if (busniness.getPotentialAction() != null) {
                collector.setDeliveryMethods((busniness.getPotentialAction().getDeliveryMethod() != null) ?
                        String.join(", ", busniness.getPotentialAction().getDeliveryMethod()) : null);
                if (busniness.getPotentialAction().getPriceSpecification() != null) {
                    PriceSpecification priceSpec = busniness.getPotentialAction().getPriceSpecification();

                    if (priceSpec.getEligibleTransactionVolume() != null) {
                        TransactionVolume transactionVolume = priceSpec.getEligibleTransactionVolume();
                        collector.setDeliveryPrice((transactionVolume.getPrice() != null) ?
                                transactionVolume.getPrice() + " " + transactionVolume.getPriceCurrency() : "N/A");
                    }
                }
                if (busniness.getPotentialAction().getDeliveryMethod() != null) {
                    collector.setDeliveryMethods(String.join(", ", busniness.getPotentialAction().getDeliveryMethod()));
                }
                if (busniness.getType() != null) {
                    String[] typeSegments = busniness.getType().split("/");
                    collector.setOrderActionType(typeSegments[typeSegments.length - 1]);
                } else if (busniness.getPotentialAction() != null &&
                        busniness.getPotentialAction().getType() != null &&
                        busniness.getPotentialAction().getType().equals("http://schema.org/OrderAction")) {
                    collector.setOrderActionType("OrderAction");
                } else {
                    collector.setOrderActionType(null);
                }
                collector.setOrderActionType((busniness.getPotentialAction().getType() != null) ?
                        busniness.getPotentialAction().getType() : null);
                if (busniness.getPotentialAction().getTarget() != null) {
                    collector.setEntryPointActionPlatform((busniness.getPotentialAction().getTarget().getActionPlatform() != null) ?
                            String.join(", ", busniness.getPotentialAction().getTarget().getActionPlatform()) : null);
                    collector.setEntryPointUrlTemplate((busniness.getPotentialAction().getTarget().getUrlTemplate() != null) ?
                            busniness.getPotentialAction().getTarget().getUrlTemplate() : null);
                    collector.setEntryPointInLanguage((busniness.getPotentialAction().getTarget().getInLanguage() != null) ?
                            busniness.getPotentialAction().getTarget().getInLanguage() : null);
                }

                collector.setUrl((busniness.getPotentialAction().getTarget() != null &&
                        busniness.getPotentialAction().getTarget().getUrlTemplate() != null) ?
                        busniness.getPotentialAction().getTarget().getUrlTemplate() : null);
                collector.setSameAs(busniness.getSameAs());
            }
            collectorList.add(collector);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /** Where data being modeled for RDF*/
        for (Collector collector : collectorList) {
            //Collector collector = collectorList.get(1);
            StringBuilder businessInfoStringBuilder = new StringBuilder();
            uniqueEntryPoints.put(collector.getName(), new HashSet<>());
            String businessName = collector.getName().replaceAll("[^a-zA-Z0-9]", "_");
            String businessURI = businessBaseURI + "/" + businessName;
            Resource businessResource = model.createResource(businessURI);

            // Image URL
            String imageUrl = collector.getImage();
            if (imageUrl != null) {
                businessResource.addProperty(model.createProperty("http://schema.org/image"), imageUrl);
            }

            // PotentialAction Type
            String orderActionType = collector.getOrderActionType();
            if (orderActionType != null) {
                Resource orderActionTypeResource = model.createResource(orderActionType);
                businessResource.addProperty(model.createProperty("http://schema.org/potentialActionType"), orderActionTypeResource);
            }
            // Name
            businessResource.addProperty(model.createProperty(businessBaseURI + "/name"), collector.getName());

            // Address
            if (collector.getAddress() != null) {
                String addressString = collector.getAddress();
                if (uniqueAddresses.add(addressString)) {
                    Resource addressResource = model.createResource();
                    addressResource.addProperty(model.createProperty(businessBaseURI + "/streetAddress"), addressString);
                    if (collector.getCoordinates() != null) {
                        String coordinatesString = collector.getCoordinates();
                        String[] coordinates = coordinatesString.split(",");
                        if (coordinates.length == 2) {
                            String latitudeString = coordinates[0].trim().replaceAll("[^\\d.]", "");
                            String longitudeString = coordinates[1].trim().replaceAll("[^\\d.]", "");
                            if (!latitudeString.isEmpty() && !longitudeString.isEmpty()) {
                                double latitude = Double.parseDouble(latitudeString);
                                double longitude = Double.parseDouble(longitudeString);

                                // Geocoding
                                GeocodingResult geocodingResult = GeocodingService.getGeocodingDetails(latitude, longitude);
                                if (geocodingResult != null) {
                                    if (geocodingResult.getPostalCode() != null && !geocodingResult.getPostalCode().isEmpty()) {
                                        addressResource.addProperty(model.createProperty(businessBaseURI + "/postalCode"), geocodingResult.getPostalCode());
                                    }
                                    if (geocodingResult.getCity() != null && !geocodingResult.getCity().isEmpty()) {
                                        addressResource.addProperty(model.createProperty(businessBaseURI + "/City"), geocodingResult.getCity());
                                    }
                                    if (geocodingResult.getCountry() != null && !geocodingResult.getCountry().isEmpty()) {
                                        addressResource.addProperty(model.createProperty(businessBaseURI + "/Country"), geocodingResult.getCountry());
                                    }
                                }
                                businessInfoStringBuilder.append(collector.getName()).append(", ").append(collector.getAddress());

                                String sanitizedString = businessInfoStringBuilder.toString().replaceAll("[^a-zA-Z0-9.,]", "");

                                reviews = GooglePlacesAPI.reviewHandler(sanitizedString);

                                // Google Place reviews
                                if (reviews != null) {
                                    // Process each review
                                    List<String> reviewAuthors = (List<String>) reviews.get("reviewAuthors");
                                    List<Integer> reviewRatings = (List<Integer>) reviews.get("reviewRatings");
                                    List<String> reviewTexts = (List<String>) reviews.get("reviewTexts");
                      /*      List<String> languages = (List<String>) reviews.get("languages");
                            List<String> originalLanguages = (List<String>) reviews.get("originalLanguages");
                            List<String> profilePhotoUrls = (List<String>) reviews.get("profilePhotoUrls");
                            List<String> relativeTimeDescriptions = (List<String>) reviews.get("relativeTimeDescriptions");
                            List<String> times = (List<String>) reviews.get("times");
                            List<Boolean> translatedList = (List<Boolean>) reviews.get("translatedList");
*/

                                    for (int i = 0; i < reviews.size(); i++) {
                                        String reviewAuthor = reviewAuthors.get(i);
                                        Integer reviewRating = reviewRatings.get(i);
                                        String reviewText = reviewTexts.get(i);
                           /*     String language = languages.get(i);
                                String originalLanguage = originalLanguages.get(i);
                                String profilePhotoUrl = profilePhotoUrls.get(i);
                                String relativeTimeDescription = relativeTimeDescriptions.get(i);
                                long time = Long.parseLong(times.get(i));
                                boolean translated = translatedList.get(i);
*/
                                        // Review resource
                                        Resource reviewResource = model.createResource();
                                        if (reviewAuthor != null) {
                                            reviewResource.addProperty(model.createProperty("http://schema.org/" + "author"), reviewAuthor);
                                        }
                                        if (reviewRating != null) {
                                            reviewResource.addProperty(model.createProperty("http://schema.org/" + "reviewRating"), String.valueOf(reviewRating));
                                        }
                                        if (reviewText != null) {
                                            reviewResource.addProperty(model.createProperty("http://schema.org/" + "reviewBody"), reviewText);
                                        }
                           /*     if (language != null) {
                                    reviewResource.addProperty(model.createProperty("http://schema.org/" + "language"), language);
                                }

                                if (originalLanguage != null) {
                                    reviewResource.addProperty(model.createProperty("http://schema.org/" + "originalLanguage"), originalLanguage);
                                }

                                if (profilePhotoUrl != null) {
                                    reviewResource.addProperty(model.createProperty("http://schema.org/" + "profilePhotoUrl"), profilePhotoUrl);
                                }

                                if (relativeTimeDescription != null) {
                                    reviewResource.addProperty(model.createProperty("http://schema.org/" + "relativeTimeDescription"), relativeTimeDescription);
                                }

                                if (time != 0) {
                                    reviewResource.addProperty(model.createProperty("http://schema.org/" + "reviewTime"), String.valueOf(time));
                                }

                                if (translated) {
                                    reviewResource.addProperty(model.createProperty("http://schema.org/" + "translated"), String.valueOf(translated));
                                }*/
                                        businessResource.addProperty(model.createProperty("http://schema.org/" + "Review"), reviewResource);
                                    }
                                }
                                addressResource.addProperty(model.createProperty(businessBaseURI + "/latitude"), String.valueOf(latitude));
                                addressResource.addProperty(model.createProperty(businessBaseURI + "/longitude"), String.valueOf(longitude));
                            } else {
                                throw new NumberFormatException("Latitude or longitude is empty");
                            }
                        } else {
                            throw new NumberFormatException("Invalid coordinates format");
                        }
                    }

                    if (collector.getTelephone() != null) {
                        addressResource.addProperty(model.createProperty(businessBaseURI + "/telephone"), collector.getTelephone());
                    }

                    businessResource.addProperty(model.createProperty(businessBaseURI + "/address"), addressResource);
                }
            }

            // PotentialAction
            if (collector.getOrderActionType() != null) {
                businessResource.addProperty(model.createProperty("http://schema.org/potentialActionType"), collector.getOrderActionType());
            }

            // EntryPoint
            if (collector.getEntryPointActionPlatform() != null) {
                String entryPointKey = collector.getName() + collector.getEntryPointActionPlatform();
                if (uniqueEntryPoints.computeIfAbsent(entryPointKey, k -> new HashSet<>()).add(collector.getEntryPointUrlTemplate())) {
                    // Create entryPoint resource
                    Resource entryPointResource = model.createResource();
                    entryPointResource.addProperty(model.createProperty("http://schema.org/actionPlatform"), collector.getEntryPointActionPlatform());
                    entryPointResource.addProperty(model.createProperty("http://schema.org/urlTemplate"), collector.getEntryPointUrlTemplate());
                    entryPointResource.addProperty(model.createProperty("http://schema.org/inLanguage"), collector.getEntryPointInLanguage());

                    businessResource.addProperty(model.createProperty("http://schema.org/EntryPoint"), entryPointResource);
                }
            }

            // Description
            if (collector.getOpeningHours() != null) {
                String openingHoursString = collector.getOpeningHours();
                if (uniqueOpeningHours.add(openingHoursString)) {
                    Pattern openingHoursPattern = Pattern.compile("(.*?): (.*?)-(.*?)(?:,|$)", Pattern.DOTALL);
                    Matcher matcher = openingHoursPattern.matcher(openingHoursString);
                    while (matcher.find()) {
                        String dayOfWeek = matcher.group(1).trim();
                        String opens = matcher.group(2).trim();
                        String closes = matcher.group(3).trim();
                        Resource openingHoursResource = model.createResource();
                        openingHoursResource.addProperty(model.createProperty("http://schema.org/dayOfWeek"), dayOfWeek);
                        openingHoursResource.addProperty(model.createProperty("http://schema.org/opens"), opens);
                        openingHoursResource.addProperty(model.createProperty("http://schema.org/closes"), closes);
                        businessResource.addProperty(model.createProperty("http://schema.org/openingHours"), openingHoursResource);
                    }
                }
            }
            // Delivery price
            if (collector.getDeliveryPrice() != null && !collector.getDeliveryPrice().equals("N/A")) {
                String deliveryPrice = collector.getDeliveryPrice();
                if (uniqueDeliveryPrices.add(deliveryPrice)) {
                    String[] priceComponents = deliveryPrice.split(" ");

                    if (priceComponents.length == 2) {
                        String priceValue = priceComponents[0];
                        String currency = priceComponents[1];
                        Resource priceResource = model.createResource();
                        priceResource.addProperty(model.createProperty("http://schema.org/priceSpecification"), priceValue);
                        priceResource.addProperty(model.createProperty("http://schema.org/currency"), currency);
                        businessResource.addProperty(model.createProperty("http://schema.org/DeliveryChargeSpecification"), priceResource);
                    }
                }
            }
            // Delivery methods
            if (collector.getDeliveryMethods() != null) {
                String[] deliveryMethodsArray = collector.getDeliveryMethods().split(", ");
                for (String deliveryMethod : deliveryMethodsArray) {
                    businessResource.addProperty(model.createProperty("http://schema.org/deliveryMethod"), deliveryMethod);
                }
            }
            // URL
            if (collector.getUrl() != null) {
                try {
                    URI urlUri = new URI(collector.getUrl());
                    businessResource.addProperty(model.createProperty("http://schema.org/url"), model.createTypedLiteral(urlUri.toString(), "http://www.w3.org/2001/XMLSchema#anyURI"));
                } catch (URISyntaxException e) {
                    System.err.println("Invalid URL: " + collector.getUrl());
                }
            }
            // SameAs
            if (collector.getSameAs() != null) {
                businessResource.addProperty(model.createProperty(businessBaseURI + "/sameAs"), collector.getSameAs());
            }
        }
        // Turtle file
        try (FileWriter output = new FileWriter(Main.path + "\\data\\RDF.ttl")) {
            model.write(output, RDFLanguages.TTL.getName());
            System.out.println("RDF data has been extracted from the JSON-LD data and written.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}