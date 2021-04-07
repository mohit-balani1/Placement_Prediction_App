package com.example.placement_prediction.login;

import android.os.Parcel;
import android.os.Parcelable;

public class LogoInfo implements Parcelable {
    String name;
    String password;
    String email;

    public LogoInfo() {

    }

    public LogoInfo(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    protected LogoInfo(Parcel in) {
        name = in.readString();
        password = in.readString();

        email = in.readString();
    }

    public static final Creator<LogoInfo> CREATOR = new Creator<LogoInfo>() {
        @Override
        public LogoInfo createFromParcel(Parcel in) {
            return new com.example.placement_prediction.login.LogoInfo(in);
        }

        @Override
        public LogoInfo[] newArray(int size) {
            return new com.example.placement_prediction.login.LogoInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(email);
    }
}
