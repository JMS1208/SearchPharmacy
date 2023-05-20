package com.jms.searchpharmacy.data.model.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;


public class Station {
    @SerializedName("dong")
    private String dong;
    @SerializedName("line")
    private String line;
    @SerializedName("name")
    private String name;

    public Station(String dong, String line, String name) {
        this.dong = dong;
        this.line = line;
        this.name = name;
    }

    public String getDong() {
        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

