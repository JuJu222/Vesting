package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable{
    private int id;
    private String nama, email, password;
    private double balance;
    private ArrayList<OwnedCompany> ownedCompanies;

    public User() {
        this.email = "";
        this.nama = "";
        this.password = "";
    }

    public User(String email, String nama, String password) {
        this.email = email;
        this.nama = nama;
        this.password = password;
    }

    public User(String email, String nama, String password, ArrayList<OwnedCompany> ownedCompanies) {
        this.email = email;
        this.nama = nama;
        this.password = password;
        this.balance = 10000.00;
        this.ownedCompanies = ownedCompanies;
    }

    protected User(Parcel in) {
        nama = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ArrayList<OwnedCompany> getOwnedCompanies() {
        return ownedCompanies;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nama);
        dest.writeString(email);
        dest.writeString(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
