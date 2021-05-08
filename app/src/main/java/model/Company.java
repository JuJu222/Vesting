package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Company implements Parcelable {
    private String companySymbol;
    private ArrayList<Price> companyStockPrices;

    public Company(String companySymbol, ArrayList<Price> companyStockPrices) {
        this.companySymbol = companySymbol;
        this.companyStockPrices = companyStockPrices;
    }

    protected Company(Parcel in) {
        companySymbol = in.readString();
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public String getCompanySymbol() {
        return companySymbol;
    }

    public ArrayList<Price> getCompanyStockPrices() {
        return companyStockPrices;
    }

    public void setCompanyStockPrices(ArrayList<Price> companyStockPrices) {
        this.companyStockPrices = companyStockPrices;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companySymbol);
    }
}
