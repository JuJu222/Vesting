package model;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable {

    private String author, title, desc, source, image_path;

    public News(String author, String title, String desc, String source, String image_path) {
        this.author = author;
        this.title = title;
        this.desc = desc;
        this.source = source;
        this.image_path = image_path;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    protected News(Parcel in) {
        author = in.readString();
        title = in.readString();
        desc = in.readString();
        source = in.readString();
        image_path = in.readString();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public int describeContents() {
        return 0;
    }
}
