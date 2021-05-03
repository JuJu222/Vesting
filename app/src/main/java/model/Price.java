package model;

public class Price {
    private String priceDate;
    private double priceOpen;
    private double priceHigh;
    private double priceLow;
    private double priceClose;
    private int priceVolume;

    public Price(String priceDate, double priceOpen, double priceHigh, double priceLow, double priceClose, int priceVolume) {
        this.priceDate = priceDate;
        this.priceOpen = priceOpen;
        this.priceHigh = priceHigh;
        this.priceLow = priceLow;
        this.priceClose = priceClose;
        this.priceVolume = priceVolume;
    }

    public String getPriceDate() {
        return priceDate;
    }

    public double getPriceOpen() {
        return priceOpen;
    }

    public double getPriceHigh() {
        return priceHigh;
    }

    public double getPriceLow() {
        return priceLow;
    }

    public double getPriceClose() {
        return priceClose;
    }

    public int getPriceVolume() {
        return priceVolume;
    }
}
