package hu.mobilmegoldasok.projekt.uzemanyagfigyelo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FuelPriceScraper {

    private static final String URL = "https://holtankoljak.hu";
    public static class FuelPrice {
        private String fuelType;
        private double minPrice;
        private double avgPrice;
        private double maxPrice;

        public FuelPrice(String fuelType, double minPrice, double avgPrice, double maxPrice) {
            this.fuelType = fuelType;
            this.minPrice = minPrice;
            this.avgPrice = avgPrice;
            this.maxPrice = maxPrice;
        }

        public String getFuelType() {
            return fuelType;
        }

        public double getMinPrice() {
            return minPrice;
        }

        public double getAvgPrice() {
            return avgPrice;
        }

        public double getMaxPrice() {
            return maxPrice;
        }

        @Override
        public String toString() {
            return "FuelPrice{" +
                    "fuelType='" + fuelType + '\'' +
                    ", minPrice=" + minPrice +
                    ", avgPrice=" + avgPrice +
                    ", maxPrice=" + maxPrice +
                    '}';
        }
    }

    public List<FuelPrice> fetchFuelPrices() {
        List<FuelPrice> fuelPrices = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(URL).get();
            Elements priceDivs = doc.select("div.d-flex.mb-3");

            for (Element priceDiv : priceDivs) {
                String fuelType = priceDiv.select("div img").attr("src").split("/")[2].split("\\.")[0]; // Pl. "95-benzin-e10" vagy "gazolaj"

                Elements priceElements = priceDiv.select("span.ar");
                if (priceElements.size() >= 3) {
                    double minPrice = Double.parseDouble(priceElements.get(0).text().replace(",", "."));
                    double avgPrice = Double.parseDouble(priceElements.get(1).text().replace(",", "."));
                    double maxPrice = Double.parseDouble(priceElements.get(2).text().replace(",", "."));

                    fuelPrices.add(new FuelPrice(fuelType, minPrice, avgPrice, maxPrice));
                } else {
                    // Hiba vagy figyelmeztetés, ha nincs elegendő adat
                    System.err.println("Not enough price data found for " + fuelType);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return fuelPrices;
    }
}

