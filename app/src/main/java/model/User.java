package model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String nama, email, password;

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
}
