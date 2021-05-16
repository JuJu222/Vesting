package model;

public class OwnedCompany {
    private int lots = 0;
    private double averageBoughtPrice = 0;
    private String companySymbol;

    public int getLots() {
        return lots;
    }

    public void setLots(int lots) {
        this.lots = lots;
    }

    public double getAverageBoughtPrice() {
        return averageBoughtPrice;
    }

    public void setAverageBoughtPrice(double averageBoughtPrice) {
        this.averageBoughtPrice = averageBoughtPrice;
    }

    public String getCompanySymbol() {
        return companySymbol;
    }

    public void setCompanySymbol(String companySymbol) {
        this.companySymbol = companySymbol;
    }
}
