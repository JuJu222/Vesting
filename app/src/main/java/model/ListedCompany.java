package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ListedCompany implements Parcelable {
    private String companySymbol;
    private ArrayList<Price> companyStockPrices;

    public ListedCompany(String companySymbol) {
        this.companySymbol = companySymbol;
    }

    protected ListedCompany(Parcel in) {
        companySymbol = in.readString();
    }

    public static final Creator<ListedCompany> CREATOR = new Creator<ListedCompany>() {
        @Override
        public ListedCompany createFromParcel(Parcel in) {
            return new ListedCompany(in);
        }

        @Override
        public ListedCompany[] newArray(int size) {
            return new ListedCompany[size];
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
