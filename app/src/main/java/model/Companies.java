package model;

import java.util.ArrayList;

public class Companies {
    private ArrayList<Company> listedCompanies;

    public Companies(ArrayList<Company> listedCompanies) {
        this.listedCompanies = listedCompanies;
    }

    public ArrayList<Company> getListedCompanies() {
        return listedCompanies;
    }
}
