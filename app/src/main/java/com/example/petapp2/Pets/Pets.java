package com.example.petapp2.Pets;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Pets implements Parcelable {

    private String name;
    private String race;
    private String gender;
    private String birthday;
    private String Uri;
    private String Key;


    public Pets() {
    }

    public Pets (String name, String race, String gender, String birthday, String Uri) {
        this.name = name;
        this.gender = gender;
        this.race = race;
        this.birthday = birthday;
        this.Uri=Uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUri() {
        return Uri;
    }

    public void setUri(String uri) {
        Uri = uri;
    }

    @Exclude
    public String getKey() {
        return Key;
    }
    @Exclude
    public void setKey(String key) {
        Key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(race);
        parcel.writeString(gender);
        parcel.writeString(birthday);
        parcel.writeString(Uri);
    }
    public static final Parcelable.Creator<Pets> CREATOR = new Parcelable.Creator<Pets>() {
        public Pets createFromParcel(Parcel pc) {
            return new Pets(pc);
        }
        public Pets[] newArray(int size) {
            return new Pets[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public Pets(Parcel pc){
        name     =pc.readString();
        race     =pc.readString();
        gender =pc.readString();
        birthday =pc.readString();
        Uri=pc.readString();
    }
}

