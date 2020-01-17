package com.twwm.ems;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Comparator;

@IgnoreExtraProperties
public class User implements Parcelable {

    private String UID;
    private String name;
    private String designation;
    private String contact;
    private String email;
    private String password;
    private int salary;
    private String rating;
    private boolean isAdmin;

    public User()
    {
        this.UID = UID;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.designation = designation;
        this.password = password;
        this.salary = salary;
        this.isAdmin = isAdmin;
        this.rating = rating;
    }

    protected User(Parcel in) {

        UID = in.readString();
        name = in.readString();
        email = in.readString();
        contact = in.readString();
        designation = in.readString();
        password = in.readString();
        salary = in.readInt();
        isAdmin = in.readInt() == 1;
        rating = in.readString();
    }
    public String getUID(){return UID;}

    public void setUID(String UID){this.UID = UID;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getDesignation(){return designation;}

    public void setDesignation(String designation){this.designation = designation;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getContact() {return contact;}

    public void setContact(String contact) {this.contact = contact;}

    public String getPassword(){return password;}

    public void setPassword(String password){this.password = password;}

    public int getSalary(){return salary;}

    public void setSalary(int salary){this.salary = salary;}

    public boolean getIsAdmin(){return isAdmin;}

    public void setIsAdmin(boolean isAdmin){this.isAdmin = isAdmin;}

    public String getRating(){return rating;}

    public void setRating(String rating){this.rating = rating;}

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(UID);
        dest.writeString(name);
        dest.writeString(designation);
        dest.writeString(email);
        dest.writeString(contact);
        dest.writeString(password);
        dest.writeInt(salary);
        dest.writeString(rating);
    }

    /*public static final Comparator<User> UPDATE_LIST = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {

            int clueComp = o2.getSalary() - o1.getSalary();

            if(clueComp!=0){
                return clueComp;
            }

            String Name1 = o1.getName();
            String Name2 = o2.getName();

            return Name1.compareTo(Name2);

        }
    };*/
}