package model;

import java.util.ArrayList;

public class Company {
    private String companySymbol;
    private ArrayList<Price> companyStockPrices;

    public Company(String companySymbol, ArrayList<Price> companyStockPrices) {
        this.companySymbol = companySymbol;
        this.companyStockPrices = companyStockPrices;
    }

    public String getCompanySymbol() {
        return companySymbol;
    }

    public ArrayList<Price> getCompanyStockPrices() {
        return companyStockPrices;
    }
}
