package com.jms.searchpharmacy.data.model.server;

import com.google.gson.annotations.SerializedName;

public class Convenience {
//    load_address -> loadaddress
    @SerializedName("loadaddress")
    private String load_address;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("startdate")
    private String startdate;

    public Convenience(String load_address, String name, String address, String startdate) {
        this.load_address = load_address;
        this.name = name;
        this.address = address;
        this.startdate = startdate;
    }

    public String getLoad_address() {
        return load_address;
    }

    public void setLoad_address(String load_address) {
        this.load_address = load_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
}
