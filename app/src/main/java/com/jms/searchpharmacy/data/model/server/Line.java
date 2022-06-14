package com.jms.searchpharmacy.data.model.server;

import com.google.gson.annotations.SerializedName;

public class Line {
    @SerializedName("name")
    private String name;

    public Line(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
