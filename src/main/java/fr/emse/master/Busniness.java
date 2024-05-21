package fr.emse.master;

import java.util.List;

public class Busniness {
    /**
     * This class made to help parse JsonLD to rdf
     */
    private String image;
    private Address address;
    private String type;
    private PotentialAction potentialAction;
    private String name;
    private List<OpeningHoursSpecification> openingHoursSpecification;
    private String description;
    private String id;
    private String context;
    private String sameAs;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PotentialAction getPotentialAction() {
        return potentialAction;
    }

    public void setPotentialAction(PotentialAction potentialAction) {
        this.potentialAction = potentialAction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OpeningHoursSpecification> getOpeningHoursSpecification() {
        return openingHoursSpecification;
    }

    public void setOpeningHoursSpecification(List<OpeningHoursSpecification> openingHoursSpecification) {
        this.openingHoursSpecification = openingHoursSpecification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSameAs() {
        return sameAs;
    }

    public void setSameAs(String sameAs) {
        this.sameAs = sameAs;
    }
}

class Address {
    private GeoCoordinates geo;
    private String streetAddress;
    private String type;
    private String name;
    private String telephone;
    private String id;

    public GeoCoordinates getGeo() {
        return geo;
    }

    public void setGeo(GeoCoordinates geo) {
        this.geo = geo;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class GeoCoordinates {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private String type;
    private double latitude;
    private double longitude;

}

class PotentialAction {
    public List<String> getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(List<String> deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public PriceSpecification getPriceSpecification() {
        return priceSpecification;
    }

    public void setPriceSpecification(PriceSpecification priceSpecification) {
        this.priceSpecification = priceSpecification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    private List<String> deliveryMethod;
    private PriceSpecification priceSpecification;
    private String type;
    private Target target;
}

class PriceSpecification {
    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public TransactionVolume getEligibleTransactionVolume() {
        return eligibleTransactionVolume;
    }

    public void setEligibleTransactionVolume(TransactionVolume eligibleTransactionVolume) {
        this.eligibleTransactionVolume = eligibleTransactionVolume;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String priceCurrency;
    private TransactionVolume eligibleTransactionVolume;
    private String type;
    private String price;
}

class TransactionVolume {
    public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String priceCurrency;
    private String type;
    private String price;
}

class Target {
    public List<String> getActionPlatform() {
        return actionPlatform;
    }

    public void setActionPlatform(List<String> actionPlatform) {
        this.actionPlatform = actionPlatform;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public String getInLanguage() {
        return inLanguage;
    }

    public void setInLanguage(String inLanguage) {
        this.inLanguage = inLanguage;
    }

    private List<String> actionPlatform;
    private String type;
    private String urlTemplate;
    private String inLanguage;
}

class OpeningHoursSpecification {
    public List<String> getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(List<String> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpens() {
        return opens;
    }

    public void setOpens(String opens) {
        this.opens = opens;
    }

    public String getCloses() {
        return closes;
    }

    public void setCloses(String closes) {
        this.closes = closes;
    }

    private List<String> dayOfWeek;
    private String type;
    private String opens;
    private String closes;

    public class PotentialAction {
        private List<String> deliveryMethod;
        private PriceSpecification priceSpecification;
        private String type;
        private Target target;

        public List<String> getDeliveryMethod() {
            return deliveryMethod;
        }

        public void setDeliveryMethod(List<String> deliveryMethod) {
            this.deliveryMethod = deliveryMethod;
        }

        public PriceSpecification getPriceSpecification() {
            return priceSpecification;
        }

        public void setPriceSpecification(PriceSpecification priceSpecification) {
            this.priceSpecification = priceSpecification;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Target getTarget() {
            return target;
        }

        public void setTarget(Target target) {
            this.target = target;
        }
    }

    public class PriceSpecification {
        private String priceCurrency;
        private TransactionVolume eligibleTransactionVolume;
        private String type;
        private String price;

        public String getPriceCurrency() {
            return priceCurrency;
        }

        public void setPriceCurrency(String priceCurrency) {
            this.priceCurrency = priceCurrency;
        }

        public TransactionVolume getEligibleTransactionVolume() {
            return eligibleTransactionVolume;
        }

        public void setEligibleTransactionVolume(TransactionVolume eligibleTransactionVolume) {
            this.eligibleTransactionVolume = eligibleTransactionVolume;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public class TransactionVolume {
        private String priceCurrency;
        private String type;
        private String price;

        public String getPriceCurrency() {
            return priceCurrency;
        }

        public void setPriceCurrency(String priceCurrency) {
            this.priceCurrency = priceCurrency;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public class Target {
        private List<String> actionPlatform;
        private String type;
        private String urlTemplate;
        private String inLanguage;

        public List<String> getActionPlatform() {
            return actionPlatform;
        }

        public void setActionPlatform(List<String> actionPlatform) {
            this.actionPlatform = actionPlatform;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrlTemplate() {
            return urlTemplate;
        }

        public void setUrlTemplate(String urlTemplate) {
            this.urlTemplate = urlTemplate;
        }

        public String getInLanguage() {
            return inLanguage;
        }

        public void setInLanguage(String inLanguage) {
            this.inLanguage = inLanguage;
        }
    }
}
