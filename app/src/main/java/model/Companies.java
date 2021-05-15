package model;

import java.util.ArrayList;

public class Companies {
    private static ArrayList<ListedCompany> listedCompanies = new ArrayList<>();

    public static ArrayList<ListedCompany> getListedCompanies() {
        return listedCompanies;
    }
}
